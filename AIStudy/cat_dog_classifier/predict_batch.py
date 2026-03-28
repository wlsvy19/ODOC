import os
import json
import numpy as np
import tensorflow as tf
from tensorflow.keras.utils import load_img, img_to_array

# =====================
# 루트 폴더 고정 (predict_batch.py가 있는 폴더 = cat_dog_classifier)
# =====================
ROOT_DIR = os.path.dirname(os.path.abspath(__file__))

MODEL_PATH = os.path.join(ROOT_DIR, "cat_dog_model.keras")
CLASS_PATH = os.path.join(ROOT_DIR, "class_names.json")
IMAGE_DIR  = os.path.join(ROOT_DIR, "test_images")

IMG_SIZE = 128

# =====================
# 모델 로드
# =====================
model = tf.keras.models.load_model(MODEL_PATH)

# =====================
# class_names 로드 (라벨 매핑 고정)
# =====================
if not os.path.exists(CLASS_PATH):
    raise FileNotFoundError(
        f"class_names.json not found: {CLASS_PATH}\n"
        f"먼저 train.py를 실행해서 class_names.json을 생성하세요."
    )

with open(CLASS_PATH, "r", encoding="utf-8") as f:
    class_names = json.load(f)

# sigmoid 출력은 "class_names[1]" 확률
class0 = class_names[0]
class1 = class_names[1]

print("Loaded class names:", class_names)
print(f"Sigmoid output means: p({class1})\n")

# =====================
# 배치 예측
# =====================
for file_name in os.listdir(IMAGE_DIR):
    if not file_name.lower().endswith((".jpg", ".jpeg", ".png")):
        continue

    image_path = os.path.join(IMAGE_DIR, file_name)

    try:
        img = load_img(image_path, target_size=(IMG_SIZE, IMG_SIZE), color_mode="rgb")
        x = img_to_array(img)                 # (H, W, 3) float
        x = np.expand_dims(x, axis=0)         # (1, H, W, 3)

        # ✅ train.py와 동일 전처리: x = x/127.5 - 1
        x = x / 127.5 - 1.0

        p_class1 = float(model.predict(x, verbose=0)[0][0])
        pred_name = class1 if p_class1 >= 0.5 else class0
        conf = p_class1 if p_class1 >= 0.5 else 1.0 - p_class1

        print(f"{file_name:30s} → {pred_name} (confidence: {conf:.2f})  [p({class1})={p_class1:.2f}]")

    except Exception as e:
        print(f"{file_name:30s} → Error: {e}")
