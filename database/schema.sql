CREATE TABLE IF NOT EXISTS usuarios (
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
    id_empresa BIGINT,
    cargo VARCHAR(120)
);

CREATE TABLE IF NOT EXISTS empresas (
    id_empresa BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(160) NOT NULL,
    correo VARCHAR(160) NOT NULL UNIQUE,
    ruc VARCHAR(13) NOT NULL UNIQUE,
    sector VARCHAR(120) NOT NULL,
    ubicacion VARCHAR(180) NOT NULL,
    tiene_convenio BOOLEAN NOT NULL DEFAULT FALSE,
    activa BOOLEAN NOT NULL DEFAULT TRUE
);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_usuario_empresa'
    ) THEN
        ALTER TABLE usuarios
            ADD CONSTRAINT fk_usuario_empresa
            FOREIGN KEY (id_empresa)
            REFERENCES empresas(id_empresa);
    END IF;
END $$;

CREATE TABLE IF NOT EXISTS ofertas (
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

CREATE TABLE IF NOT EXISTS postulaciones (
    id_postulacion BIGSERIAL PRIMARY KEY,
    id_estudiante BIGINT NOT NULL REFERENCES usuarios(id_usuario),
    id_oferta BIGINT NOT NULL REFERENCES ofertas(id_oferta),
    fecha_postulacion DATE NOT NULL DEFAULT CURRENT_DATE,
    estado VARCHAR(40) NOT NULL DEFAULT 'pendiente',
    ruta_documento_malla TEXT,
    UNIQUE (id_estudiante, id_oferta)
);

CREATE TABLE IF NOT EXISTS practicas (
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

CREATE TABLE IF NOT EXISTS actividades (
    id_actividad BIGSERIAL PRIMARY KEY,
    id_practica BIGINT NOT NULL REFERENCES practicas(id_practica) ON DELETE CASCADE,
    descripcion TEXT NOT NULL,
    horas INTEGER NOT NULL CHECK (horas > 0),
    fecha DATE NOT NULL DEFAULT CURRENT_DATE,
    aprobada_por_tutor_academico BOOLEAN NOT NULL DEFAULT FALSE,
    completada_por_tutor_empresarial BOOLEAN NOT NULL DEFAULT FALSE,
    id_tutor_academico_aprobador BIGINT REFERENCES usuarios(id_usuario),
    id_tutor_empresarial_completador BIGINT REFERENCES usuarios(id_usuario),
    fecha_aprobacion DATE,
    fecha_completado DATE
);

CREATE TABLE IF NOT EXISTS notificaciones (
    id_notificacion BIGSERIAL PRIMARY KEY,
    id_usuario BIGINT NOT NULL REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    titulo VARCHAR(160) NOT NULL,
    mensaje TEXT NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo VARCHAR(40) NOT NULL DEFAULT 'informativa',
    leida BOOLEAN NOT NULL DEFAULT FALSE
);

INSERT INTO usuarios (
    nombres,
    apellidos,
    cedula,
    email,
    password_hash,
    rol,
    activo
)
SELECT
    'Administrador',
    'General',
    '1300000005',
    'admin',
    'admin',
    'administrador',
    TRUE
ON CONFLICT (email) DO UPDATE SET
    nombres = EXCLUDED.nombres,
    apellidos = EXCLUDED.apellidos,
    cedula = EXCLUDED.cedula,
    password_hash = EXCLUDED.password_hash,
    rol = EXCLUDED.rol,
    activo = TRUE;
