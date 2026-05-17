-- ============================================================
-- Base de datos: rrhh_ministerio
-- Sistema de Gestión Integral para RRHH
-- Ministerio de Gobierno, Infraestructura y Desarrollo Territorial
-- Autor: Rodrigo A. Quinteros
-- AP2 - Seminario de Práctica
-- ============================================================

-- ============================================================
-- 1. CREACIÓN DE BASE DE DATOS
-- ============================================================

CREATE DATABASE IF NOT EXISTS rrhh_ministerio
CHARACTER SET utf8mb4
COLLATE utf8mb4_spanish_ci;

USE rrhh_ministerio;

-- ============================================================
-- 2. ELIMINACIÓN DE TABLAS EXISTENTES
-- Se eliminan en orden inverso a las dependencias.
-- ============================================================

DROP TABLE IF EXISTS auditoria;
DROP TABLE IF EXISTS documentos_legajo;
DROP TABLE IF EXISTS licencias;
DROP TABLE IF EXISTS legajos;
DROP TABLE IF EXISTS agentes;
DROP TABLE IF EXISTS tipos_licencia;
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS roles;

-- ============================================================
-- 3. CREACIÓN DE TABLAS
-- ============================================================

CREATE TABLE roles (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(150)
);

CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    id_rol INT NOT NULL,
    usuario VARCHAR(50) NOT NULL UNIQUE,
    clave VARCHAR(100) NOT NULL,
    nombre_completo VARCHAR(100) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_usuarios_roles
        FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
);

CREATE TABLE agentes (
    id_agente INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(8) NOT NULL UNIQUE,
    cuil VARCHAR(20) NOT NULL UNIQUE,
    apellido VARCHAR(80) NOT NULL,
    nombre VARCHAR(80) NOT NULL,
    cargo VARCHAR(100),
    reparticion VARCHAR(120),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE legajos (
    id_legajo INT AUTO_INCREMENT PRIMARY KEY,
    id_agente INT NOT NULL UNIQUE,
    numero_legajo VARCHAR(30) NOT NULL UNIQUE,
    fecha_alta DATE NOT NULL,
    situacion_revista VARCHAR(100),
    observaciones TEXT,
    CONSTRAINT fk_legajos_agentes
        FOREIGN KEY (id_agente) REFERENCES agentes(id_agente)
);

CREATE TABLE tipos_licencia (
    id_tipo_licencia INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(200),
    requiere_documentacion BOOLEAN NOT NULL DEFAULT FALSE,
    consume_saldo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE licencias (
    id_licencia INT AUTO_INCREMENT PRIMARY KEY,
    id_agente INT NOT NULL,
    id_tipo_licencia INT NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    dias_solicitados INT NOT NULL,
    estado VARCHAR(30) NOT NULL,
    observaciones TEXT,
    CONSTRAINT fk_licencias_agentes
        FOREIGN KEY (id_agente) REFERENCES agentes(id_agente),
    CONSTRAINT fk_licencias_tipos
        FOREIGN KEY (id_tipo_licencia) REFERENCES tipos_licencia(id_tipo_licencia)
);

CREATE TABLE documentos_legajo (
    id_documento INT AUTO_INCREMENT PRIMARY KEY,
    id_legajo INT NOT NULL,
    id_licencia INT NULL,
    nombre_archivo VARCHAR(150) NOT NULL,
    tipo_documento VARCHAR(80),
    ruta_archivo VARCHAR(255),
    fecha_carga DATE NOT NULL,
    CONSTRAINT fk_documentos_legajos
        FOREIGN KEY (id_legajo) REFERENCES legajos(id_legajo),
    CONSTRAINT fk_documentos_licencias
        FOREIGN KEY (id_licencia) REFERENCES licencias(id_licencia)
);

CREATE TABLE auditoria (
    id_auditoria INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    accion VARCHAR(100) NOT NULL,
    entidad_afectada VARCHAR(80) NOT NULL,
    id_entidad INT,
    fecha_hora DATETIME NOT NULL,
    CONSTRAINT fk_auditoria_usuarios
        FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

-- ============================================================
-- 4. INSERCIÓN DE REGISTROS DE PRUEBA
-- ============================================================

INSERT INTO roles (nombre, descripcion) VALUES
('Administrador', 'Usuario con permisos completos sobre el sistema'),
('Personal RRHH', 'Usuario operativo de la Oficina de Personal');

INSERT INTO usuarios (
    id_rol,
    usuario,
    clave,
    nombre_completo,
    activo
) VALUES
(1, 'admin', 'admin123', 'Administrador del Sistema', TRUE),
(2, 'rrhh', 'rrhh123', 'Personal de RRHH', TRUE);

INSERT INTO agentes (
    dni,
    cuil,
    apellido,
    nombre,
    cargo,
    reparticion,
    activo
) VALUES
('27949990', '20-27949990-1', 'Quinteros', 'Rodrigo', 'Administrativo', 'Oficina de Personal', TRUE),
('30111222', '20-30111222-3', 'Gonzalez', 'Mariana', 'Analista Administrativo', 'Dirección de Recursos Humanos', TRUE),
('28666777', '20-28666777-5', 'Pereyra', 'Carlos', 'Auxiliar Administrativo', 'Subsecretaría Administrativa', TRUE);

INSERT INTO legajos (
    id_agente,
    numero_legajo,
    fecha_alta,
    situacion_revista,
    observaciones
) VALUES
(1, 'LEG-0001', '2020-03-10', 'Planta permanente', 'Legajo inicial del agente'),
(2, 'LEG-0002', '2021-07-15', 'Contratado', 'Legajo administrativo activo'),
(3, 'LEG-0003', '2019-11-05', 'Planta permanente', 'Sin observaciones');

INSERT INTO tipos_licencia (
    nombre,
    descripcion,
    requiere_documentacion,
    consume_saldo
) VALUES
('Licencia Anual Reglamentaria', 'Licencia anual ordinaria del agente', FALSE, TRUE),
('Licencia por Enfermedad', 'Licencia por razones de salud', TRUE, FALSE),
('Licencia por Estudio', 'Licencia por actividad académica', TRUE, TRUE),
('Licencia por Razones Particulares', 'Licencia por motivos particulares', FALSE, TRUE);

INSERT INTO licencias (
    id_agente,
    id_tipo_licencia,
    fecha_inicio,
    fecha_fin,
    dias_solicitados,
    estado,
    observaciones
) VALUES
(1, 1, '2026-05-20', '2026-05-21', 2, 'Registrada', 'Prueba de licencia anual reglamentaria'),
(2, 2, '2026-06-03', '2026-06-05', 3, 'Registrada', 'Licencia por enfermedad con documentación pendiente'),
(3, 3, '2026-08-10', '2026-08-10', 1, 'Registrada', 'Licencia por examen académico');

INSERT INTO documentos_legajo (
    id_legajo,
    id_licencia,
    nombre_archivo,
    tipo_documento,
    ruta_archivo,
    fecha_carga
) VALUES
(1, 1, 'solicitud_licencia_27949990.pdf', 'Solicitud de licencia', 'C:/TP2_RRHH/documentos/solicitud_licencia_27949990.pdf', '2026-05-16'),
(2, 2, 'certificado_medico_30111222.pdf', 'Certificado médico', 'C:/TP2_RRHH/documentos/certificado_medico_30111222.pdf', '2026-06-03');

INSERT INTO auditoria (
    id_usuario,
    accion,
    entidad_afectada,
    id_entidad,
    fecha_hora
) VALUES
(1, 'ALTA DE AGENTE', 'agentes', 1, NOW()),
(1, 'REGISTRO DE LICENCIA', 'licencias', 1, NOW()),
(2, 'CONSULTA DE LEGAJO', 'legajos', 1, NOW());

-- ============================================================
-- 5. CONSULTAS DE VERIFICACIÓN
-- ============================================================

-- 5.1 Mostrar tablas creadas
SHOW TABLES;

-- 5.2 Consultar roles
SELECT 
    id_rol,
    nombre,
    descripcion
FROM roles;

-- 5.3 Consultar usuarios registrados
SELECT 
    u.id_usuario,
    u.usuario,
    u.nombre_completo,
    r.nombre AS rol,
    u.activo
FROM usuarios u
INNER JOIN roles r
    ON u.id_rol = r.id_rol;

-- 5.4 Consultar agentes registrados
SELECT 
    id_agente,
    dni,
    cuil,
    apellido,
    nombre,
    cargo,
    reparticion,
    activo
FROM agentes;

-- 5.5 Consultar agentes con legajo asociado
SELECT 
    a.id_agente,
    a.dni,
    a.apellido,
    a.nombre,
    l.numero_legajo,
    l.fecha_alta,
    l.situacion_revista
FROM agentes a
INNER JOIN legajos l 
    ON a.id_agente = l.id_agente;

-- 5.6 Consultar tipos de licencia
SELECT 
    id_tipo_licencia,
    nombre,
    descripcion,
    requiere_documentacion,
    consume_saldo
FROM tipos_licencia;

-- 5.7 Consultar licencias registradas asociadas a agentes y tipos de licencia
SELECT 
    l.id_licencia,
    a.dni,
    a.apellido,
    a.nombre,
    tl.nombre AS tipo_licencia,
    l.fecha_inicio,
    l.fecha_fin,
    l.dias_solicitados,
    l.estado,
    l.observaciones
FROM licencias l
INNER JOIN agentes a 
    ON l.id_agente = a.id_agente
INNER JOIN tipos_licencia tl 
    ON l.id_tipo_licencia = tl.id_tipo_licencia;

-- 5.8 Consultar licencias de un agente específico por DNI
SELECT 
    l.id_licencia,
    a.dni,
    a.apellido,
    a.nombre,
    tl.nombre AS tipo_licencia,
    l.fecha_inicio,
    l.fecha_fin,
    l.dias_solicitados,
    l.estado
FROM licencias l
INNER JOIN agentes a 
    ON l.id_agente = a.id_agente
INNER JOIN tipos_licencia tl 
    ON l.id_tipo_licencia = tl.id_tipo_licencia
WHERE a.dni = '27949990';

-- 5.9 Consultar documentos asociados a legajos
SELECT
    d.id_documento,
    a.dni,
    a.apellido,
    a.nombre,
    l.numero_legajo,
    d.nombre_archivo,
    d.tipo_documento,
    d.ruta_archivo,
    d.fecha_carga
FROM documentos_legajo d
INNER JOIN legajos l
    ON d.id_legajo = l.id_legajo
INNER JOIN agentes a
    ON l.id_agente = a.id_agente;

-- 5.10 Consultar auditoría
SELECT
    au.id_auditoria,
    u.usuario,
    au.accion,
    au.entidad_afectada,
    au.id_entidad,
    au.fecha_hora
FROM auditoria au
INNER JOIN usuarios u
    ON au.id_usuario = u.id_usuario;

-- ============================================================
-- 6. PRUEBA DE BORRADO CONTROLADO
-- ============================================================

-- 6.1 Insertar licencia temporal para prueba de borrado
INSERT INTO licencias (
    id_agente,
    id_tipo_licencia,
    fecha_inicio,
    fecha_fin,
    dias_solicitados,
    estado,
    observaciones
) VALUES (
    1,
    4,
    '2026-09-01',
    '2026-09-01',
    1,
    'Temporal',
    'Registro temporal para prueba de borrado'
);

-- 6.2 Consultar registro temporal insertado
SELECT 
    id_licencia,
    id_agente,
    id_tipo_licencia,
    fecha_inicio,
    fecha_fin,
    dias_solicitados,
    estado,
    observaciones
FROM licencias
WHERE estado = 'Temporal';

-- 6.3 Borrar registro temporal
DELETE FROM licencias
WHERE estado = 'Temporal'
AND observaciones = 'Registro temporal para prueba de borrado';

-- 6.4 Verificar que el registro temporal fue eliminado
SELECT 
    id_licencia,
    id_agente,
    id_tipo_licencia,
    fecha_inicio,
    fecha_fin,
    dias_solicitados,
    estado,
    observaciones
FROM licencias
WHERE estado = 'Temporal';

-- ============================================================
-- FIN DEL ARCHIVO
-- ============================================================