import tensorflow as tf
from tensorflow.keras import layers, models
import matplotlib.pyplot as plt

# ===============================
# 1. 기본 설정
# ===============================
IMG_SIZE = 128
BATCH_SIZE = 32
EPOCHS = 5

# ===============================
# 2. 데이터 로딩
# ===============================
train_ds = tf.keras.utils.image_dataset_from_directory(
    "data/train",
    image_size=(IMG_SIZE, IMG_SIZE),
    batch_size=BATCH_SIZE,
    shuffle=True
)

val_ds = tf.keras.utils.image_dataset_from_directory(
    "data/val",
    image_size=(IMG_SIZE, IMG_SIZE),
    batch_size=BATCH_SIZE,
    shuffle=True
)

class_names = train_ds.class_names
print("Class names:", class_names)

# ===============================
# 3. 성능 최적화 (CPU)
# ===============================
AUTOTUNE = tf.data.AUTOTUNE
train_ds = train_ds.cache().prefetch(buffer_size=AUTOTUNE)
val_ds = val_ds.cache().prefetch(buffer_size=AUTOTUNE)

# ===============================
# 4. CNN 모델 정의
# ===============================
model = models.Sequential([
    layers.Rescaling(1./255, input_shape=(IMG_SIZE, IMG_SIZE, 3)),

    layers.Conv2D(32, 3, activation='relu'),
    layers.MaxPooling2D(),

    layers.Conv2D(64, 3, activation='relu'),
    layers.MaxPooling2D(),

    layers.Conv2D(128, 3, activation='relu'),
    layers.MaxPooling2D(),

    layers.Flatten(),
    layers.Dense(128, activation='relu'),
    layers.Dense(1, activation='sigmoid')  # cat / dog 이진 분류
])

model.compile(
    optimizer='adam',
    loss='binary_crossentropy',
    metrics=['accuracy']
)

model.summary()

# ===============================
# 5. 모델 학습
# ===============================
history = model.fit(
    train_ds,
    validation_data=val_ds,
    epochs=EPOCHS
)

# ===============================
# 6. 결과 시각화
# ===============================
plt.figure(figsize=(8, 4))

plt.subplot(1, 2, 1)
plt.plot(history.history['accuracy'], label='train acc')
plt.plot(history.history['val_accuracy'], label='val acc')
plt.title('Accuracy')
plt.legend()

plt.subplot(1, 2, 2)
plt.plot(history.history['loss'], label='train loss')
plt.plot(history.history['val_loss'], label='val loss')
plt.title('Loss')
plt.legend()

plt.tight_layout()
plt.show()

# ===============================
# 7. 모델 저장
# ===============================
model.save("cat_dog_model.keras")
print("Model saved: cat_dog_model.keras")
