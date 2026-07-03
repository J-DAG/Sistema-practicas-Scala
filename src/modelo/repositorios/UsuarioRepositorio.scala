package modelo.repositorios

import modelo.bd.Conexion
import modelo.entidades.{EstudianteResumen, TutorResumen, Usuario, UsuarioFactory}

import java.sql.{Connection, ResultSet}
import scala.util.Using

class UsuarioRepositorio {

  private val columnasUsuario =
    "id_usuario, nombres, apellidos, cedula, email, password_hash, rol, activo, carrera, ciclo_actual, id_empresa, cargo"

  def buscarPorId(idUsuario: Long): Option[Usuario] = {
    val sql = s"SELECT $columnasUsuario FROM usuarios WHERE id_usuario = ?"

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setLong(1, idUsuario)
      val resultado = use(sentencia.executeQuery())

      if (resultado.next()) Some(mapearUsuario(resultado)) else None
    }.get
  }

  def crear(usuario: Usuario): Long = {
    val sql =
      """
        |INSERT INTO usuarios (
        |  nombres, apellidos, cedula, email, password_hash, rol, activo,
        |  carrera, ciclo_actual, id_empresa, cargo
        |)
        |VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        |RETURNING id_usuario
        |""".stripMargin

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      asignarCamposUsuario(sentencia, usuario)
      val resultado = use(sentencia.executeQuery())

      if (resultado.next()) resultado.getLong(1) else 0L
    }.get
  }

  def actualizar(usuario: Usuario, cambiarPassword: Boolean): Int = {
    val sql =
      if (cambiarPassword) {
        """
          |UPDATE usuarios
          |SET nombres = ?, apellidos = ?, cedula = ?, email = ?, password_hash = ?, rol = ?, activo = ?,
          |    carrera = ?, ciclo_actual = ?, id_empresa = ?, cargo = ?
          |WHERE id_usuario = ?
          |""".stripMargin
      } else {
        """
          |UPDATE usuarios
          |SET nombres = ?, apellidos = ?, cedula = ?, email = ?, rol = ?, activo = ?,
          |    carrera = ?, ciclo_actual = ?, id_empresa = ?, cargo = ?
          |WHERE id_usuario = ?
          |""".stripMargin
      }

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      if (cambiarPassword) {
        asignarCamposUsuario(sentencia, usuario)
        sentencia.setLong(12, usuario.idUsuario)
      } else {
        sentencia.setString(1, usuario.nombres)
        sentencia.setString(2, usuario.apellidos)
        sentencia.setString(3, usuario.cedula)
        sentencia.setString(4, usuario.email)
        sentencia.setString(5, usuario.rol)
        sentencia.setBoolean(6, usuario.activo)
        sentencia.setString(7, usuario.carrera.orNull)
        usuario.cicloActual match {
          case Some(ciclo) => sentencia.setInt(8, ciclo)
          case None => sentencia.setNull(8, java.sql.Types.INTEGER)
        }
        usuario.idEmpresa match {
          case Some(idEmpresa) => sentencia.setLong(9, idEmpresa)
          case None => sentencia.setNull(9, java.sql.Types.BIGINT)
        }
        sentencia.setString(10, usuario.cargo.orNull)
        sentencia.setLong(11, usuario.idUsuario)
      }
      sentencia.executeUpdate()
    }.get
  }

  def listar(filtro: String = ""): List[Usuario] = {
    val sql =
      """
        |SELECT id_usuario, nombres, apellidos, cedula, email, password_hash, rol, activo, carrera, ciclo_actual, id_empresa, cargo
        |FROM usuarios
        |WHERE
        |  ? = ''
        |  OR LOWER(nombres) LIKE ?
        |  OR LOWER(apellidos) LIKE ?
        |  OR LOWER(cedula) LIKE ?
        |  OR LOWER(email) LIKE ?
        |  OR LOWER(rol) LIKE ?
        |ORDER BY id_usuario DESC
        |""".stripMargin
    val busqueda = filtro.trim.toLowerCase
    val patron = s"%$busqueda%"

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setString(1, busqueda)
      sentencia.setString(2, patron)
      sentencia.setString(3, patron)
      sentencia.setString(4, patron)
      sentencia.setString(5, patron)
      sentencia.setString(6, patron)
      val resultado = use(sentencia.executeQuery())
      val usuarios = List.newBuilder[Usuario]

      while (resultado.next()) {
        usuarios += mapearUsuario(resultado)
      }

      usuarios.result()
    }.get
  }

  def listarTutores(filtro: String = ""): List[TutorResumen] = {
    val sql =
      """
        |SELECT
        |  u.id_usuario,
        |  u.nombres,
        |  u.apellidos,
        |  u.cedula,
        |  u.email,
        |  u.rol,
        |  u.carrera,
        |  e.nombre AS empresa,
        |  u.cargo,
        |  u.activo
        |FROM usuarios u
        |LEFT JOIN empresas e ON e.id_empresa = u.id_empresa
        |WHERE LOWER(u.rol) IN ('tutor_academico', 'tutor_empresarial')
        |  AND (
        |    ? = ''
        |    OR LOWER(u.nombres) LIKE ?
        |    OR LOWER(u.apellidos) LIKE ?
        |    OR LOWER(u.cedula) LIKE ?
        |    OR LOWER(u.email) LIKE ?
        |    OR LOWER(u.rol) LIKE ?
        |    OR LOWER(COALESCE(u.carrera, '')) LIKE ?
        |    OR LOWER(COALESCE(e.nombre, '')) LIKE ?
        |    OR LOWER(COALESCE(u.cargo, '')) LIKE ?
        |  )
        |ORDER BY u.rol, u.apellidos, u.nombres
        |""".stripMargin
    val busqueda = filtro.trim.toLowerCase
    val patron = s"%$busqueda%"

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setString(1, busqueda)
      sentencia.setString(2, patron)
      sentencia.setString(3, patron)
      sentencia.setString(4, patron)
      sentencia.setString(5, patron)
      sentencia.setString(6, patron)
      sentencia.setString(7, patron)
      sentencia.setString(8, patron)
      sentencia.setString(9, patron)
      val resultado = use(sentencia.executeQuery())
      val tutores = List.newBuilder[TutorResumen]

      while (resultado.next()) {
        tutores += TutorResumen(
          idUsuario = resultado.getLong("id_usuario"),
          nombres = resultado.getString("nombres"),
          apellidos = resultado.getString("apellidos"),
          cedula = resultado.getString("cedula"),
          email = resultado.getString("email"),
          rol = resultado.getString("rol"),
          carrera = Option(resultado.getString("carrera")),
          empresa = Option(resultado.getString("empresa")),
          cargo = Option(resultado.getString("cargo")),
          activo = resultado.getBoolean("activo")
        )
      }

      tutores.result()
    }.get
  }

  def listarTutoresAcademicosActivos(): List[Usuario] =
    listarPorRolActivo("tutor_academico")

  def listarTutoresEmpresarialesActivosPorEmpresa(idEmpresa: Long): List[Usuario] = {
    val sql =
      s"""
         |SELECT $columnasUsuario
         |FROM usuarios
         |WHERE LOWER(rol) = 'tutor_empresarial'
         |  AND activo = TRUE
         |  AND id_empresa = ?
         |ORDER BY apellidos, nombres
         |""".stripMargin

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setLong(1, idEmpresa)
      val resultado = use(sentencia.executeQuery())
      val tutores = List.newBuilder[Usuario]

      while (resultado.next()) {
        tutores += mapearUsuario(resultado)
      }

      tutores.result()
    }.get
  }

  def listarEstudiantes(filtro: String = ""): List[EstudianteResumen] = {
    val sql =
      """
        |SELECT
        |  u.id_usuario,
        |  u.nombres,
        |  u.apellidos,
        |  u.cedula,
        |  u.email,
        |  u.carrera,
        |  u.ciclo_actual,
        |  u.activo,
        |  CASE
        |    WHEN EXISTS (
        |      SELECT 1 FROM practicas p
        |      WHERE p.id_estudiante = u.id_usuario AND LOWER(p.estado) = 'activa'
        |    ) THEN 'En practica'
        |    WHEN EXISTS (
        |      SELECT 1 FROM practicas p
        |      WHERE p.id_estudiante = u.id_usuario AND LOWER(p.estado) IN ('finalizada', 'completada')
        |    ) THEN 'Aprobado'
        |    WHEN EXISTS (
        |      SELECT 1 FROM postulaciones po
        |      WHERE po.id_estudiante = u.id_usuario AND LOWER(po.estado) = 'pendiente'
        |    ) THEN 'Postulando'
        |    WHEN EXISTS (
        |      SELECT 1 FROM postulaciones po
        |      WHERE po.id_estudiante = u.id_usuario AND LOWER(po.estado) = 'aprobada'
        |    ) THEN 'Aprobado'
        |    ELSE 'Sin actividad'
        |  END AS estado_practica
        |FROM usuarios u
        |WHERE LOWER(u.rol) = 'estudiante'
        |  AND (
        |    ? = ''
        |    OR LOWER(u.nombres) LIKE ?
        |    OR LOWER(u.apellidos) LIKE ?
        |    OR LOWER(u.cedula) LIKE ?
        |    OR LOWER(u.email) LIKE ?
        |    OR LOWER(COALESCE(u.carrera, '')) LIKE ?
        |    OR CAST(COALESCE(u.ciclo_actual, 0) AS TEXT) LIKE ?
        |  )
        |ORDER BY u.apellidos, u.nombres
        |""".stripMargin
    val busqueda = filtro.trim.toLowerCase
    val patron = s"%$busqueda%"

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setString(1, busqueda)
      sentencia.setString(2, patron)
      sentencia.setString(3, patron)
      sentencia.setString(4, patron)
      sentencia.setString(5, patron)
      sentencia.setString(6, patron)
      sentencia.setString(7, patron)
      val resultado = use(sentencia.executeQuery())
      val estudiantes = List.newBuilder[EstudianteResumen]

      while (resultado.next()) {
        estudiantes += EstudianteResumen(
          idUsuario = resultado.getLong("id_usuario"),
          nombres = resultado.getString("nombres"),
          apellidos = resultado.getString("apellidos"),
          cedula = resultado.getString("cedula"),
          email = resultado.getString("email"),
          carrera = Option(resultado.getString("carrera")),
          cicloActual = obtenerLongOpcional(resultado, "ciclo_actual").map(_.toInt),
          estadoPractica = resultado.getString("estado_practica"),
          activo = resultado.getBoolean("activo")
        )
      }

      estudiantes.result()
    }.get
  }

  def contarPorRol(rol: String): Int = {
    val sql = "SELECT COUNT(*) FROM usuarios WHERE LOWER(rol) = LOWER(?) AND activo = TRUE"

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setString(1, rol)
      val resultado = use(sentencia.executeQuery())

      if (resultado.next()) resultado.getInt(1) else 0
    }.get
  }

  def eliminar(idUsuario: Long): Int = {
    eliminarConReasignacion(idUsuario, None)
  }

  def eliminarConReasignacion(idUsuario: Long, idReemplazo: Option[Long]): Int = {
    val conexion = Conexion.obtener()
    try {
      conexion.setAutoCommit(false)
      val usuario = buscarPorIdBloqueado(conexion, idUsuario)

      usuario.rol.trim.toLowerCase match {
        case "estudiante" =>
          eliminarDependenciasEstudiante(conexion, idUsuario)
        case "tutor_academico" | "tutor academico" | "ta" =>
          val tieneReferencias = contarReferenciasTutor(conexion, idUsuario, "tutor_academico") > 0
          if (tieneReferencias) {
            val reemplazo = idReemplazo.getOrElse {
              throw new IllegalStateException("Debe seleccionar un tutor academico de reemplazo.")
            }
            validarTutorAcademicoReemplazo(conexion, idUsuario, reemplazo)
            reasignarTutorAcademico(conexion, idUsuario, reemplazo)
          }
        case "tutor_empresarial" | "tutor empresarial" | "te" =>
          val tieneReferencias = contarReferenciasTutor(conexion, idUsuario, "tutor_empresarial") > 0
          if (tieneReferencias) {
            val reemplazo = idReemplazo.getOrElse {
              throw new IllegalStateException("Debe seleccionar un tutor empresarial de reemplazo.")
            }
            validarTutorEmpresarialReemplazo(conexion, usuario, reemplazo)
            reasignarTutorEmpresarial(conexion, idUsuario, reemplazo)
          }
        case _ =>
      }

      val eliminados = eliminarUsuario(conexion, idUsuario)
      conexion.commit()
      eliminados
    } catch {
      case error: Exception =>
        conexion.rollback()
        throw error
    } finally {
      conexion.close()
    }
  }

  def candidatosReemplazoTutor(idUsuario: Long): List[Usuario] = {
    buscarPorId(idUsuario) match {
      case Some(usuario) if usuario.rol.equalsIgnoreCase("tutor_academico") =>
        listarTutoresAcademicosActivos().filterNot(_.idUsuario == idUsuario)
      case Some(usuario) if usuario.rol.equalsIgnoreCase("tutor_empresarial") =>
        usuario.idEmpresa match {
          case Some(idEmpresa) => listarTutoresEmpresarialesActivosPorEmpresa(idEmpresa).filterNot(_.idUsuario == idUsuario)
          case None => Nil
        }
      case _ =>
        Nil
    }
  }

  def requiereReasignacionTutor(idUsuario: Long): Boolean = {
    buscarPorId(idUsuario).exists { usuario =>
      val rol = usuario.rol.trim.toLowerCase
      (rol == "tutor_academico" || rol == "tutor_empresarial") && contarReferenciasTutor(idUsuario, rol) > 0
    }
  }

  private def contarReferenciasTutor(idUsuario: Long, rol: String): Int = {
    val sql =
      if (rol == "tutor_academico") {
        """
          |SELECT
          |  (SELECT COUNT(*) FROM practicas WHERE id_tutor_academico = ?) +
          |  (SELECT COUNT(*) FROM actividades WHERE id_tutor_academico_aprobador = ?)
          |""".stripMargin
      } else {
        """
          |SELECT
          |  (SELECT COUNT(*) FROM practicas WHERE id_tutor_empresarial = ?) +
          |  (SELECT COUNT(*) FROM actividades WHERE id_tutor_empresarial_completador = ?)
          |""".stripMargin
      }
    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setLong(1, idUsuario)
      sentencia.setLong(2, idUsuario)
      val resultado = use(sentencia.executeQuery())
      if (resultado.next()) resultado.getInt(1) else 0
    }.get
  }

  private def contarReferenciasTutor(conexion: Connection, idUsuario: Long, rol: String): Int = {
    val sql =
      if (rol == "tutor_academico") {
        """
          |SELECT
          |  (SELECT COUNT(*) FROM practicas WHERE id_tutor_academico = ?) +
          |  (SELECT COUNT(*) FROM actividades WHERE id_tutor_academico_aprobador = ?)
          |""".stripMargin
      } else {
        """
          |SELECT
          |  (SELECT COUNT(*) FROM practicas WHERE id_tutor_empresarial = ?) +
          |  (SELECT COUNT(*) FROM actividades WHERE id_tutor_empresarial_completador = ?)
          |""".stripMargin
      }
    val sentencia = conexion.prepareStatement(sql)
    try {
      sentencia.setLong(1, idUsuario)
      sentencia.setLong(2, idUsuario)
      val resultado = sentencia.executeQuery()
      try if (resultado.next()) resultado.getInt(1) else 0
      finally resultado.close()
    } finally {
      sentencia.close()
    }
  }

  def buscarPorUsuarioOEmail(identificador: String): Option[Usuario] = {
    val sql =
      """
        |SELECT id_usuario, nombres, apellidos, cedula, email, password_hash, rol, activo, carrera, ciclo_actual, id_empresa, cargo
        |FROM usuarios
        |WHERE email = ? OR CAST(id_usuario AS TEXT) = ?
        |LIMIT 1
        |""".stripMargin

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setString(1, identificador)
      sentencia.setString(2, identificador)
      val resultado = use(sentencia.executeQuery())

      if (resultado.next()) {
        Some(mapearUsuario(resultado))
      } else {
        None
      }
    }.get
  }

  private def mapearUsuario(resultado: ResultSet): Usuario =
    UsuarioFactory.crear(
      idUsuario = resultado.getLong("id_usuario"),
      nombres = resultado.getString("nombres"),
      apellidos = resultado.getString("apellidos"),
      cedula = resultado.getString("cedula"),
      email = resultado.getString("email"),
      passwordHash = resultado.getString("password_hash"),
      rol = resultado.getString("rol"),
      activo = resultado.getBoolean("activo"),
      carrera = Option(resultado.getString("carrera")),
      cicloActual = obtenerLongOpcional(resultado, "ciclo_actual").map(_.toInt),
      idEmpresa = obtenerLongOpcional(resultado, "id_empresa"),
      cargo = Option(resultado.getString("cargo"))
    )

  private def buscarPorIdBloqueado(conexion: Connection, idUsuario: Long): Usuario = {
    val sentencia = conexion.prepareStatement(s"SELECT $columnasUsuario FROM usuarios WHERE id_usuario = ? FOR UPDATE")
    try {
      sentencia.setLong(1, idUsuario)
      val resultado = sentencia.executeQuery()
      try {
        if (resultado.next()) mapearUsuario(resultado)
        else throw new IllegalStateException("No se encontro el usuario.")
      } finally {
        resultado.close()
      }
    } finally {
      sentencia.close()
    }
  }

  private def eliminarDependenciasEstudiante(conexion: Connection, idEstudiante: Long): Unit = {
    ejecutar(conexion, "DELETE FROM practicas WHERE id_estudiante = ?", idEstudiante)
    ejecutar(conexion, "DELETE FROM postulaciones WHERE id_estudiante = ?", idEstudiante)
  }

  private def validarTutorAcademicoReemplazo(conexion: Connection, idTutorActual: Long, idReemplazo: Long): Unit = {
    if (idTutorActual == idReemplazo) {
      throw new IllegalStateException("El tutor de reemplazo debe ser diferente al tutor eliminado.")
    }
    val reemplazo = buscarPorIdBloqueado(conexion, idReemplazo)
    if (!reemplazo.activo || !reemplazo.rol.equalsIgnoreCase("tutor_academico")) {
      throw new IllegalStateException("Seleccione un tutor academico activo como reemplazo.")
    }
  }

  private def validarTutorEmpresarialReemplazo(conexion: Connection, tutorActual: Usuario, idReemplazo: Long): Unit = {
    if (tutorActual.idUsuario == idReemplazo) {
      throw new IllegalStateException("El tutor de reemplazo debe ser diferente al tutor eliminado.")
    }
    val reemplazo = buscarPorIdBloqueado(conexion, idReemplazo)
    if (!reemplazo.activo || !reemplazo.rol.equalsIgnoreCase("tutor_empresarial")) {
      throw new IllegalStateException("Seleccione un tutor empresarial activo como reemplazo.")
    }
    if (tutorActual.idEmpresa.isEmpty || reemplazo.idEmpresa != tutorActual.idEmpresa) {
      throw new IllegalStateException("El tutor empresarial de reemplazo debe pertenecer a la misma empresa.")
    }
  }

  private def reasignarTutorAcademico(conexion: Connection, idTutorActual: Long, idReemplazo: Long): Unit = {
    ejecutar(conexion, "UPDATE practicas SET id_tutor_academico = ? WHERE id_tutor_academico = ?", idReemplazo, idTutorActual)
    ejecutar(conexion, "UPDATE actividades SET id_tutor_academico_aprobador = ? WHERE id_tutor_academico_aprobador = ?", idReemplazo, idTutorActual)
  }

  private def reasignarTutorEmpresarial(conexion: Connection, idTutorActual: Long, idReemplazo: Long): Unit = {
    ejecutar(conexion, "UPDATE practicas SET id_tutor_empresarial = ? WHERE id_tutor_empresarial = ?", idReemplazo, idTutorActual)
    ejecutar(conexion, "UPDATE actividades SET id_tutor_empresarial_completador = ? WHERE id_tutor_empresarial_completador = ?", idReemplazo, idTutorActual)
  }

  private def eliminarUsuario(conexion: Connection, idUsuario: Long): Int = {
    val sentencia = conexion.prepareStatement("DELETE FROM usuarios WHERE id_usuario = ?")
    try {
      sentencia.setLong(1, idUsuario)
      sentencia.executeUpdate()
    } finally {
      sentencia.close()
    }
  }

  private def ejecutar(conexion: Connection, sql: String, parametros: Long*): Int = {
    val sentencia = conexion.prepareStatement(sql)
    try {
      parametros.zipWithIndex.foreach { case (valor, indice) =>
        sentencia.setLong(indice + 1, valor)
      }
      sentencia.executeUpdate()
    } finally {
      sentencia.close()
    }
  }

  private def listarPorRolActivo(rol: String): List[Usuario] = {
    val sql =
      s"""
         |SELECT $columnasUsuario
         |FROM usuarios
         |WHERE LOWER(rol) = LOWER(?)
         |  AND activo = TRUE
         |ORDER BY apellidos, nombres
         |""".stripMargin

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setString(1, rol)
      val resultado = use(sentencia.executeQuery())
      val usuarios = List.newBuilder[Usuario]

      while (resultado.next()) {
        usuarios += mapearUsuario(resultado)
      }

      usuarios.result()
    }.get
  }

  private def obtenerLongOpcional(resultado: ResultSet, columna: String): Option[Long] = {
    val valor = resultado.getLong(columna)
    if (resultado.wasNull()) None else Some(valor)
  }

  private def asignarCamposUsuario(sentencia: java.sql.PreparedStatement, usuario: Usuario): Unit = {
    sentencia.setString(1, usuario.nombres)
    sentencia.setString(2, usuario.apellidos)
    sentencia.setString(3, usuario.cedula)
    sentencia.setString(4, usuario.email)
    sentencia.setString(5, usuario.passwordHash)
    sentencia.setString(6, usuario.rol)
    sentencia.setBoolean(7, usuario.activo)
    sentencia.setString(8, usuario.carrera.orNull)
    usuario.cicloActual match {
      case Some(ciclo) => sentencia.setInt(9, ciclo)
      case None => sentencia.setNull(9, java.sql.Types.INTEGER)
    }
    usuario.idEmpresa match {
      case Some(idEmpresa) => sentencia.setLong(10, idEmpresa)
      case None => sentencia.setNull(10, java.sql.Types.BIGINT)
    }
    sentencia.setString(11, usuario.cargo.orNull)
  }
}
