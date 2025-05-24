INSERT INTO roles (role_name, description) VALUES 
('ADMIN', 'Administrador del sistema'),
('CUSTOMER', 'Usuario Cliente'),
('SUPPLIER', 'Usuario Proveedor');

INSERT INTO users_roles (user_id, role_id) 
VALUES (
  (SELECT id FROM users WHERE username = 'admin'),
  (SELECT id FROM roles WHERE role_name = 'ADMIN')
);

INSERT INTO users_roles (user_id, role_id) 
VALUES (
  (SELECT id FROM users WHERE username = 'supplier'),
  (SELECT id FROM roles WHERE role_name = 'SUPPLIER')
);

INSERT INTO suppliers (user_id, tax_id, company, rating, created_at, updeted_at)
VALUES (
  (SELECT id FROM users WHERE username = 'supplier'),
  '1234567890123',
  'Herramientas Pro S.A.',
  4.5,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
);

INSERT INTO categories (name, description, created_at, updeted_at) VALUES
('Herramientas Manuales', 'Herramientas que se operan manualmente sin necesidad de energia electrica', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Herramientas Electrica', 'Herramientas que funcionan con energia electrica', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Jardineria', 'Herramientas para el cuidado y mantenimiento de jardines', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Construccion', 'Herramientas para trabajos de construccion', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Pintura', 'Herramientas y equipos para trabajos de pintura', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO users (active, name, email, address, password, username, phone, registration_date) VALUES
(true, 'Juan Perez', 'juan@example.com', 'Calle Falsa 123', '$2a$10$X5h/5X5h5X5h5X5h5X5h5e', 'juanperez', '5551234567', CURRENT_TIMESTAMP),
(true, 'Maria Garcia', 'maria@example.com', 'Avenida Siempre Viva 456', '$2a$10$X5h/5X5h5X5h5X5h5X5h5e', 'mariagarcia', '5557654321', CURRENT_TIMESTAMP),
(true, 'Carlos Lopez', 'carlos@example.com', 'Boulevard de los Sueños 789', '$2a$10$X5h/5X5h5X5h5X5h5X5h5e', 'carloslopez', '5559876543', CURRENT_TIMESTAMP);

INSERT INTO users_roles (user_id, role_id) 
SELECT u.id, r.id 
FROM users u, roles r 
WHERE u.username IN ('juanperez', 'mariagarcia', 'carloslopez') 
AND r.role_name = 'CUSTOMER';

INSERT INTO customers (user_id, tax_id, created_at, updeted_at) 
SELECT id, '9876543210123', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP 
FROM users 
WHERE username IN ('juanperez', 'mariagarcia', 'carloslopez');

INSERT INTO customer_preferences (customer_id, preference) 
VALUES 
((SELECT user_id FROM customers WHERE user_id = (SELECT id FROM users WHERE username = 'juanperez')), 'Herramientas Manuales'),
((SELECT user_id FROM customers WHERE user_id = (SELECT id FROM users WHERE username = 'juanperez')), 'Jardineria'),
((SELECT user_id FROM customers WHERE user_id = (SELECT id FROM users WHERE username = 'mariagarcia')), 'Herramientas Electrica'),
((SELECT user_id FROM customers WHERE user_id = (SELECT id FROM users WHERE username = 'carloslopez')), 'Construccion');

INSERT INTO tools (available, daily_cost, stock, category_id, supplier_id, name, description, image_url, created_at, updeted_at) VALUES
(true, 15.50, 3, (SELECT id FROM categories WHERE name = 'Herramientas Manuales'), (SELECT user_id FROM suppliers LIMIT 1), 'Martillo de carpintero', 'Martillo profesional con mango de fibra de vidrio', 'https://example.com/martillo.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(true, 25.00, 2, (SELECT id FROM categories WHERE name = 'Herramientas Electrica'), (SELECT user_id FROM suppliers LIMIT 1), 'Taladro inalambrico', 'Taladro de 18V con 2 baterias y maletin', 'https://example.com/taladro.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(true, 12.00, 5, (SELECT id FROM categories WHERE name = 'Jardineria'), (SELECT user_id FROM suppliers LIMIT 1), 'Tijeras de podar', 'Tijeras profesionales para podar arbustos', 'https://example.com/tijeras.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(true, 40.00, 1, (SELECT id FROM categories WHERE name = 'Construccion'), (SELECT user_id FROM suppliers LIMIT 1), 'Cortadora de ceramica', 'Cortadora profesional para ceramica y azulejos', 'https://example.com/cortadora.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(true, 18.75, 4, (SELECT id FROM categories WHERE name = 'Pintura'), (SELECT user_id FROM suppliers LIMIT 1), 'Rodillo profesional', 'Kit de rodillos para pintura de diferentes tamaños', 'https://example.com/rodillos.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO reservations (start_date, end_date, customer_id, tool_id, status, created_at, updeted_at) VALUES
(CURRENT_DATE + 1, CURRENT_DATE + 3, (SELECT user_id FROM customers WHERE user_id = (SELECT id FROM users WHERE username = 'juanperez')), (SELECT id FROM tools WHERE name = 'Martillo de carpintero'), 'APPROVED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(CURRENT_DATE + 2, CURRENT_DATE + 5, (SELECT user_id FROM customers WHERE user_id = (SELECT id FROM users WHERE username = 'mariagarcia')), (SELECT id FROM tools WHERE name = 'Taladro inalambrico'), 'PENDING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(CURRENT_DATE, CURRENT_DATE + 7, (SELECT user_id FROM customers WHERE user_id = (SELECT id FROM users WHERE username = 'carloslopez')), (SELECT id FROM tools WHERE name = 'Cortadora de ceramica'), 'COMPLETED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO payments (amount, reservation_id, payment_method, transaction_id, status, payment_date, created_at, updeted_at) VALUES
(31.00, (SELECT id FROM reservations WHERE tool_id = (SELECT id FROM tools WHERE name = 'Martillo de carpintero')), 'TARJETA_CREDITO', 'TXN123456', 'COMPLETED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(75.00, (SELECT id FROM reservations WHERE tool_id = (SELECT id FROM tools WHERE name = 'Cortadora de ceramica')), 'TRANSFERENCIA', 'TXN789012', 'COMPLETED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO invoices (payment_id, issue_date, details) VALUES
((SELECT id FROM payments WHERE transaction_id = 'TXN123456'), CURRENT_TIMESTAMP, 'Factura por alquiler de martillo de carpintero por 2 días'),
((SELECT id FROM payments WHERE transaction_id = 'TXN789012'), CURRENT_TIMESTAMP, 'Factura por alquiler de cortadora de ceramica por 7 días');

INSERT INTO returns (reservation_id, return_date, comments) VALUES
((SELECT id FROM reservations WHERE tool_id = (SELECT id FROM tools WHERE name = 'Cortadora de ceramica')), CURRENT_TIMESTAMP, 'Herramienta devuelta en buen estado');

INSERT INTO notifications (user_id, message, read, created_at, updeted_at) VALUES
((SELECT id FROM users WHERE username = 'juanperez'), 'Su reserva del martillo ha sido aprobada', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM users WHERE username = 'mariagarcia'), 'Su reserva del taladro está pendiente de aprobación', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM users WHERE username = 'admin'), 'Nuevo usuario registrado: carloslopez', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO damage_reports (reservation_id, description, repair_cost, resolved, report_date, created_at, updeted_at) VALUES
((SELECT id FROM reservations WHERE tool_id = (SELECT id FROM tools WHERE name = 'Martillo de carpintero')), 'Mango rajado', 5.50, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
