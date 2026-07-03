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
  vista.btnNotificaciones.addActionListener(_ => DialogoNotificaciones.mostrar(vista, usuario))
  vista.btnVerNotificaciones.addActionListener(_ => DialogoNotificaciones.mostrar(vista, usuario))
  vista.mniNotificaciones.addActionListener(_ => DialogoNotificaciones.mostrar(vista, usuario))
  vista.btnFormularios.addActionListener(_ => abrirFormularios())
  vista.mniFormularios.addActionListener(_ => abrirFormularios())
  vista.btnEnProgreso.addActionListener(_ => abrirPractica())
  vista.mniEnProgreso.addActionListener(_ => abrirPractica())

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

  private def abrirPractica(): Unit = {
    vista.dispose()
    new ControlEstudiantePractica(usuario, () => new ControlVentanaEstudiante(usuario, alCerrarSesion).mostrar(), alCerrarSesion).mostrar()
  }

  private def abrirFormularios(): Unit = {
    vista.dispose()
    new ControlEstudianteFormularios(usuario, () => new ControlVentanaEstudiante(usuario, alCerrarSesion).mostrar(), alCerrarSesion).mostrar()
  }

  private def cerrarSesion(): Unit = {
    vista.dispose()
    alCerrarSesion()
  }
}
