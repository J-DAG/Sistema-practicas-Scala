package modelo.repositorios

import modelo.bd.Conexion
import modelo.entidades.Actividad

import java.sql.{Connection, Date, ResultSet}
import java.time.LocalDate
import scala.util.Using

class ActividadRepositorio {

  def listarPorPractica(idPractica: Long, filtro: String = ""): List[Actividad] = {
    val sql =
      """
        |SELECT id_actividad, id_practica, descripcion, horas, fecha, estado_revision,
        |       aprobada_por_tutor_academico, completada_por_tutor_empresarial
        |FROM actividades
        |WHERE id_practica = ?
        |  AND (
        |    ? = ''
        |    OR LOWER(descripcion) LIKE ?
        |    OR CAST(horas AS TEXT) LIKE ?
        |    OR LOWER(
        |      CASE
        |        WHEN completada_por_tutor_empresarial THEN 'completada'
        |        WHEN aprobada_por_tutor_academico THEN 'aprobada'
        |        WHEN LOWER(estado_revision) = 'negada' THEN 'negada'
        |        ELSE 'pendiente aprobacion'
        |      END
        |    ) LIKE ?
        |  )
        |ORDER BY fecha DESC, id_actividad DESC
        |""".stripMargin
    val busqueda = filtro.trim.toLowerCase
    val patron = s"%$busqueda%"

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setLong(1, idPractica)
      sentencia.setString(2, busqueda)
      sentencia.setString(3, patron)
      sentencia.setString(4, patron)
      sentencia.setString(5, patron)
      val resultado = use(sentencia.executeQuery())
      val actividades = List.newBuilder[Actividad]

      while (resultado.next()) {
        actividades += mapear(resultado)
      }

      actividades.result()
    }.get
  }

  def crear(idPractica: Long, descripcion: String, horas: Int): Long = {
    val conexion = Conexion.obtener()
    try {
      conexion.setAutoCommit(false)
      validarPracticaActiva(conexion, idPractica)
      validarHorasDisponibles(conexion, idPractica, horas)

      val sql =
        """
          |INSERT INTO actividades (id_practica, descripcion, horas, fecha)
          |VALUES (?, ?, ?, ?)
          |RETURNING id_actividad
          |""".stripMargin
      val sentencia = conexion.prepareStatement(sql)
      try {
        sentencia.setLong(1, idPractica)
        sentencia.setString(2, descripcion)
        sentencia.setInt(3, horas)
        sentencia.setDate(4, Date.valueOf(LocalDate.now()))
        val resultado = sentencia.executeQuery()
        try {
          val id = if (resultado.next()) resultado.getLong(1) else 0L
          conexion.commit()
          id
        } finally {
          resultado.close()
        }
      } finally {
        sentencia.close()
      }
    } catch {
      case error: Exception =>
        conexion.rollback()
        throw error
    } finally {
      conexion.close()
    }
  }

  def actualizar(idActividad: Long, descripcion: String, horas: Int): Unit = {
    val conexion = Conexion.obtener()
    try {
      conexion.setAutoCommit(false)
      val actual = buscarBloqueada(conexion, idActividad)
      validarEditable(actual)
      validarPracticaActiva(conexion, actual.idPractica)
      val horasAValidar = if (actual.estado.equalsIgnoreCase("negada")) horas else horas - actual.horas
      validarHorasDisponibles(conexion, actual.idPractica, horasAValidar)

      val sentencia = conexion.prepareStatement(
        """
          |UPDATE actividades
          |SET descripcion = ?,
          |    horas = ?,
          |    estado_revision = 'pendiente',
          |    aprobada_por_tutor_academico = FALSE,
          |    id_tutor_academico_aprobador = NULL,
          |    fecha_aprobacion = NULL
          |WHERE id_actividad = ?
          |""".stripMargin
      )
      try {
        sentencia.setString(1, descripcion)
        sentencia.setInt(2, horas)
        sentencia.setLong(3, idActividad)
        sentencia.executeUpdate()
        conexion.commit()
      } finally {
        sentencia.close()
      }
    } catch {
      case error: Exception =>
        conexion.rollback()
        throw error
    } finally {
      conexion.close()
    }
  }

  def eliminar(idActividad: Long): Unit = {
    val conexion = Conexion.obtener()
    try {
      conexion.setAutoCommit(false)
      val actual = buscarBloqueada(conexion, idActividad)
      validarEliminable(actual)

      val sentencia = conexion.prepareStatement("DELETE FROM actividades WHERE id_actividad = ?")
      try {
        sentencia.setLong(1, idActividad)
        sentencia.executeUpdate()
        conexion.commit()
      } finally {
        sentencia.close()
      }
    } catch {
      case error: Exception =>
        conexion.rollback()
        throw error
    } finally {
      conexion.close()
    }
  }

  def marcarCompletada(idActividad: Long, idTutorEmpresarial: Long): Unit = {
    val conexion = Conexion.obtener()
    try {
      conexion.setAutoCommit(false)
      val actividad = buscarBloqueada(conexion, idActividad)
      if (!actividad.aprobadaPorTutorAcademico) {
        throw new IllegalStateException("Solo se pueden completar actividades aprobadas por el tutor academico.")
      }
      if (!actividad.estado.equalsIgnoreCase("aprobada")) {
        throw new IllegalStateException("Solo se pueden completar actividades en estado aprobada.")
      }
      validarPracticaActiva(conexion, actividad.idPractica)

      val sentencia = conexion.prepareStatement(
        """
          |UPDATE actividades
          |SET completada_por_tutor_empresarial = TRUE,
          |    estado_revision = 'completada',
          |    id_tutor_empresarial_completador = ?,
          |    fecha_completado = CURRENT_DATE
          |WHERE id_actividad = ?
          |""".stripMargin
      )
      try {
        sentencia.setLong(1, idTutorEmpresarial)
        sentencia.setLong(2, idActividad)
        sentencia.executeUpdate()
        recalcularHorasCumplidas(conexion, actividad.idPractica)
        finalizarSiCompleta(conexion, actividad.idPractica)
        conexion.commit()
      } finally {
        sentencia.close()
      }
    } catch {
      case error: Exception =>
        conexion.rollback()
        throw error
    } finally {
      conexion.close()
    }
  }

  def marcarPendiente(idActividad: Long): Unit = {
    val conexion = Conexion.obtener()
    try {
      conexion.setAutoCommit(false)
      val actividad = buscarBloqueada(conexion, idActividad)
      validarPracticaActivaOFinalizada(conexion, actividad.idPractica)

      val sentencia = conexion.prepareStatement(
        """
          |UPDATE actividades
          |SET completada_por_tutor_empresarial = FALSE,
          |    estado_revision = 'aprobada',
          |    id_tutor_empresarial_completador = NULL,
          |    fecha_completado = NULL
          |WHERE id_actividad = ?
          |""".stripMargin
      )
      try {
        sentencia.setLong(1, idActividad)
        sentencia.executeUpdate()
        recalcularHorasCumplidas(conexion, actividad.idPractica)
        reabrirSiBajaDe240(conexion, actividad.idPractica)
        conexion.commit()
      } finally {
        sentencia.close()
      }
    } catch {
      case error: Exception =>
        conexion.rollback()
        throw error
    } finally {
      conexion.close()
    }
  }

  def aprobarPorTutorAcademico(idActividad: Long, idTutorAcademico: Long): Unit = {
    val conexion = Conexion.obtener()
    try {
      conexion.setAutoCommit(false)
      val actividad = buscarBloqueada(conexion, idActividad)
      if (actividad.completadaPorTutorEmpresarial) {
        throw new IllegalStateException("No se puede aprobar una actividad que ya fue completada.")
      }
      if (actividad.estado.equalsIgnoreCase("aprobada")) {
        throw new IllegalStateException("La actividad ya esta aprobada.")
      }
      validarPracticaActiva(conexion, actividad.idPractica)

      val sentencia = conexion.prepareStatement(
        """
          |UPDATE actividades
          |SET estado_revision = 'aprobada',
          |    aprobada_por_tutor_academico = TRUE,
          |    id_tutor_academico_aprobador = ?,
          |    fecha_aprobacion = CURRENT_DATE
          |WHERE id_actividad = ?
          |""".stripMargin
      )
      try {
        sentencia.setLong(1, idTutorAcademico)
        sentencia.setLong(2, idActividad)
        sentencia.executeUpdate()
        conexion.commit()
      } finally {
        sentencia.close()
      }
    } catch {
      case error: Exception =>
        conexion.rollback()
        throw error
    } finally {
      conexion.close()
    }
  }

  def negarPorTutorAcademico(idActividad: Long): Unit = {
    val conexion = Conexion.obtener()
    try {
      conexion.setAutoCommit(false)
      val actividad = buscarBloqueada(conexion, idActividad)
      if (actividad.completadaPorTutorEmpresarial) {
        throw new IllegalStateException("No se puede negar una actividad que ya fue completada.")
      }
      validarPracticaActiva(conexion, actividad.idPractica)

      val sentencia = conexion.prepareStatement(
        """
          |UPDATE actividades
          |SET estado_revision = 'negada',
          |    aprobada_por_tutor_academico = FALSE,
          |    id_tutor_academico_aprobador = NULL,
          |    fecha_aprobacion = NULL
          |WHERE id_actividad = ?
          |""".stripMargin
      )
      try {
        sentencia.setLong(1, idActividad)
        sentencia.executeUpdate()
      } finally {
        sentencia.close()
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

  private def validarEditable(actividad: Actividad): Unit = {
    if (!actividad.puedeEditar) {
      throw new IllegalStateException("Solo se pueden editar actividades pendientes o negadas.")
    }
  }

  private def validarEliminable(actividad: Actividad): Unit = {
    if (!actividad.puedeEliminar) {
      throw new IllegalStateException("Solo se pueden eliminar actividades pendientes de aprobacion.")
    }
  }

  private def validarHorasDisponibles(conexion: Connection, idPractica: Long, horasNuevas: Int): Unit = {
    if (horasNuevas <= 0) {
      return
    }
    val total = totalHorasPlanificadas(conexion, idPractica)
    if (total + horasNuevas > 240) {
      throw new IllegalStateException(s"La practica no puede superar 240 horas. Horas planificadas actuales: $total.")
    }
  }

  private def totalHorasPlanificadas(conexion: Connection, idPractica: Long): Int = {
    val sentencia = conexion.prepareStatement("SELECT COALESCE(SUM(horas), 0) FROM actividades WHERE id_practica = ? AND LOWER(estado_revision) <> 'negada'")
    try {
      sentencia.setLong(1, idPractica)
      val resultado = sentencia.executeQuery()
      try if (resultado.next()) resultado.getInt(1) else 0
      finally resultado.close()
    } finally {
      sentencia.close()
    }
  }

  private def buscarBloqueada(conexion: Connection, idActividad: Long): Actividad = {
    val sentencia = conexion.prepareStatement(
      """
        |SELECT id_actividad, id_practica, descripcion, horas, fecha, estado_revision,
        |       aprobada_por_tutor_academico, completada_por_tutor_empresarial
        |FROM actividades
        |WHERE id_actividad = ?
        |FOR UPDATE
        |""".stripMargin
    )
    try {
      sentencia.setLong(1, idActividad)
      val resultado = sentencia.executeQuery()
      try {
        if (resultado.next()) mapear(resultado)
        else throw new IllegalStateException("No se encontro la actividad.")
      } finally {
        resultado.close()
      }
    } finally {
      sentencia.close()
    }
  }

  private def validarPracticaActiva(conexion: Connection, idPractica: Long): Unit = {
    val estado = estadoPractica(conexion, idPractica)
    if (!estado.equalsIgnoreCase("activa")) {
      throw new IllegalStateException("Solo se pueden gestionar actividades de practicas activas.")
    }
  }

  private def validarPracticaActivaOFinalizada(conexion: Connection, idPractica: Long): Unit = {
    val estado = estadoPractica(conexion, idPractica)
    if (!Set("activa", "finalizada", "completada").contains(estado.toLowerCase)) {
      throw new IllegalStateException("La practica no permite modificar actividades.")
    }
  }

  private def estadoPractica(conexion: Connection, idPractica: Long): String = {
    val sentencia = conexion.prepareStatement("SELECT estado FROM practicas WHERE id_practica = ? FOR UPDATE")
    try {
      sentencia.setLong(1, idPractica)
      val resultado = sentencia.executeQuery()
      try {
        if (resultado.next()) resultado.getString("estado")
        else throw new IllegalStateException("No se encontro la practica.")
      } finally {
        resultado.close()
      }
    } finally {
      sentencia.close()
    }
  }

  private def recalcularHorasCumplidas(conexion: Connection, idPractica: Long): Int = {
    val sentencia = conexion.prepareStatement(
      """
        |UPDATE practicas
        |SET horas_cumplidas = (
        |  SELECT COALESCE(SUM(horas), 0)
        |  FROM actividades
        |  WHERE id_practica = ?
        |    AND aprobada_por_tutor_academico = TRUE
        |    AND completada_por_tutor_empresarial = TRUE
        |)
        |WHERE id_practica = ?
        |RETURNING horas_cumplidas
        |""".stripMargin
    )
    try {
      sentencia.setLong(1, idPractica)
      sentencia.setLong(2, idPractica)
      val resultado = sentencia.executeQuery()
      try if (resultado.next()) resultado.getInt("horas_cumplidas") else 0
      finally resultado.close()
    } finally {
      sentencia.close()
    }
  }

  private def finalizarSiCompleta(conexion: Connection, idPractica: Long): Unit = {
    val horas = obtenerHorasCumplidas(conexion, idPractica)
    if (horas >= 240) {
      val sentencia = conexion.prepareStatement("UPDATE practicas SET estado = 'finalizada' WHERE id_practica = ?")
      try {
        sentencia.setLong(1, idPractica)
        sentencia.executeUpdate()
      } finally {
        sentencia.close()
      }
      notificarActoresPractica(conexion, idPractica)
    }
  }

  private def reabrirSiBajaDe240(conexion: Connection, idPractica: Long): Unit = {
    val horas = obtenerHorasCumplidas(conexion, idPractica)
    if (horas < 240) {
      val sentencia = conexion.prepareStatement("UPDATE practicas SET estado = 'activa' WHERE id_practica = ? AND LOWER(estado) IN ('finalizada', 'completada')")
      try {
        sentencia.setLong(1, idPractica)
        sentencia.executeUpdate()
      } finally {
        sentencia.close()
      }
    }
  }

  private def obtenerHorasCumplidas(conexion: Connection, idPractica: Long): Int = {
    val sentencia = conexion.prepareStatement("SELECT horas_cumplidas FROM practicas WHERE id_practica = ?")
    try {
      sentencia.setLong(1, idPractica)
      val resultado = sentencia.executeQuery()
      try if (resultado.next()) resultado.getInt("horas_cumplidas") else 0
      finally resultado.close()
    } finally {
      sentencia.close()
    }
  }

  private def notificarActoresPractica(conexion: Connection, idPractica: Long): Unit = {
    val sql =
      """
        |SELECT p.id_estudiante, p.id_tutor_academico, p.id_tutor_empresarial,
        |       po.id_oferta, o.titulo AS oferta,
        |       (SELECT id_usuario FROM usuarios WHERE LOWER(rol) = 'coordinador' AND activo = TRUE ORDER BY id_usuario LIMIT 1) AS id_coordinador
        |FROM practicas p
        |JOIN postulaciones po ON po.id_postulacion = p.id_postulacion
        |JOIN ofertas o ON o.id_oferta = po.id_oferta
        |WHERE p.id_practica = ?
        |""".stripMargin
    val sentencia = conexion.prepareStatement(sql)
    try {
      sentencia.setLong(1, idPractica)
      val resultado = sentencia.executeQuery()
      try {
        if (resultado.next()) {
          val oferta = resultado.getString("oferta")
          val usuarios = List(
            resultado.getLong("id_estudiante"),
            resultado.getLong("id_tutor_academico"),
            resultado.getLong("id_tutor_empresarial"),
            resultado.getLong("id_coordinador")
          ).filter(_ > 0).distinct
          usuarios.foreach { idUsuario =>
            notificar(conexion, idUsuario, "Practica finalizada", s"La practica '$oferta' completo las 240 horas requeridas.")
          }
        }
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

  private def mapear(resultado: ResultSet): Actividad =
    Actividad(
      idActividad = resultado.getLong("id_actividad"),
      idPractica = resultado.getLong("id_practica"),
      descripcion = resultado.getString("descripcion"),
      horas = resultado.getInt("horas"),
      fecha = resultado.getDate("fecha").toLocalDate,
      estadoRevision = resultado.getString("estado_revision"),
      aprobadaPorTutorAcademico = resultado.getBoolean("aprobada_por_tutor_academico"),
      completadaPorTutorEmpresarial = resultado.getBoolean("completada_por_tutor_empresarial")
    )
}
