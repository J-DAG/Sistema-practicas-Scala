package controlador

import modelo.entidades.{PracticaResumen, Usuario}
import modelo.repositorios.PracticaRepositorio
import vista.VistaTAPracticasCompletadas

import javax.swing.JOptionPane
import javax.swing.table.DefaultTableModel

class ControlTAPracticasCompletadas(usuario: Usuario, alInicio: () => Unit, alCerrarSesion: () => Unit) {
  private val vista = new VistaTAPracticasCompletadas()
  private val practicas = new PracticaRepositorio()

  vista.btnInico.addActionListener(_ => volverInicio())
  vista.btnPracticasProgreso.addActionListener(_ => abrirProgreso())
  vista.btnPracticasCompletadas.addActionListener(_ => cargarDatos())
  vista.btnNotificaciones.addActionListener(_ => verNotificaciones())
  vista.btnCerrarSesion.addActionListener(_ => cerrarSesion())
  vista.btnBuscar.addActionListener(_ => cargarDatos())
  vista.txtBuscar.addActionListener(_ => cargarDatos())
  vista.btnActividades.addActionListener(_ => calificarEstudiante())

  def mostrar(): Unit = {
    cargarDatos()
    vista.setLocationRelativeTo(null)
    vista.setVisible(true)
  }

  private def cargarDatos(): Unit = {
    try {
      llenarTabla(practicas.listarPorTutorAcademico(usuario.idUsuario, Set("finalizada", "completada"), vista.txtBuscar.getText))
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
    vista.tblEstudiantesTA.setModel(modelo)
  }

  private def abrirProgreso(): Unit = {
    vista.dispose()
    new ControlTAPracticasProgreso(usuario, alInicio, alCerrarSesion).mostrar()
  }

  private def calificarEstudiante(): Unit = {
    practicaSeleccionada() match {
      case Some(practica) =>
        val entrada = JOptionPane.showInputDialog(
          vista,
          s"Ingrese la calificacion final de ${practica.estudiante} sobre 100:",
          "Calificar estudiante",
          JOptionPane.QUESTION_MESSAGE
        )
        if (entrada != null) {
          try {
            val calificacion = entrada.trim.toInt
            val confirmar = JOptionPane.showConfirmDialog(
              vista,
              s"Desea registrar la calificacion $calificacion/100 para ${practica.estudiante}?",
              "Confirmar calificacion",
              JOptionPane.YES_NO_OPTION
            )
            if (confirmar == JOptionPane.YES_OPTION) {
              practicas.calificarPractica(practica.idPractica, usuario.idUsuario, calificacion)
              JOptionPane.showMessageDialog(vista, "Practica calificada. Formularios finales enviados al estudiante.")
              cargarDatos()
            }
          } catch {
            case _: NumberFormatException =>
              JOptionPane.showMessageDialog(vista, "Ingrese un numero entero entre 0 y 100.")
            case error: Exception =>
              JOptionPane.showMessageDialog(vista, s"No se pudo calificar la practica.\n\nDetalle: ${error.getMessage}")
          }
        }
      case None =>
    }
  }

  private def practicaSeleccionada(): Option[PracticaResumen] = {
    val fila = vista.tblEstudiantesTA.getSelectedRow
    if (fila < 0) {
      JOptionPane.showMessageDialog(vista, "Seleccione una practica.")
      None
    } else {
      val id = vista.tblEstudiantesTA.getValueAt(fila, 0).toString.toLong
      practicas.listarPorTutorAcademico(usuario.idUsuario, Set("finalizada", "completada"), "").find(_.idPractica == id)
    }
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
