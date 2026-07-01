package modelo.repositorios

import modelo.bd.Conexion
import modelo.entidades.Empresa

import java.sql.ResultSet
import scala.util.Using

class EmpresaRepositorio {

  def buscarPorId(idEmpresa: Long): Option[Empresa] = {
    val sql =
      """
        |SELECT id_empresa, nombre, correo, ruc, sector, ubicacion, tiene_convenio, activa
        |FROM empresas
        |WHERE id_empresa = ?
        |""".stripMargin

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setLong(1, idEmpresa)
      val resultado = use(sentencia.executeQuery())

      if (resultado.next()) Some(mapearEmpresa(resultado)) else None
    }.get
  }

  def crear(empresa: Empresa): Long = {
    val sql =
      """
        |INSERT INTO empresas (nombre, correo, ruc, sector, ubicacion, tiene_convenio, activa)
        |VALUES (?, ?, ?, ?, ?, ?, ?)
        |RETURNING id_empresa
        |""".stripMargin

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setString(1, empresa.nombre)
      sentencia.setString(2, empresa.correo)
      sentencia.setString(3, empresa.ruc)
      sentencia.setString(4, empresa.sector)
      sentencia.setString(5, empresa.ubicacion)
      sentencia.setBoolean(6, empresa.tieneConvenio)
      sentencia.setBoolean(7, empresa.activa)
      val resultado = use(sentencia.executeQuery())

      if (resultado.next()) resultado.getLong(1) else 0L
    }.get
  }

  def actualizar(empresa: Empresa): Int = {
    val sql =
      """
        |UPDATE empresas
        |SET nombre = ?, correo = ?, ruc = ?, sector = ?, ubicacion = ?, tiene_convenio = ?, activa = ?
        |WHERE id_empresa = ?
        |""".stripMargin

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setString(1, empresa.nombre)
      sentencia.setString(2, empresa.correo)
      sentencia.setString(3, empresa.ruc)
      sentencia.setString(4, empresa.sector)
      sentencia.setString(5, empresa.ubicacion)
      sentencia.setBoolean(6, empresa.tieneConvenio)
      sentencia.setBoolean(7, empresa.activa)
      sentencia.setLong(8, empresa.idEmpresa)
      sentencia.executeUpdate()
    }.get
  }

  def listar(filtro: String = ""): List[Empresa] = {
    val sql =
      """
        |SELECT id_empresa, nombre, correo, ruc, sector, ubicacion, tiene_convenio, activa
        |FROM empresas
        |WHERE
        |  ? = ''
        |  OR LOWER(nombre) LIKE ?
        |  OR LOWER(correo) LIKE ?
        |  OR LOWER(ruc) LIKE ?
        |  OR LOWER(sector) LIKE ?
        |  OR LOWER(ubicacion) LIKE ?
        |ORDER BY id_empresa DESC
        |""".stripMargin
    val busqueda = filtro.trim.toLowerCase
    val patron = s"%$busqueda%"

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setString(1, busqueda)
      sentencia.setString(2, patron)
      sentencia.setString(3, patron)
      sentencia.setString(4, patron)
      sentencia.setString(5, patron)
      sentencia.setString(6, patron)
      val resultado = use(sentencia.executeQuery())
      val empresas = List.newBuilder[Empresa]

      while (resultado.next()) {
        empresas += mapearEmpresa(resultado)
      }

      empresas.result()
    }.get
  }

  def contarActivas(): Int = {
    val sql = "SELECT COUNT(*) FROM empresas WHERE activa = TRUE"

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      val resultado = use(sentencia.executeQuery())

      if (resultado.next()) resultado.getInt(1) else 0
    }.get
  }

  def eliminar(idEmpresa: Long): Int = {
    val sql = "DELETE FROM empresas WHERE id_empresa = ?"

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setLong(1, idEmpresa)
      sentencia.executeUpdate()
    }.get
  }

  private def mapearEmpresa(resultado: ResultSet): Empresa =
    Empresa(
      idEmpresa = resultado.getLong("id_empresa"),
      nombre = resultado.getString("nombre"),
      correo = resultado.getString("correo"),
      ruc = resultado.getString("ruc"),
      sector = resultado.getString("sector"),
      ubicacion = resultado.getString("ubicacion"),
      tieneConvenio = resultado.getBoolean("tiene_convenio"),
      activa = resultado.getBoolean("activa")
    )
}
