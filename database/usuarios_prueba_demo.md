# Usuarios de prueba demo

Ejecutar primero:

```text
database/esquema.sql
database/datos_demo_reportes.sql
```

Todos los usuarios demo tienen clave:

```text
123
```

## Usuarios principales

| Rol | Usuario | Clave | Uso recomendado |
| --- | --- | --- | --- |
| Administrador | admin | admin | Gestionar usuarios y empresas. |
| Coordinador | magali.mejia@ucuenca.edu.ec | 123 | Revisar reportes, postulaciones y practicas. |
| Tutor academico | ana.ortega@ucuenca.edu.ec | 123 | Aprobar, negar o corregir actividades de estudiantes asignados. |
| Tutor academico | carlos.mendez@ucuenca.edu.ec | 123 | Revisar estudiantes de TechSoft Demo. |
| Tutor empresarial | maria.torres@bancoaustro.com | 123 | Gestionar actividades de Banco del Austro. |
| Tutor empresarial | luis.rios@techsoft.com | 123 | Gestionar actividades de TechSoft Demo. |
| Tutor empresarial | elena.paz@innovademo.com | 123 | Revisar practica finalizada de Innova Demo. |

## Estudiantes para reglas de negocio

| Estudiante | Usuario | Ciclo | Estado esperado | Prueba recomendada |
| --- | --- | --- | --- | --- |
| Sofia Calle | sofia.calle@demo.edu.ec | 1 | Sin actividad | Debe ser bloqueada porque no cumple el ciclo minimo para postular. |
| Mateo Paredes | mateo.paredes@demo.edu.ec | 2 | Postulando | Tiene postulacion pendiente; probar cancelar postulacion. |
| Valeria Mora | valeria.mora@demo.edu.ec | 3 | En practica | Practica activa con 80/240 horas; revisar progreso. |
| Andres Lopez | andres.lopez@demo.edu.ec | 4 | En practica | Tiene actividad pendiente y negada; probar correccion TE y aprobacion TA. |
| Camila Vega | camila.vega@demo.edu.ec | 5 | Finalizada | Practica finalizada 240/240 sin calificacion; TA puede calificar. |
| Daniela Serrano | daniela.serrano@demo.edu.ec | 6 | Completada | Practica completada y calificada 95/100. |
| Nicolas Bravo | nicolas.bravo@demo.edu.ec | 7 | Sin actividad | Tiene postulacion negada; puede revisar comportamiento de postulaciones negadas. |
| Lucia Arias | lucia.arias@demo.edu.ec | 8 | Postulando | Tiene postulacion pendiente en TechSoft Demo. |
| Roberto Salazar | roberto.salazar@demo.edu.ec | 9 | Sin actividad | Puede postular desde cero. |
| Teresa Molina | teresa.molina@demo.edu.ec | 10 | Completada | Practica completada y calificada 88/100. |

## Datos esperados en reportes

- Practicas por estado:
  - Activas: 2
  - Finalizadas: 1
  - Completadas: 2
  - Cerradas: 0

- Estudiantes por estado:
  - En practica: Valeria, Andres
  - Postulando: Mateo, Lucia
  - Finalizada: Camila
  - Completada: Daniela, Teresa
  - Sin actividad: Paula, Sofia, Nicolas, Roberto

- Ciclos:
  - Hay estudiantes demo distribuidos del ciclo 1 al 10.
  - Paula Sacoto de la semilla queda en ciclo 6 sin actividad.
