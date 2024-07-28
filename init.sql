-- Eliminar vistas si existen
DROP VIEW IF EXISTS cotizaciones_completas CASCADE;
DROP VIEW IF EXISTS productos_con_cambios CASCADE;
DROP VIEW IF EXISTS cotizaciones_con_cambios CASCADE;

-- Crear las tablas
CREATE TABLE IF NOT EXISTS products (
                                        id SERIAL PRIMARY KEY,
                                        name VARCHAR(100),
                                        price NUMERIC(10, 2)
);

CREATE TABLE IF NOT EXISTS quotations (
                                          id SERIAL PRIMARY KEY,
                                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                          total NUMERIC(10, 2) DEFAULT 0
);

CREATE TABLE IF NOT EXISTS quotation_items (
                                               id SERIAL PRIMARY KEY,
                                               quotation_id INT REFERENCES quotations(id),
                                               product_id INT REFERENCES products(id),
                                               quantity INT
);

CREATE TABLE IF NOT EXISTS products_log (
                                            id SERIAL PRIMARY KEY,
                                            product_id INT REFERENCES products(id) ON DELETE SET NULL,
                                            action VARCHAR(50),
                                            action_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                            old_name VARCHAR(100),
                                            new_name VARCHAR(100),
                                            old_price NUMERIC(10, 2),
                                            new_price NUMERIC(10, 2)
);

CREATE TABLE IF NOT EXISTS quotations_log (
                                              id SERIAL PRIMARY KEY,
                                              quotation_id INT REFERENCES quotations(id) ON DELETE SET NULL,
                                              action VARCHAR(50),
                                              action_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                              old_total NUMERIC(10, 2),
                                              new_total NUMERIC(10, 2)
);

-- Crear función para calcular el total
CREATE OR REPLACE FUNCTION calculate_total() RETURNS TRIGGER AS $$
BEGIN
    UPDATE quotations
    SET total = (
        SELECT SUM(p.price * qi.quantity)
        FROM quotation_items qi
                 JOIN products p ON qi.product_id = p.id
        WHERE qi.quotation_id = NEW.quotation_id
    )
    WHERE id = NEW.quotation_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Crear trigger para ejecutar la función después de cada inserción o actualización en quotation_items
DROP TRIGGER IF EXISTS trg_calculate_total ON quotation_items;
CREATE TRIGGER trg_calculate_total
    AFTER INSERT OR UPDATE ON quotation_items
    FOR EACH ROW EXECUTE FUNCTION calculate_total();

-- Crear función para registrar cambios en productos (inserciones y actualizaciones)
CREATE OR REPLACE FUNCTION log_product_changes_insert_update() RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'UPDATE' THEN
        INSERT INTO products_log (product_id, action, old_name, new_name, old_price, new_price)
        VALUES (OLD.id, 'UPDATE', OLD.name, NEW.name, OLD.price, NEW.price);
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO products_log (product_id, action, new_name, new_price)
        VALUES (NEW.id, 'INSERT', NEW.name, NEW.price);
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Crear función para registrar cambios en productos (eliminaciones)
CREATE OR REPLACE FUNCTION log_product_changes_delete() RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO products_log (product_id, action, old_name, old_price)
    VALUES (OLD.id, 'DELETE', OLD.name, OLD.price);
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

-- Crear triggers para registrar cambios en productos
DROP TRIGGER IF EXISTS trg_log_product_changes_insert_update ON products;
CREATE TRIGGER trg_log_product_changes_insert_update
    AFTER INSERT OR UPDATE ON products
    FOR EACH ROW EXECUTE FUNCTION log_product_changes_insert_update();

DROP TRIGGER IF EXISTS trg_log_product_changes_delete ON products;
CREATE TRIGGER trg_log_product_changes_delete
    BEFORE DELETE ON products
    FOR EACH ROW EXECUTE FUNCTION log_product_changes_delete();

-- Crear función para registrar cambios en cotizaciones (inserciones y actualizaciones)
CREATE OR REPLACE FUNCTION log_quotation_changes_insert_update() RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'UPDATE' THEN
        INSERT INTO quotations_log (quotation_id, action, old_total, new_total)
        VALUES (OLD.id, 'UPDATE', OLD.total, NEW.total);
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO quotations_log (quotation_id, action, new_total)
        VALUES (NEW.id, 'INSERT', NEW.total);
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Crear función para registrar cambios en cotizaciones (eliminaciones)
CREATE OR REPLACE FUNCTION log_quotation_changes_delete() RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO quotations_log (quotation_id, action, old_total)
    VALUES (OLD.id, 'DELETE', OLD.total);
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

-- Crear triggers para registrar cambios en cotizaciones
DROP TRIGGER IF EXISTS trg_log_quotation_changes_insert_update ON quotations;
CREATE TRIGGER trg_log_quotation_changes_insert_update
    AFTER INSERT OR UPDATE ON quotations
    FOR EACH ROW EXECUTE FUNCTION log_quotation_changes_insert_update();

DROP TRIGGER IF EXISTS trg_log_quotation_changes_delete ON quotations;
CREATE TRIGGER trg_log_quotation_changes_delete
    BEFORE DELETE ON quotations
    FOR EACH ROW EXECUTE FUNCTION log_quotation_changes_delete();

-- Insertar productos
/*
INSERT INTO products (name, price) VALUES
                                       ('Producto A', 10.00),
                                       ('Producto B', 15.50),
                                       ('Producto C', 20.00),
                                       ('Producto D', 25.00),
                                       ('Producto E', 30.00),
                                       ('Producto F', 35.00),
                                       ('Producto G', 40.00),
                                       ('Producto H', 45.00),
                                       ('Producto I', 50.00),
                                       ('Producto J', 55.00),
                                       ('Producto K', 60.00),
                                       ('Producto L', 65.00),
                                       ('Producto M', 70.00),
                                       ('Producto N', 75.00),
                                       ('Producto O', 80.00),
                                       ('Producto P', 85.00),
                                       ('Producto Q', 90.00),
                                       ('Producto R', 95.00),
                                       ('Producto S', 100.00),
                                       ('Producto T', 105.00);

-- Insertar cotizaciones
INSERT INTO quotations DEFAULT VALUES; -- Cotización 1
INSERT INTO quotations DEFAULT VALUES; -- Cotización 2
INSERT INTO quotations DEFAULT VALUES; -- Cotización 3
INSERT INTO quotations DEFAULT VALUES; -- Cotización 4
INSERT INTO quotations DEFAULT VALUES; -- Cotización 5
INSERT INTO quotations DEFAULT VALUES; -- Cotización 6
INSERT INTO quotations DEFAULT VALUES; -- Cotización 7
INSERT INTO quotations DEFAULT VALUES; -- Cotización 8
INSERT INTO quotations DEFAULT VALUES; -- Cotización 9
INSERT INTO quotations DEFAULT VALUES; -- Cotización 10
INSERT INTO quotations DEFAULT VALUES; -- Cotización 11
INSERT INTO quotations DEFAULT VALUES; -- Cotización 12
INSERT INTO quotations DEFAULT VALUES; -- Cotización 13
INSERT INTO quotations DEFAULT VALUES; -- Cotización 14
INSERT INTO quotations DEFAULT VALUES; -- Cotización 15
INSERT INTO quotations DEFAULT VALUES; -- Cotización 16
INSERT INTO quotations DEFAULT VALUES; -- Cotización 17
INSERT INTO quotations DEFAULT VALUES; -- Cotización 18
INSERT INTO quotations DEFAULT VALUES; -- Cotización 19
INSERT INTO quotations DEFAULT VALUES; -- Cotización 20

-- Insertar items de cotización
INSERT INTO quotation_items (quotation_id, product_id, quantity) VALUES
                                                                     (1, 1, 2),  -- Cotización 1, Producto 1, Cantidad 2
                                                                     (1, 2, 1),  -- Cotización 1, Producto 2, Cantidad 1
                                                                     (2, 2, 3),  -- Cotización 2, Producto 2, Cantidad 3
                                                                     (2, 3, 2),  -- Cotización 2, Producto 3, Cantidad 2
                                                                     (3, 1, 1),  -- Cotización 3, Producto 1, Cantidad 1
                                                                     (3, 4, 2),  -- Cotización 3, Producto 4, Cantidad 2
                                                                     (4, 3, 1),  -- Cotización 4, Producto 3, Cantidad 1
                                                                     (4, 5, 1),  -- Cotización 4, Producto 5, Cantidad 1
                                                                     (5, 1, 3),  -- Cotización 5, Producto 1, Cantidad 3
                                                                     (5, 5, 2),  -- Cotización 5, Producto 5, Cantidad 2
                                                                     (6, 6, 2),  -- Cotización 6, Producto 6, Cantidad 2
                                                                     (6, 7, 1),  -- Cotización 6, Producto 7, Cantidad 1
                                                                     (7, 8, 4),  -- Cotización 7, Producto 8, Cantidad 4
                                                                     (7, 9, 3),  -- Cotización 7, Producto 9, Cantidad 3
                                                                     (8, 10, 2), -- Cotización 8, Producto 10, Cantidad 2
                                                                     (8, 11, 1), -- Cotización 8, Producto 11, Cantidad 1
                                                                     (9, 12, 1), -- Cotización 9, Producto 12, Cantidad 1
                                                                     (9, 13, 3), -- Cotización 9, Producto 13, Cantidad 3
                                                                     (10, 14, 2),-- Cotización 10, Producto 14, Cantidad 2
                                                                     (10, 15, 2),-- Cotización 10, Producto 15, Cantidad 2
                                                                     (11, 16, 2),-- Cotización 11, Producto 16, Cantidad 2
                                                                     (11, 17, 1),-- Cotización 11, Producto 17, Cantidad 1
                                                                     (12, 18, 3),-- Cotización 12, Producto 18, Cantidad 3
                                                                     (12, 19, 2),-- Cotización 12, Producto 19, Cantidad 2
                                                                     (13, 20, 1),-- Cotización 13, Producto 20, Cantidad 1
                                                                     (13, 1, 2), -- Cotización 13, Producto 1, Cantidad 2
                                                                     (14, 2, 1), -- Cotización 14, Producto 2, Cantidad 1
                                                                     (14, 3, 1), -- Cotización 14, Producto 3, Cantidad 1
                                                                     (15, 4, 2), -- Cotización 15, Producto 4, Cantidad 2
                                                                     (15, 5, 2), -- Cotización 15, Producto 5, Cantidad 2
                                                                     (16, 6, 2), -- Cotización 16, Producto 6, Cantidad 2
                                                                     (16, 7, 1), -- Cotización 16, Producto 7, Cantidad 1
                                                                     (17, 8, 4), -- Cotización 17, Producto 8, Cantidad 4
                                                                     (17, 9, 3), -- Cotización 17, Producto 9, Cantidad 3
                                                                     (18, 10, 2),-- Cotización 18, Producto 10, Cantidad 2
                                                                     (18, 11, 1),-- Cotización 18, Producto 11, Cantidad 1
                                                                     (19, 12, 1),-- Cotización 19, Producto 12, Cantidad 1
                                                                     (19, 13, 3),-- Cotización 19, Producto 13, Cantidad 3
                                                                     (20, 14, 2),-- Cotización 20, Producto 14, Cantidad 2
                                                                     (20, 15, 2);-- Cotización 20, Producto 15, Cantidad 2
*/
-- Crear vistas después de modificar las tablas
CREATE VIEW cotizaciones_completas AS
SELECT
    q.id AS cotizacion_id,
    q.created_at,
    q.total,
    p.id AS producto_id,
    p.name AS producto_nombre,
    p.price AS producto_precio,
    qi.quantity AS cantidad
FROM
    quotations q
        JOIN quotation_items qi ON q.id = qi.quotation_id
        JOIN products p ON qi.product_id = p.id;

CREATE VIEW productos_con_cambios AS
SELECT
    pl.id,
    pl.product_id,
    pl.action,
    pl.action_time,
    pl.old_name,
    pl.new_name,
    pl.old_price,
    pl.new_price
FROM
    products_log pl;

CREATE VIEW cotizaciones_con_cambios AS
SELECT
    ql.id,
    ql.quotation_id,
    ql.action,
    ql.action_time,
    ql.old_total,
    ql.new_total
FROM
    quotations_log ql;
