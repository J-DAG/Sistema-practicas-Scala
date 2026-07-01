package modelo.entidades

import java.time.LocalDate

final case class Oferta(
    idOferta: Long,
    idEmpresa: Long,
    empresa: Option[String],
    titulo: String,
    descripcion: String,
    requisitos: String,
    area: String,
    cupos: Int,
    fechaPublicacion: LocalDate,
    fechaCierre: LocalDate,
    estado: String
)
