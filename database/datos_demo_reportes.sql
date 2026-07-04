-- Datos demo para revisar reportes y reglas de negocio.
--
-- Uso recomendado:
-- 1. Ejecutar primero database/esquema.sql.
-- 2. Ejecutar este archivo completo.
--
-- Este script es reejecutable: elimina solamente datos demo creados por este archivo.

BEGIN;

SET LOCAL client_min_messages TO WARNING;

-- 1. Limpiar datos demo anteriores.
DELETE FROM notificaciones
WHERE id_usuario IN (
    SELECT id_usuario
    FROM usuarios
    WHERE email LIKE '%@demo.edu.ec'
       OR email IN ('ana.ortega@ucuenca.edu.ec', 'carlos.mendez@ucuenca.edu.ec', 'maria.torres@bancoaustro.com', 'luis.rios@techsoft.com', 'elena.paz@innovademo.com')
);

DELETE FROM actividades
WHERE id_practica IN (
    SELECT p.id_practica
    FROM practicas p
    JOIN usuarios u ON u.id_usuario = p.id_estudiante
    WHERE u.email LIKE '%@demo.edu.ec'
);

DELETE FROM practicas
WHERE id_estudiante IN (
    SELECT id_usuario FROM usuarios WHERE email LIKE '%@demo.edu.ec'
);

DELETE FROM postulaciones
WHERE id_estudiante IN (
    SELECT id_usuario FROM usuarios WHERE email LIKE '%@demo.edu.ec'
);

DELETE FROM ofertas
WHERE titulo LIKE 'Demo - %';

DELETE FROM usuarios
WHERE email LIKE '%@demo.edu.ec'
   OR email IN ('ana.ortega@ucuenca.edu.ec', 'carlos.mendez@ucuenca.edu.ec', 'maria.torres@bancoaustro.com', 'luis.rios@techsoft.com', 'elena.paz@innovademo.com');

DELETE FROM empresas
WHERE correo IN ('techsoft.demo@empresa.com', 'innova.demo@empresa.com');

-- 2. Empresas demo.
INSERT INTO empresas (nombre, correo, ruc, sector, ubicacion, tiene_convenio, activa)
VALUES
    ('TechSoft Demo', 'techsoft.demo@empresa.com', '0199999999001', 'TICS', 'Av. Loja y Remigio Crespo', FALSE, TRUE),
    ('Innova Demo', 'innova.demo@empresa.com', '0199999998001', 'TICS', 'Parque Industrial', TRUE, TRUE);

-- 3. Tutores demo.
INSERT INTO usuarios (nombres, apellidos, cedula, email, password_hash, rol, activo, carrera)
VALUES
    ('Ana', 'Ortega', '0107000010', 'ana.ortega@ucuenca.edu.ec', '123', 'tutor_academico', TRUE, 'Computacion'),
    ('Carlos', 'Mendez', '0107000028', 'carlos.mendez@ucuenca.edu.ec', '123', 'tutor_academico', TRUE, 'Computacion');

INSERT INTO usuarios (nombres, apellidos, cedula, email, password_hash, rol, activo, id_empresa, cargo)
VALUES
    ('Maria', 'Torres', '0107000036', 'maria.torres@bancoaustro.com', '123', 'tutor_empresarial', TRUE,
        (SELECT id_empresa FROM empresas WHERE correo = 'bancoaustro@gmail.com'), 'Tutora empresarial'),
    ('Luis', 'Rios', '0107000044', 'luis.rios@techsoft.com', '123', 'tutor_empresarial', TRUE,
        (SELECT id_empresa FROM empresas WHERE correo = 'techsoft.demo@empresa.com'), 'Tutor empresarial'),
    ('Elena', 'Paz', '0107000150', 'elena.paz@innovademo.com', '123', 'tutor_empresarial', TRUE,
        (SELECT id_empresa FROM empresas WHERE correo = 'innova.demo@empresa.com'), 'Tutora empresarial');

-- 4. Estudiantes demo para cubrir ciclos y estados de reportes.
INSERT INTO usuarios (nombres, apellidos, cedula, email, password_hash, rol, activo, carrera, ciclo_actual)
VALUES
    ('Sofia', 'Calle', '0107000051', 'sofia.calle@demo.edu.ec', '123', 'estudiante', TRUE, 'Computacion', 1),
    ('Mateo', 'Paredes', '0107000069', 'mateo.paredes@demo.edu.ec', '123', 'estudiante', TRUE, 'Computacion', 2),
    ('Valeria', 'Mora', '0107000077', 'valeria.mora@demo.edu.ec', '123', 'estudiante', TRUE, 'Computacion', 3),
    ('Andres', 'Lopez', '0107000085', 'andres.lopez@demo.edu.ec', '123', 'estudiante', TRUE, 'Computacion', 4),
    ('Camila', 'Vega', '0107000093', 'camila.vega@demo.edu.ec', '123', 'estudiante', TRUE, 'Computacion', 5),
    ('Daniela', 'Serrano', '0107000101', 'daniela.serrano@demo.edu.ec', '123', 'estudiante', TRUE, 'Computacion', 6),
    ('Nicolas', 'Bravo', '0107000119', 'nicolas.bravo@demo.edu.ec', '123', 'estudiante', TRUE, 'Computacion', 7),
    ('Lucia', 'Arias', '0107000127', 'lucia.arias@demo.edu.ec', '123', 'estudiante', TRUE, 'Computacion', 8),
    ('Roberto', 'Salazar', '0107000135', 'roberto.salazar@demo.edu.ec', '123', 'estudiante', TRUE, 'Computacion', 9),
    ('Teresa', 'Molina', '0107000143', 'teresa.molina@demo.edu.ec', '123', 'estudiante', TRUE, 'Computacion', 10);

-- 5. Ofertas demo.
INSERT INTO ofertas (id_empresa, titulo, descripcion, requisitos, area, cupos, fecha_publicacion, fecha_cierre, estado)
VALUES
    ((SELECT id_empresa FROM empresas WHERE correo = 'bancoaustro@gmail.com'),
        'Demo - Desarrollo backend', 'Practicas en servicios backend.', 'Scala, Java, SQL', 'Desarrollo', 1, CURRENT_DATE, '2026-12-31', 'abierta'),
    ((SELECT id_empresa FROM empresas WHERE correo = 'techsoft.demo@empresa.com'),
        'Demo - QA funcional', 'Pruebas funcionales y automatizadas.', 'Pruebas, documentacion', 'QA', 0, CURRENT_DATE, '2026-12-31', 'abierta'),
    ((SELECT id_empresa FROM empresas WHERE correo = 'innova.demo@empresa.com'),
        'Demo - Analisis de datos', 'Reportes internos y tableros.', 'SQL, reportes', 'Datos', 0, CURRENT_DATE, '2026-12-31', 'abierta'),
    ((SELECT id_empresa FROM empresas WHERE correo = 'techsoft.demo@empresa.com'),
        'Demo - Soporte tecnico', 'Soporte a usuarios internos.', 'Redes, soporte', 'Soporte', 1, CURRENT_DATE, '2026-10-31', 'cerrada');

-- 6. Postulaciones: pendientes, negadas y aprobadas con practica.
INSERT INTO postulaciones (id_estudiante, id_oferta, fecha_postulacion, estado, ruta_documento_malla)
VALUES
    ((SELECT id_usuario FROM usuarios WHERE email = 'mateo.paredes@demo.edu.ec'),
        (SELECT id_oferta FROM ofertas WHERE titulo = 'Demo - Desarrollo backend'), CURRENT_DATE - 2, 'pendiente', 'documentos_postulacion/demo_mateo.pdf'),
    ((SELECT id_usuario FROM usuarios WHERE email = 'lucia.arias@demo.edu.ec'),
        (SELECT id_oferta FROM ofertas WHERE titulo = 'Demo - QA funcional'), CURRENT_DATE - 1, 'pendiente', 'documentos_postulacion/demo_lucia.pdf'),
    ((SELECT id_usuario FROM usuarios WHERE email = 'nicolas.bravo@demo.edu.ec'),
        (SELECT id_oferta FROM ofertas WHERE titulo = 'Demo - Soporte tecnico'), CURRENT_DATE - 20, 'negada', 'documentos_postulacion/demo_nicolas.pdf');

INSERT INTO postulaciones (id_estudiante, id_oferta, fecha_postulacion, estado, ruta_documento_malla)
VALUES
    ((SELECT id_usuario FROM usuarios WHERE email = 'valeria.mora@demo.edu.ec'),
        (SELECT id_oferta FROM ofertas WHERE titulo = 'Demo - Desarrollo backend'), CURRENT_DATE - 40, 'aprobada', 'documentos_postulacion/demo_valeria.pdf'),
    ((SELECT id_usuario FROM usuarios WHERE email = 'andres.lopez@demo.edu.ec'),
        (SELECT id_oferta FROM ofertas WHERE titulo = 'Demo - QA funcional'), CURRENT_DATE - 35, 'aprobada', 'documentos_postulacion/demo_andres.pdf'),
    ((SELECT id_usuario FROM usuarios WHERE email = 'camila.vega@demo.edu.ec'),
        (SELECT id_oferta FROM ofertas WHERE titulo = 'Demo - Analisis de datos'), CURRENT_DATE - 95, 'aprobada', 'documentos_postulacion/demo_camila.pdf'),
    ((SELECT id_usuario FROM usuarios WHERE email = 'daniela.serrano@demo.edu.ec'),
        (SELECT id_oferta FROM ofertas WHERE titulo = 'Demo - Desarrollo backend'), CURRENT_DATE - 120, 'aprobada', 'documentos_postulacion/demo_daniela.pdf'),
    ((SELECT id_usuario FROM usuarios WHERE email = 'teresa.molina@demo.edu.ec'),
        (SELECT id_oferta FROM ofertas WHERE titulo = 'Demo - QA funcional'), CURRENT_DATE - 125, 'aprobada', 'documentos_postulacion/demo_teresa.pdf');

-- 7. Practicas coherentes con horas y estados.
INSERT INTO practicas (
    id_postulacion, id_estudiante, id_empresa, fecha_inicio, fecha_fin,
    id_tutor_academico, id_tutor_empresarial, estado, horas_cumplidas, calificacion, formularios_finales_enviados
)
VALUES
    (
        (SELECT id_postulacion FROM postulaciones WHERE id_estudiante = (SELECT id_usuario FROM usuarios WHERE email = 'valeria.mora@demo.edu.ec')),
        (SELECT id_usuario FROM usuarios WHERE email = 'valeria.mora@demo.edu.ec'),
        (SELECT id_empresa FROM empresas WHERE correo = 'bancoaustro@gmail.com'),
        '2026-05-15', '2026-09-15',
        (SELECT id_usuario FROM usuarios WHERE email = 'ana.ortega@ucuenca.edu.ec'),
        (SELECT id_usuario FROM usuarios WHERE email = 'maria.torres@bancoaustro.com'),
        'activa', 80, NULL, FALSE
    ),
    (
        (SELECT id_postulacion FROM postulaciones WHERE id_estudiante = (SELECT id_usuario FROM usuarios WHERE email = 'andres.lopez@demo.edu.ec')),
        (SELECT id_usuario FROM usuarios WHERE email = 'andres.lopez@demo.edu.ec'),
        (SELECT id_empresa FROM empresas WHERE correo = 'techsoft.demo@empresa.com'),
        '2026-05-20', '2026-09-20',
        (SELECT id_usuario FROM usuarios WHERE email = 'carlos.mendez@ucuenca.edu.ec'),
        (SELECT id_usuario FROM usuarios WHERE email = 'luis.rios@techsoft.com'),
        'activa', 120, NULL, FALSE
    ),
    (
        (SELECT id_postulacion FROM postulaciones WHERE id_estudiante = (SELECT id_usuario FROM usuarios WHERE email = 'camila.vega@demo.edu.ec')),
        (SELECT id_usuario FROM usuarios WHERE email = 'camila.vega@demo.edu.ec'),
        (SELECT id_empresa FROM empresas WHERE correo = 'innova.demo@empresa.com'),
        '2026-02-01', '2026-06-30',
        (SELECT id_usuario FROM usuarios WHERE email = 'ana.ortega@ucuenca.edu.ec'),
        (SELECT id_usuario FROM usuarios WHERE email = 'elena.paz@innovademo.com'),
        'finalizada', 240, NULL, FALSE
    ),
    (
        (SELECT id_postulacion FROM postulaciones WHERE id_estudiante = (SELECT id_usuario FROM usuarios WHERE email = 'daniela.serrano@demo.edu.ec')),
        (SELECT id_usuario FROM usuarios WHERE email = 'daniela.serrano@demo.edu.ec'),
        (SELECT id_empresa FROM empresas WHERE correo = 'bancoaustro@gmail.com'),
        '2026-01-10', '2026-05-30',
        (SELECT id_usuario FROM usuarios WHERE email = 'pablo.vanegas@ucuenca.edu.ec'),
        (SELECT id_usuario FROM usuarios WHERE email = 'juanarpi@gmail.com'),
        'completada', 240, 95, TRUE
    ),
    (
        (SELECT id_postulacion FROM postulaciones WHERE id_estudiante = (SELECT id_usuario FROM usuarios WHERE email = 'teresa.molina@demo.edu.ec')),
        (SELECT id_usuario FROM usuarios WHERE email = 'teresa.molina@demo.edu.ec'),
        (SELECT id_empresa FROM empresas WHERE correo = 'techsoft.demo@empresa.com'),
        '2026-01-15', '2026-06-10',
        (SELECT id_usuario FROM usuarios WHERE email = 'carlos.mendez@ucuenca.edu.ec'),
        (SELECT id_usuario FROM usuarios WHERE email = 'luis.rios@techsoft.com'),
        'completada', 240, 88, TRUE
    );

-- 8. Actividades: no superan 240 horas por practica.
INSERT INTO actividades (
    id_practica, descripcion, horas, fecha, estado_revision,
    aprobada_por_tutor_academico, completada_por_tutor_empresarial,
    id_tutor_academico_aprobador, id_tutor_empresarial_completador,
    fecha_aprobacion, fecha_completado
)
VALUES
    ((SELECT id_practica FROM practicas WHERE id_estudiante = (SELECT id_usuario FROM usuarios WHERE email = 'valeria.mora@demo.edu.ec')),
        'Levantamiento de requerimientos', 40, '2026-05-20', 'completada', TRUE, TRUE,
        (SELECT id_usuario FROM usuarios WHERE email = 'ana.ortega@ucuenca.edu.ec'),
        (SELECT id_usuario FROM usuarios WHERE email = 'maria.torres@bancoaustro.com'), '2026-05-22', '2026-05-30'),
    ((SELECT id_practica FROM practicas WHERE id_estudiante = (SELECT id_usuario FROM usuarios WHERE email = 'valeria.mora@demo.edu.ec')),
        'Implementacion de modulo inicial', 40, '2026-06-05', 'completada', TRUE, TRUE,
        (SELECT id_usuario FROM usuarios WHERE email = 'ana.ortega@ucuenca.edu.ec'),
        (SELECT id_usuario FROM usuarios WHERE email = 'maria.torres@bancoaustro.com'), '2026-06-07', '2026-06-18'),
    ((SELECT id_practica FROM practicas WHERE id_estudiante = (SELECT id_usuario FROM usuarios WHERE email = 'valeria.mora@demo.edu.ec')),
        'Revision tecnica pendiente', 30, '2026-06-25', 'aprobada', TRUE, FALSE,
        (SELECT id_usuario FROM usuarios WHERE email = 'ana.ortega@ucuenca.edu.ec'), NULL, '2026-06-26', NULL),
    ((SELECT id_practica FROM practicas WHERE id_estudiante = (SELECT id_usuario FROM usuarios WHERE email = 'andres.lopez@demo.edu.ec')),
        'Plan de pruebas funcionales', 60, '2026-05-25', 'completada', TRUE, TRUE,
        (SELECT id_usuario FROM usuarios WHERE email = 'carlos.mendez@ucuenca.edu.ec'),
        (SELECT id_usuario FROM usuarios WHERE email = 'luis.rios@techsoft.com'), '2026-05-27', '2026-06-05'),
    ((SELECT id_practica FROM practicas WHERE id_estudiante = (SELECT id_usuario FROM usuarios WHERE email = 'andres.lopez@demo.edu.ec')),
        'Ejecucion de casos de prueba', 60, '2026-06-10', 'completada', TRUE, TRUE,
        (SELECT id_usuario FROM usuarios WHERE email = 'carlos.mendez@ucuenca.edu.ec'),
        (SELECT id_usuario FROM usuarios WHERE email = 'luis.rios@techsoft.com'), '2026-06-11', '2026-06-28'),
    ((SELECT id_practica FROM practicas WHERE id_estudiante = (SELECT id_usuario FROM usuarios WHERE email = 'andres.lopez@demo.edu.ec')),
        'Automatizacion propuesta', 30, '2026-07-01', 'pendiente', FALSE, FALSE, NULL, NULL, NULL, NULL),
    ((SELECT id_practica FROM practicas WHERE id_estudiante = (SELECT id_usuario FROM usuarios WHERE email = 'andres.lopez@demo.edu.ec')),
        'Actividad devuelta por alcance incorrecto', 20, '2026-07-02', 'negada', FALSE, FALSE, NULL, NULL, NULL, NULL);

INSERT INTO actividades (
    id_practica, descripcion, horas, fecha, estado_revision,
    aprobada_por_tutor_academico, completada_por_tutor_empresarial,
    id_tutor_academico_aprobador, id_tutor_empresarial_completador,
    fecha_aprobacion, fecha_completado
)
SELECT p.id_practica, actividad.descripcion, actividad.horas, actividad.fecha, 'completada',
       TRUE, TRUE, p.id_tutor_academico, p.id_tutor_empresarial, actividad.fecha, actividad.fecha + 7
FROM practicas p
JOIN usuarios u ON u.id_usuario = p.id_estudiante
JOIN (
    VALUES
        ('Analisis y documentacion', 80, DATE '2026-02-10'),
        ('Desarrollo de actividades asignadas', 80, DATE '2026-03-15'),
        ('Cierre y entrega de evidencias', 80, DATE '2026-05-20')
) AS actividad(descripcion, horas, fecha) ON TRUE
WHERE u.email = 'camila.vega@demo.edu.ec';

INSERT INTO actividades (
    id_practica, descripcion, horas, fecha, estado_revision,
    aprobada_por_tutor_academico, completada_por_tutor_empresarial,
    id_tutor_academico_aprobador, id_tutor_empresarial_completador,
    fecha_aprobacion, fecha_completado
)
SELECT p.id_practica, actividad.descripcion, actividad.horas, actividad.fecha, 'completada',
       TRUE, TRUE, p.id_tutor_academico, p.id_tutor_empresarial, actividad.fecha, actividad.fecha + 10
FROM practicas p
JOIN usuarios u ON u.id_usuario = p.id_estudiante
JOIN (
    VALUES
        ('Servicios backend', 120, DATE '2026-01-20'),
        ('Integracion y pruebas', 120, DATE '2026-03-10')
) AS actividad(descripcion, horas, fecha) ON TRUE
WHERE u.email = 'daniela.serrano@demo.edu.ec';

INSERT INTO actividades (
    id_practica, descripcion, horas, fecha, estado_revision,
    aprobada_por_tutor_academico, completada_por_tutor_empresarial,
    id_tutor_academico_aprobador, id_tutor_empresarial_completador,
    fecha_aprobacion, fecha_completado
)
SELECT p.id_practica, actividad.descripcion, actividad.horas, actividad.fecha, 'completada',
       TRUE, TRUE, p.id_tutor_academico, p.id_tutor_empresarial, actividad.fecha, actividad.fecha + 8
FROM practicas p
JOIN usuarios u ON u.id_usuario = p.id_estudiante
JOIN (
    VALUES
        ('Soporte y diagnostico', 100, DATE '2026-01-25'),
        ('Documentacion de incidencias', 80, DATE '2026-03-01'),
        ('Cierre tecnico', 60, DATE '2026-05-01')
) AS actividad(descripcion, horas, fecha) ON TRUE
WHERE u.email = 'teresa.molina@demo.edu.ec';

-- 9. Notificaciones demo.
INSERT INTO notificaciones (id_usuario, titulo, mensaje, tipo, leida)
SELECT id_usuario, 'Datos demo cargados', 'Usuario incluido para pruebas manuales de reglas y reportes.', 'informativa', FALSE
FROM usuarios
WHERE email LIKE '%@demo.edu.ec'
   OR email IN ('ana.ortega@ucuenca.edu.ec', 'carlos.mendez@ucuenca.edu.ec', 'maria.torres@bancoaustro.com', 'luis.rios@techsoft.com', 'elena.paz@innovademo.com');

COMMIT;

-- 10. Resumen rapido para revision.
SELECT 'Usuarios demo cargados' AS resumen, COUNT(*) AS total
FROM usuarios
WHERE email LIKE '%@demo.edu.ec'
   OR email IN ('ana.ortega@ucuenca.edu.ec', 'carlos.mendez@ucuenca.edu.ec', 'maria.torres@bancoaustro.com', 'luis.rios@techsoft.com', 'elena.paz@innovademo.com')
UNION ALL
SELECT 'Practicas demo', COUNT(*) FROM practicas p JOIN usuarios u ON u.id_usuario = p.id_estudiante WHERE u.email LIKE '%@demo.edu.ec'
UNION ALL
SELECT 'Postulaciones demo', COUNT(*) FROM postulaciones p JOIN usuarios u ON u.id_usuario = p.id_estudiante WHERE u.email LIKE '%@demo.edu.ec';
