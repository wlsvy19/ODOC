import os
import json
import tensorflow as tf
from tensorflow.keras import layers, models
import matplotlib.pyplot as plt
from tensorflow.keras.applications import MobileNetV2

# =====================
# 루트 폴더 고정 (train.py가 있는 폴더 = cat_dog_classifier)
# =====================
ROOT_DIR = os.path.dirname(os.path.abspath(__file__))

DATA_DIR  = os.path.join(ROOT_DIR, "train")               # ROOT/train/cat, ROOT/train/dog
MODEL_OUT = os.path.join(ROOT_DIR, "cat_dog_model.keras") # ROOT/cat_dog_model.keras
CLASS_OUT = os.path.join(ROOT_DIR, "class_names.json")    # ROOT/class_names.json

# =====================
# 설정
# =====================
IMG_SIZE = 128
BATCH_SIZE = 32
EPOCHS = 15
SEED = 123

# =====================
# 데이터 로드 (train/val split)
# =====================
train_ds = tf.keras.utils.image_dataset_from_directory(
    DATA_DIR,
    validation_split=0.2,
    subset="training",
    seed=SEED,
    image_size=(IMG_SIZE, IMG_SIZE),
    batch_size=BATCH_SIZE,
    shuffle=True
)

val_ds = tf.keras.utils.image_dataset_from_directory(
    DATA_DIR,
    validation_split=0.2,
    subset="validation",
    seed=SEED,
    image_size=(IMG_SIZE, IMG_SIZE),
    batch_size=BATCH_SIZE,
    shuffle=False
)

class_names = train_ds.class_names
print("Class names:", class_names)

# class_names 저장 (예측에서 라벨 매핑 고정)
with open(CLASS_OUT, "w", encoding="utf-8") as f:
    json.dump(class_names, f, ensure_ascii=False)
print("Saved class names:", CLASS_OUT)

# =====================
# 성능 최적화
# =====================
AUTOTUNE = tf.data.AUTOTUNE
train_ds = train_ds.cache().shuffle(1000, seed=SEED).prefetch(AUTOTUNE)
val_ds = val_ds.cache().prefetch(AUTOTUNE)

# =====================
# Data Augmentation
# =====================
data_augmentation = tf.keras.Sequential([
    layers.RandomFlip("horizontal"),
    layers.RandomRotation(0.2),
    layers.RandomZoom(0.2),
    layers.RandomContrast(0.2),
    layers.RandomBrightness(0.2),
], name="augmentation")

# =====================
# MobileNetV2 전이학습
# =====================
base_model = MobileNetV2(
    input_shape=(IMG_SIZE, IMG_SIZE, 3),
    include_top=False,
    weights="imagenet"
)
base_model.trainable = False  # 처음엔 freeze

# =====================
# 모델 정의
#  - MobileNetV2 전처리: x = x/127.5 - 1  => Rescaling(1/127.5, offset=-1)
#  - 이 전처리가 모델 안에 있으므로, 예측에서는 추가 전처리( /255 or /127.5-1 ) 하지 말 것
# =====================
inputs = layers.Input(shape=(IMG_SIZE, IMG_SIZE, 3))
x = data_augmentation(inputs)

# MobileNetV2 전처리 (모델 내부에서 수행)
x = layers.Rescaling(1.0/127.5, offset=-1.0, name="mobilenetv2_preprocess")(x)

x = base_model(x, training=False)
x = layers.GlobalAveragePooling2D()(x)
x = layers.Dropout(0.5)(x)
outputs = layers.Dense(1, activation="sigmoid")(x)

model = models.Model(inputs, outputs, name="cat_dog_mobilenetv2")

# =====================
# 컴파일
# =====================
model.compile(
    optimizer=tf.keras.optimizers.Adam(learning_rate=1e-3),
    loss="binary_crossentropy",
    metrics=["accuracy"]
)

model.summary()

# =====================
# 콜백
# =====================
callbacks = [
    tf.keras.callbacks.EarlyStopping(
        monitor="val_accuracy",
        patience=4,
        restore_best_weights=True
    )
]

# =====================
# 학습
# =====================
history = model.fit(
    train_ds,
    validation_data=val_ds,
    epochs=EPOCHS,
    callbacks=callbacks
)

# =====================
# 모델 저장
# =====================
model.save(MODEL_OUT)
print("Saved model:", MODEL_OUT)

# =====================
# 학습 결과 시각화
# =====================
acc = history.history["accuracy"]
val_acc = history.history["val_accuracy"]
loss = history.history["loss"]
val_loss = history.history["val_loss"]

epochs_range = range(1, len(acc) + 1)

plt.figure(figsize=(12, 4))
plt.subplot(1, 2, 1)
plt.plot(epochs_range, acc, label="Train Accuracy")
plt.plot(epochs_range, val_acc, label="Val Accuracy")
plt.legend()
plt.title("Accuracy")

plt.subplot(1, 2, 2)
plt.plot(epochs_range, loss, label="Train Loss")
plt.plot(epochs_range, val_loss, label="Val Loss")
plt.legend()
plt.title("Loss")

plt.tight_layout()
plt.show()
