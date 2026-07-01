package modelo.entidades

import java.time.LocalDate

final case class PostulacionDetalle(
    idPostulacion: Long,
    idEstudiante: Long,
    estudiante: String,
    idOferta: Long,
    oferta: String,
    area: String,
    idEmpresa: Long,
    empresa: String,
    empresaTieneConvenio: Boolean,
    cuposOferta: Int,
    fechaPostulacion: LocalDate,
    estado: String,
    rutaDocumentoMalla: Option[String],
    idPractica: Option[Long] = None,
    fechaInicioPractica: Option[LocalDate] = None,
    fechaFinPractica: Option[LocalDate] = None,
    tutorAcademico: Option[String] = None,
    tutorEmpresarial: Option[String] = None
)
