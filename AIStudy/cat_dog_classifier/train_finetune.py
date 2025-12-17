import tensorflow as tf
from tensorflow.keras import layers, models
import matplotlib.pyplot as plt
import os

# =====================
# 설정
# =====================
DATA_DIR = r"C:\sw\ODOC\AIStudy\cat_dog_classifier\train"
IMG_SIZE = 160
BATCH_SIZE = 32
EPOCHS = 20

# =====================
# 데이터 로드
# =====================
train_ds = tf.keras.utils.image_dataset_from_directory(
    DATA_DIR,
    validation_split=0.2,
    subset="training",
    seed=123,
    image_size=(IMG_SIZE, IMG_SIZE),
    batch_size=BATCH_SIZE
)

val_ds = tf.keras.utils.image_dataset_from_directory(
    DATA_DIR,
    validation_split=0.2,
    subset="validation",
    seed=123,
    image_size=(IMG_SIZE, IMG_SIZE),
    batch_size=BATCH_SIZE
)

class_names = train_ds.class_names
print("Class names:", class_names)

AUTOTUNE = tf.data.AUTOTUNE
train_ds = train_ds.cache().shuffle(1000).prefetch(buffer_size=AUTOTUNE)
val_ds = val_ds.cache().prefetch(buffer_size=AUTOTUNE)

# =====================
# Data Augmentation 강화
# =====================
data_augmentation = tf.keras.Sequential([
    layers.RandomFlip("horizontal"),
    layers.RandomRotation(0.3),
    layers.RandomZoom(0.3),
    layers.RandomContrast(0.3),
    layers.RandomBrightness(0.3)
])

# =====================
# MobileNetV2 전이학습 + Fine-tuning
# =====================
base_model = tf.keras.applications.MobileNetV2(
    input_shape=(IMG_SIZE, IMG_SIZE, 3),
    include_top=False,
    weights='imagenet'
)

# Fine-tuning: 마지막 30층만 학습 가능
base_model.trainable = True
for layer in base_model.layers[:-30]:
    layer.trainable = False

# =====================
# 모델 정의
# =====================
model = models.Sequential([
    data_augmentation,
    layers.Rescaling(1./255),
    base_model,
    layers.GlobalAveragePooling2D(),
    layers.Dropout(0.5),
    layers.Dense(1, activation='sigmoid')
])

# =====================
# 컴파일
# =====================
model.compile(
    optimizer=tf.keras.optimizers.Adam(learning_rate=1e-4),  # fine-tuning에는 낮은 lr 추천
    loss='binary_crossentropy',
    metrics=['accuracy']
)

model.summary()

# =====================
# 학습
# =====================
history = model.fit(
    train_ds,
    validation_data=val_ds,
    epochs=EPOCHS
)

# =====================
# 모델 저장
# =====================
model.save("cat_dog_model_finetune.keras")
print("모델 저장 완료: cat_dog_model_finetune.keras")

# =====================
# 학습 결과 시각화
# =====================
acc = history.history['accuracy']
val_acc = history.history['val_accuracy']
loss = history.history['loss']
val_loss = history.history['val_loss']

epochs_range = range(EPOCHS)
plt.figure(figsize=(12, 4))
plt.subplot(1, 2, 1)
plt.plot(epochs_range, acc, label='Train Accuracy')
plt.plot(epochs_range, val_acc, label='Val Accuracy')
plt.legend()
plt.title('Accuracy')
plt.subplot(1, 2, 2)
plt.plot(epochs_range, loss, label='Train Loss')
plt.plot(epochs_range, val_loss, label='Val Loss')
plt.legend()
plt.title('Loss')
plt.show()
