package modelo.entidades

import java.time.LocalDate

final case class PracticaEstudianteDetalle(
    idPractica: Long,
    idEstudiante: Long,
    empresa: String,
    empresaTieneConvenio: Boolean,
    oferta: String,
    area: String,
    fechaInicio: LocalDate,
    fechaFin: LocalDate,
    estado: String,
    horasCumplidas: Int,
    tutorAcademico: String,
    tutorEmpresarial: String,
    calificacion: Option[Int],
    formulariosFinalesEnviados: Boolean
) {
  def porcentaje: Int = math.min(100, (horasCumplidas * 100) / 240)
}
