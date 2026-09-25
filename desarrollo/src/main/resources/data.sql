-- =========================================================
-- Datos iniciales de MuvU (se cargan al arrancar la app)
-- Admin: admin@muvu.com / admin123
-- Arrendadores de prueba (todos verificados): contraseña Clave123!
-- =========================================================

-- ---------- Admin ----------
INSERT INTO usuario (nombre, correo, contrasena, verificado, rol)
VALUES ('Administrador', 'admin@muvu.com',
        '$2a$10$i4yGias7IgHPGQz5YKF/auCBE.E1kJVCAFJElLSETYFlNaiCb4Mlu',
        true, 'ADMIN');

INSERT INTO admin (id)
SELECT id FROM usuario WHERE correo = 'admin@muvu.com';

-- ---------- Universidades ----------
INSERT INTO universidad (nombre, direccion, latitud, longitud) VALUES
('UTEC', 'Jr. Medrano Silva 165, Barranco, Lima', -12.1352, -77.0221),
('PUCP', 'Av. Universitaria 1801, San Miguel, Lima', -12.0694, -77.0799),
('UNI', 'Av. Túpac Amaru 210, Rímac, Lima', -12.0237, -77.0480),
('UNMSM', 'Av. Carlos Germán Amezaga 375, Cercado de Lima, Lima', -12.0560, -77.0840),
('Universidad de Lima', 'Av. Javier Prado Este 4600, Santiago de Surco, Lima', -12.0845, -76.9710),
('UPC', 'Av. Prolongación Primavera 2390, Santiago de Surco, Lima', -12.1040, -76.9630),
('Universidad del Pacífico', 'Av. Salaverry 2020, Jesús María, Lima', -12.0830, -77.0490),
('UPCH', 'Av. Honorio Delgado 430, San Martín de Porres, Lima', -12.0240, -77.0550),
('USIL', 'Av. La Fontana 550, La Molina, Lima', -12.0730, -76.9540),
('ESAN', 'Alonso de Molina 1652, Santiago de Surco, Lima', -12.1050, -76.9620),
('Universidad Ricardo Palma', 'Av. Benavides 5440, Santiago de Surco, Lima', -12.1320, -76.9870);

-- ---------- Arrendadores (5 verificados, 10 habitaciones cada uno) ----------
INSERT INTO usuario (nombre, correo, contrasena, verificado, rol) VALUES
('Rosa Quispe Huamán', 'rosa.quispe@muvu.com', '$2a$10$kw6salxyT3B72bfEqTtIRuswSONQZliys1lmkxWNtV55/6EfsWtI.', true, 'ARRENDADOR'),
('Carlos Mendoza Ríos', 'carlos.mendoza@muvu.com', '$2a$10$kw6salxyT3B72bfEqTtIRuswSONQZliys1lmkxWNtV55/6EfsWtI.', true, 'ARRENDADOR'),
('Lucía Fernández Soto', 'lucia.fernandez@muvu.com', '$2a$10$kw6salxyT3B72bfEqTtIRuswSONQZliys1lmkxWNtV55/6EfsWtI.', true, 'ARRENDADOR'),
('Jorge Paredes Villanueva', 'jorge.paredes@muvu.com', '$2a$10$kw6salxyT3B72bfEqTtIRuswSONQZliys1lmkxWNtV55/6EfsWtI.', true, 'ARRENDADOR'),
('María Torres Castillo', 'maria.torres@muvu.com', '$2a$10$kw6salxyT3B72bfEqTtIRuswSONQZliys1lmkxWNtV55/6EfsWtI.', true, 'ARRENDADOR');

INSERT INTO arrendador (id, dni_foto, puntaje_promedio, total_calificaciones, cantidad_habitaciones)
SELECT id, 'https://muvu-demo.s3.amazonaws.com/dni/' || id || '.jpg', 0.0, 0, 10
FROM usuario WHERE rol = 'ARRENDADOR';

-- ---------- Habitaciones (50: 10 por arrendador, algunas destacadas) ----------
INSERT INTO habitacion (direccion, latitud, longitud, precio, area, es_destacada, arrendador_id) VALUES
('Jr. Colina 940 Dpto. 504, Barranco, Lima', -12.1419, -77.0181, 830.0, 15, true, (SELECT id FROM usuario WHERE correo = 'rosa.quispe@muvu.com')),
('Av. Grau 1120 Dpto. 302, Barranco, Lima', -12.1364, -77.0307, 860.0, 15, true, (SELECT id FROM usuario WHERE correo = 'rosa.quispe@muvu.com')),
('Av. Grau 200 Dpto. 404, Barranco, Lima', -12.1286, -77.0145, 850.0, 18, true, (SELECT id FROM usuario WHERE correo = 'rosa.quispe@muvu.com')),
('Av. Grau 520 Dpto. 604, Barranco, Lima', -12.1470, -77.0318, 600.0, 10, false, (SELECT id FROM usuario WHERE correo = 'rosa.quispe@muvu.com')),
('Calle Domeyer 710, Barranco, Lima', -12.1392, -77.0229, 700.0, 10, false, (SELECT id FROM usuario WHERE correo = 'rosa.quispe@muvu.com')),
('Jr. Colina 1580 Dpto. 103, Barranco, Lima', -12.1451, -77.0178, 790.0, 16, false, (SELECT id FROM usuario WHERE correo = 'rosa.quispe@muvu.com')),
('Av. Grau 610 Dpto. 804, Barranco, Lima', -12.1324, -77.0300, 950.0, 18, false, (SELECT id FROM usuario WHERE correo = 'rosa.quispe@muvu.com')),
('Av. Pedro de Osma 690, Barranco, Lima', -12.1318, -77.0116, 900.0, 20, false, (SELECT id FROM usuario WHERE correo = 'rosa.quispe@muvu.com')),
('Av. Pedro de Osma 710 Dpto. 203, Barranco, Lima', -12.1358, -77.0103, 820.0, 18, false, (SELECT id FROM usuario WHERE correo = 'rosa.quispe@muvu.com')),
('Av. Grau 330, Barranco, Lima', -12.1490, -77.0166, 630.0, 10, false, (SELECT id FROM usuario WHERE correo = 'rosa.quispe@muvu.com')),
('Av. Caminos del Inca 1450, Santiago de Surco, Lima', -12.0980, -76.9609, 1010.0, 25, true, (SELECT id FROM usuario WHERE correo = 'carlos.mendoza@muvu.com')),
('Calle Los Álamos 1150, Santiago de Surco, Lima', -12.0986, -76.9679, 1210.0, 28, true, (SELECT id FROM usuario WHERE correo = 'carlos.mendoza@muvu.com')),
('Calle Los Álamos 1740, Santiago de Surco, Lima', -12.1033, -76.9801, 850.0, 20, true, (SELECT id FROM usuario WHERE correo = 'carlos.mendoza@muvu.com')),
('Av. Caminos del Inca 490, Santiago de Surco, Lima', -12.1035, -76.9621, 830.0, 18, false, (SELECT id FROM usuario WHERE correo = 'carlos.mendoza@muvu.com')),
('Av. Primavera 300, Santiago de Surco, Lima', -12.0914, -76.9780, 820.0, 16, false, (SELECT id FROM usuario WHERE correo = 'carlos.mendoza@muvu.com')),
('Jr. Monte Rosa 600 Dpto. 104, Santiago de Surco, Lima', -12.0885, -76.9796, 940.0, 18, false, (SELECT id FROM usuario WHERE correo = 'carlos.mendoza@muvu.com')),
('Calle Los Álamos 1300 Dpto. 102, Santiago de Surco, Lima', -12.1012, -76.9614, 910.0, 16, false, (SELECT id FROM usuario WHERE correo = 'carlos.mendoza@muvu.com')),
('Calle Los Álamos 1170, Santiago de Surco, Lima', -12.1054, -76.9741, 760.0, 12, false, (SELECT id FROM usuario WHERE correo = 'carlos.mendoza@muvu.com')),
('Calle Los Álamos 880, Santiago de Surco, Lima', -12.1051, -76.9708, 890.0, 20, false, (SELECT id FROM usuario WHERE correo = 'carlos.mendoza@muvu.com')),
('Av. El Derby 410 Dpto. 403, Santiago de Surco, Lima', -12.1087, -76.9791, 860.0, 20, false, (SELECT id FROM usuario WHERE correo = 'carlos.mendoza@muvu.com')),
('Av. Universitaria 1020 Dpto. 803, San Miguel, Lima', -12.0679, -77.0748, 920.0, 16, true, (SELECT id FROM usuario WHERE correo = 'lucia.fernandez@muvu.com')),
('Calle Federico Gallese 1660, San Miguel, Lima', -12.0757, -77.0876, 1180.0, 30, true, (SELECT id FROM usuario WHERE correo = 'lucia.fernandez@muvu.com')),
('Av. Universitaria 1190, San Miguel, Lima', -12.0809, -77.0840, 950.0, 18, true, (SELECT id FROM usuario WHERE correo = 'lucia.fernandez@muvu.com')),
('Calle Federico Gallese 1850, San Miguel, Lima', -12.0763, -77.0837, 830.0, 14, false, (SELECT id FROM usuario WHERE correo = 'lucia.fernandez@muvu.com')),
('Av. Riva Agüero 1780 Dpto. 602, San Miguel, Lima', -12.0731, -77.0757, 1250.0, 30, false, (SELECT id FROM usuario WHERE correo = 'lucia.fernandez@muvu.com')),
('Av. Riva Agüero 840 Dpto. 401, San Miguel, Lima', -12.0845, -77.0943, 1030.0, 22, false, (SELECT id FROM usuario WHERE correo = 'lucia.fernandez@muvu.com')),
('Calle Federico Gallese 1510 Dpto. 901, San Miguel, Lima', -12.0674, -77.0776, 1210.0, 30, false, (SELECT id FROM usuario WHERE correo = 'lucia.fernandez@muvu.com')),
('Av. Universitaria 1420 Dpto. 302, San Miguel, Lima', -12.0815, -77.0771, 830.0, 16, false, (SELECT id FROM usuario WHERE correo = 'lucia.fernandez@muvu.com')),
('Av. La Marina 1680, San Miguel, Lima', -12.0784, -77.0737, 930.0, 18, false, (SELECT id FROM usuario WHERE correo = 'lucia.fernandez@muvu.com')),
('Av. Universitaria 900 Dpto. 502, San Miguel, Lima', -12.0843, -77.0734, 960.0, 22, false, (SELECT id FROM usuario WHERE correo = 'lucia.fernandez@muvu.com')),
('Jr. Mariscal Miller 1070 Dpto. 702, Jesús María, Lima', -12.0688, -77.0453, 640.0, 10, true, (SELECT id FROM usuario WHERE correo = 'jorge.paredes@muvu.com')),
('Av. Brasil 1850 Dpto. 201, Jesús María, Lima', -12.0677, -77.0472, 890.0, 15, true, (SELECT id FROM usuario WHERE correo = 'jorge.paredes@muvu.com')),
('Av. Brasil 1390, Jesús María, Lima', -12.0892, -77.0558, 1170.0, 30, true, (SELECT id FROM usuario WHERE correo = 'jorge.paredes@muvu.com')),
('Av. Salaverry 890, Jesús María, Lima', -12.0706, -77.0570, 1230.0, 30, false, (SELECT id FROM usuario WHERE correo = 'jorge.paredes@muvu.com')),
('Jr. Mariscal Miller 290, Jesús María, Lima', -12.0865, -77.0438, 690.0, 12, false, (SELECT id FROM usuario WHERE correo = 'jorge.paredes@muvu.com')),
('Av. Salaverry 640, Jesús María, Lima', -12.0881, -77.0577, 810.0, 15, false, (SELECT id FROM usuario WHERE correo = 'jorge.paredes@muvu.com')),
('Av. Salaverry 880, Jesús María, Lima', -12.0832, -77.0522, 680.0, 12, false, (SELECT id FROM usuario WHERE correo = 'jorge.paredes@muvu.com')),
('Jr. Huiracocha 890, Jesús María, Lima', -12.0712, -77.0470, 1020.0, 22, false, (SELECT id FROM usuario WHERE correo = 'jorge.paredes@muvu.com')),
('Av. Arnaldo Márquez 850, Jesús María, Lima', -12.0811, -77.0556, 760.0, 15, false, (SELECT id FROM usuario WHERE correo = 'jorge.paredes@muvu.com')),
('Jr. Mariscal Miller 1250, Jesús María, Lima', -12.0825, -77.0437, 840.0, 18, false, (SELECT id FROM usuario WHERE correo = 'jorge.paredes@muvu.com')),
('Av. Javier Prado Este 1700 Dpto. 603, La Molina, Lima', -12.0890, -76.9500, 1280.0, 30, true, (SELECT id FROM usuario WHERE correo = 'maria.torres@muvu.com')),
('Av. Javier Prado Este 1770, La Molina, Lima', -12.0759, -76.9440, 1240.0, 30, true, (SELECT id FROM usuario WHERE correo = 'maria.torres@muvu.com')),
('Jr. Las Retamas 1490, La Molina, Lima', -12.0825, -76.9437, 580.0, 10, true, (SELECT id FROM usuario WHERE correo = 'maria.torres@muvu.com')),
('Jr. Las Retamas 1260 Dpto. 603, La Molina, Lima', -12.0884, -76.9449, 790.0, 18, false, (SELECT id FROM usuario WHERE correo = 'maria.torres@muvu.com')),
('Calle Los Fresnos 1390, La Molina, Lima', -12.0839, -76.9394, 1100.0, 28, false, (SELECT id FROM usuario WHERE correo = 'maria.torres@muvu.com')),
('Av. Raúl Ferrero 840 Dpto. 203, La Molina, Lima', -12.0764, -76.9602, 1000.0, 20, false, (SELECT id FROM usuario WHERE correo = 'maria.torres@muvu.com')),
('Av. La Fontana 1070 Dpto. 602, La Molina, Lima', -12.0854, -76.9484, 820.0, 14, false, (SELECT id FROM usuario WHERE correo = 'maria.torres@muvu.com')),
('Av. La Fontana 1820 Dpto. 803, La Molina, Lima', -12.0832, -76.9525, 770.0, 16, false, (SELECT id FROM usuario WHERE correo = 'maria.torres@muvu.com')),
('Jr. Las Retamas 1710 Dpto. 103, La Molina, Lima', -12.0777, -76.9453, 1190.0, 28, false, (SELECT id FROM usuario WHERE correo = 'maria.torres@muvu.com')),
('Jr. Las Retamas 1220 Dpto. 803, La Molina, Lima', -12.0721, -76.9547, 930.0, 20, false, (SELECT id FROM usuario WHERE correo = 'maria.torres@muvu.com'));

-- ---------- Pagos de publicidad de las habitaciones destacadas (vigentes) ----------
-- 30 = 30 días, 54 = 60 días. Las fechas son relativas al día en que arranca la app.
INSERT INTO pago_publicidad (habitacion_id, monto, metodo_pago, fecha_inicio, fecha_fin) VALUES
((SELECT id FROM habitacion WHERE direccion = 'Jr. Colina 940 Dpto. 504, Barranco, Lima'), 30.0, 'TARJETA_DEBITO', CURRENT_DATE - 8, CURRENT_DATE + 22),
((SELECT id FROM habitacion WHERE direccion = 'Av. Grau 1120 Dpto. 302, Barranco, Lima'), 54.0, 'TARJETA_CREDITO', CURRENT_DATE - 54, CURRENT_DATE + 6),
((SELECT id FROM habitacion WHERE direccion = 'Av. Grau 200 Dpto. 404, Barranco, Lima'), 30.0, 'TARJETA_CREDITO', CURRENT_DATE - 8, CURRENT_DATE + 22),
((SELECT id FROM habitacion WHERE direccion = 'Av. Caminos del Inca 1450, Santiago de Surco, Lima'), 54.0, 'TARJETA_DEBITO', CURRENT_DATE - 55, CURRENT_DATE + 5),
((SELECT id FROM habitacion WHERE direccion = 'Calle Los Álamos 1150, Santiago de Surco, Lima'), 30.0, 'TARJETA_CREDITO', CURRENT_DATE - 23, CURRENT_DATE + 7),
((SELECT id FROM habitacion WHERE direccion = 'Calle Los Álamos 1740, Santiago de Surco, Lima'), 54.0, 'TARJETA_CREDITO', CURRENT_DATE - 15, CURRENT_DATE + 45),
((SELECT id FROM habitacion WHERE direccion = 'Av. Universitaria 1020 Dpto. 803, San Miguel, Lima'), 30.0, 'TARJETA_DEBITO', CURRENT_DATE - 17, CURRENT_DATE + 13),
((SELECT id FROM habitacion WHERE direccion = 'Calle Federico Gallese 1660, San Miguel, Lima'), 54.0, 'TARJETA_CREDITO', CURRENT_DATE - 39, CURRENT_DATE + 21),
((SELECT id FROM habitacion WHERE direccion = 'Av. Universitaria 1190, San Miguel, Lima'), 30.0, 'TARJETA_CREDITO', CURRENT_DATE - 12, CURRENT_DATE + 18),
((SELECT id FROM habitacion WHERE direccion = 'Jr. Mariscal Miller 1070 Dpto. 702, Jesús María, Lima'), 54.0, 'TARJETA_DEBITO', CURRENT_DATE - 40, CURRENT_DATE + 20),
((SELECT id FROM habitacion WHERE direccion = 'Av. Brasil 1850 Dpto. 201, Jesús María, Lima'), 30.0, 'TARJETA_CREDITO', CURRENT_DATE - 12, CURRENT_DATE + 18),
((SELECT id FROM habitacion WHERE direccion = 'Av. Brasil 1390, Jesús María, Lima'), 54.0, 'TARJETA_CREDITO', CURRENT_DATE - 22, CURRENT_DATE + 38),
((SELECT id FROM habitacion WHERE direccion = 'Av. Javier Prado Este 1700 Dpto. 603, La Molina, Lima'), 30.0, 'TARJETA_DEBITO', CURRENT_DATE - 4, CURRENT_DATE + 26),
((SELECT id FROM habitacion WHERE direccion = 'Av. Javier Prado Este 1770, La Molina, Lima'), 54.0, 'TARJETA_CREDITO', CURRENT_DATE - 35, CURRENT_DATE + 25),
((SELECT id FROM habitacion WHERE direccion = 'Jr. Las Retamas 1490, La Molina, Lima'), 30.0, 'TARJETA_CREDITO', CURRENT_DATE - 2, CURRENT_DATE + 28);