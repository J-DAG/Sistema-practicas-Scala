package controlador

import modelo.entidades.{Oferta, Usuario}
import modelo.repositorios.OfertaRepositorio
import vista.VistaMainEstudiante

import javax.swing.JOptionPane
import javax.swing.table.DefaultTableModel

class ControlVentanaEstudiante(usuario: Usuario, alCerrarSesion: () => Unit) {
  private val vista = new VistaMainEstudiante()
  private val ofertas = new OfertaRepositorio()

  vista.lblInformacion.setText(s"Bienvenido ${usuario.nombreCompleto}")

  vista.btnOfertas.addActionListener(_ => abrirOfertas())
  vista.mniOfertas.addActionListener(_ => abrirOfertas())
  vista.btnPostulaciones.addActionListener(_ => abrirPostulaciones())
  vista.mniPostulaciones.addActionListener(_ => abrirPostulaciones())
  vista.btnCerrarSesion.addActionListener(_ => cerrarSesion())
  vista.btnCerrarSesion_2.addActionListener(_ => cerrarSesion())
  vista.mniCerrarSesion.addActionListener(_ => cerrarSesion())
  vista.btnNotificaciones.addActionListener(_ => moduloPendiente("Notificaciones"))
  vista.btnVerNotificaciones.addActionListener(_ => moduloPendiente("Notificaciones"))
  vista.mniNotificaciones.addActionListener(_ => moduloPendiente("Notificaciones"))
  vista.btnFormularios.addActionListener(_ => moduloPendiente("Formularios"))
  vista.mniFormularios.addActionListener(_ => moduloPendiente("Formularios"))
  vista.btnEnProgreso.addActionListener(_ => moduloPendiente("Practica"))
  vista.mniEnProgreso.addActionListener(_ => moduloPendiente("Practica"))

  def mostrar(): Unit = {
    cargarOfertasRecientes()
    vista.setLocationRelativeTo(null)
    vista.setVisible(true)
  }

  private def cargarOfertasRecientes(): Unit = {
    try {
      llenarTabla(ofertas.listarAbiertas().take(20))
    } catch {
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudo cargar ofertas.\n\nDetalle: ${error.getMessage}")
    }
  }

  private def llenarTabla(datos: List[Oferta]): Unit = {
    val modelo = new DefaultTableModel(Array[AnyRef]("ID", "Empresa", "Titulo", "Area", "Cupos", "Cierre"), 0) {
      override def isCellEditable(row: Int, column: Int): Boolean = false
    }

    datos.foreach { oferta =>
      modelo.addRow(Array[AnyRef](
        Long.box(oferta.idOferta),
        oferta.empresa.getOrElse(""),
        oferta.titulo,
        oferta.area,
        Int.box(oferta.cupos),
        oferta.fechaCierre.toString
      ))
    }

    vista.tblListaOfertasLaborales.setModel(modelo)
  }

  private def abrirOfertas(): Unit = {
    vista.dispose()
    new ControlEstudianteOfertas(usuario, () => new ControlVentanaEstudiante(usuario, alCerrarSesion).mostrar(), alCerrarSesion).mostrar()
  }

  private def abrirPostulaciones(): Unit = {
    vista.dispose()
    new ControlEstudiantePostulaciones(usuario, () => new ControlVentanaEstudiante(usuario, alCerrarSesion).mostrar(), alCerrarSesion).mostrar()
  }

  private def moduloPendiente(nombre: String): Unit =
    JOptionPane.showMessageDialog(vista, s"El modulo de $nombre se conectara en el siguiente paso.")

  private def cerrarSesion(): Unit = {
    vista.dispose()
    alCerrarSesion()
  }
}
