import tensorflow as tf
import numpy as np
from tensorflow.keras.utils import load_img, img_to_array

# ===============================
# 설정
# ===============================
MODEL_PATH = "cat_dog_model.keras"
IMAGE_PATH = "test_images/test1.jpg"
IMG_SIZE = 128

# ===============================
# 모델 로드
# ===============================
model = tf.keras.models.load_model(MODEL_PATH)

# ===============================
# 이미지 로드 & 전처리
# ===============================
img = load_img(IMAGE_PATH, target_size=(IMG_SIZE, IMG_SIZE))
img_array = img_to_array(img)
img_array = img_array / 255.0
img_array = np.expand_dims(img_array, axis=0)

# ===============================
# 예측
# ===============================
prediction = model.predict(img_array)[0][0]

print("Prediction score:", prediction)

if prediction > 0.5:
    print("🐶 Dog")
else:
    print("🐱 Cat")
