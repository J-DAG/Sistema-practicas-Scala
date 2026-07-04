package modelo.servicios

import modelo.entidades.{Usuario, UsuarioFactory}
import modelo.validaciones.CedulaEcuador

final case class DatosUsuarioFormulario(
    idUsuario: Long,
    nombres: String,
    apellidos: String,
    email: String,
    cedula: String,
    rol: String,
    password: String,
    confirmar: String,
    passwordActual: String
)

object UsuarioFormularioServicio {
  def usuarioBase(datos: DatosUsuarioFormulario): Either[String, Usuario] = {
    val campos = List(datos.nombres, datos.apellidos, datos.email, datos.cedula).map(_.trim)
    val rolNormalizado = datos.rol.trim.toLowerCase

    if (campos.exists(_.isEmpty)) {
      Left("Complete todos los campos obligatorios.")
    } else if (rolNormalizado != "administrador" && !campos(2).contains("@")) {
      Left("Ingrese un correo electronico valido.")
    } else if (!CedulaEcuador.esValida(campos(3))) {
      Left("Ingrese una cedula ecuatoriana valida.")
    } else if (datos.idUsuario == 0L && datos.password.isEmpty) {
      Left("Ingrese una contrasenia.")
    } else if (datos.password != datos.confirmar) {
      Left("Las contrasenias no coinciden.")
    } else {
      Right(UsuarioFactory.crear(
        idUsuario = datos.idUsuario,
        nombres = campos(0),
        apellidos = campos(1),
        cedula = campos(3),
        email = campos(2),
        passwordHash = if (datos.password.nonEmpty) datos.password else datos.passwordActual,
        rol = datos.rol,
        activo = true
      ))
    }
  }

  def estudiante(datos: DatosUsuarioFormulario, carrera: Option[String], ciclo: Option[Int]): Either[String, Usuario] =
    usuarioBase(datos.copy(rol = "estudiante"))
      .map(usuario => UsuarioFactory.especializar(usuario.copy(carrera = carrera, cicloActual = ciclo)))

  def tutorAcademico(datos: DatosUsuarioFormulario, carrera: Option[String]): Either[String, Usuario] =
    usuarioBase(datos.copy(rol = "tutor_academico"))
      .map(usuario => UsuarioFactory.especializar(usuario.copy(carrera = carrera)))

  def tutorEmpresarial(datos: DatosUsuarioFormulario, idEmpresa: Option[Long], cargo: String): Either[String, Usuario] =
    if (idEmpresa.isEmpty) {
      Left("Debe seleccionar una empresa para el tutor empresarial.")
    } else if (cargo.trim.isEmpty) {
      Left("Ingrese el cargo del tutor empresarial.")
    } else {
      usuarioBase(datos.copy(rol = "tutor_empresarial"))
        .map(usuario => UsuarioFactory.especializar(usuario.copy(idEmpresa = idEmpresa, cargo = Some(cargo.trim))))
    }
}
