-- HU-00: datos de prueba. Correr DESPUES de 02_esquema_uuid.sql y 03_datos_iniciales.sql.
-- Es idempotente: se puede correr varias veces y no duplica barrios, usuarios ni tecnicos.
-- Clave de TODOS los usuarios de prueba: Prueba2026
-- Correos: admin@example.com, ciudadano1@example.com, ciudadano2@example.com, tecnico1@example.com ... tecnico8@example.com
-- tecnico7@example.com esta SUSPENDIDO: no debe poder entrar ni aparecer en la busqueda.
-- tecnico8@example.com no tiene perfil (HU-04): puede entrar, pero no aparece en la busqueda.
USE manos_a_la_obra;

INSERT IGNORE INTO Barrio (ciudad_id, nombre)
SELECT c.id, b.nombre
FROM Ciudad c
JOIN (
  SELECT 'Alto del Medio' AS nombre
  UNION ALL SELECT 'Barro Blanco'
  UNION ALL SELECT 'Cuatro Esquinas'
  UNION ALL SELECT 'El Porvenir'
  UNION ALL SELECT 'La Convención'
  UNION ALL SELECT 'San Antonio'
) b
WHERE c.nombre = 'Rionegro';

INSERT IGNORE INTO Usuario (rol_id, estado_usuario_id, nombre, correo, clave_hash, indicativo_pais_id, numero_telefono)
SELECT r.id, e.id, u.nombre, u.correo, '$2a$10$uNaQznFBIb6lZfHG4M3lBOj5QUuKzcv4Z92/ThjWg651UO51d3a7.', i.id, u.numero_telefono
FROM (
  SELECT 'Administrador de Prueba' AS nombre, 'admin@example.com' AS correo, 'ADMIN' AS rol, 'ACTIVO' AS estado, '3100000000' AS numero_telefono
  UNION ALL SELECT 'Laura Gómez', 'ciudadano1@example.com', 'CIUDADANO', 'ACTIVO', '3100000001'
  UNION ALL SELECT 'Carlos Ramírez', 'ciudadano2@example.com', 'CIUDADANO', 'ACTIVO', '3100000002'
  UNION ALL SELECT 'Andrés Felipe Ruiz', 'tecnico1@example.com', 'TECNICO', 'ACTIVO', '3200000001'
  UNION ALL SELECT 'Mauricio Cárdenas', 'tecnico2@example.com', 'TECNICO', 'ACTIVO', '3200000002'
  UNION ALL SELECT 'Jhon Edison Osorio', 'tecnico3@example.com', 'TECNICO', 'ACTIVO', '3200000003'
  UNION ALL SELECT 'Wilmar Zapata', 'tecnico4@example.com', 'TECNICO', 'ACTIVO', '3200000004'
  UNION ALL SELECT 'Duván Arango', 'tecnico5@example.com', 'TECNICO', 'ACTIVO', '3200000005'
  UNION ALL SELECT 'Sebastián Londoño', 'tecnico6@example.com', 'TECNICO', 'ACTIVO', '3200000006'
  UNION ALL SELECT 'Fabio Hernández', 'tecnico7@example.com', 'TECNICO', 'SUSPENDIDO', '3200000007'
) u
JOIN Rol r ON r.nombre = u.rol
JOIN EstadoUsuario e ON e.nombre = u.estado
JOIN IndicativoPais i ON i.pais = 'Colombia';

INSERT IGNORE INTO Tecnico (usuario_id, tipo_documento, servicio_id, barrio_id, tarifa, tipo_tarifa, experiencia, descripcion, documento, promedio)
SELECT us.id, 'CC', s.id, b.id, t.tarifa, t.tipo_tarifa, t.experiencia, t.descripcion, t.documento, t.promedio
FROM (
  SELECT 'tecnico1@example.com' AS correo, 'Electricista' AS servicio, 'San Antonio' AS barrio, 90000 AS tarifa, 'POR_TRABAJO' AS tipo_tarifa, 12 AS experiencia,
         'Instalaciones eléctricas residenciales, tableros y revisión de cortos.' AS descripcion, '1037500001' AS documento, 4.8 AS promedio
  UNION ALL SELECT 'tecnico2@example.com', 'Electricista', 'Cuatro Esquinas', 70000, 'POR_HORA', 6, NULL, '1037500002', 4.2
  UNION ALL SELECT 'tecnico3@example.com', 'Plomero', 'San Antonio', 80000, 'POR_TRABAJO', 9,
         'Reparación de fugas, cambio de grifería y destape de tuberías.', '1037500003', 4.5
  UNION ALL SELECT 'tecnico4@example.com', 'Plomero', 'El Porvenir', 55000, 'POR_HORA', 3, NULL, '1037500004', 3.9
  UNION ALL SELECT 'tecnico5@example.com', 'Gasista', 'Barro Blanco', 95000, 'POR_TRABAJO', 8,
         'Instalación y mantenimiento de redes de gas y calentadores.', '1037500005', 4.6
  UNION ALL SELECT 'tecnico6@example.com', 'Carpintero', 'La Convención', 45000, 'POR_HORA', 1, NULL, '1037500006', 0
  UNION ALL SELECT 'tecnico7@example.com', 'Electricista', 'Alto del Medio', 65000, 'POR_TRABAJO', 5, NULL, '1037500007', 4.0
) t
JOIN Usuario us ON us.correo = t.correo
JOIN Servicio s ON s.nombre = t.servicio
JOIN Ciudad c ON c.nombre = 'Rionegro'
JOIN Barrio b ON b.ciudad_id = c.id AND b.nombre = t.barrio;

-- tecnico8@example.com se registró (HU-02) pero aún no guarda su perfil (HU-04):
-- puede iniciar sesión, pero no debe aparecer en la búsqueda.
INSERT IGNORE INTO Usuario (rol_id, estado_usuario_id, nombre, correo, clave_hash, indicativo_pais_id, numero_telefono)
SELECT r.id, e.id, 'Juan Pablo Restrepo', 'tecnico8@example.com', '$2a$10$uNaQznFBIb6lZfHG4M3lBOj5QUuKzcv4Z92/ThjWg651UO51d3a7.', i.id, '3200000008'
FROM Rol r
JOIN EstadoUsuario e ON e.nombre = 'ACTIVO'
JOIN IndicativoPais i ON i.pais = 'Colombia'
WHERE r.nombre = 'TECNICO';

INSERT IGNORE INTO Tecnico (usuario_id, tipo_documento, documento)
SELECT us.id, 'CC', '1037500008'
FROM Usuario us
WHERE us.correo = 'tecnico8@example.com';
