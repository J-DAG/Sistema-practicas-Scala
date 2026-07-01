package modelo.repositorios

import modelo.bd.Conexion

import scala.util.Using

class PracticaRepositorio {

  def contarPorEstado(estado: String): Int = {
    val sql = "SELECT COUNT(*) FROM practicas WHERE LOWER(estado) = LOWER(?)"

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setString(1, estado)
      val resultado = use(sentencia.executeQuery())

      if (resultado.next()) resultado.getInt(1) else 0
    }.get
  }
}
