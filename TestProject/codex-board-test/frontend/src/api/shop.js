const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';

let currentUser = null;

export function setApiUser(user) {
  currentUser = user;
}

async function request(path, options = {}) {
  const headers = {
    'Content-Type': 'application/json',
    ...options.headers,
  };

  if (currentUser?.username) {
    headers['X-User-Id'] = currentUser.username;
  }

  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...options,
    headers,
  });

  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || `Request failed with ${response.status}`);
  }

  if (response.status === 204) {
    return null;
  }

  return response.json();
}

export function login(payload) {
  return request('/api/auth/login', {
    method: 'POST',
    body: JSON.stringify(payload),
  });
}

export function getProducts() {
  return request('/api/products');
}

export function createProduct(payload) {
  return request('/api/products', {
    method: 'POST',
    body: JSON.stringify(payload),
  });
}

export function updateProduct(id, payload) {
  return request(`/api/products/${id}`, {
    method: 'PUT',
    body: JSON.stringify(payload),
  });
}

export function deleteProduct(id) {
  return request(`/api/products/${id}`, {
    method: 'DELETE',
  });
}

export function getOrders() {
  return request('/api/orders');
}

export function createOrder(payload) {
  return request('/api/orders', {
    method: 'POST',
    body: JSON.stringify(payload),
  });
}

export function getQuestions() {
  return request('/api/questions');
}

export function createQuestion(payload) {
  return request('/api/questions', {
    method: 'POST',
    body: JSON.stringify(payload),
  });
}

export function updateQuestion(id, payload) {
  return request(`/api/questions/${id}`, {
    method: 'PUT',
    body: JSON.stringify(payload),
  });
}

export function answerQuestion(id, answer) {
  return request(`/api/questions/${id}/answer`, {
    method: 'PUT',
    body: JSON.stringify({ answer }),
  });
}

export function deleteQuestion(id) {
  return request(`/api/questions/${id}`, {
    method: 'DELETE',
  });
}

