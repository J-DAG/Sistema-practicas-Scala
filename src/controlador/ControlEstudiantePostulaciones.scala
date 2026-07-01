package controlador

import modelo.entidades.{PostulacionResumen, Usuario}
import modelo.repositorios.PostulacionRepositorio
import vista.VistaEstPostulaciones

import javax.swing.JOptionPane
import javax.swing.table.DefaultTableModel

class ControlEstudiantePostulaciones(usuario: Usuario, alInicio: () => Unit, alCerrarSesion: () => Unit) {
  private val vista = new VistaEstPostulaciones()
  private val postulaciones = new PostulacionRepositorio()

  vista.btnInico.addActionListener(_ => volverInicio())
  vista.btnBuscar.addActionListener(_ => cargarDatos())
  vista.txtBuscar.addActionListener(_ => cargarDatos())
  vista.btnMisPostulaciones.addActionListener(_ => cargarDatos())
  vista.btnCancelarPostulacion.addActionListener(_ => cancelarPostulacion())
  vista.btnOfertaLaboral.addActionListener(_ => abrirOfertas())
  vista.btnCerrarSesion.addActionListener(_ => cerrarSesion())
  vista.btnMiPractica.addActionListener(_ => moduloPendiente("Mi practica"))
  vista.btnMisFormularios.addActionListener(_ => moduloPendiente("Formularios"))

  def mostrar(): Unit = {
    cargarDatos()
    vista.setLocationRelativeTo(null)
    vista.setVisible(true)
  }

  private def cargarDatos(): Unit = {
    try {
      llenarTabla(postulaciones.listarResumenEstudiante(usuario.idUsuario, vista.txtBuscar.getText))
    } catch {
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudo cargar postulaciones.\n\nDetalle: ${error.getMessage}")
    }
  }

  private def llenarTabla(datos: List[PostulacionResumen]): Unit = {
    val modelo = new DefaultTableModel(Array[AnyRef]("ID", "Empresa", "Oferta", "Area", "Fecha", "Estado", "Documento"), 0) {
      override def isCellEditable(row: Int, column: Int): Boolean = false
    }

    datos.foreach { postulacion =>
      modelo.addRow(Array[AnyRef](
        Long.box(postulacion.idPostulacion),
        postulacion.empresa,
        postulacion.oferta,
        postulacion.area,
        postulacion.fechaPostulacion.toString,
        postulacion.estado,
        postulacion.rutaDocumentoMalla.getOrElse("")
      ))
    }

    vista.tblPostulaciones.setModel(modelo)
  }

  private def abrirOfertas(): Unit = {
    vista.dispose()
    new ControlEstudianteOfertas(usuario, alInicio, alCerrarSesion).mostrar()
  }

  private def cancelarPostulacion(): Unit = {
    val fila = vista.tblPostulaciones.getSelectedRow
    if (fila < 0) {
      JOptionPane.showMessageDialog(vista, "Seleccione una postulacion para cancelar.")
      return
    }

    val estado = vista.tblPostulaciones.getValueAt(fila, 5).toString
    if (!estado.equalsIgnoreCase("pendiente")) {
      JOptionPane.showMessageDialog(vista, "Solo se pueden cancelar postulaciones pendientes.")
      return
    }

    val idPostulacion = vista.tblPostulaciones.getValueAt(fila, 0).toString.toLong
    val respuesta = JOptionPane.showConfirmDialog(
      vista,
      "Desea cancelar esta postulacion?",
      "Confirmar cancelacion",
      JOptionPane.YES_NO_OPTION
    )

    if (respuesta == JOptionPane.YES_OPTION) {
      val actualizadas = postulaciones.cancelarPendiente(idPostulacion, usuario.idUsuario)
      if (actualizadas > 0) {
        cargarDatos()
      } else {
        JOptionPane.showMessageDialog(vista, "No se pudo cancelar la postulacion. Verifique que siga pendiente.")
      }
    }
  }

  private def moduloPendiente(nombre: String): Unit =
    JOptionPane.showMessageDialog(vista, s"El modulo de $nombre se conectara en el siguiente paso.")

  private def volverInicio(): Unit = {
    vista.dispose()
    alInicio()
  }

  private def cerrarSesion(): Unit = {
    vista.dispose()
    alCerrarSesion()
  }
}
