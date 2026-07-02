package modelo.entidades

import java.time.LocalDateTime

final case class Notificacion(
    idNotificacion: Long,
    idUsuario: Long,
    titulo: String,
    mensaje: String,
    fechaCreacion: LocalDateTime,
    tipo: String,
    leida: Boolean
)
