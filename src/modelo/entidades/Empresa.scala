package modelo.entidades

final case class Empresa(
    idEmpresa: Long,
    nombre: String,
    correo: String,
    ruc: String,
    sector: String,
    ubicacion: String,
    tieneConvenio: Boolean,
    activa: Boolean = true
)
