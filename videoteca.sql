-- =============================================
-- Base de Datos: Videoteca
-- Sistema de Gestión de Videos
-- =============================================

CREATE DATABASE IF NOT EXISTS videoteca;
USE videoteca;

-- Tabla de Usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    usuario VARCHAR(50) NOT NULL UNIQUE,
    contrasena VARCHAR(100) NOT NULL
);

-- Tabla de Videos
CREATE TABLE IF NOT EXISTS videos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(200) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    anio INT NOT NULL,
    tema VARCHAR(100) NOT NULL,
    url VARCHAR(500) NOT NULL,
    imagen_preview VARCHAR(500)
);

-- Datos de prueba - Usuarios
INSERT INTO usuarios (nombre, usuario, contrasena) VALUES
('Administrador', 'admin', 'admin123'),
('Nathaly Aguilar', 'nathaly', 'nathaly123');

-- Datos de prueba - Videos
INSERT INTO videos (titulo, autor, anio, tema, url, imagen_preview) VALUES
('Introducción a JSF', 'Juan Pérez', 2023, 'Programación', 'https://www.youtube.com/watch?v=dQw4w9WgXcQ', 'https://img.youtube.com/vi/dQw4w9WgXcQ/hqdefault.jpg'),
('PrimeFaces Tutorial', 'María López', 2023, 'Programación Web', 'https://www.youtube.com/watch?v=dQw4w9WgXcQ', 'https://img.youtube.com/vi/dQw4w9WgXcQ/hqdefault.jpg'),
('Java EE Completo', 'Carlos Martínez', 2022, 'Java', 'https://www.youtube.com/watch?v=dQw4w9WgXcQ', 'https://img.youtube.com/vi/dQw4w9WgXcQ/hqdefault.jpg'),
('SQL Avanzado', 'Ana García', 2022, 'Base de Datos', 'https://www.youtube.com/watch?v=dQw4w9WgXcQ', 'https://img.youtube.com/vi/dQw4w9WgXcQ/hqdefault.jpg'),
('Diseño Web Moderno', 'Luis Rodríguez', 2024, 'Diseño', 'https://www.youtube.com/watch?v=dQw4w9WgXcQ', 'https://img.youtube.com/vi/dQw4w9WgXcQ/hqdefault.jpg');
