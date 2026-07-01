package modelo.validaciones

object CedulaEcuador {

  def esValida(cedula: String): Boolean = {
    val limpia = cedula.trim

    limpia.length == 10 &&
    limpia.forall(_.isDigit) &&
    provinciaValida(limpia) &&
    digitoVerificadorValido(limpia)
  }

  private def provinciaValida(cedula: String): Boolean = {
    val provincia = cedula.take(2).toInt
    provincia >= 1 && provincia <= 24
  }

  private def digitoVerificadorValido(cedula: String): Boolean = {
    val digitos = cedula.map(_.asDigit)
    val suma = digitos.take(9).zipWithIndex.map {
      case (digito, indice) if indice % 2 == 0 =>
        val doble = digito * 2
        if (doble > 9) doble - 9 else doble
      case (digito, _) =>
        digito
    }.sum

    val verificador = (10 - (suma % 10)) % 10
    verificador == digitos.last
  }
}
