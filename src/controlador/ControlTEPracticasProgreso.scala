package controlador

import modelo.entidades.{PracticaResumen, Usuario}
import modelo.repositorios.PracticaRepositorio
import vista.VistaTEPracticasProgreso

import javax.swing.JOptionPane
import javax.swing.table.DefaultTableModel

class ControlTEPracticasProgreso(usuario: Usuario, alInicio: () => Unit, alCerrarSesion: () => Unit) {
  private val vista = new VistaTEPracticasProgreso()
  private val practicas = new PracticaRepositorio()

  vista.lblInformacion.setText(s"Bienvenido ${usuario.nombreCompleto}")
  vista.btnInico.addActionListener(_ => volverInicio())
  vista.btnPracticasProgreso.addActionListener(_ => cargarDatos())
  vista.btnPracticasCompletadas.addActionListener(_ => abrirCompletadas())
  vista.btnNotificaciones.addActionListener(_ => verNotificaciones())
  vista.btnCerrarSesion.addActionListener(_ => cerrarSesion())
  vista.btnBuscar.addActionListener(_ => cargarDatos())
  vista.txtBuscar.addActionListener(_ => cargarDatos())
  vista.btnActividades.addActionListener(_ => abrirActividades())

  def mostrar(): Unit = {
    cargarDatos()
    vista.setLocationRelativeTo(null)
    vista.setVisible(true)
  }

  private def cargarDatos(): Unit = {
    try {
      llenarTabla(practicas.listarPorTutorEmpresarial(usuario.idUsuario, Set("activa"), vista.txtBuscar.getText))
    } catch {
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudo cargar practicas en progreso.\n\nDetalle: ${error.getMessage}")
    }
  }

  private def llenarTabla(datos: List[PracticaResumen]): Unit = {
    val modelo = new DefaultTableModel(Array[AnyRef]("ID", "Estudiante", "Cedula", "Empresa", "Oferta", "Area", "Inicio", "Fin", "Horas", "Progreso"), 0) {
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
        s"${practica.horasCumplidas}/240",
        s"${practica.porcentaje}%"
      ))
    }

    vista.tblEstudiantesTE.setModel(modelo)
  }

  private def abrirActividades(): Unit = {
    practicaSeleccionada() match {
      case Some(practica) =>
        vista.dispose()
        new ControlTEActividades(usuario, practica, () => new ControlTEPracticasProgreso(usuario, alInicio, alCerrarSesion).mostrar(), alInicio).mostrar()
      case None =>
    }
  }

  private def practicaSeleccionada(): Option[PracticaResumen] = {
    val fila = vista.tblEstudiantesTE.getSelectedRow
    if (fila < 0) {
      JOptionPane.showMessageDialog(vista, "Seleccione una practica.")
      None
    } else {
      val id = vista.tblEstudiantesTE.getValueAt(fila, 0).toString.toLong
      practicas.listarPorTutorEmpresarial(usuario.idUsuario, Set("activa"), "").find(_.idPractica == id)
    }
  }

  private def abrirCompletadas(): Unit = {
    vista.dispose()
    new ControlTEPracticasCompletadas(usuario, alInicio, alCerrarSesion).mostrar()
  }

  private def verNotificaciones(): Unit =
    DialogoNotificaciones.mostrar(vista, usuario)

  private def volverInicio(): Unit = {
    vista.dispose()
    alInicio()
  }

  private def cerrarSesion(): Unit = {
    vista.dispose()
    alCerrarSesion()
  }
}
