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
    descripcion TEXT,
    es_default BOOLEAN
) ENGINE=InnoDB; 