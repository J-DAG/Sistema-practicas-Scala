-- Sistema de practicas preprofesionales
-- Script de reinicio para pruebas.
--
-- Uso recomendado:
-- 1. Crear la base de datos sistema_practicas si aun no existe.
-- 2. Conectarse a la base sistema_practicas.
-- 3. Ejecutar este archivo completo.
--
-- ADVERTENCIA: este script elimina las tablas y datos actuales.

BEGIN;

-- Oculta avisos normales de reinicio como "tabla no existe, omitiendo".
SET LOCAL client_min_messages TO WARNING;

-- 1. Limpieza de tablas existentes.
DROP TABLE IF EXISTS notificaciones CASCADE;
DROP TABLE IF EXISTS actividades CASCADE;
DROP TABLE IF EXISTS practicas CASCADE;
DROP TABLE IF EXISTS postulaciones CASCADE;
DROP TABLE IF EXISTS ofertas CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE;
DROP TABLE IF EXISTS empresas CASCADE;

-- 2. Tablas base.
CREATE TABLE empresas (
    id_empresa BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(160) NOT NULL,
    correo VARCHAR(160) NOT NULL UNIQUE,
    ruc VARCHAR(13) NOT NULL UNIQUE,
    sector VARCHAR(120) NOT NULL,
    ubicacion VARCHAR(180) NOT NULL,
    tiene_convenio BOOLEAN NOT NULL DEFAULT FALSE,
    activa BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE usuarios (
    id_usuario BIGSERIAL PRIMARY KEY,
    nombres VARCHAR(120) NOT NULL,
    apellidos VARCHAR(120) NOT NULL,
    cedula VARCHAR(10) NOT NULL UNIQUE,
    email VARCHAR(160) NOT NULL UNIQUE,
    password_hash VARCHAR(120) NOT NULL,
    rol VARCHAR(40) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    carrera VARCHAR(120),
    ciclo_actual INTEGER,
    matriculado BOOLEAN,
    id_empresa BIGINT REFERENCES empresas(id_empresa),
    cargo VARCHAR(120)
);

-- 3. Tablas de postulacion y practica.
CREATE TABLE ofertas (
    id_oferta BIGSERIAL PRIMARY KEY,
    id_empresa BIGINT NOT NULL REFERENCES empresas(id_empresa),
    titulo VARCHAR(180) NOT NULL,
    descripcion TEXT NOT NULL,
    requisitos TEXT NOT NULL,
    area VARCHAR(120) NOT NULL,
    cupos INTEGER NOT NULL CHECK (cupos >= 0),
    fecha_publicacion DATE NOT NULL DEFAULT CURRENT_DATE,
    fecha_cierre DATE NOT NULL,
    estado VARCHAR(40) NOT NULL DEFAULT 'abierta'
);

CREATE TABLE postulaciones (
    id_postulacion BIGSERIAL PRIMARY KEY,
    id_estudiante BIGINT NOT NULL REFERENCES usuarios(id_usuario),
    id_oferta BIGINT NOT NULL REFERENCES ofertas(id_oferta),
    fecha_postulacion DATE NOT NULL DEFAULT CURRENT_DATE,
    estado VARCHAR(40) NOT NULL DEFAULT 'pendiente',
    ruta_documento_malla TEXT,
    UNIQUE (id_estudiante, id_oferta)
);

CREATE TABLE practicas (
    id_practica BIGSERIAL PRIMARY KEY,
    id_postulacion BIGINT NOT NULL REFERENCES postulaciones(id_postulacion),
    id_estudiante BIGINT NOT NULL REFERENCES usuarios(id_usuario),
    id_empresa BIGINT NOT NULL REFERENCES empresas(id_empresa),
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    id_tutor_academico BIGINT NOT NULL REFERENCES usuarios(id_usuario),
    id_tutor_empresarial BIGINT NOT NULL REFERENCES usuarios(id_usuario),
    estado VARCHAR(40) NOT NULL DEFAULT 'activa',
    horas_cumplidas INTEGER NOT NULL DEFAULT 0,
    calificacion INTEGER,
    formularios_finales_enviados BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE actividades (
    id_actividad BIGSERIAL PRIMARY KEY,
    id_practica BIGINT NOT NULL REFERENCES practicas(id_practica) ON DELETE CASCADE,
    descripcion TEXT NOT NULL,
    horas INTEGER NOT NULL CHECK (horas > 0),
    fecha DATE NOT NULL DEFAULT CURRENT_DATE,
    estado_revision VARCHAR(40) NOT NULL DEFAULT 'pendiente',
    aprobada_por_tutor_academico BOOLEAN NOT NULL DEFAULT FALSE,
    completada_por_tutor_empresarial BOOLEAN NOT NULL DEFAULT FALSE,
    id_tutor_academico_aprobador BIGINT REFERENCES usuarios(id_usuario),
    id_tutor_empresarial_completador BIGINT REFERENCES usuarios(id_usuario),
    fecha_aprobacion DATE,
    fecha_completado DATE
);

CREATE TABLE notificaciones (
    id_notificacion BIGSERIAL PRIMARY KEY,
    id_usuario BIGINT NOT NULL REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    titulo VARCHAR(160) NOT NULL,
    mensaje TEXT NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo VARCHAR(40) NOT NULL DEFAULT 'informativa',
    leida BOOLEAN NOT NULL DEFAULT FALSE
);

-- 4. Datos semilla.
INSERT INTO empresas (
    nombre,
    correo,
    ruc,
    sector,
    ubicacion,
    tiene_convenio,
    activa
) VALUES (
    'Banco del Austro',
    'bancoaustro@gmail.com',
    '0190055965001',
    'TICS',
    'Sucre y Borrero',
    TRUE,
    TRUE
);

INSERT INTO usuarios (
    nombres,
    apellidos,
    cedula,
    email,
    password_hash,
    rol,
    activo
) VALUES
    ('Administrador', 'General', '1300000005', 'admin', 'admin', 'administrador', TRUE),
    ('Magali', 'Mejia', '0102791506', 'magali.mejia@ucuenca.edu.ec', '123', 'coordinador', TRUE);

INSERT INTO usuarios (
    nombres,
    apellidos,
    cedula,
    email,
    password_hash,
    rol,
    activo,
    carrera
) VALUES (
    'Pablo',
    'Vanegas',
    '0150905941',
    'pablo.vanegas@ucuenca.edu.ec',
    '123',
    'tutor_academico',
    TRUE,
    'Computacion'
);

INSERT INTO usuarios (
    nombres,
    apellidos,
    cedula,
    email,
    password_hash,
    rol,
    activo,
    id_empresa,
    cargo
) VALUES (
    'Juan',
    'Arpi',
    '0106976228',
    'juanarpi@gmail.com',
    '123',
    'tutor_empresarial',
    TRUE,
    (SELECT id_empresa FROM empresas WHERE correo = 'bancoaustro@gmail.com'),
    'Tutor empresarial'
);

INSERT INTO usuarios (
    nombres,
    apellidos,
    cedula,
    email,
    password_hash,
    rol,
    activo,
    carrera,
    ciclo_actual
) VALUES (
    'Paula',
    'Sacoto',
    '0150701779',
    'paula.sacoto@ucuenca.edu.ec',
    '123',
    'estudiante',
    TRUE,
    'Computacion',
    6
);

COMMIT;

-- 5. Resumen de credenciales para iniciar pruebas.
SELECT 'admin' AS usuario, 'admin' AS clave, 'administrador' AS rol
UNION ALL
SELECT 'magali.mejia@ucuenca.edu.ec', '123', 'coordinador'
UNION ALL
SELECT 'pablo.vanegas@ucuenca.edu.ec', '123', 'tutor_academico'
UNION ALL
SELECT 'juanarpi@gmail.com', '123', 'tutor_empresarial'
UNION ALL
SELECT 'paula.sacoto@ucuenca.edu.ec', '123', 'estudiante';
