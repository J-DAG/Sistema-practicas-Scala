package modelo.entidades

final case class EstudianteResumen(
    idUsuario: Long,
    nombres: String,
    apellidos: String,
    cedula: String,
    email: String,
    carrera: Option[String],
    cicloActual: Option[Int],
    estadoPractica: String,
    activo: Boolean
) {
  def nombreCompleto: String = s"$nombres $apellidos".trim
}
