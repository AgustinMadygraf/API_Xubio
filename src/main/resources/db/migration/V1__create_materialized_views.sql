-- Vista materializada para Centro de Costo
CREATE TABLE mv_centros_costo_summary (
    centro_de_costo_id INT PRIMARY KEY,
    codigo VARCHAR(255),
    nombre VARCHAR(255),
    total_registros INT,
    ultima_actualizacion TIMESTAMP,
    INDEX idx_codigo (codigo),
    INDEX idx_nombre (nombre)
) ENGINE=InnoDB;

-- Vista materializada para Lista de Precios
CREATE TABLE mv_lista_precios_summary (
    lista_precio_id INT PRIMARY KEY,
    nombre VARCHAR(255),
    activo BOOLEAN,
    tipo INT,
    cantidad_items INT,
    ultima_actualizacion TIMESTAMP,
    INDEX idx_nombre (nombre),
    INDEX idx_activo (activo)
) ENGINE=InnoDB;

-- Crear las tablas base
CREATE TABLE IF NOT EXISTS centros_de_costo (
    centro_de_costo_id INT PRIMARY KEY AUTO_INCREMENT,
    codigo VARCHAR(255),
    nombre VARCHAR(255)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS listas_precio (
    lista_precio_id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    activo BOOLEAN,
    tipo INT,
    cantidad_items INT,
    ultima_actualizacion TIMESTAMP,
    descripcion TEXT,
    es_default BOOLEAN,
    moneda_id1 INT,
    moneda_id2 INT,
    iva INT,
    ocultar_sin_precio BOOLEAN,
    INDEX idx_nombre (nombre),
    INDEX idx_activo (activo)
) ENGINE=InnoDB;

-- Tabla de clientes
CREATE TABLE IF NOT EXISTS clientes (
    cliente_id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    INDEX idx_cliente_nombre (nombre)
) ENGINE=InnoDB;

-- Vistas para resúmenes
CREATE VIEW v_centros_costo_summary AS
SELECT 
    cc.centro_de_costo_id,
    cc.codigo,
    cc.nombre,
    COUNT(*) as total_registros,
    NOW() as ultima_actualizacion
FROM centros_de_costo cc
GROUP BY cc.centro_de_costo_id;

CREATE VIEW v_lista_precios_summary AS
SELECT 
    lp.lista_precio_id,
    lp.nombre,
    lp.activo,
    lp.tipo,
    lp.cantidad_items,
    lp.ultima_actualizacion
FROM listas_precio lp; 