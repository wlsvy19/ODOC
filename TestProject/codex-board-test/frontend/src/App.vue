<script setup>
import { computed, onMounted, reactive, ref } from 'vue';
import {
  answerQuestion,
  createOrder,
  createProduct,
  createQuestion,
  deleteProduct,
  deleteQuestion,
  getOrders,
  getProducts,
  getQuestions,
  login,
  setApiUser,
  updateProduct,
  updateQuestion,
} from './api/shop';

const savedUser = localStorage.getItem('shopUser');
const user = ref(savedUser ? JSON.parse(savedUser) : null);
setApiUser(user.value);

const products = ref([]);
const orders = ref([]);
const questions = ref([]);
const cart = ref([]);
const loading = ref(false);
const error = ref('');
const success = ref('');

const loginForm = reactive({ username: 'user1', password: 'user1' });
const productForm = reactive({ id: null, name: '', description: '', category: '', price: 0, stock: 0, imageUrl: '' });
const orderForm = reactive({ customerName: '', email: '', phone: '', address: '' });
const questionForm = reactive({ id: null, title: '', content: '' });
const answers = reactive({});

const isAdmin = computed(() => user.value?.admin === true);
const isEditingProduct = computed(() => productForm.id !== null);
const isEditingQuestion = computed(() => questionForm.id !== null);
const cartTotal = computed(() => cart.value.reduce((sum, item) => sum + item.price * item.quantity, 0));
const cartCount = computed(() => cart.value.reduce((sum, item) => sum + item.quantity, 0));
const canOrder = computed(() => cart.value.length > 0 && orderForm.customerName && orderForm.email && orderForm.phone && orderForm.address);
const canSaveProduct = computed(() => productForm.name && productForm.description && productForm.category && productForm.imageUrl);
const canSaveQuestion = computed(() => questionForm.title.trim() && questionForm.content.trim());

function money(value) {
  return new Intl.NumberFormat('ko-KR', { style: 'currency', currency: 'KRW', maximumFractionDigits: 0 }).format(value);
}

function formatDate(value) {
  if (!value) return '';
  return new Intl.DateTimeFormat('ko-KR', { dateStyle: 'medium', timeStyle: 'short' }).format(new Date(value));
}

function resetMessages() {
  error.value = '';
  success.value = '';
}

async function submitLogin() {
  resetMessages();
  try {
    const loginUser = await login({ username: loginForm.username.trim(), password: loginForm.password.trim() });
    user.value = loginUser;
    setApiUser(loginUser);
    localStorage.setItem('shopUser', JSON.stringify(loginUser));
    success.value = `${loginUser.username} 계정으로 로그인했습니다.`;
    await loadStore();
  } catch (err) {
    error.value = '로그인에 실패했습니다. 계정 정보를 확인해주세요.';
  }
}

function logout() {
  user.value = null;
  setApiUser(null);
  localStorage.removeItem('shopUser');
  orders.value = [];
  questions.value = [];
  cart.value = [];
  resetMessages();
}

async function loadStore() {
  loading.value = true;
  resetMessages();
  try {
    const productPromise = getProducts();
    if (user.value) {
      const [productData, orderData, questionData] = await Promise.all([productPromise, getOrders(), getQuestions()]);
      products.value = productData;
      orders.value = orderData;
      questions.value = questionData;
    } else {
      products.value = await productPromise;
    }
    syncCartWithProducts();
  } catch (err) {
    error.value = '데이터를 불러오지 못했습니다. 백엔드 서버와 로그인 상태를 확인해주세요.';
  } finally {
    loading.value = false;
  }
}

function syncCartWithProducts() {
  cart.value = cart.value
    .map((item) => {
      const product = products.value.find((candidate) => candidate.id === item.productId);
      return product ? { ...item, name: product.name, price: product.price, stock: product.stock, quantity: Math.min(item.quantity, product.stock) } : null;
    })
    .filter(Boolean)
    .filter((item) => item.quantity > 0);
}

function requireLogin() {
  if (!user.value) {
    error.value = '로그인이 필요합니다.';
    return false;
  }
  return true;
}

function addToCart(product) {
  if (!requireLogin()) return;
  resetMessages();
  const existing = cart.value.find((item) => item.productId === product.id);
  if (existing) {
    if (existing.quantity >= product.stock) {
      error.value = '재고보다 많이 담을 수 없습니다.';
      return;
    }
    existing.quantity += 1;
    return;
  }
  cart.value.push({ productId: product.id, name: product.name, price: product.price, stock: product.stock, quantity: 1 });
}

function changeQuantity(item, delta) {
  item.quantity = Math.max(1, Math.min(item.stock, item.quantity + delta));
}

function removeCartItem(productId) {
  cart.value = cart.value.filter((item) => item.productId !== productId);
}

function resetProductForm() {
  Object.assign(productForm, { id: null, name: '', description: '', category: '', price: 0, stock: 0, imageUrl: '' });
}

function editProduct(product) {
  Object.assign(productForm, product);
}

async function submitProduct() {
  if (!isAdmin.value || !canSaveProduct.value) return;
  resetMessages();
  const payload = {
    name: productForm.name.trim(),
    description: productForm.description.trim(),
    category: productForm.category.trim(),
    price: Number(productForm.price),
    stock: Number(productForm.stock),
    imageUrl: productForm.imageUrl.trim(),
  };
  try {
    isEditingProduct.value ? await updateProduct(productForm.id, payload) : await createProduct(payload);
    success.value = '상품을 저장했습니다.';
    resetProductForm();
    await loadStore();
  } catch (err) {
    error.value = '상품 저장은 관리자만 가능합니다.';
  }
}

async function removeProduct(product) {
  if (!isAdmin.value || !window.confirm(`"${product.name}" 상품을 삭제할까요?`)) return;
  resetMessages();
  try {
    await deleteProduct(product.id);
    removeCartItem(product.id);
    success.value = '상품을 삭제했습니다.';
    await loadStore();
  } catch (err) {
    error.value = '상품을 삭제하지 못했습니다.';
  }
}

async function submitOrder() {
  if (!requireLogin() || !canOrder.value) return;
  resetMessages();
  try {
    await createOrder({
      customerName: orderForm.customerName.trim(),
      email: orderForm.email.trim(),
      phone: orderForm.phone.trim(),
      address: orderForm.address.trim(),
      items: cart.value.map((item) => ({ productId: item.productId, quantity: item.quantity })),
    });
    cart.value = [];
    Object.assign(orderForm, { customerName: '', email: '', phone: '', address: '' });
    success.value = '주문이 완료되었습니다.';
    await loadStore();
  } catch (err) {
    error.value = '주문을 완료하지 못했습니다. 재고와 입력 정보를 확인해주세요.';
  }
}

function resetQuestionForm() {
  Object.assign(questionForm, { id: null, title: '', content: '' });
}

function editQuestion(question) {
  Object.assign(questionForm, { id: question.id, title: question.title, content: question.content });
}

async function submitQuestion() {
  if (!requireLogin() || !canSaveQuestion.value) return;
  resetMessages();
  try {
    const payload = { title: questionForm.title.trim(), content: questionForm.content.trim() };
    isEditingQuestion.value ? await updateQuestion(questionForm.id, payload) : await createQuestion(payload);
    success.value = '질문을 저장했습니다.';
    resetQuestionForm();
    await loadStore();
  } catch (err) {
    error.value = '질문을 저장하지 못했습니다.';
  }
}

async function submitAnswer(question) {
  if (!isAdmin.value || !answers[question.id]?.trim()) return;
  resetMessages();
  try {
    await answerQuestion(question.id, answers[question.id].trim());
    answers[question.id] = '';
    success.value = '답변을 등록했습니다.';
    await loadStore();
  } catch (err) {
    error.value = '답변 등록은 관리자만 가능합니다.';
  }
}

async function removeQuestion(question) {
  if (!window.confirm(`"${question.title}" 질문을 삭제할까요?`)) return;
  resetMessages();
  try {
    await deleteQuestion(question.id);
    success.value = '질문을 삭제했습니다.';
    await loadStore();
  } catch (err) {
    error.value = '질문을 삭제하지 못했습니다.';
  }
}

onMounted(loadStore);
</script>

<template>
  <main class="shop-shell">
    <header class="shop-header">
      <div>
        <p class="eyebrow">Spring Boot + Vue 3</p>
        <h1>Codex Store</h1>
      </div>
      <div v-if="user" class="session-box">
        <strong>{{ user.username }}</strong>
        <span>{{ user.admin ? '관리자' : '사용자' }}</span>
        <button class="secondary-button" type="button" @click="logout">로그아웃</button>
      </div>
    </header>

    <form v-if="!user" class="panel login-panel" @submit.prevent="submitLogin">
      <div class="section-title">
        <h2>로그인</h2>
        <span>admin/admin 또는 user1/user1</span>
      </div>
      <label>
        ID
        <input v-model="loginForm.username" autocomplete="username" />
      </label>
      <label>
        PW
        <input v-model="loginForm.password" type="password" autocomplete="current-password" />
      </label>
      <button class="primary-button wide" type="submit">로그인</button>
    </form>

    <p v-if="error" class="alert error">{{ error }}</p>
    <p v-if="success" class="alert success">{{ success }}</p>

    <section class="layout">
      <section class="catalog" aria-label="상품 목록">
        <div class="section-title">
          <h2>상품</h2>
          <span>{{ products.length }}개</span>
        </div>
        <p v-if="loading" class="empty">불러오는 중...</p>
        <div v-else class="product-grid">
          <article v-for="product in products" :key="product.id" class="product-card">
            <img :src="product.imageUrl" :alt="product.name" />
            <div class="product-body">
              <div>
                <span class="category">{{ product.category }}</span>
                <h3>{{ product.name }}</h3>
                <p>{{ product.description }}</p>
              </div>
              <div class="product-footer">
                <strong>{{ money(product.price) }}</strong>
                <span>재고 {{ product.stock }}</span>
              </div>
              <div class="button-row">
                <button class="primary-button" type="button" :disabled="product.stock === 0" @click="addToCart(product)">담기</button>
                <button v-if="isAdmin" class="secondary-button" type="button" @click="editProduct(product)">수정</button>
                <button v-if="isAdmin" class="danger-button" type="button" @click="removeProduct(product)">삭제</button>
              </div>
            </div>
          </article>
        </div>
      </section>

      <aside class="checkout-panel" aria-label="장바구니와 주문">
        <section class="panel">
          <div class="section-title">
            <h2>장바구니</h2>
            <span>{{ cartCount }}개 · {{ money(cartTotal) }}</span>
          </div>
          <p v-if="cart.length === 0" class="empty">상품을 담아주세요.</p>
          <article v-for="item in cart" :key="item.productId" class="cart-item">
            <div>
              <strong>{{ item.name }}</strong>
              <span>{{ money(item.price) }}</span>
            </div>
            <div class="quantity">
              <button type="button" @click="changeQuantity(item, -1)">-</button>
              <span>{{ item.quantity }}</span>
              <button type="button" @click="changeQuantity(item, 1)">+</button>
              <button class="text-button" type="button" @click="removeCartItem(item.productId)">삭제</button>
            </div>
          </article>
        </section>

        <form class="panel order-form" @submit.prevent="submitOrder">
          <div class="section-title">
            <h2>주문서</h2>
          </div>
          <label>이름<input v-model="orderForm.customerName" placeholder="홍길동" /></label>
          <label>이메일<input v-model="orderForm.email" type="email" placeholder="hello@example.com" /></label>
          <label>연락처<input v-model="orderForm.phone" placeholder="010-0000-0000" /></label>
          <label>주소<textarea v-model="orderForm.address" rows="3" placeholder="배송지 주소"></textarea></label>
          <button class="primary-button wide" type="submit" :disabled="!canOrder">주문하기</button>
        </form>
      </aside>
    </section>

    <section class="bottom-layout">
      <form v-if="isAdmin" class="panel product-form" @submit.prevent="submitProduct">
        <div class="section-title">
          <h2>{{ isEditingProduct ? '상품 수정' : '상품 등록' }}</h2>
          <button class="secondary-button" type="button" @click="resetProductForm">새 상품</button>
        </div>
        <div class="form-grid">
          <label>상품명<input v-model="productForm.name" maxlength="120" /></label>
          <label>카테고리<input v-model="productForm.category" maxlength="40" /></label>
          <label>가격<input v-model.number="productForm.price" min="0" type="number" /></label>
          <label>재고<input v-model.number="productForm.stock" min="0" type="number" /></label>
        </div>
        <label>이미지 URL<input v-model="productForm.imageUrl" maxlength="1000" /></label>
        <label>설명<textarea v-model="productForm.description" rows="4" maxlength="500"></textarea></label>
        <div class="button-row end">
          <button class="secondary-button" type="button" @click="resetProductForm">취소</button>
          <button class="primary-button" type="submit" :disabled="!canSaveProduct">저장</button>
        </div>
      </form>

      <section class="panel orders">
        <div class="section-title">
          <h2>{{ isAdmin ? '전체 주문' : '내 주문' }}</h2>
          <span>{{ orders.length }}건</span>
        </div>
        <p v-if="!user" class="empty">로그인하면 주문 내역을 볼 수 있습니다.</p>
        <p v-else-if="orders.length === 0" class="empty">아직 주문이 없습니다.</p>
        <article v-for="order in orders" :key="order.id" class="order-card">
          <div class="order-top">
            <strong>#{{ order.id }} {{ order.customerName }}</strong>
            <span>{{ order.username }} · {{ formatDate(order.orderedAt) }}</span>
          </div>
          <p>{{ order.address }}</p>
          <ul>
            <li v-for="item in order.items" :key="item.productId">{{ item.productName }} x {{ item.quantity }} · {{ money(item.lineTotal) }}</li>
          </ul>
          <strong class="order-total">{{ money(order.totalAmount) }}</strong>
        </article>
      </section>
    </section>

    <section class="qa-layout">
      <form class="panel question-form" @submit.prevent="submitQuestion">
        <div class="section-title">
          <h2>{{ isEditingQuestion ? '질문 수정' : '질문하기' }}</h2>
          <button class="secondary-button" type="button" @click="resetQuestionForm">새 질문</button>
        </div>
        <label>제목<input v-model="questionForm.title" maxlength="120" placeholder="궁금한 내용을 적어주세요" /></label>
        <label>내용<textarea v-model="questionForm.content" rows="5" maxlength="1000" placeholder="상품, 주문, 배송 관련 질문"></textarea></label>
        <div class="button-row end">
          <button class="primary-button" type="submit" :disabled="!canSaveQuestion">저장</button>
        </div>
      </form>

      <section class="panel questions">
        <div class="section-title">
          <h2>{{ isAdmin ? '질문 게시판' : '내 질문' }}</h2>
          <span>{{ questions.length }}건</span>
        </div>
        <p v-if="!user" class="empty">로그인하면 질문을 남길 수 있습니다.</p>
        <p v-else-if="questions.length === 0" class="empty">아직 질문이 없습니다.</p>
        <article v-for="question in questions" :key="question.id" class="question-card">
          <div class="order-top">
            <strong>{{ question.title }}</strong>
            <span>{{ question.author }} · {{ formatDate(question.createdAt) }}</span>
          </div>
          <p>{{ question.content }}</p>
          <div v-if="question.answer" class="answer-box">
            <strong>답변 · {{ question.answeredBy }}</strong>
            <p>{{ question.answer }}</p>
          </div>
          <div class="button-row">
            <button v-if="question.author === user?.username" class="secondary-button" type="button" @click="editQuestion(question)">수정</button>
            <button v-if="question.author === user?.username || isAdmin" class="danger-button" type="button" @click="removeQuestion(question)">삭제</button>
          </div>
          <form v-if="isAdmin" class="answer-form" @submit.prevent="submitAnswer(question)">
            <textarea v-model="answers[question.id]" rows="3" placeholder="관리자 답변"></textarea>
            <button class="primary-button" type="submit">답변 저장</button>
          </form>
        </article>
      </section>
    </section>
  </main>
</template>
