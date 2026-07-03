package modelo.entidades

import java.time.LocalDate

final case class PracticaCoordinadorResumen(
    idPractica: Long,
    idEstudiante: Long,
    estudiante: String,
    cedula: String,
    empresa: String,
    oferta: String,
    area: String,
    tutorAcademico: String,
    tutorEmpresarial: String,
    fechaInicio: LocalDate,
    fechaFin: LocalDate,
    estado: String,
    horasCumplidas: Int,
    calificacion: Option[Int]
) {
  def porcentaje: Int = math.min(100, (horasCumplidas * 100) / 240)
}
