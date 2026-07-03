# Sistema de practicas preprofesionales

Sistema de gestion de practicas preprofesionales con vistas Java Swing UI Designer y persistencia en PostgreSQL.

El proyecto implementa la logica principal de negocio para administrar usuarios, empresas, ofertas, postulaciones, practicas, actividades, formularios simulados y notificaciones.

## Instalacion

1. Instalar JDK 17 o superior.
2. Instalar sbt.
3. Instalar PostgreSQL.
4. Abrir el proyecto en IntelliJ IDEA si se desea editar las vistas `.java` generadas por Swing UI Designer.

No se necesita instalar una libreria adicional para las graficas de reportes. Los graficos se muestran con componentes Swing del propio proyecto y las dependencias necesarias se descargan automaticamente con `sbt`.

Verificar compilacion:

```bash
sbt compile
```

## Base de datos

Crear la base de datos en PostgreSQL:

```sql
CREATE DATABASE sistema_practicas;
```

Luego ejecutar el archivo completo:

```text
database/esquema.sql
```

Opciones recomendadas:

- En pgAdmin: abrir la base `sistema_practicas`, abrir Query Tool, pegar o cargar `database/esquema.sql` y ejecutar todo el script.

Configurar la conexion en:
```text
src/main/resources/application.properties
```

Configuracion:

```properties
db.url=jdbc:postgresql://localhost:5432/sistema_practicas
db.user=postgres
db.password=admin
```
Si la clave local de PostgreSQL no es `admin`, actualizar `db.password` antes de iniciar la aplicacion.
Usuarios iniciales sembrados por `database/esquema.sql`:

```text
Administrador
Usuario: admin
Clave: admin

Coordinadora
Usuario: magali.mejia@ucuenca.edu.ec
Clave: 123

Tutor academico
Usuario: pablo.vanegas@ucuenca.edu.ec
Clave: 123

Tutor empresarial
Usuario: juanarpi@gmail.com
Clave: 123

Estudiante
Usuario: paula.sacoto@ucuenca.edu.ec
Clave: 123
```

Tambien se siembra la empresa `Banco del Austro` con convenio activo.

## Roles
- Administrador: gestiona usuarios y empresas.
- Coordinador: gestiona empresas, tutores, estudiantes, ofertas, postulaciones, practicas y reportes.
- Estudiante: revisa ofertas, postula con PDF, consulta postulaciones, practica, actividades y formularios.
- Tutor academico: revisa practicas asignadas, aprueba o niega actividades y califica practicas finalizadas.
- Tutor empresarial: gestiona actividades de estudiantes asignados y marca actividades aprobadas como completadas.

## Reglas de negocio

- Todos los usuarios heredan de `Usuario`: estudiante, tutor academico, tutor empresarial, coordinador y administrador.
- La cedula se valida con reglas de Ecuador.
- El estudiante debe adjuntar un PDF de avance de malla para postular.
- El estudiante no puede postular si tiene una practica activa, finalizada pendiente de calificacion o completada.
- El estudiante puede cancelar postulaciones pendientes; se elimina el registro pendiente.
- El coordinador aprueba o niega postulaciones desde una ventana de revision.
- Al aprobar una postulacion se asignan tutor academico, tutor empresarial, fechas y se crea la practica.
- Si la empresa no tiene convenio, el estudiante puede visualizar carta compromiso simulada.
- Las actividades no pueden superar 240 horas planificadas.
- El tutor empresarial crea, edita o elimina actividades segun estado.
- No se pueden editar actividades aprobadas o completadas.
- Solo se pueden eliminar actividades pendientes de aprobacion.
- El tutor academico aprueba o niega actividades.
- Una actividad negada no se elimina; el tutor empresarial puede editarla y vuelve a pendiente de aprobacion.
- Si el tutor academico nego por error, puede aprobar directamente la actividad negada.
- El tutor empresarial solo puede completar actividades aprobadas.
- Al llegar a 240 horas cumplidas, la practica pasa a `finalizada` y se notifican estudiante, tutores y coordinador.
- El tutor academico califica practicas finalizadas sobre 100.
- Al calificar, la practica pasa a `completada` y se marca el Formulario 2 final como enviado al correo del estudiante.
- Una practica `completada` no se reabre modificando actividades.
- Al eliminar un estudiante se eliminan sus postulaciones, practicas y actividades relacionadas.
- Al eliminar un tutor con practicas o actividades relacionadas, se exige seleccionar un tutor reemplazo.
- La reasignacion de tutores migra practicas y referencias de actividades al tutor reemplazo.
- El tutor empresarial reemplazo debe pertenecer a la misma empresa.

## Formularios

El sistema simula los formularios en pantalla y mediante notificaciones.
- Formulario 1: datos iniciales de la practica, empresa, tutores, fechas, oferta y area.
- Carta compromiso: disponible solo cuando la empresa no tiene convenio.
- Formulario 2 final: disponible cuando la practica fue calificada por el tutor academico.

## Flujo de prueba recomendado
1. Ingresar como `admin/admin`.
2. Crear una empresa.
3. Crear un coordinador.
4. Crear un tutor academico.
5. Crear un tutor empresarial asociado a la empresa.
6. Crear un estudiante.
7. Ingresar como coordinador.
8. Crear una oferta.
9. Ingresar como estudiante.
10. Postular a la oferta adjuntando un PDF.
11. Ingresar como coordinador.
12. Revisar la postulacion.
13. Aprobar asignando TA, TE y fechas.
14. Ingresar como tutor empresarial.
15. Crear actividades sin superar 240 horas.
16. Ingresar como tutor academico.
17. Aprobar o negar actividades.
18. Si una actividad fue negada, ingresar como tutor empresarial, corregirla y reenviarla.
19. Marcar actividades aprobadas como completadas.
20. Completar 240 horas.
21. Ingresar como tutor academico y calificar la practica.
22. Ingresar como estudiante y revisar Formulario 2 final.

## Compilacion

Compilar:

```bash
sbt compile
```
## Notas
- `documentos_postulacion/` guarda los PDFs enviados por estudiantes.
- Para cambiar credenciales de base de datos, editar `application.properties` o usar variables de entorno `DB_URL`, `DB_USER` y `DB_PASSWORD`.
