CREATE TABLE IF NOT EXISTS vendedores (
    vendedor_id BIGINT PRIMARY KEY,
    nombre VARCHAR(255),
    apellido VARCHAR(255),
    activo INT,
    INDEX idx_vendedor_nombre_apellido (nombre, apellido)
) ENGINE=InnoDB; 