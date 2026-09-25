-- Admin inicial  (contraseña: admin123)
INSERT INTO usuario (nombre, correo, contrasena, verificado, rol)
VALUES ('Administrador', 'admin@muvu.com',
        '$2a$10$i4yGias7IgHPGQz5YKF/auCBE.E1kJVCAFJElLSETYFlNaiCb4Mlu',
        true, 'ADMIN');

INSERT INTO admin (id)
SELECT id FROM usuario WHERE correo = 'admin@muvu.com';


INSERT INTO universidad (nombre, direccion, latitud, longitud) VALUES
('UTEC', 'Jr. Medrano Silva 165, Barranco, Lima', -12.1352, -77.0221),
('PUCP', 'Av. Universitaria 1801, San Miguel, Lima', -12.0694, -77.0799),
('UNI',  'Av. Túpac Amaru 210, Rímac, Lima',      -12.0237, -77.0480);