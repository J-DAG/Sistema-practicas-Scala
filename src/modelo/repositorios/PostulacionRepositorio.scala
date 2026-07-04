package modelo.repositorios

import modelo.bd.Conexion
import modelo.entidades.{PostulacionDetalle, PostulacionResumen}

import java.sql.Date
import java.time.LocalDate
import java.sql.ResultSet
import scala.util.Using

class PostulacionRepositorio {
  private val CicloMinimoPostulacion = 6

  def crear(idEstudiante: Long, idOferta: Long, rutaDocumentoMalla: String): Long = {
    validarEstudiantePuedePostular(idEstudiante)
    val sql =
      """
        |INSERT INTO postulaciones (id_estudiante, id_oferta, fecha_postulacion, estado, ruta_documento_malla)
        |VALUES (?, ?, ?, 'pendiente', ?)
        |RETURNING id_postulacion
        |""".stripMargin

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setLong(1, idEstudiante)
      sentencia.setLong(2, idOferta)
      sentencia.setDate(3, Date.valueOf(LocalDate.now()))
      sentencia.setString(4, rutaDocumentoMalla)
      val resultado = use(sentencia.executeQuery())

      if (resultado.next()) resultado.getLong(1) else 0L
    }.get
  }

  private def validarEstudiantePuedePostular(idEstudiante: Long): Unit = {
    validarCicloPermitido(idEstudiante)

    val sql =
      """
        |SELECT COUNT(*)
        |FROM practicas
        |WHERE id_estudiante = ?
        |  AND LOWER(estado) IN ('activa', 'finalizada', 'completada')
        |""".stripMargin

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setLong(1, idEstudiante)
      val resultado = use(sentencia.executeQuery())

      if (resultado.next() && resultado.getInt(1) > 0) {
        throw new IllegalStateException("El estudiante ya tiene o ya completo una practica, no puede enviar nuevas postulaciones.")
      }
    }.get
  }

  private def validarCicloPermitido(idEstudiante: Long): Unit = {
    val sql =
      """
        |SELECT rol, activo, ciclo_actual
        |FROM usuarios
        |WHERE id_usuario = ?
        |""".stripMargin

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setLong(1, idEstudiante)
      val resultado = use(sentencia.executeQuery())

      if (!resultado.next()) {
        throw new IllegalStateException("No se encontro el estudiante para postular.")
      }

      val rol = Option(resultado.getString("rol")).map(_.trim.toLowerCase).getOrElse("")
      val activo = resultado.getBoolean("activo")
      val ciclo = {
        val valor = resultado.getInt("ciclo_actual")
        if (resultado.wasNull()) None else Some(valor)
      }

      if (rol != "estudiante" || !activo) {
        throw new IllegalStateException("Solo un estudiante activo puede enviar postulaciones.")
      }

      ciclo match {
        case Some(valor) if valor >= CicloMinimoPostulacion =>
        case Some(valor) =>
          throw new IllegalStateException(
            s"El estudiante esta en ciclo $valor. Solo puede postular desde ciclo $CicloMinimoPostulacion."
          )
        case None =>
          throw new IllegalStateException(
            s"El estudiante no tiene ciclo registrado. Solo puede postular desde ciclo $CicloMinimoPostulacion."
          )
      }
    }.get
  }

  def listarResumenEstudiante(idEstudiante: Long, filtro: String = ""): List[PostulacionResumen] = {
    val sql =
      """
        |SELECT
        |  p.id_postulacion,
        |  u.nombres || ' ' || u.apellidos AS estudiante,
        |  u.cedula,
        |  e.nombre AS empresa,
        |  o.titulo AS oferta,
        |  o.area,
        |  p.fecha_postulacion,
        |  p.estado,
        |  p.ruta_documento_malla
        |FROM postulaciones p
        |JOIN usuarios u ON u.id_usuario = p.id_estudiante
        |JOIN ofertas o ON o.id_oferta = p.id_oferta
        |JOIN empresas e ON e.id_empresa = o.id_empresa
        |WHERE p.id_estudiante = ?
        |  AND (
        |    ? = ''
        |    OR LOWER(e.nombre) LIKE ?
        |    OR LOWER(o.titulo) LIKE ?
        |    OR LOWER(o.area) LIKE ?
        |    OR LOWER(p.estado) LIKE ?
        |  )
        |ORDER BY p.fecha_postulacion DESC, p.id_postulacion DESC
        |""".stripMargin
    val busqueda = filtro.trim.toLowerCase
    val patron = s"%$busqueda%"

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setLong(1, idEstudiante)
      sentencia.setString(2, busqueda)
      sentencia.setString(3, patron)
      sentencia.setString(4, patron)
      sentencia.setString(5, patron)
      sentencia.setString(6, patron)
      val resultado = use(sentencia.executeQuery())
      val postulaciones = List.newBuilder[PostulacionResumen]

      while (resultado.next()) {
        postulaciones += mapear(resultado)
      }

      postulaciones.result()
    }.get
  }

  def cancelarPendiente(idPostulacion: Long, idEstudiante: Long): Int = {
    val sql =
      """
        |DELETE FROM postulaciones
        |WHERE id_postulacion = ?
        |  AND id_estudiante = ?
        |  AND LOWER(estado) = 'pendiente'
        |""".stripMargin

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setLong(1, idPostulacion)
      sentencia.setLong(2, idEstudiante)
      sentencia.executeUpdate()
    }.get
  }

  def cancelarPendientePorOferta(idEstudiante: Long, idOferta: Long): Int = {
    val sql =
      """
        |DELETE FROM postulaciones
        |WHERE id_estudiante = ?
        |  AND id_oferta = ?
        |  AND LOWER(estado) = 'pendiente'
        |""".stripMargin

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setLong(1, idEstudiante)
      sentencia.setLong(2, idOferta)
      sentencia.executeUpdate()
    }.get
  }

  def buscarDetalle(idPostulacion: Long): Option[PostulacionDetalle] = {
    val sql =
      """
        |SELECT
        |  p.id_postulacion,
        |  p.id_estudiante,
        |  u.nombres || ' ' || u.apellidos AS estudiante,
        |  p.id_oferta,
        |  o.titulo AS oferta,
        |  o.area,
        |  o.id_empresa,
        |  e.nombre AS empresa,
        |  e.tiene_convenio,
        |  o.cupos,
        |  p.fecha_postulacion,
        |  p.estado,
        |  p.ruta_documento_malla,
        |  pr.id_practica,
        |  pr.fecha_inicio,
        |  pr.fecha_fin,
        |  ta.nombres || ' ' || ta.apellidos AS tutor_academico,
        |  te.nombres || ' ' || te.apellidos AS tutor_empresarial
        |FROM postulaciones p
        |JOIN usuarios u ON u.id_usuario = p.id_estudiante
        |JOIN ofertas o ON o.id_oferta = p.id_oferta
        |JOIN empresas e ON e.id_empresa = o.id_empresa
        |LEFT JOIN practicas pr ON pr.id_postulacion = p.id_postulacion
        |  AND LOWER(pr.estado) = 'activa'
        |LEFT JOIN usuarios ta ON ta.id_usuario = pr.id_tutor_academico
        |LEFT JOIN usuarios te ON te.id_usuario = pr.id_tutor_empresarial
        |WHERE p.id_postulacion = ?
        |""".stripMargin

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setLong(1, idPostulacion)
      val resultado = use(sentencia.executeQuery())

      if (resultado.next()) Some(mapearDetalle(resultado)) else None
    }.get
  }

  def listarResumen(filtro: String = "", limite: Int = 100): List[PostulacionResumen] = {
    val sql =
      """
        |SELECT
        |  p.id_postulacion,
        |  u.nombres || ' ' || u.apellidos AS estudiante,
        |  u.cedula,
        |  e.nombre AS empresa,
        |  o.titulo AS oferta,
        |  o.area,
        |  p.fecha_postulacion,
        |  p.estado,
        |  p.ruta_documento_malla
        |FROM postulaciones p
        |JOIN usuarios u ON u.id_usuario = p.id_estudiante
        |JOIN ofertas o ON o.id_oferta = p.id_oferta
        |JOIN empresas e ON e.id_empresa = o.id_empresa
        |WHERE
        |  ? = ''
        |  OR LOWER(u.nombres || ' ' || u.apellidos) LIKE ?
        |  OR LOWER(u.cedula) LIKE ?
        |  OR LOWER(e.nombre) LIKE ?
        |  OR LOWER(o.titulo) LIKE ?
        |  OR LOWER(o.area) LIKE ?
        |  OR LOWER(p.estado) LIKE ?
        |ORDER BY p.fecha_postulacion DESC, p.id_postulacion DESC
        |LIMIT ?
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
      sentencia.setInt(8, limite)
      val resultado = use(sentencia.executeQuery())
      val postulaciones = List.newBuilder[PostulacionResumen]

      while (resultado.next()) {
        postulaciones += mapear(resultado)
      }

      postulaciones.result()
    }.get
  }

  def contarPorEstado(estado: String): Int = {
    val sql = "SELECT COUNT(*) FROM postulaciones WHERE LOWER(estado) = LOWER(?)"

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setString(1, estado)
      val resultado = use(sentencia.executeQuery())

      if (resultado.next()) resultado.getInt(1) else 0
    }.get
  }

  def contarTodas(): Int = {
    val sql = "SELECT COUNT(*) FROM postulaciones"

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      val resultado = use(sentencia.executeQuery())

      if (resultado.next()) resultado.getInt(1) else 0
    }.get
  }

  private def mapear(resultado: ResultSet): PostulacionResumen =
    PostulacionResumen(
      idPostulacion = resultado.getLong("id_postulacion"),
      estudiante = resultado.getString("estudiante"),
      cedula = resultado.getString("cedula"),
      empresa = resultado.getString("empresa"),
      oferta = resultado.getString("oferta"),
      area = resultado.getString("area"),
      fechaPostulacion = resultado.getDate("fecha_postulacion").toLocalDate,
      estado = resultado.getString("estado"),
      rutaDocumentoMalla = Option(resultado.getString("ruta_documento_malla"))
    )

  private def mapearDetalle(resultado: ResultSet): PostulacionDetalle =
    PostulacionDetalle(
      idPostulacion = resultado.getLong("id_postulacion"),
      idEstudiante = resultado.getLong("id_estudiante"),
      estudiante = resultado.getString("estudiante"),
      idOferta = resultado.getLong("id_oferta"),
      oferta = resultado.getString("oferta"),
      area = resultado.getString("area"),
      idEmpresa = resultado.getLong("id_empresa"),
      empresa = resultado.getString("empresa"),
      empresaTieneConvenio = resultado.getBoolean("tiene_convenio"),
      cuposOferta = resultado.getInt("cupos"),
      fechaPostulacion = resultado.getDate("fecha_postulacion").toLocalDate,
      estado = resultado.getString("estado"),
      rutaDocumentoMalla = Option(resultado.getString("ruta_documento_malla")),
      idPractica = obtenerLongOpcional(resultado, "id_practica"),
      fechaInicioPractica = Option(resultado.getDate("fecha_inicio")).map(_.toLocalDate),
      fechaFinPractica = Option(resultado.getDate("fecha_fin")).map(_.toLocalDate),
      tutorAcademico = Option(resultado.getString("tutor_academico")),
      tutorEmpresarial = Option(resultado.getString("tutor_empresarial"))
    )

  private def obtenerLongOpcional(resultado: ResultSet, columna: String): Option[Long] = {
    val valor = resultado.getLong(columna)
    if (resultado.wasNull()) None else Some(valor)
  }
}
