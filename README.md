# Sistema de practicas preprofesionales - Scala/Swing/PostgreSQL

Sistema de gestion de practicas preprofesionales migrado desde la version Python/PyQt hacia Scala con vistas Java Swing UI Designer y persistencia en PostgreSQL.

## Requisitos

- JDK 17 o superior.
- Scala 2.13.14.
- sbt.
- PostgreSQL.
- IntelliJ IDEA recomendado para abrir las vistas Java Swing UI Designer.

Dependencias configuradas en `build.sbt`:

- PostgreSQL JDBC `42.7.4`.
- jBCrypt `0.4`.

## Base de datos

Configurar conexion en:

`src/main/resources/application.properties`

Valores actuales:

```properties
db.url=jdbc:postgresql://localhost:5432/sistema_practicas
db.user=postgres
db.password=admin
```

Crear o actualizar tablas ejecutando:

```sql
database/schema.sql
```

Usuario semilla:

- Usuario/email: `admin`
- Contrasena: `admin`
- Rol: `administrador`

## Estructura general

- `src/modelo/entidades`: clases del dominio.
- `src/modelo/repositorios`: acceso a PostgreSQL.
- `src/modelo/servicios`: reglas transaccionales de negocio.
- `src/modelo/validaciones`: validaciones, incluida cedula Ecuador.
- `src/controlador`: controladores Swing.
- `src/vista`: vistas Java Swing.
- `src/vista/iconos`: iconos.
- `src/vista/imagenes`: imagenes.
- `documentos_postulacion`: documentos PDF de avance de malla.
- `database/schema.sql`: estructura y semilla inicial.

## Reglas ya migradas o implementadas

- Herencia de usuarios: `Usuario`, `Estudiante`, `TutorAcademico`, `TutorEmpresarial`, `Coordinador`, `Administrador`.
- Validacion de cedula ecuatoriana.
- Empresa sin `mision`, `vision` ni `razon_social`.
- Login con verificacion de conexion a PostgreSQL.
- Admin y coordinador gestionan usuarios, tutores, empresas y ofertas.
- Estudiante puede postular a ofertas adjuntando PDF de avance de malla.
- La postulacion abre un formulario para seleccionar y confirmar el PDF de avance de malla.
- El estudiante no puede postular si tiene una practica activa/finalizada pendiente de calificacion o si ya completo una practica.
- El estudiante puede cancelar una postulacion pendiente; la cancelacion elimina el registro de postulacion.
- Coordinador revisa postulacion desde un formulario unico.
- Coordinador puede aprobar, negar, cancelar aprobacion y volver a aprobar una negada.
- Al aprobar postulacion se asignan tutores, fechas y se crea practica activa.
- Tutor empresarial gestiona actividades.
- Las actividades no pueden superar 240 horas planificadas.
- No se puede editar una actividad aprobada o completada.
- Las actividades negadas por TA se conservan y el TE puede editarlas para volverlas a `pendiente aprobacion`.
- Si TA nego una actividad por error, puede volver a aprobarla directamente.
- Solo se pueden eliminar actividades pendientes de aprobacion.
- Tutor academico aprueba o niega actividades.
- Tutor empresarial solo puede completar actividades aprobadas por tutor academico.
- Al completar 240 horas, la practica pasa a `finalizada` y se notifican actores.
- Tutor academico puede calificar practicas finalizadas sobre 100.
- Al calificar, la practica pasa a `completada` y se marcan formularios finales enviados.
- Notificaciones basicas con tabla reutilizable.
- Estudiante puede ver su practica activa, finalizada o completada con progreso y actividades.
- Estudiante puede ver formularios simulados:
  - Formulario 1 inicial.
  - Carta compromiso si la empresa no tiene convenio.
  - Formulario 2 final cuando la practica ya fue calificada.

## Flujo de prueba recomendado

1. Ingresar como `admin/admin`.
2. Crear empresa.
3. Crear coordinador.
4. Crear tutor academico.
5. Crear tutor empresarial asociado a la empresa.
6. Crear estudiante.
7. Ingresar como coordinador y crear oferta.
8. Ingresar como estudiante y postular con PDF de avance de malla.
9. Ingresar como coordinador, revisar postulacion y aprobar asignando TA, TE y fechas.
10. Ingresar como tutor empresarial y crear actividades.
11. Ingresar como tutor academico, revisar actividades y aprobarlas.
12. Volver como tutor empresarial y marcar actividades aprobadas como completadas.
13. Completar 240 horas.
14. Ingresar como tutor academico y calificar la practica finalizada.

## Pendiente por completar

### Estudiante

- Probar en interfaz la vista de practica/progreso ya conectada.
- Agregar filtro visible para actividades `negadas` si se desea un radio boton especifico.
- Probar en interfaz la vista de formularios ya conectada.
- Completar formulario de culminacion como Formulario 3 si se requiere una seccion separada.

### Tutor academico

- Probar flujo completo en interfaz:
  - Ver practicas.
  - Aprobar actividades.
  - Negar actividades.
  - Calificar practica finalizada.
- Mejorar vista de practicas completadas para mostrar calificacion si ya existe.
- Validar en pruebas que negar actividad la marque como `negada` y que TE pueda corregirla.

### Tutor empresarial

- Probar flujo completo luego de aprobacion por TA.
- Revisar mensajes de error cuando una actividad aun no esta aprobada.
- Reutilizar ventana real de notificaciones, como ya se hizo con TA.
- Opcional: mostrar horas planificadas restantes antes de crear actividad.

### Coordinador

- Conectar modulo de practicas.
- Conectar reportes.
- Conectar notificaciones reales usando `DialogoNotificaciones`.
- Ver actividades desde practicas si se requiere para auditoria.

### Formularios

- Mejorar presentacion visual de formularios simulados.
- Separar Formulario 2 y Formulario 3 si se desea mantener ambos documentos finales.
- Persistir formularios emitidos en tabla propia si se requiere historial.
- Generar documentos reales o PDFs en una etapa posterior.

### Base de datos y reglas futuras

- Considerar tabla de formularios emitidos.
- Considerar tabla de documentos generados.
- Revisar borrado o reasignacion de tutores con practicas activas.
- Revisar eliminacion de estudiantes con practicas/postulaciones asociadas.

### Entorno

- Instalar o configurar `sbt` en PATH para poder compilar desde terminal:

```bash
sbt compile
```

En esta terminal no se encontro `sbt`, `scala` ni `scalac`, por eso las verificaciones se hicieron por revision estatica y pruebas manuales desde la IDE.
