-- Insertar usuario administrador por defecto
INSERT INTO platzi_play_usuarios 
(username, email, password, nombre_completo, role, activo, email_verificado, fecha_creacion)
VALUES 
('admin', 'admin@playapp.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Administrador', 'ADMIN', true, true, CURRENT_TIMESTAMP)
ON CONFLICT (username) DO NOTHING;

-- Insertar usuario de prueba
INSERT INTO platzi_play_usuarios 
(username, email, password, nombre_completo, role, activo, email_verificado, fecha_creacion)
VALUES 
('testuser', 'test@playapp.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Usuario de Prueba', 'USER', true, true, CURRENT_TIMESTAMP)
ON CONFLICT (username) DO NOTHING;
