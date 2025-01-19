CREATE DATABASE IF NOT EXISTS zoomed_images;

USE zoomed_images;

CREATE TABLE IF NOT EXISTS images (
    id INT AUTO_INCREMENT PRIMARY KEY,
    image_url VARCHAR(255) NOT NULL,
    description TEXT
);