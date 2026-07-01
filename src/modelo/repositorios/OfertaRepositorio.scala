package modelo.repositorios

import modelo.bd.Conexion
import modelo.entidades.Oferta

import java.sql.{Date, ResultSet}
import java.time.LocalDate
import scala.util.Using

class OfertaRepositorio {

  def buscarPorId(idOferta: Long): Option[Oferta] = {
    val sql =
      """
        |SELECT o.id_oferta, o.id_empresa, e.nombre AS empresa, o.titulo, o.descripcion,
        |       o.requisitos, o.area, o.cupos, o.fecha_publicacion, o.fecha_cierre, o.estado
        |FROM ofertas o
        |JOIN empresas e ON e.id_empresa = o.id_empresa
        |WHERE o.id_oferta = ?
        |""".stripMargin

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setLong(1, idOferta)
      val resultado = use(sentencia.executeQuery())

      if (resultado.next()) Some(mapear(resultado)) else None
    }.get
  }

  def listar(filtro: String = ""): List[Oferta] = {
    val sql =
      """
        |SELECT o.id_oferta, o.id_empresa, e.nombre AS empresa, o.titulo, o.descripcion,
        |       o.requisitos, o.area, o.cupos, o.fecha_publicacion, o.fecha_cierre, o.estado
        |FROM ofertas o
        |JOIN empresas e ON e.id_empresa = o.id_empresa
        |WHERE
        |  ? = ''
        |  OR LOWER(e.nombre) LIKE ?
        |  OR LOWER(o.titulo) LIKE ?
        |  OR LOWER(o.descripcion) LIKE ?
        |  OR LOWER(o.area) LIKE ?
        |  OR LOWER(o.estado) LIKE ?
        |ORDER BY o.fecha_publicacion DESC, o.id_oferta DESC
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
      val ofertas = List.newBuilder[Oferta]

      while (resultado.next()) {
        ofertas += mapear(resultado)
      }

      ofertas.result()
    }.get
  }

  def listarAbiertas(filtro: String = ""): List[Oferta] = {
    val sql =
      """
        |SELECT o.id_oferta, o.id_empresa, e.nombre AS empresa, o.titulo, o.descripcion,
        |       o.requisitos, o.area, o.cupos, o.fecha_publicacion, o.fecha_cierre, o.estado
        |FROM ofertas o
        |JOIN empresas e ON e.id_empresa = o.id_empresa
        |WHERE LOWER(o.estado) = 'abierta'
        |  AND o.cupos > 0
        |  AND o.fecha_cierre >= CURRENT_DATE
        |  AND (
        |    ? = ''
        |    OR LOWER(e.nombre) LIKE ?
        |    OR LOWER(o.titulo) LIKE ?
        |    OR LOWER(o.descripcion) LIKE ?
        |    OR LOWER(o.area) LIKE ?
        |  )
        |ORDER BY o.fecha_publicacion DESC, o.id_oferta DESC
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
      val resultado = use(sentencia.executeQuery())
      val ofertas = List.newBuilder[Oferta]

      while (resultado.next()) {
        ofertas += mapear(resultado)
      }

      ofertas.result()
    }.get
  }

  def crear(oferta: Oferta): Long = {
    val sql =
      """
        |INSERT INTO ofertas (id_empresa, titulo, descripcion, requisitos, area, cupos, fecha_publicacion, fecha_cierre, estado)
        |VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        |RETURNING id_oferta
        |""".stripMargin

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      asignarCampos(sentencia, oferta)
      val resultado = use(sentencia.executeQuery())

      if (resultado.next()) resultado.getLong(1) else 0L
    }.get
  }

  def actualizar(oferta: Oferta): Int = {
    val sql =
      """
        |UPDATE ofertas
        |SET id_empresa = ?, titulo = ?, descripcion = ?, requisitos = ?, area = ?,
        |    cupos = ?, fecha_publicacion = ?, fecha_cierre = ?, estado = ?
        |WHERE id_oferta = ?
        |""".stripMargin

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      asignarCampos(sentencia, oferta)
      sentencia.setLong(10, oferta.idOferta)
      sentencia.executeUpdate()
    }.get
  }

  def eliminar(idOferta: Long): Int = {
    val sql = "DELETE FROM ofertas WHERE id_oferta = ?"

    Using.Manager { use =>
      val conexion = use(Conexion.obtener())
      val sentencia = use(conexion.prepareStatement(sql))
      sentencia.setLong(1, idOferta)
      sentencia.executeUpdate()
    }.get
  }

  private def asignarCampos(sentencia: java.sql.PreparedStatement, oferta: Oferta): Unit = {
    sentencia.setLong(1, oferta.idEmpresa)
    sentencia.setString(2, oferta.titulo)
    sentencia.setString(3, oferta.descripcion)
    sentencia.setString(4, oferta.requisitos)
    sentencia.setString(5, oferta.area)
    sentencia.setInt(6, oferta.cupos)
    sentencia.setDate(7, Date.valueOf(oferta.fechaPublicacion))
    sentencia.setDate(8, Date.valueOf(oferta.fechaCierre))
    sentencia.setString(9, oferta.estado)
  }

  private def mapear(resultado: ResultSet): Oferta =
    Oferta(
      idOferta = resultado.getLong("id_oferta"),
      idEmpresa = resultado.getLong("id_empresa"),
      empresa = Option(resultado.getString("empresa")),
      titulo = resultado.getString("titulo"),
      descripcion = resultado.getString("descripcion"),
      requisitos = resultado.getString("requisitos"),
      area = resultado.getString("area"),
      cupos = resultado.getInt("cupos"),
      fechaPublicacion = resultado.getDate("fecha_publicacion").toLocalDate,
      fechaCierre = resultado.getDate("fecha_cierre").toLocalDate,
      estado = resultado.getString("estado")
    )
}
