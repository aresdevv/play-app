-- Crear tabla de usuarios si no existe
CREATE TABLE IF NOT EXISTS platzi_play_usuarios (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(150),
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ultimo_acceso TIMESTAMP,
    activo BOOLEAN NOT NULL DEFAULT true,
    email_verificado BOOLEAN NOT NULL DEFAULT false
);

-- Insertar usuario administrador por defecto
INSERT INTO platzi_play_usuarios 
(username, email, password, nombre_completo, role, activo, email_verificado)
VALUES 
('admin', 'admin@playapp.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Administrador', 'ADMIN', true, true)
ON CONFLICT (username) DO NOTHING;

-- Insertar usuario de prueba
INSERT INTO platzi_play_usuarios 
(username, email, password, nombre_completo, role, activo, email_verificado)
VALUES 
('testuser', 'test@playapp.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Usuario de Prueba', 'USER', true, true)
ON CONFLICT (username) DO NOTHING;
