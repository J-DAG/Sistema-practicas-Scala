package modelo.entidades

import java.time.LocalDate

final case class PracticaResumen(
    idPractica: Long,
    idEstudiante: Long,
    estudiante: String,
    cedula: String,
    empresa: String,
    oferta: String,
    area: String,
    fechaInicio: LocalDate,
    fechaFin: LocalDate,
    estado: String,
    horasCumplidas: Int,
    totalHorasActividades: Int
) {
  def porcentaje: Int = math.min(100, (horasCumplidas * 100) / 240)
}
