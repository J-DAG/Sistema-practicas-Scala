package modelo.entidades

class Usuario(
    val idUsuario: Long,
    val nombres: String,
    val apellidos: String,
    val cedula: String,
    val email: String,
    val passwordHash: String,
    val rol: String,
    val activo: Boolean,
    val carrera: Option[String] = None,
    val cicloActual: Option[Int] = None,
    val idEmpresa: Option[Long] = None,
    val cargo: Option[String] = None
) {
  def nombreCompleto: String = s"$nombres $apellidos".trim

  def copy(
      idUsuario: Long = this.idUsuario,
      nombres: String = this.nombres,
      apellidos: String = this.apellidos,
      cedula: String = this.cedula,
      email: String = this.email,
      passwordHash: String = this.passwordHash,
      rol: String = this.rol,
      activo: Boolean = this.activo,
      carrera: Option[String] = this.carrera,
      cicloActual: Option[Int] = this.cicloActual,
      idEmpresa: Option[Long] = this.idEmpresa,
      cargo: Option[String] = this.cargo
  ): Usuario =
    UsuarioFactory.crear(
      idUsuario = idUsuario,
      nombres = nombres,
      apellidos = apellidos,
      cedula = cedula,
      email = email,
      passwordHash = passwordHash,
      rol = rol,
      activo = activo,
      carrera = carrera,
      cicloActual = cicloActual,
      idEmpresa = idEmpresa,
      cargo = cargo
    )
}

object Usuario {
  def apply(
      idUsuario: Long,
      nombres: String,
      apellidos: String,
      cedula: String,
      email: String,
      passwordHash: String,
      rol: String,
      activo: Boolean,
      carrera: Option[String] = None,
      cicloActual: Option[Int] = None,
      idEmpresa: Option[Long] = None,
      cargo: Option[String] = None
  ): Usuario =
    new Usuario(
      idUsuario = idUsuario,
      nombres = nombres,
      apellidos = apellidos,
      cedula = cedula,
      email = email,
      passwordHash = passwordHash,
      rol = rol,
      activo = activo,
      carrera = carrera,
      cicloActual = cicloActual,
      idEmpresa = idEmpresa,
      cargo = cargo
    )
}

final class Estudiante(
    idUsuario: Long,
    nombres: String,
    apellidos: String,
    cedula: String,
    email: String,
    passwordHash: String,
    activo: Boolean,
    carrera: Option[String],
    cicloActual: Option[Int]
) extends Usuario(
      idUsuario = idUsuario,
      nombres = nombres,
      apellidos = apellidos,
      cedula = cedula,
      email = email,
      passwordHash = passwordHash,
      rol = "estudiante",
      activo = activo,
      carrera = carrera,
      cicloActual = cicloActual
    )

final class TutorAcademico(
    idUsuario: Long,
    nombres: String,
    apellidos: String,
    cedula: String,
    email: String,
    passwordHash: String,
    activo: Boolean,
    carrera: Option[String]
) extends Usuario(
      idUsuario = idUsuario,
      nombres = nombres,
      apellidos = apellidos,
      cedula = cedula,
      email = email,
      passwordHash = passwordHash,
      rol = "tutor_academico",
      activo = activo,
      carrera = carrera
    )

final class TutorEmpresarial(
    idUsuario: Long,
    nombres: String,
    apellidos: String,
    cedula: String,
    email: String,
    passwordHash: String,
    activo: Boolean,
    idEmpresa: Option[Long],
    cargo: Option[String]
) extends Usuario(
      idUsuario = idUsuario,
      nombres = nombres,
      apellidos = apellidos,
      cedula = cedula,
      email = email,
      passwordHash = passwordHash,
      rol = "tutor_empresarial",
      activo = activo,
      idEmpresa = idEmpresa,
      cargo = cargo
    )

final class Coordinador(
    idUsuario: Long,
    nombres: String,
    apellidos: String,
    cedula: String,
    email: String,
    passwordHash: String,
    activo: Boolean
) extends Usuario(
      idUsuario = idUsuario,
      nombres = nombres,
      apellidos = apellidos,
      cedula = cedula,
      email = email,
      passwordHash = passwordHash,
      rol = "coordinador",
      activo = activo
    )

final class Administrador(
    idUsuario: Long,
    nombres: String,
    apellidos: String,
    cedula: String,
    email: String,
    passwordHash: String,
    activo: Boolean
) extends Usuario(
      idUsuario = idUsuario,
      nombres = nombres,
      apellidos = apellidos,
      cedula = cedula,
      email = email,
      passwordHash = passwordHash,
      rol = "administrador",
      activo = activo
    )

object UsuarioFactory {

  def especializar(usuario: Usuario): Usuario =
    crear(
      idUsuario = usuario.idUsuario,
      nombres = usuario.nombres,
      apellidos = usuario.apellidos,
      cedula = usuario.cedula,
      email = usuario.email,
      passwordHash = usuario.passwordHash,
      rol = usuario.rol,
      activo = usuario.activo,
      carrera = usuario.carrera,
      cicloActual = usuario.cicloActual,
      idEmpresa = usuario.idEmpresa,
      cargo = usuario.cargo
    )

  def crear(
      idUsuario: Long,
      nombres: String,
      apellidos: String,
      cedula: String,
      email: String,
      passwordHash: String,
      rol: String,
      activo: Boolean,
      carrera: Option[String] = None,
      cicloActual: Option[Int] = None,
      idEmpresa: Option[Long] = None,
      cargo: Option[String] = None
  ): Usuario =
    rol.trim.toLowerCase match {
      case "estudiante" =>
        new Estudiante(idUsuario, nombres, apellidos, cedula, email, passwordHash, activo, carrera, cicloActual)
      case "tutor_academico" | "tutor academico" | "ta" =>
        new TutorAcademico(idUsuario, nombres, apellidos, cedula, email, passwordHash, activo, carrera)
      case "tutor_empresarial" | "tutor empresarial" | "te" =>
        new TutorEmpresarial(idUsuario, nombres, apellidos, cedula, email, passwordHash, activo, idEmpresa, cargo)
      case "coordinador" =>
        new Coordinador(idUsuario, nombres, apellidos, cedula, email, passwordHash, activo)
      case "administrador" | "admin" =>
        new Administrador(idUsuario, nombres, apellidos, cedula, email, passwordHash, activo)
      case otro =>
        Usuario(
          idUsuario = idUsuario,
          nombres = nombres,
          apellidos = apellidos,
          cedula = cedula,
          email = email,
          passwordHash = passwordHash,
          rol = otro,
          activo = activo,
          carrera = carrera,
          cicloActual = cicloActual,
          idEmpresa = idEmpresa,
          cargo = cargo
        )
    }
}
