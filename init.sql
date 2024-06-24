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

-- Insertar productos
INSERT INTO products (name, price) VALUES
                                       ('Producto A', 10.00),
                                       ('Producto B', 15.50),
                                       ('Producto C', 20.00),
                                       ('Producto D', 25.00),
                                       ('Producto E', 30.00);

-- Insertar cotizaciones
INSERT INTO quotations DEFAULT VALUES; -- Cotización 1
INSERT INTO quotations DEFAULT VALUES; -- Cotización 2
INSERT INTO quotations DEFAULT VALUES; -- Cotización 3
INSERT INTO quotations DEFAULT VALUES; -- Cotización 4
INSERT INTO quotations DEFAULT VALUES; -- Cotización 5

-- Insertar items de cotización
INSERT INTO quotation_items (quotation_id, product_id, quantity) VALUES
                                                                     (1, 1, 2), -- 2 x Producto A
                                                                     (1, 2, 1), -- 1 x Producto B
                                                                     (2, 2, 3), -- 3 x Producto B
                                                                     (2, 3, 2), -- 2 x Producto C
                                                                     (3, 1, 1), -- 1 x Producto A
                                                                     (3, 4, 2), -- 2 x Producto D
                                                                     (4, 3, 1), -- 1 x Producto C
                                                                     (4, 5, 1), -- 1 x Producto E
                                                                     (5, 1, 3), -- 3 x Producto A
                                                                     (5, 5, 2); -- 2 x Producto E

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
