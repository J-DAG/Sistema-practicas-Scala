# Sistema de practicas preprofesionales

Aplicacion de escritorio para gestionar practicas preprofesionales. Permite administrar usuarios, empresas, ofertas, postulaciones, practicas, actividades, reportes, notificaciones y formularios simulados.

## Requisitos

Antes de iniciar la aplicacion se necesita instalar:

- IntelliJ IDEA.
- JDK 17 o superior configurado en IntelliJ IDEA.
- PostgreSQL.

No se necesita instalar librerias adicionales para los graficos de reportes. La aplicacion usa componentes Swing incluidos en el proyecto.

## sbt opcional

El proyecto puede ejecutarse desde IntelliJ IDEA sin instalar sbt manualmente, siempre que el IDE tenga configurado un JDK compatible.

Instalar sbt solo es necesario si se desea ejecutar el proyecto desde terminal.

Para instalarlo en Windows:

1. Descargar el instalador desde:

```text
https://www.scala-sbt.org/download/
```

2. Ejecutar el instalador de Windows.
3. Cerrar y volver a abrir la terminal.
4. Verificar la instalacion con:

```bash
sbt --version
```

Si Windows no reconoce el comando `sbt`, reiniciar el equipo o revisar que la carpeta de instalacion de sbt este agregada al `Path` del sistema.

## Preparar la base de datos

1. Abrir PostgreSQL o pgAdmin.
2. Crear una base de datos llamada:

```sql
CREATE DATABASE sistema_practicas;
```

3. Conectarse a la base `sistema_practicas`.
4. Ejecutar completo el archivo:

```text
database/esquema.sql
```

Este archivo reinicia las tablas y carga los datos iniciales necesarios para ingresar al sistema.

## Configurar conexion

Revisar el archivo:

```text
src/main/resources/application.properties
```

Configuracion por defecto:

```properties
db.url=jdbc:postgresql://localhost:5432/sistema_practicas
db.user=postgres
db.password=admin
```

Si la clave local de PostgreSQL es diferente, cambiar el valor de `db.password`.

Tambien se pueden usar variables de entorno:

```text
DB_URL
DB_USER
DB_PASSWORD
```

## Ejecutar la aplicacion

Opcion recomendada:

1. Abrir el proyecto en IntelliJ IDEA.
2. Verificar que tenga configurado un JDK 17 o superior.
3. Ejecutar la clase principal del sistema.

Opcion desde terminal, si se instalo sbt:

```bash
sbt run
```


## Usuarios iniciales

Despues de ejecutar `database/esquema.sql`, se puede ingresar con estos usuarios:

| Rol | Usuario | Clave |
| --- | --- | --- |
| Administrador | admin | admin |
| Coordinadora | magali.mejia@ucuenca.edu.ec | 123 |
| Tutor academico | pablo.vanegas@ucuenca.edu.ec | 123 |
| Tutor empresarial | juanarpi@gmail.com | 123 |
| Estudiante | paula.sacoto@ucuenca.edu.ec | 123 |

La empresa inicial es `Banco del Austro`.

## Roles del sistema

El sistema maneja cinco tipos de usuario:

- Administrador: gestiona usuarios y empresas.
- Coordinador: gestiona empresas, tutores, estudiantes, ofertas, postulaciones, practicas y reportes.
- Estudiante: revisa ofertas, envia postulaciones con PDF, consulta postulaciones, practica, actividades y formularios.
- Tutor academico: revisa practicas asignadas, aprueba o niega actividades y califica practicas finalizadas.
- Tutor empresarial: registra actividades, edita actividades pendientes o negadas y marca actividades aprobadas como completadas.

## Flujo general de uso

1. El administrador o coordinador registra empresas, tutores y estudiantes.
2. El coordinador crea ofertas de practicas.
3. El estudiante revisa las ofertas disponibles.
4. El estudiante postula adjuntando su PDF de avance de malla.
5. El coordinador revisa la postulacion.
6. Si la postulacion es aprobada, el coordinador asigna tutor academico, tutor empresarial y fechas de practica.
7. El tutor empresarial registra actividades para el estudiante.
8. El tutor academico aprueba o niega las actividades.
9. El tutor empresarial marca como completadas las actividades aprobadas.
10. Al completar 240 horas, la practica pasa a finalizada.
11. El tutor academico califica la practica.
12. El estudiante puede revisar sus formularios finales.

## Reglas principales

- La cedula se valida con reglas de Ecuador.
- El estudiante debe estar en ciclo 6 o superior para postular.
- El estudiante debe adjuntar un PDF de avance de malla.
- El estudiante no puede postular si tiene una practica activa, finalizada pendiente de calificacion o completada.
- El estudiante puede cancelar postulaciones pendientes.
- El coordinador aprueba o niega postulaciones desde la ventana de revision.
- Al aprobar una postulacion se crea la practica y se asignan los tutores.
- Si la empresa no tiene convenio, el estudiante puede visualizar la carta compromiso.
- Las actividades no pueden superar 240 horas planificadas.
- No se pueden editar actividades aprobadas o completadas.
- Una actividad negada puede ser corregida por el tutor empresarial y vuelve a pendiente de aprobacion.
- El tutor empresarial solo puede completar actividades aprobadas.
- Al llegar a 240 horas cumplidas, se notifica al estudiante, tutores y coordinador.
- El tutor academico califica practicas finalizadas sobre 100.
- Al calificar, la practica pasa a completada.
- Al eliminar un tutor con practicas asignadas, se solicita un tutor reemplazo.
- Al eliminar un estudiante, se eliminan sus postulaciones, practicas y actividades relacionadas.

## Formularios

El sistema simula el envio de formularios y documentos:

- Formulario 1: informacion inicial de la practica.
- Carta compromiso: disponible cuando la empresa no tiene convenio.
- Formulario 2 final: disponible cuando la practica fue calificada.

## Documentos adjuntos

Los PDFs enviados por los estudiantes se guardan en:

```text
documentos_postulacion/
```

Esta carpeta contiene los archivos cargados por los estudiantes al enviar sus postulaciones.
