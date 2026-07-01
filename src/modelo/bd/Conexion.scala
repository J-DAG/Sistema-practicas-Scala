package modelo.bd

import java.io.InputStream
import java.sql.{Connection, DriverManager}
import java.util.Properties

object Conexion {
  private val propiedades: Properties = cargarPropiedades()

  private def cargarPropiedades(): Properties = {
    val props = new Properties()
    var recurso: InputStream = null

    try {
      recurso = getClass.getClassLoader.getResourceAsStream("application.properties")
      if (recurso == null) {
        throw new IllegalStateException("No se encontro application.properties")
      }
      props.load(recurso)
      props
    } finally {
      if (recurso != null) {
        recurso.close()
      }
    }
  }

  private def valor(nombreVariable: String, propiedad: String): String =
    sys.env.getOrElse(nombreVariable, propiedades.getProperty(propiedad))

  def obtener(): Connection = {
    val url = valor("DB_URL", "db.url")
    val usuario = valor("DB_USER", "db.user")
    val contrasena = valor("DB_PASSWORD", "db.password")

    DriverManager.getConnection(url, usuario, contrasena)
  }

  def probar(): Unit = {
    val conexion = obtener()
    try {
      if (!conexion.isValid(3)) {
        throw new IllegalStateException("La conexion a la base de datos no es valida.")
      }
    } finally {
      conexion.close()
    }
  }
}
