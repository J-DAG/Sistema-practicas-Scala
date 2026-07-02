package controlador

import modelo.entidades.{PracticaResumen, Usuario}
import modelo.repositorios.PracticaRepositorio
import vista.VistaTEPracticasCompletadas

import javax.swing.JOptionPane
import javax.swing.table.DefaultTableModel

class ControlTEPracticasCompletadas(usuario: Usuario, alInicio: () => Unit, alCerrarSesion: () => Unit) {
  private val vista = new VistaTEPracticasCompletadas()
  private val practicas = new PracticaRepositorio()

  vista.btnInico.addActionListener(_ => volverInicio())
  vista.btnPracticasProgreso.addActionListener(_ => abrirProgreso())
  vista.btnPracticasCompletadas.addActionListener(_ => cargarDatos())
  vista.btnNotificaciones.addActionListener(_ => verNotificaciones())
  vista.btnCerrarSesion.addActionListener(_ => cerrarSesion())
  vista.btnBuscar.addActionListener(_ => cargarDatos())
  vista.txtBuscar.addActionListener(_ => cargarDatos())

  def mostrar(): Unit = {
    cargarDatos()
    vista.setLocationRelativeTo(null)
    vista.setVisible(true)
  }

  private def cargarDatos(): Unit = {
    try {
      llenarTabla(practicas.listarPorTutorEmpresarial(usuario.idUsuario, Set("finalizada", "completada"), vista.txtBuscar.getText))
    } catch {
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudo cargar practicas completadas.\n\nDetalle: ${error.getMessage}")
    }
  }

  private def llenarTabla(datos: List[PracticaResumen]): Unit = {
    val modelo = new DefaultTableModel(Array[AnyRef]("ID", "Estudiante", "Cedula", "Empresa", "Oferta", "Area", "Inicio", "Fin", "Estado", "Horas"), 0) {
      override def isCellEditable(row: Int, column: Int): Boolean = false
    }

    datos.foreach { practica =>
      modelo.addRow(Array[AnyRef](
        Long.box(practica.idPractica),
        practica.estudiante,
        practica.cedula,
        practica.empresa,
        practica.oferta,
        practica.area,
        practica.fechaInicio.toString,
        practica.fechaFin.toString,
        practica.estado,
        s"${practica.horasCumplidas}/240"
      ))
    }

    vista.tblPracticasCompletadas.setModel(modelo)
  }

  private def abrirProgreso(): Unit = {
    vista.dispose()
    new ControlTEPracticasProgreso(usuario, alInicio, alCerrarSesion).mostrar()
  }

  private def verNotificaciones(): Unit =
    JOptionPane.showMessageDialog(vista, "El panel de notificaciones del tutor empresarial se conectara en el siguiente paso.")

  private def volverInicio(): Unit = {
    vista.dispose()
    alInicio()
  }

  private def cerrarSesion(): Unit = {
    vista.dispose()
    alCerrarSesion()
  }
}
