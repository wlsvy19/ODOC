import os
import tensorflow as tf
import numpy as np
from tensorflow.keras.utils import load_img, img_to_array

MODEL_PATH = r"C:\sw\ODOC\AIStudy\cat_dog_classifier\cat_dog_model.keras"
IMAGE_DIR = r"C:\sw\ODOC\AIStudy\cat_dog_classifier\test_images"
IMG_SIZE = 160  # train.py와 동일

model = tf.keras.models.load_model(MODEL_PATH)

for file_name in os.listdir(IMAGE_DIR):
    if not file_name.lower().endswith((".jpg", ".jpeg", ".png")):
        continue

    image_path = os.path.join(IMAGE_DIR, file_name)
    try:
        img = load_img(image_path, target_size=(IMG_SIZE, IMG_SIZE), color_mode="rgb")
        img_array = img_to_array(img) / 255.0
        img_array = np.expand_dims(img_array, axis=0)

        prediction = model.predict(img_array, verbose=0)[0][0]
        label = "Dog 🐶" if prediction > 0.5 else "Cat 🐱"
        confidence = prediction if prediction > 0.5 else 1 - prediction

        print(f"{file_name:30s} → {label} (confidence: {confidence:.2f})")

    except Exception as e:
        print(f"{file_name:30s} → Error: {e}")
