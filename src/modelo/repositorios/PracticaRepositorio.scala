package modelo.repositorios

import modelo.bd.Conexion
import modelo.entidades.{PracticaCoordinadorResumen, PracticaEstudianteDetalle, PracticaResumen}

import java.sql.Connection
import java.sql.ResultSet
import scala.util.Using

class PracticaRepositorio {

  def listarCoordinador(filtro: String = ""): List[PracticaCoordinadorResumen] = {
    val sql =
      """
        |SELECT
        |  p.id_practica,
        |  p.id_estudiante,
        |  est.nombres || ' ' || est.apellidos AS estudiante,
        |  est.cedula,
        |  e.nombre AS empresa,
        |  o.titulo AS oferta,
        |  o.area,
        |  ta.nombres || ' ' || ta.apellidos AS tutor_academico,
        |  te.nombres || ' ' || te.apellidos AS tutor_empresarial,
        |  p.fecha_inicio,
        |  p.fecha_fin,
        |  p.estado,
        |  p.horas_cumplidas,
        |  p.calificacion
        |FROM practicas p
        |JOIN usuarios est ON est.id_usuario = p.id_estudiante
        |JOIN empresas e ON e.id_empresa = p.id_empresa
        |JOIN postulaciones po ON po.id_postulacion = p.id_postulacion
        |JOIN ofertas o ON o.id_oferta = po.id_oferta
        |JOIN usuarios ta ON ta.id_usuario = p.id_tutor_academico
        |JOIN usuarios te ON te.id_usuario = p.id_tutor_empresarial
        |WHERE
        |  ? = ''
        |  OR LOWER(est.nombres || ' ' || est.apellidos) LIKE ?
        |  OR LOWER(est.cedula) LIKE ?
        |  OR LOWER(e.nombre) LIKE ?
        |  OR LOWER(o.titulo) LIKE ?
        |  OR LOWER(o.area) LIKE ?
        |  OR LOWER(ta.nombres || ' ' || ta.apellidos) LIKE ?
        |  OR LOWER(te.nombres || ' ' || te.apellidos) LIKE ?
        |  OR LOWER(p.estado) LIKE ?
        |ORDER BY p.fecha_inicio DESC, p.id_practica DESC
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
      val practicas = List.newBuilder[PracticaCoordinadorResumen]

      while (resultado.next()) {
        practicas += mapearCoordinador(resultado)
      }

      practicas.result()
    }.get
  }

  def contarEstados(): Map[String, Int] = {
    val sql =
      """
        |SELECT LOWER(estado) AS estado, COUNT(*) AS total
        |FROM practicas
        |GROUP BY LOWER(estado)
        |""".stripMargin

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      val resultado = use(sentencia.executeQuery())
      val conteos = Map.newBuilder[String, Int]

      while (resultado.next()) {
        conteos += resultado.getString("estado") -> resultado.getInt("total")
      }

      conteos.result()
    }.get
  }

  def contarPorEstado(estado: String): Int = {
    val sql = "SELECT COUNT(*) FROM practicas WHERE LOWER(estado) = LOWER(?)"

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setString(1, estado)
      val resultado = use(sentencia.executeQuery())

      if (resultado.next()) resultado.getInt(1) else 0
    }.get
  }

  def listarPorTutorEmpresarial(idTutorEmpresarial: Long, estados: Set[String], filtro: String = ""): List[PracticaResumen] = {
    listarPorTutor("id_tutor_empresarial", idTutorEmpresarial, estados, filtro)
  }

  def listarPorTutorAcademico(idTutorAcademico: Long, estados: Set[String], filtro: String = ""): List[PracticaResumen] = {
    listarPorTutor("id_tutor_academico", idTutorAcademico, estados, filtro)
  }

  private def listarPorTutor(columnaTutor: String, idTutor: Long, estados: Set[String], filtro: String): List[PracticaResumen] = {
    val estadosSql = estados.map(_.toLowerCase)
    val sql =
      s"""
        |SELECT
        |  p.id_practica,
        |  p.id_estudiante,
        |  u.nombres || ' ' || u.apellidos AS estudiante,
        |  u.cedula,
        |  e.nombre AS empresa,
        |  o.titulo AS oferta,
        |  o.area,
        |  p.fecha_inicio,
        |  p.fecha_fin,
        |  p.estado,
        |  p.horas_cumplidas,
        |  COALESCE(SUM(a.horas), 0) AS total_horas_actividades
        |FROM practicas p
        |JOIN usuarios u ON u.id_usuario = p.id_estudiante
        |JOIN empresas e ON e.id_empresa = p.id_empresa
        |JOIN postulaciones po ON po.id_postulacion = p.id_postulacion
        |JOIN ofertas o ON o.id_oferta = po.id_oferta
        |LEFT JOIN actividades a ON a.id_practica = p.id_practica
        |WHERE p.$columnaTutor = ?
        |  AND LOWER(p.estado) = ANY (?)
        |  AND (
        |    ? = ''
        |    OR LOWER(u.nombres || ' ' || u.apellidos) LIKE ?
        |    OR LOWER(u.cedula) LIKE ?
        |    OR LOWER(e.nombre) LIKE ?
        |    OR LOWER(o.titulo) LIKE ?
        |    OR LOWER(o.area) LIKE ?
        |    OR LOWER(p.estado) LIKE ?
        |  )
        |GROUP BY p.id_practica, p.id_estudiante, u.nombres, u.apellidos, u.cedula,
        |         e.nombre, o.titulo, o.area, p.fecha_inicio, p.fecha_fin, p.estado, p.horas_cumplidas
        |ORDER BY p.fecha_inicio DESC, p.id_practica DESC
        |""".stripMargin
    val busqueda = filtro.trim.toLowerCase
    val patron = s"%$busqueda%"

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setLong(1, idTutor)
      sentencia.setArray(2, conexion.createArrayOf("text", estadosSql.toArray[AnyRef]))
      sentencia.setString(3, busqueda)
      sentencia.setString(4, patron)
      sentencia.setString(5, patron)
      sentencia.setString(6, patron)
      sentencia.setString(7, patron)
      sentencia.setString(8, patron)
      sentencia.setString(9, patron)
      val resultado = use(sentencia.executeQuery())
      val practicas = List.newBuilder[PracticaResumen]

      while (resultado.next()) {
        practicas += mapearResumen(resultado)
      }

      practicas.result()
    }.get
  }

  def contarPorTutorEmpresarial(idTutorEmpresarial: Long, estado: String): Int = {
    contarPorTutor("id_tutor_empresarial", idTutorEmpresarial, estado)
  }

  def contarPorTutorAcademico(idTutorAcademico: Long, estado: String): Int = {
    contarPorTutor("id_tutor_academico", idTutorAcademico, estado)
  }

  private def contarPorTutor(columnaTutor: String, idTutor: Long, estado: String): Int = {
    val sql = s"SELECT COUNT(*) FROM practicas WHERE $columnaTutor = ? AND LOWER(estado) = LOWER(?)"

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setLong(1, idTutor)
      sentencia.setString(2, estado)
      val resultado = use(sentencia.executeQuery())

      if (resultado.next()) resultado.getInt(1) else 0
    }.get
  }

  def contarEstudiantesPorTutorEmpresarial(idTutorEmpresarial: Long): Int = {
    contarEstudiantesPorTutor("id_tutor_empresarial", idTutorEmpresarial)
  }

  def contarEstudiantesPorTutorAcademico(idTutorAcademico: Long): Int = {
    contarEstudiantesPorTutor("id_tutor_academico", idTutorAcademico)
  }

  def buscarActualPorEstudiante(idEstudiante: Long): Option[PracticaEstudianteDetalle] = {
    val sql =
      """
        |SELECT
        |  p.id_practica,
        |  p.id_estudiante,
        |  e.nombre AS empresa,
        |  e.tiene_convenio,
        |  o.titulo AS oferta,
        |  o.area,
        |  p.fecha_inicio,
        |  p.fecha_fin,
        |  p.estado,
        |  p.horas_cumplidas,
        |  ta.nombres || ' ' || ta.apellidos AS tutor_academico,
        |  te.nombres || ' ' || te.apellidos AS tutor_empresarial,
        |  p.calificacion,
        |  p.formularios_finales_enviados
        |FROM practicas p
        |JOIN empresas e ON e.id_empresa = p.id_empresa
        |JOIN postulaciones po ON po.id_postulacion = p.id_postulacion
        |JOIN ofertas o ON o.id_oferta = po.id_oferta
        |JOIN usuarios ta ON ta.id_usuario = p.id_tutor_academico
        |JOIN usuarios te ON te.id_usuario = p.id_tutor_empresarial
        |WHERE p.id_estudiante = ?
        |  AND LOWER(p.estado) IN ('activa', 'finalizada', 'completada')
        |ORDER BY
        |  CASE LOWER(p.estado)
        |    WHEN 'activa' THEN 1
        |    WHEN 'finalizada' THEN 2
        |    WHEN 'completada' THEN 3
        |    ELSE 4
        |  END,
        |  p.id_practica DESC
        |LIMIT 1
        |""".stripMargin

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setLong(1, idEstudiante)
      val resultado = use(sentencia.executeQuery())

      if (resultado.next()) Some(mapearDetalleEstudiante(resultado)) else None
    }.get
  }

  def estudianteTienePracticaActiva(idEstudiante: Long): Boolean =
    existePracticaEstudiante(idEstudiante, Set("activa", "finalizada"))

  def estudianteYaTuvoPractica(idEstudiante: Long): Boolean =
    existePracticaEstudiante(idEstudiante, Set("completada"))

  def estudianteTieneOPoseePractica(idEstudiante: Long): Boolean =
    existePracticaEstudiante(idEstudiante, Set("activa", "finalizada", "completada"))

  private def existePracticaEstudiante(idEstudiante: Long, estados: Set[String]): Boolean = {
    val sql =
      """
        |SELECT COUNT(*)
        |FROM practicas
        |WHERE id_estudiante = ?
        |  AND LOWER(estado) = ANY (?)
        |""".stripMargin

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setLong(1, idEstudiante)
      sentencia.setArray(2, conexion.createArrayOf("text", estados.map(_.toLowerCase).toArray[AnyRef]))
      val resultado = use(sentencia.executeQuery())

      resultado.next() && resultado.getInt(1) > 0
    }.get
  }

  def calificarPractica(idPractica: Long, idTutorAcademico: Long, calificacion: Int): Unit = {
    if (calificacion < 0 || calificacion > 100) {
      throw new IllegalArgumentException("La calificacion debe estar entre 0 y 100.")
    }

    val conexion = Conexion.obtener()
    try {
      conexion.setAutoCommit(false)
      val detalle = obtenerDetalleCalificacion(conexion, idPractica, idTutorAcademico)
      if (!Set("finalizada", "completada").contains(detalle.estado.toLowerCase)) {
        throw new IllegalStateException("Solo se pueden calificar practicas finalizadas.")
      }
      if (detalle.horasCumplidas < 240) {
        throw new IllegalStateException("La practica debe tener 240 horas cumplidas para ser calificada.")
      }

      val sentencia = conexion.prepareStatement(
        """
          |UPDATE practicas
          |SET calificacion = ?,
          |    formularios_finales_enviados = TRUE,
          |    estado = 'completada'
          |WHERE id_practica = ?
          |  AND id_tutor_academico = ?
          |""".stripMargin
      )
      try {
        sentencia.setInt(1, calificacion)
        sentencia.setLong(2, idPractica)
        sentencia.setLong(3, idTutorAcademico)
        sentencia.executeUpdate()
      } finally {
        sentencia.close()
      }

      notificar(conexion, detalle.idEstudiante, "Formulario 2 disponible", s"Tu practica '${detalle.oferta}' fue calificada con $calificacion/100. El Formulario 2 final fue enviado a tu correo y esta disponible en Mis formularios.")
      notificar(conexion, detalle.idTutorEmpresarial, "Practica calificada", s"La practica '${detalle.oferta}' de ${detalle.estudiante} fue calificada con $calificacion/100.")
      detalle.idCoordinador.foreach { id =>
        notificar(conexion, id, "Practica calificada", s"La practica '${detalle.oferta}' de ${detalle.estudiante} fue calificada con $calificacion/100.")
      }
      conexion.commit()
    } catch {
      case error: Exception =>
        conexion.rollback()
        throw error
    } finally {
      conexion.close()
    }
  }

  private def contarEstudiantesPorTutor(columnaTutor: String, idTutor: Long): Int = {
    val sql = s"SELECT COUNT(DISTINCT id_estudiante) FROM practicas WHERE $columnaTutor = ?"

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setLong(1, idTutor)
      val resultado = use(sentencia.executeQuery())

      if (resultado.next()) resultado.getInt(1) else 0
    }.get
  }

  private case class DetalleCalificacion(
      idEstudiante: Long,
      estudiante: String,
      idTutorEmpresarial: Long,
      idCoordinador: Option[Long],
      oferta: String,
      estado: String,
      horasCumplidas: Int
  )

  private def obtenerDetalleCalificacion(conexion: Connection, idPractica: Long, idTutorAcademico: Long): DetalleCalificacion = {
    val sql =
      """
        |SELECT
        |  p.id_estudiante,
        |  u.nombres || ' ' || u.apellidos AS estudiante,
        |  p.id_tutor_empresarial,
        |  p.estado,
        |  p.horas_cumplidas,
        |  o.titulo AS oferta,
        |  (
        |    SELECT id_usuario
        |    FROM usuarios
        |    WHERE LOWER(rol) = 'coordinador' AND activo = TRUE
        |    ORDER BY id_usuario
        |    LIMIT 1
        |  ) AS id_coordinador
        |FROM practicas p
        |JOIN usuarios u ON u.id_usuario = p.id_estudiante
        |JOIN postulaciones po ON po.id_postulacion = p.id_postulacion
        |JOIN ofertas o ON o.id_oferta = po.id_oferta
        |WHERE p.id_practica = ?
        |  AND p.id_tutor_academico = ?
        |FOR UPDATE
        |""".stripMargin
    val sentencia = conexion.prepareStatement(sql)
    try {
      sentencia.setLong(1, idPractica)
      sentencia.setLong(2, idTutorAcademico)
      val resultado = sentencia.executeQuery()
      try {
        if (!resultado.next()) {
          throw new IllegalStateException("No se encontro una practica finalizada asignada al tutor academico.")
        }
        DetalleCalificacion(
          idEstudiante = resultado.getLong("id_estudiante"),
          estudiante = resultado.getString("estudiante"),
          idTutorEmpresarial = resultado.getLong("id_tutor_empresarial"),
          idCoordinador = obtenerLongOpcional(resultado, "id_coordinador"),
          oferta = resultado.getString("oferta"),
          estado = resultado.getString("estado"),
          horasCumplidas = resultado.getInt("horas_cumplidas")
        )
      } finally {
        resultado.close()
      }
    } finally {
      sentencia.close()
    }
  }

  private def notificar(conexion: Connection, idUsuario: Long, titulo: String, mensaje: String): Unit = {
    val sentencia = conexion.prepareStatement("INSERT INTO notificaciones (id_usuario, titulo, mensaje, tipo, leida) VALUES (?, ?, ?, 'informativa', FALSE)")
    try {
      sentencia.setLong(1, idUsuario)
      sentencia.setString(2, titulo)
      sentencia.setString(3, mensaje)
      sentencia.executeUpdate()
    } finally {
      sentencia.close()
    }
  }

  private def obtenerLongOpcional(resultado: ResultSet, columna: String): Option[Long] = {
    val valor = resultado.getLong(columna)
    if (resultado.wasNull()) None else Some(valor)
  }

  private def obtenerIntOpcional(resultado: ResultSet, columna: String): Option[Int] = {
    val valor = resultado.getInt(columna)
    if (resultado.wasNull()) None else Some(valor)
  }

  private def mapearDetalleEstudiante(resultado: ResultSet): PracticaEstudianteDetalle =
    PracticaEstudianteDetalle(
      idPractica = resultado.getLong("id_practica"),
      idEstudiante = resultado.getLong("id_estudiante"),
      empresa = resultado.getString("empresa"),
      empresaTieneConvenio = resultado.getBoolean("tiene_convenio"),
      oferta = resultado.getString("oferta"),
      area = resultado.getString("area"),
      fechaInicio = resultado.getDate("fecha_inicio").toLocalDate,
      fechaFin = resultado.getDate("fecha_fin").toLocalDate,
      estado = resultado.getString("estado"),
      horasCumplidas = resultado.getInt("horas_cumplidas"),
      tutorAcademico = resultado.getString("tutor_academico"),
      tutorEmpresarial = resultado.getString("tutor_empresarial"),
      calificacion = obtenerIntOpcional(resultado, "calificacion"),
      formulariosFinalesEnviados = resultado.getBoolean("formularios_finales_enviados")
    )

  private def mapearCoordinador(resultado: ResultSet): PracticaCoordinadorResumen =
    PracticaCoordinadorResumen(
      idPractica = resultado.getLong("id_practica"),
      idEstudiante = resultado.getLong("id_estudiante"),
      estudiante = resultado.getString("estudiante"),
      cedula = resultado.getString("cedula"),
      empresa = resultado.getString("empresa"),
      oferta = resultado.getString("oferta"),
      area = resultado.getString("area"),
      tutorAcademico = resultado.getString("tutor_academico"),
      tutorEmpresarial = resultado.getString("tutor_empresarial"),
      fechaInicio = resultado.getDate("fecha_inicio").toLocalDate,
      fechaFin = resultado.getDate("fecha_fin").toLocalDate,
      estado = resultado.getString("estado"),
      horasCumplidas = resultado.getInt("horas_cumplidas"),
      calificacion = obtenerIntOpcional(resultado, "calificacion")
    )

  private def mapearResumen(resultado: ResultSet): PracticaResumen =
    PracticaResumen(
      idPractica = resultado.getLong("id_practica"),
      idEstudiante = resultado.getLong("id_estudiante"),
      estudiante = resultado.getString("estudiante"),
      cedula = resultado.getString("cedula"),
      empresa = resultado.getString("empresa"),
      oferta = resultado.getString("oferta"),
      area = resultado.getString("area"),
      fechaInicio = resultado.getDate("fecha_inicio").toLocalDate,
      fechaFin = resultado.getDate("fecha_fin").toLocalDate,
      estado = resultado.getString("estado"),
      horasCumplidas = resultado.getInt("horas_cumplidas"),
      totalHorasActividades = resultado.getInt("total_horas_actividades")
    )
}
