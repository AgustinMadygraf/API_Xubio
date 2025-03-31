CREATE TABLE IF NOT EXISTS mi_empresa (
    nombre_empresa VARCHAR(255) PRIMARY KEY,
    categoria_fiscal INT,
    tipo_de_cuenta INT,
    ingresos_brutos VARCHAR(255),
    fecha_inicio_actividad DATE,
    direccion VARCHAR(255),
    pais INT,
    provincia INT,
    localidad INT,
    telefono VARCHAR(255),
    email VARCHAR(255),
    facturam INT,
    cuit VARCHAR(11) UNIQUE,
    INDEX idx_empresa_cuit (cuit)
) ENGINE=InnoDB; 