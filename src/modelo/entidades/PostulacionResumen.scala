package modelo.entidades

import java.time.LocalDate

final case class PostulacionResumen(
    idPostulacion: Long,
    estudiante: String,
    cedula: String,
    empresa: String,
    oferta: String,
    area: String,
    fechaPostulacion: LocalDate,
    estado: String,
    rutaDocumentoMalla: Option[String]
)
