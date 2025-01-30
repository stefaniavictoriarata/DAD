CREATE DATABASE IF NOT EXISTS zoomed_images CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE zoomed_images;

CREATE TABLE IF NOT EXISTS images (
    id INT AUTO_INCREMENT PRIMARY KEY,
    image_url TEXT NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
