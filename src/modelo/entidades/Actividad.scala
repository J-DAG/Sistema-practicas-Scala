package modelo.entidades

import java.time.LocalDate

final case class Actividad(
    idActividad: Long,
    idPractica: Long,
    descripcion: String,
    horas: Int,
    fecha: LocalDate,
    estadoRevision: String,
    aprobadaPorTutorAcademico: Boolean,
    completadaPorTutorEmpresarial: Boolean
) {
  def estado: String = {
    val normalizado = estadoRevision.trim.toLowerCase
    if (completadaPorTutorEmpresarial || normalizado == "completada") "completada"
    else if (aprobadaPorTutorAcademico || normalizado == "aprobada") "aprobada"
    else if (normalizado == "negada") "negada"
    else "pendiente aprobacion"
  }

  def puedeEditar: Boolean =
    !aprobadaPorTutorAcademico && !completadaPorTutorEmpresarial && estado != "completada"

  def puedeEliminar: Boolean =
    estado == "pendiente aprobacion"
}
