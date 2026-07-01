package modelo.entidades

final case class TutorResumen(
    idUsuario: Long,
    nombres: String,
    apellidos: String,
    cedula: String,
    email: String,
    rol: String,
    carrera: Option[String],
    empresa: Option[String],
    cargo: Option[String],
    activo: Boolean
) {
  def nombreCompleto: String = s"$nombres $apellidos".trim
}
