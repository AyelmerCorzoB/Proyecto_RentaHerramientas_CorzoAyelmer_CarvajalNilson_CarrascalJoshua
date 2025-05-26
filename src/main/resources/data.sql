
INSERT INTO roles (role_name, description) VALUES 
('ADMIN', 'Administrador del sistema'),
('CUSTOMER', 'Usuario Cliente'),
('SUPPLIER', 'Usuario Proveedor')
ON CONFLICT (role_name) DO NOTHING;


INSERT INTO users (active, name, email, address, password, username, phone, registration_date) 
VALUES (true, 'Admin Principal', 'admin@alkile.com', 'Oficina Central', '$2a$10$X5h/5X5h5X5h5X5h5X5h5e', 'admin', '5550000001', CURRENT_TIMESTAMP)
ON CONFLICT (username) DO NOTHING;


INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'admin' AND r.role_name = 'ADMIN'
ON CONFLICT (user_id, role_id) DO NOTHING;


INSERT INTO users (active, name, email, address, password, username, phone, registration_date) 
VALUES (true, 'Proveedor Ejemplo', 'supplier@alkile.com', 'Almacén Principal', '$2a$10$X5h/5X5h5X5h5X5h5X5h5e', 'supplier', '5550000002', CURRENT_TIMESTAMP)
ON CONFLICT (username) DO NOTHING;


INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'supplier' AND r.role_name = 'SUPPLIER'
ON CONFLICT (user_id, role_id) DO NOTHING;


INSERT INTO suppliers (user_id, tax_id, company, rating, created_at, updeted_at)
SELECT u.id, '1234567890123', 'Herramientas Pro S.A.', 4.5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM users u WHERE u.username = 'supplier'
ON CONFLICT (user_id) DO NOTHING;


INSERT INTO categories (name, description, created_at, updeted_at) VALUES
('Herramientas Manuales', 'Herramientas que se operan manualmente sin necesidad de energía eléctrica', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Herramientas Eléctricas', 'Herramientas que funcionan con energía eléctrica', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Jardinería', 'Herramientas para el cuidado y mantenimiento de jardines', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Construcción', 'Herramientas para trabajos de construcción', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Pintura', 'Herramientas y equipos para trabajos de pintura', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING;


INSERT INTO users (active, name, email, address, password, username, phone, registration_date) VALUES
(true, 'Juan Pérez', 'juan@example.com', 'Calle Falsa 123', '$2a$10$X5h/5X5h5X5h5X5h5X5h5e', 'juanperez', '5551234567', CURRENT_TIMESTAMP),
(true, 'María García', 'maria@example.com', 'Avenida Siempre Viva 456', '$2a$10$X5h/5X5h5X5h5X5h5X5h5e', 'mariagarcia', '5557654321', CURRENT_TIMESTAMP),
(true, 'Carlos López', 'carlos@example.com', 'Boulevard de los Sueños 789', '$2a$10$X5h/5X5h5X5h5X5h5X5h5e', 'carloslopez', '5559876543', CURRENT_TIMESTAMP)
ON CONFLICT (username) DO NOTHING;


INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username IN ('juanperez', 'mariagarcia', 'carloslopez') AND r.role_name = 'CUSTOMER'
ON CONFLICT (user_id, role_id) DO NOTHING;


INSERT INTO customers (user_id, tax_id, created_at, updeted_at) 
SELECT u.id, '987654321' || LPAD(ROW_NUMBER() OVER(ORDER BY u.id)::text, 4, '0'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP 
FROM users u WHERE u.username IN ('juanperez', 'mariagarcia', 'carloslopez')
ON CONFLICT (user_id) DO NOTHING;


INSERT INTO customer_preferences (customer_id, preference) 
SELECT c.user_id, 'Herramientas Manuales' FROM customers c JOIN users u ON c.user_id = u.id WHERE u.username = 'juanperez'
ON CONFLICT (customer_id, preference) DO NOTHING;

INSERT INTO customer_preferences (customer_id, preference) 
SELECT c.user_id, 'Jardinería' FROM customers c JOIN users u ON c.user_id = u.id WHERE u.username = 'juanperez'
ON CONFLICT (customer_id, preference) DO NOTHING;

INSERT INTO customer_preferences (customer_id, preference) 
SELECT c.user_id, 'Herramientas Eléctricas' FROM customers c JOIN users u ON c.user_id = u.id WHERE u.username = 'mariagarcia'
ON CONFLICT (customer_id, preference) DO NOTHING;

INSERT INTO customer_preferences (customer_id, preference) 
SELECT c.user_id, 'Construcción' FROM customers c JOIN users u ON c.user_id = u.id WHERE u.username = 'carloslopez'
ON CONFLICT (customer_id, preference) DO NOTHING;


INSERT INTO tools (available, daily_cost, stock, category_id, supplier_id, name, description, image_url, created_at, updeted_at)
SELECT 
    true, 15.50, 3, cat.id, sup.id, 
    'Martillo de carpintero', 'Martillo profesional con mango de fibra de vidrio', 'https://example.com/martillo.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM categories cat, suppliers sup 
WHERE cat.name = 'Herramientas Manuales' AND EXISTS (SELECT 1 FROM suppliers);

INSERT INTO tools (available, daily_cost, stock, category_id, supplier_id, name, description, image_url, created_at, updeted_at)
SELECT 
    true, 25.00, 2, cat.id, sup.id,
    'Taladro inalámbrico', 'Taladro de 18V con 2 baterías y maletín', 'https://example.com/taladro.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM categories cat, suppliers sup 
WHERE cat.name = 'Herramientas Eléctricas' AND EXISTS (SELECT 1 FROM suppliers);

INSERT INTO tools (available, daily_cost, stock, category_id, supplier_id, name, description, image_url, created_at, updeted_at)
SELECT 
    true, 12.00, 5, cat.id, sup.id,
    'Tijeras de podar', 'Tijeras profesionales para podar arbustos', 'https://example.com/tijeras.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM categories cat, suppliers sup 
WHERE cat.name = 'Jardinería' AND EXISTS (SELECT 1 FROM suppliers);

INSERT INTO tools (available, daily_cost, stock, category_id, supplier_id, name, description, image_url, created_at, updeted_at)
SELECT 
    true, 40.00, 1, cat.id, sup.id,
    'Cortadora de cerámica', 'Cortadora profesional para cerámica y azulejos', 'https://example.com/cortadora.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM categories cat, suppliers sup 
WHERE cat.name = 'Construcción' AND EXISTS (SELECT 1 FROM suppliers);

INSERT INTO tools (available, daily_cost, stock, category_id, supplier_id, name, description, image_url, created_at, updeted_at)
SELECT 
    true, 18.75, 4, cat.id, sup.id,
    'Rodillo profesional', 'Kit de rodillos para pintura de diferentes tamaños', 'https://example.com/rodillos.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM categories cat, suppliers sup 
WHERE cat.name = 'Pintura' AND EXISTS (SELECT 1 FROM suppliers);


INSERT INTO reservations (start_date, end_date, customer_id, tool_id, status, created_at, updeted_at)
SELECT 
    CURRENT_DATE + 1, CURRENT_DATE + 3, 
    c.user_id, 
    (SELECT id FROM tools WHERE name = 'Martillo de carpintero' LIMIT 1), 
    'APPROVED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM customers c JOIN users u ON c.user_id = u.id WHERE u.username = 'juanperez'
AND EXISTS (SELECT 1 FROM tools WHERE name = 'Martillo de carpintero');

INSERT INTO reservations (start_date, end_date, customer_id, tool_id, status, created_at, updeted_at)
SELECT 
    CURRENT_DATE + 2, CURRENT_DATE + 5, 
    c.user_id,
    (SELECT id FROM tools WHERE name = 'Taladro inalámbrico' LIMIT 1),
    'PENDING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM customers c JOIN users u ON c.user_id = u.id WHERE u.username = 'mariagarcia'
AND EXISTS (SELECT 1 FROM tools WHERE name = 'Taladro inalámbrico');

INSERT INTO reservations (start_date, end_date, customer_id, tool_id, status, created_at, updeted_at)
SELECT 
    CURRENT_DATE, CURRENT_DATE + 7,
    c.user_id,
    (SELECT id FROM tools WHERE name = 'Cortadora de cerámica' LIMIT 1),
    'COMPLETED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM customers c JOIN users u ON c.user_id = u.id WHERE u.username = 'carloslopez'
AND EXISTS (SELECT 1 FROM tools WHERE name = 'Cortadora de cerámica');


INSERT INTO payments (amount, reservation_id, payment_method, transaction_id, status, payment_date, created_at, updeted_at)
SELECT 
    31.00, r.id, 'CREDIT_CARD', 'TXN' || LPAD(r.id::text, 6, '0'), 'COMPLETED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM reservations r JOIN tools t ON r.tool_id = t.id WHERE t.name = 'Martillo de carpintero'
ON CONFLICT (reservation_id) DO NOTHING;

INSERT INTO payments (amount, reservation_id, payment_method, transaction_id, status, payment_date, created_at, updeted_at)
SELECT 
    75.00, r.id, 'TRANSFER', 'TXN' || LPAD(r.id::text, 6, '1'), 'COMPLETED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM reservations r JOIN tools t ON r.tool_id = t.id WHERE t.name = 'Cortadora de cerámica'
ON CONFLICT (reservation_id) DO NOTHING;


INSERT INTO invoices (payment_id, issue_date, details)
SELECT 
    p.id, CURRENT_TIMESTAMP, 'Factura por alquiler de ' || t.name || ' por ' || 
    (r.end_date - r.start_date) || ' días'
FROM payments p 
JOIN reservations r ON p.reservation_id = r.id 
JOIN tools t ON r.tool_id = t.id
WHERE t.name IN ('Martillo de carpintero', 'Cortadora de cerámica')
ON CONFLICT (payment_id) DO NOTHING;


INSERT INTO returns (reservation_id, return_date, comments)
SELECT 
    r.id, CURRENT_TIMESTAMP, 'Herramienta devuelta en buen estado'
FROM reservations r 
WHERE r.status = 'COMPLETED' AND r.tool_id IN (SELECT id FROM tools WHERE name = 'Cortadora de cerámica')
ON CONFLICT (reservation_id) DO NOTHING;


INSERT INTO notifications (user_id, message, read, created_at, updeted_at)
SELECT 
    u.id, 'Su reserva del martillo ha sido aprobada', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM users u WHERE u.username = 'juanperez'
ON CONFLICT DO NOTHING;

INSERT INTO notifications (user_id, message, read, created_at, updeted_at)
SELECT 
    u.id, 'Su reserva del taladro está pendiente de aprobación', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM users u WHERE u.username = 'mariagarcia'
ON CONFLICT DO NOTHING;

INSERT INTO notifications (user_id, message, read, created_at, updeted_at)
SELECT 
    u.id, 'Nuevo usuario registrado: carloslopez', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM users u WHERE u.username = 'admin'
ON CONFLICT DO NOTHING;


INSERT INTO damage_reports (reservation_id, description, repair_cost, resolved, report_date, created_at, updeted_at)
SELECT 
    r.id, 'Mango rajado', 5.50, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM reservations r 
WHERE r.tool_id IN (SELECT id FROM tools WHERE name = 'Martillo de carpintero')
ON CONFLICT (reservation_id) DO NOTHING;