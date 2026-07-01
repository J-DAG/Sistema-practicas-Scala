package modelo.servicios

import modelo.bd.Conexion

import java.sql.{Connection, Date}
import java.time.LocalDate

class PostulacionServicio {

  def aprobar(
      idPostulacion: Long,
      idTutorAcademico: Long,
      idTutorEmpresarial: Long,
      idCoordinador: Long,
      fechaInicio: LocalDate,
      fechaFin: LocalDate
  ): Long = {
    val conexion = Conexion.obtener()
    try {
      conexion.setAutoCommit(false)

      val detalle = obtenerDetalleBloqueado(conexion, idPostulacion)
      validarAprobacion(conexion, detalle, idTutorAcademico, idTutorEmpresarial, fechaInicio, fechaFin)

      actualizarPostulacion(conexion, idPostulacion, "aprobada")
      descontarCupo(conexion, detalle.idOferta)
      val idPractica = crearPractica(conexion, detalle, idTutorAcademico, idTutorEmpresarial, fechaInicio, fechaFin)

      notificar(conexion, detalle.idEstudiante, "Practica aprobada", s"Tu postulacion a '${detalle.oferta}' fue aprobada. Practica activa creada.")
      notificar(conexion, idTutorAcademico, "Nuevo estudiante asignado", s"Se te asigno el estudiante ${detalle.estudiante} para la practica '${detalle.oferta}'.")
      notificar(conexion, idTutorEmpresarial, "Nuevo estudiante asignado", s"Se te asigno el estudiante ${detalle.estudiante} para gestionar actividades en ${detalle.empresa}.")
      notificar(conexion, idCoordinador, "Practica creada", s"Se creo la practica de ${detalle.estudiante} para la oferta '${detalle.oferta}'.")

      if (!detalle.empresaTieneConvenio) {
        notificar(conexion, detalle.idEstudiante, "Carta compromiso", s"La empresa ${detalle.empresa} no tiene convenio. Se genero la carta compromiso para tu practica.")
      }

      conexion.commit()
      idPractica
    } catch {
      case error: Exception =>
        conexion.rollback()
        throw error
    } finally {
      conexion.close()
    }
  }

  def negar(idPostulacion: Long, idCoordinador: Long): Unit = {
    val conexion = Conexion.obtener()
    try {
      conexion.setAutoCommit(false)
      val detalle = obtenerDetalleBloqueado(conexion, idPostulacion)
      if (detalle.estado.equalsIgnoreCase("aprobada")) {
        revertirPracticaAprobada(conexion, detalle)
      } else if (!detalle.estado.equalsIgnoreCase("pendiente")) {
        throw new IllegalStateException("Solo se pueden negar postulaciones pendientes o aprobadas.")
      }
      actualizarPostulacion(conexion, idPostulacion, "negada")
      notificar(conexion, detalle.idEstudiante, "Postulacion negada", s"Tu postulacion a '${detalle.oferta}' fue negada por coordinacion.")
      notificar(conexion, idCoordinador, "Postulacion negada", s"Se nego la postulacion de ${detalle.estudiante} a '${detalle.oferta}'.")
      conexion.commit()
    } catch {
      case error: Exception =>
        conexion.rollback()
        throw error
    } finally {
      conexion.close()
    }
  }

  def cancelarAprobacion(idPostulacion: Long, idCoordinador: Long): Unit = {
    val conexion = Conexion.obtener()
    try {
      conexion.setAutoCommit(false)
      val detalle = obtenerDetalleBloqueado(conexion, idPostulacion)
      if (!detalle.estado.equalsIgnoreCase("aprobada")) {
        throw new IllegalStateException("Solo se puede cancelar la aprobacion de postulaciones aprobadas.")
      }

      revertirPracticaAprobada(conexion, detalle)
      actualizarPostulacion(conexion, idPostulacion, "pendiente")
      notificar(conexion, detalle.idEstudiante, "Aprobacion cancelada", s"La aprobacion de tu postulacion a '${detalle.oferta}' fue cancelada por coordinacion.")
      notificar(conexion, idCoordinador, "Aprobacion cancelada", s"Se cancelo la aprobacion de ${detalle.estudiante} a '${detalle.oferta}'.")
      conexion.commit()
    } catch {
      case error: Exception =>
        conexion.rollback()
        throw error
    } finally {
      conexion.close()
    }
  }

  private case class DetalleAprobacion(
      idPostulacion: Long,
      idEstudiante: Long,
      estudiante: String,
      idOferta: Long,
      oferta: String,
      idEmpresa: Long,
      empresa: String,
      empresaTieneConvenio: Boolean,
      cupos: Int,
      estado: String
  )

  private def obtenerDetalleBloqueado(conexion: Connection, idPostulacion: Long): DetalleAprobacion = {
    val sql =
      """
        |SELECT p.id_postulacion, p.id_estudiante, u.nombres || ' ' || u.apellidos AS estudiante,
        |       p.id_oferta, o.titulo AS oferta, o.id_empresa, e.nombre AS empresa,
        |       e.tiene_convenio, o.cupos, p.estado
        |FROM postulaciones p
        |JOIN usuarios u ON u.id_usuario = p.id_estudiante
        |JOIN ofertas o ON o.id_oferta = p.id_oferta
        |JOIN empresas e ON e.id_empresa = o.id_empresa
        |WHERE p.id_postulacion = ?
        |FOR UPDATE
        |""".stripMargin
    val sentencia = conexion.prepareStatement(sql)
    try {
      sentencia.setLong(1, idPostulacion)
      val resultado = sentencia.executeQuery()
      try {
        if (!resultado.next()) {
          throw new IllegalStateException("No se encontro la postulacion.")
        }
        DetalleAprobacion(
          idPostulacion = resultado.getLong("id_postulacion"),
          idEstudiante = resultado.getLong("id_estudiante"),
          estudiante = resultado.getString("estudiante"),
          idOferta = resultado.getLong("id_oferta"),
          oferta = resultado.getString("oferta"),
          idEmpresa = resultado.getLong("id_empresa"),
          empresa = resultado.getString("empresa"),
          empresaTieneConvenio = resultado.getBoolean("tiene_convenio"),
          cupos = resultado.getInt("cupos"),
          estado = resultado.getString("estado")
        )
      } finally {
        resultado.close()
      }
    } finally {
      sentencia.close()
    }
  }

  private def validarAprobacion(
      conexion: Connection,
      detalle: DetalleAprobacion,
      idTutorAcademico: Long,
      idTutorEmpresarial: Long,
      fechaInicio: LocalDate,
      fechaFin: LocalDate
  ): Unit = {
    if (!detalle.estado.equalsIgnoreCase("pendiente") && !detalle.estado.equalsIgnoreCase("negada")) {
      throw new IllegalStateException("Solo se pueden aprobar postulaciones pendientes o negadas.")
    }
    if (detalle.cupos <= 0) {
      throw new IllegalStateException("La oferta no tiene cupos disponibles.")
    }
    if (fechaFin.isBefore(fechaInicio) || fechaFin.isEqual(fechaInicio)) {
      throw new IllegalStateException("La fecha fin debe ser posterior a la fecha inicio.")
    }
    if (!usuarioTieneRol(conexion, idTutorAcademico, "tutor_academico", None)) {
      throw new IllegalStateException("El tutor academico seleccionado no es valido.")
    }
    if (!usuarioTieneRol(conexion, idTutorEmpresarial, "tutor_empresarial", Some(detalle.idEmpresa))) {
      throw new IllegalStateException("El tutor empresarial debe pertenecer a la misma empresa de la oferta.")
    }
  }

  private def usuarioTieneRol(conexion: Connection, idUsuario: Long, rol: String, idEmpresa: Option[Long]): Boolean = {
    val sql =
      """
        |SELECT COUNT(*)
        |FROM usuarios
        |WHERE id_usuario = ?
        |  AND LOWER(rol) = LOWER(?)
        |  AND activo = TRUE
        |  AND (? IS NULL OR id_empresa = ?)
        |""".stripMargin
    val sentencia = conexion.prepareStatement(sql)
    try {
      sentencia.setLong(1, idUsuario)
      sentencia.setString(2, rol)
      idEmpresa match {
        case Some(id) =>
          sentencia.setLong(3, id)
          sentencia.setLong(4, id)
        case None =>
          sentencia.setNull(3, java.sql.Types.BIGINT)
          sentencia.setNull(4, java.sql.Types.BIGINT)
      }
      val resultado = sentencia.executeQuery()
      try resultado.next() && resultado.getInt(1) > 0
      finally resultado.close()
    } finally {
      sentencia.close()
    }
  }

  private def actualizarPostulacion(conexion: Connection, idPostulacion: Long, estado: String): Unit = {
    val sentencia = conexion.prepareStatement("UPDATE postulaciones SET estado = ? WHERE id_postulacion = ?")
    try {
      sentencia.setString(1, estado)
      sentencia.setLong(2, idPostulacion)
      sentencia.executeUpdate()
    } finally {
      sentencia.close()
    }
  }

  private def descontarCupo(conexion: Connection, idOferta: Long): Unit = {
    val sentencia = conexion.prepareStatement("UPDATE ofertas SET cupos = cupos - 1 WHERE id_oferta = ? AND cupos > 0")
    try {
      sentencia.setLong(1, idOferta)
      if (sentencia.executeUpdate() == 0) {
        throw new IllegalStateException("No se pudo descontar cupo de la oferta.")
      }
    } finally {
      sentencia.close()
    }
  }

  private def devolverCupo(conexion: Connection, idOferta: Long): Unit = {
    val sentencia = conexion.prepareStatement("UPDATE ofertas SET cupos = cupos + 1 WHERE id_oferta = ?")
    try {
      sentencia.setLong(1, idOferta)
      sentencia.executeUpdate()
    } finally {
      sentencia.close()
    }
  }

  private def revertirPracticaAprobada(conexion: Connection, detalle: DetalleAprobacion): Unit = {
    val idPractica = buscarPracticaActivaPorPostulacion(conexion, detalle.idPostulacion)
    val actividades = contarActividades(conexion, idPractica)
    if (actividades > 0) {
      throw new IllegalStateException("No se puede cancelar la aprobacion porque la practica ya tiene actividades registradas.")
    }

    eliminarPractica(conexion, idPractica)
    devolverCupo(conexion, detalle.idOferta)
  }

  private def buscarPracticaActivaPorPostulacion(conexion: Connection, idPostulacion: Long): Long = {
    val sql =
      """
        |SELECT id_practica
        |FROM practicas
        |WHERE id_postulacion = ?
        |  AND LOWER(estado) = 'activa'
        |ORDER BY id_practica DESC
        |LIMIT 1
        |FOR UPDATE
        |""".stripMargin
    val sentencia = conexion.prepareStatement(sql)
    try {
      sentencia.setLong(1, idPostulacion)
      val resultado = sentencia.executeQuery()
      try {
        if (resultado.next()) resultado.getLong("id_practica")
        else throw new IllegalStateException("No se encontro una practica activa asociada a la postulacion aprobada.")
      } finally {
        resultado.close()
      }
    } finally {
      sentencia.close()
    }
  }

  private def contarActividades(conexion: Connection, idPractica: Long): Int = {
    val sentencia = conexion.prepareStatement("SELECT COUNT(*) FROM actividades WHERE id_practica = ?")
    try {
      sentencia.setLong(1, idPractica)
      val resultado = sentencia.executeQuery()
      try {
        if (resultado.next()) resultado.getInt(1) else 0
      } finally {
        resultado.close()
      }
    } finally {
      sentencia.close()
    }
  }

  private def eliminarPractica(conexion: Connection, idPractica: Long): Unit = {
    val sentencia = conexion.prepareStatement("DELETE FROM practicas WHERE id_practica = ?")
    try {
      sentencia.setLong(1, idPractica)
      sentencia.executeUpdate()
    } finally {
      sentencia.close()
    }
  }

  private def crearPractica(
      conexion: Connection,
      detalle: DetalleAprobacion,
      idTutorAcademico: Long,
      idTutorEmpresarial: Long,
      fechaInicio: LocalDate,
      fechaFin: LocalDate
  ): Long = {
    val sql =
      """
        |INSERT INTO practicas (
        |  id_postulacion, id_estudiante, id_empresa, fecha_inicio, fecha_fin,
        |  id_tutor_academico, id_tutor_empresarial, estado, horas_cumplidas
        |)
        |VALUES (?, ?, ?, ?, ?, ?, ?, 'activa', 0)
        |RETURNING id_practica
        |""".stripMargin
    val sentencia = conexion.prepareStatement(sql)
    try {
      sentencia.setLong(1, detalle.idPostulacion)
      sentencia.setLong(2, detalle.idEstudiante)
      sentencia.setLong(3, detalle.idEmpresa)
      sentencia.setDate(4, Date.valueOf(fechaInicio))
      sentencia.setDate(5, Date.valueOf(fechaFin))
      sentencia.setLong(6, idTutorAcademico)
      sentencia.setLong(7, idTutorEmpresarial)
      val resultado = sentencia.executeQuery()
      try {
        if (resultado.next()) resultado.getLong(1)
        else throw new IllegalStateException("No se pudo crear la practica.")
      } finally {
        resultado.close()
      }
    } finally {
      sentencia.close()
    }
  }

  private def notificar(conexion: Connection, idUsuario: Long, titulo: String, mensaje: String): Unit = {
    val sql = "INSERT INTO notificaciones (id_usuario, titulo, mensaje, tipo, leida) VALUES (?, ?, ?, 'informativa', FALSE)"
    val sentencia = conexion.prepareStatement(sql)
    try {
      sentencia.setLong(1, idUsuario)
      sentencia.setString(2, titulo)
      sentencia.setString(3, mensaje)
      sentencia.executeUpdate()
    } finally {
      sentencia.close()
    }
  }
}
