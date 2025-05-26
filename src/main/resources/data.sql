-- 1. Insertar roles (si no existen)
INSERT INTO roles (role_name, description) VALUES 
('ADMIN', 'Administrador del sistema'),
('CUSTOMER', 'Usuario Cliente'),
('SUPPLIER', 'Usuario Proveedor')
ON CONFLICT (role_name) DO NOTHING;

-- 2. Insertar usuarios iniciales
INSERT INTO users (active, name, email, address, password, username, phone)
VALUES 
(true, 'Admin Principal', 'admin@alkile.com', 'Oficina Central', '$2a$10$X5h/5X5h5X5h5X5h5X5h5e', 'admin', '5550000001'),
(true, 'Proveedor Ejemplo', 'supplier@alkile.com', 'Almacén Principal', '$2a$10$X5h/5X5h5X5h5X5h5X5h5e', 'supplier', '5550000002'),
(true, 'Juan Pérez', 'juan@example.com', 'Calle Falsa 123', '$2a$10$X5h/5X5h5X5h5X5h5X5h5e', 'juanperez', '5551234567'),
(true, 'María García', 'maria@example.com', 'Avenida Siempre Viva 456', '$2a$10$X5h/5X5h5X5h5X5h5X5h5e', 'mariagarcia', '5557654321'),
(true, 'Carlos López', 'carlos@example.com', 'Boulevard de los Sueños 789', '$2a$10$X5h/5X5h5X5h5X5h5X5h5e', 'carloslopez', '5559876543')
ON CONFLICT (username) DO NOTHING;

-- 3. Asignar roles a usuarios
INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'admin' AND r.role_name = 'ADMIN'
ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username IN ('supplier','juanperez') AND r.role_name = 'SUPPLIER'
ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username IN ('mariagarcia', 'carloslopez') AND r.role_name = 'CUSTOMER'
ON CONFLICT (user_id, role_id) DO NOTHING;

-- 4. Insertar categorías
INSERT INTO categories (name, description) VALUES
('Herramientas Manuales', 'Herramientas operadas manualmente'),
('Herramientas Eléctricas', 'Herramientas con motor eléctrico'),
('Jardinería', 'Herramientas para jardines'),
('Construcción', 'Herramientas para construcción'),
('Pintura', 'Herramientas para pintura')
ON CONFLICT (name) DO NOTHING;

-- 5. Insertar herramientas asociadas al proveedor (usuario con rol SUPPLIER)
INSERT INTO tools (available, daily_cost, stock, category_id, user_id, name, description, image_url)
SELECT true, 15.50, 3, cat.id, u.id, 'Martillo de carpintero', 'Martillo profesional con mango de fibra de vidrio', 'https://olimpica.vtexassets.com/arquivos/ids/1391404/image-7ae563d90f9247609ba26133c01dad31.jpg?v=638495005571170000'
FROM categories cat, users u
WHERE cat.name = 'Herramientas Manuales' AND u.username = 'supplier'
ON CONFLICT DO NOTHING;

INSERT INTO tools (available, daily_cost, stock, category_id, user_id, name, description, image_url)
SELECT true, 25.00, 2, cat.id, u.id, 'Taladro inalámbrico', 'Taladro de 18V con 2 baterías y maletín', 'https://example.com/taladro.jpg'
FROM categories cat, users u
WHERE cat.name = 'Herramientas Eléctricas' AND u.username = 'supplier'
ON CONFLICT DO NOTHING;

-- 6. Insertar reservaciones (clientes son usuarios con rol CUSTOMER)
INSERT INTO reservations (start_date, end_date, customer_id, tool_id, status)
SELECT CURRENT_DATE + 1, CURRENT_DATE + 3, c.id, t.id, 'APPROVED'
FROM users c
JOIN tools t ON t.name = 'Martillo de carpintero'
WHERE c.username = 'juanperez'
ON CONFLICT DO NOTHING;

INSERT INTO reservations (start_date, end_date, customer_id, tool_id, status)
SELECT CURRENT_DATE + 2, CURRENT_DATE + 5, c.id, t.id, 'PENDING'
FROM users c
JOIN tools t ON t.name = 'Taladro inalámbrico'
WHERE c.username = 'mariagarcia'
ON CONFLICT DO NOTHING;

-- 7. Insertar pagos
INSERT INTO payments (amount, reservation_id, payment_method, transaction_id, status)
SELECT 31.00, r.id, 'CREDIT_CARD', 'TXN' || LPAD(r.id::text, 6, '0'), 'COMPLETED'
FROM reservations r
WHERE r.status = 'APPROVED'
ON CONFLICT DO NOTHING;

-- 8. Insertar facturas
INSERT INTO invoices (payment_id, issue_date, details)
SELECT p.id, CURRENT_TIMESTAMP, 'Factura por alquiler de ' || t.name || ' por ' || (r.end_date - r.start_date) || ' días'
FROM payments p
JOIN reservations r ON p.reservation_id = r.id
JOIN tools t ON r.tool_id = t.id
ON CONFLICT (payment_id) DO NOTHING;

-- 9. Insertar devoluciones
INSERT INTO "returns" (reservation_id, return_date, comments)
SELECT r.id, CURRENT_TIMESTAMP, 'Herramienta devuelta en buen estado'
FROM reservations r
WHERE r.status = 'COMPLETED'
ON CONFLICT (reservation_id) DO NOTHING;

-- 10. Insertar notificaciones
INSERT INTO notifications (user_id, message, "read", creation_date)
SELECT u.id, 'Su reserva del martillo ha sido aprobada', false, CURRENT_TIMESTAMP
FROM users u WHERE u.username = 'juanperez'
ON CONFLICT DO NOTHING;

INSERT INTO notifications (user_id, message, "read", creation_date)
SELECT u.id, 'Su reserva del taladro está pendiente de aprobación', false, CURRENT_TIMESTAMP
FROM users u WHERE u.username = 'mariagarcia'
ON CONFLICT DO NOTHING;

-- 11. Insertar reportes de daño (opcional)
INSERT INTO damage_reports (reservation_id, description, repair_cost, resolved, report_date)
SELECT r.id, 'Mango rajado', 5.50, false, CURRENT_TIMESTAMP
FROM reservations r
JOIN tools t ON r.tool_id = t.id
WHERE t.name = 'Martillo de carpintero'
ON CONFLICT (reservation_id) DO NOTHING;

-- 12. Insertar más usuarios proveedores
INSERT INTO users (active, name, email, address, password, username, phone)
VALUES 
(true, 'Laura Torres', 'laura@herramientas.com', 'Calle Industrial 789', '$2a$10$X5h/5X5h5X5h5X5h5X5h5e', 'lauratorres', '5553332222'),
(true, 'Carlos Méndez', 'carlos@herramientas.com', 'Parque Empresarial', '$2a$10$X5h/5X5h5X5h5X5h5X5h5e', 'carlosmendez', '5554443333'),
(true, 'Ana Rojas', 'ana@jardin.com', 'Zona Verde 456', '$2a$10$X5h/5X5h5X5h5X5h5X5h5e', 'anarojas', '5556667777')
ON CONFLICT (username) DO NOTHING;

-- 13. Asignar roles a nuevos proveedores
INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username IN ('lauratorres', 'carlosmendez', 'anarojas') AND r.role_name = 'SUPPLIER'
ON CONFLICT (user_id, role_id) DO NOTHING;

-- 14. Insertar más categorías
INSERT INTO categories (name, description) VALUES
('Electricidad', 'Herramientas para electricistas'),
('Fontanería', 'Herramientas para plomería'),
('Automotriz', 'Herramientas para mecánica'),
('Seguridad', 'Equipos de protección personal'),
('Jardinería Avanzada', 'Herramientas especializadas para jardinería')
ON CONFLICT (name) DO NOTHING;

-- 15. Insertar más herramientas
-- Herramientas para proveedor 'supplier'
INSERT INTO tools (available, daily_cost, stock, category_id, user_id, name, description, image_url)
SELECT true, 18.75, 4, cat.id, u.id, 'Llave inglesa ajustable', 'De 12 pulgadas con mango ergonómico', 'https://example.com/llave.jpg '
FROM categories cat, users u
WHERE cat.name = 'Herramientas Manuales' AND u.username = 'supplier'
UNION ALL
SELECT true, 35.00, 1, cat.id, u.id, 'Sierra circular eléctrica', '7-1/4 pulgadas con guía láser', 'https://example.com/sierra.jpg '
FROM categories cat, users u
WHERE cat.name = 'Herramientas Eléctricas' AND u.username = 'supplier'
UNION ALL
SELECT true, 12.00, 5, cat.id, u.id, 'Rastrillo de jardín', 'Con dientes reforzados de acero', 'https://example.com/rastrillo.jpg '
FROM categories cat, users u
WHERE cat.name = 'Jardinería' AND u.username = 'supplier';

-- Herramientas para proveedor 'lauratorres'
INSERT INTO tools (available, daily_cost, stock, category_id, user_id, name, description, image_url)
SELECT true, 45.00, 2, cat.id, u.id, 'Multímetro digital', 'Para mediciones eléctricas precisas', 'https://example.com/multimetro.jpg '
FROM categories cat, users u
WHERE cat.name = 'Electricidad' AND u.username = 'lauratorres'
UNION ALL
SELECT true, 28.50, 3, cat.id, u.id, 'Taladro percutor', '24V con juego de brocas', 'https://example.com/taladropercut.jpg '
FROM categories cat, users u
WHERE cat.name = 'Herramientas Eléctricas' AND u.username = 'lauratorres';

-- 16. Insertar más clientes
INSERT INTO users (active, name, email, address, password, username, phone)
VALUES 
(true, 'Pedro Sánchez', 'pedro@example.com', 'Calle Real 789', '$2a$10$X5h/5X5h5X5h5X5h5X5h5e', 'pedrosanchez', '5558889999'),
(true, 'Sofía Fernández', 'sofia@example.com', 'Urbanización Norte', '$2a$10$X5h/5X5h5X5h5X5h5X5h5e', 'sofiafernandez', '5551112222')
ON CONFLICT (username) DO NOTHING;

-- 17. Asignar roles a nuevos clientes
INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username IN ('pedrosanchez', 'sofiafernandez') AND r.role_name = 'CUSTOMER'
ON CONFLICT (user_id, role_id) DO NOTHING;

-- 18. Insertar más reservaciones
INSERT INTO reservations (start_date, end_date, customer_id, tool_id, status)
SELECT CURRENT_DATE + 3, CURRENT_DATE + 6, c.id, t.id, 'PENDING'
FROM users c
JOIN tools t ON t.name = 'Llave inglesa ajustable'
WHERE c.username = 'mariagarcia'
UNION ALL
SELECT CURRENT_DATE + 1, CURRENT_DATE + 2, c.id, t.id, 'APPROVED'
FROM users c
JOIN tools t ON t.name = 'Rastrillo de jardín'
WHERE c.username = 'sofiafernandez'
UNION ALL
SELECT CURRENT_DATE + 4, CURRENT_DATE + 7, c.id, t.id, 'COMPLETED'
FROM users c
JOIN tools t ON t.name = 'Martillo de carpintero'
WHERE c.username = 'pedrosanchez'
UNION ALL
SELECT CURRENT_DATE + 5, CURRENT_DATE + 8, c.id, t.id, 'CANCELED'
FROM users c
JOIN tools t ON t.name = 'Taladro inalámbrico'
WHERE c.username = 'carloslopez';

-- 19. Insertar más pagos
INSERT INTO payments (amount, reservation_id, payment_method, transaction_id, status)
SELECT (r.end_date - r.start_date) * t.daily_cost, r.id, 'PAYPAL', 'PP' || LPAD(r.id::text, 6, '0'), 'COMPLETED'
FROM reservations r
JOIN tools t ON r.tool_id = t.id
WHERE r.status = 'APPROVED'
UNION ALL
SELECT (r.end_date - r.start_date) * t.daily_cost, r.id, 'BANK_TRANSFER', 'TR' || LPAD(r.id::text, 6, '0'), 'PROCESSING'
FROM reservations r
JOIN tools t ON r.tool_id = t.id
WHERE r.status = 'PENDING';

-- 20. Insertar más facturas
INSERT INTO invoices (payment_id, issue_date, details)
SELECT p.id, CURRENT_TIMESTAMP, 'Factura por alquiler de ' || t.name || ' desde ' || r.start_date || ' hasta ' || r.end_date
FROM payments p
JOIN reservations r ON p.reservation_id = r.id
JOIN tools t ON r.tool_id = t.id
WHERE p.status = 'COMPLETED'
ON CONFLICT (payment_id) DO NOTHING;

-- 21. Insertar más devoluciones
INSERT INTO "returns" (reservation_id, return_date, comments)
SELECT r.id, CURRENT_TIMESTAMP + INTERVAL '1 day', 'Herramienta devuelta con ligeros desgastes'
FROM reservations r
WHERE r.status = 'COMPLETED'
UNION ALL
SELECT r.id, CURRENT_TIMESTAMP, 'No se devolvió por cancelación'
FROM reservations r
WHERE r.status = 'CANCELED'
ON CONFLICT (reservation_id) DO NOTHING;

-- 22. Insertar más notificaciones
INSERT INTO notifications (user_id, message, "read", creation_date)
SELECT u.id, 'Nuevo pago realizado para su herramienta: ' || t.name, false, CURRENT_TIMESTAMP
FROM users u
JOIN tools t ON u.id = t.user_id
JOIN reservations r ON t.id = r.tool_id
JOIN payments p ON r.id = p.reservation_id
WHERE p.status = 'COMPLETED'
GROUP BY u.id, t.name
UNION ALL
SELECT u.id, 'Tiene una nueva reserva pendiente por aprobar', false, CURRENT_TIMESTAMP
FROM users u
WHERE EXISTS (
    SELECT 1 FROM reservations r 
    WHERE r.status = 'PENDING' AND r.tool_id IN (
        SELECT id FROM tools WHERE user_id = u.id
    )
);

-- 23. Insertar más reportes de daño
INSERT INTO damage_reports (reservation_id, description, repair_cost, resolved, report_date)
SELECT r.id, 'Motor sobrecalentado', 20.00, true, CURRENT_TIMESTAMP
FROM reservations r
JOIN tools t ON r.tool_id = t.id
WHERE t.name = 'Taladro inalámbrico'
UNION ALL
SELECT r.id, 'Corte en la punta', 8.50, false, CURRENT_TIMESTAMP
FROM reservations r
JOIN tools t ON r.tool_id = t.id
WHERE t.name = 'Sierra circular eléctrica';