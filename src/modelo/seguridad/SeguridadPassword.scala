package modelo.seguridad

object SeguridadPassword {
  def verificar(passwordIngresada: String, passwordGuardada: String): Boolean =
    passwordIngresada == passwordGuardada
}
