package modelo.repositorios

import modelo.bd.Conexion
import modelo.entidades.Notificacion

import java.sql.ResultSet
import scala.util.Using

class NotificacionRepositorio {

  def listarPorUsuario(idUsuario: Long): List[Notificacion] = {
    val sql =
      """
        |SELECT id_notificacion, id_usuario, titulo, mensaje, fecha_creacion, tipo, leida
        |FROM notificaciones
        |WHERE id_usuario = ?
        |ORDER BY fecha_creacion DESC, id_notificacion DESC
        |LIMIT 100
        |""".stripMargin

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setLong(1, idUsuario)
      val resultado = use(sentencia.executeQuery())
      val notificaciones = List.newBuilder[Notificacion]

      while (resultado.next()) {
        notificaciones += mapear(resultado)
      }

      notificaciones.result()
    }.get
  }

  def marcarLeidas(idUsuario: Long): Int = {
    val sql = "UPDATE notificaciones SET leida = TRUE WHERE id_usuario = ? AND leida = FALSE"

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setLong(1, idUsuario)
      sentencia.executeUpdate()
    }.get
  }

  private def mapear(resultado: ResultSet): Notificacion =
    Notificacion(
      idNotificacion = resultado.getLong("id_notificacion"),
      idUsuario = resultado.getLong("id_usuario"),
      titulo = resultado.getString("titulo"),
      mensaje = resultado.getString("mensaje"),
      fechaCreacion = resultado.getTimestamp("fecha_creacion").toLocalDateTime,
      tipo = resultado.getString("tipo"),
      leida = resultado.getBoolean("leida")
    )
}
