package controlador

import modelo.entidades.{Actividad, PracticaResumen, Usuario}
import modelo.repositorios.ActividadRepositorio
import vista.VistaTAActividades

import javax.swing.JOptionPane
import javax.swing.table.DefaultTableModel

class ControlTAActividades(usuario: Usuario, practica: PracticaResumen, alRegresar: () => Unit, alInicio: () => Unit) {
  private val vista = new VistaTAActividades()
  private val actividades = new ActividadRepositorio()

  vista.lblNombresEstudianteEditar.setText(practica.estudiante)
  vista.btnInicio.addActionListener(_ => volverInicio())
  vista.btnRegresar.addActionListener(_ => regresar())
  vista.btnBuscar.addActionListener(_ => cargarDatos())
  vista.txtBuscar.addActionListener(_ => cargarDatos())
  vista.btnAprobar.addActionListener(_ => aprobarActividad())
  vista.btnNegar.addActionListener(_ => negarActividad())

  def mostrar(): Unit = {
    cargarDatos()
    vista.setLocationRelativeTo(null)
    vista.setVisible(true)
  }

  private def cargarDatos(): Unit = {
    try {
      llenarTabla(actividades.listarPorPractica(practica.idPractica, vista.txtBuscar.getText))
    } catch {
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudo cargar actividades.\n\nDetalle: ${error.getMessage}")
    }
  }

  private def llenarTabla(datos: List[Actividad]): Unit = {
    val modelo = new DefaultTableModel(Array[AnyRef]("ID", "Descripcion", "Horas", "Fecha", "Aprobada TA", "Completada TE", "Estado"), 0) {
      override def isCellEditable(row: Int, column: Int): Boolean = false
    }
    datos.foreach { actividad =>
      modelo.addRow(Array[AnyRef](
        Long.box(actividad.idActividad),
        actividad.descripcion,
        Int.box(actividad.horas),
        actividad.fecha.toString,
        if (actividad.aprobadaPorTutorAcademico) "Si" else "No",
        if (actividad.completadaPorTutorEmpresarial) "Si" else "No",
        actividad.estado
      ))
    }
    vista.tblActividades.setModel(modelo)
  }

  private def aprobarActividad(): Unit = {
    actividadSeleccionada() match {
      case Some(actividad) =>
        val respuesta = JOptionPane.showConfirmDialog(vista, "Desea aprobar esta actividad?", "Confirmar aprobacion", JOptionPane.YES_NO_OPTION)
        if (respuesta == JOptionPane.YES_OPTION) {
          try {
            actividades.aprobarPorTutorAcademico(actividad.idActividad, usuario.idUsuario)
            cargarDatos()
          } catch {
            case error: Exception =>
              JOptionPane.showMessageDialog(vista, s"No se pudo aprobar la actividad.\n\nDetalle: ${error.getMessage}")
          }
        }
      case None =>
    }
  }

  private def negarActividad(): Unit = {
    actividadSeleccionada() match {
      case Some(actividad) =>
        val mensaje =
          if (actividad.aprobadaPorTutorAcademico) "Desea marcar esta actividad como negada y quitar su aprobacion?"
          else "Desea marcar esta actividad como negada para que el tutor empresarial la corrija?"
        val respuesta = JOptionPane.showConfirmDialog(vista, mensaje, "Confirmar negacion", JOptionPane.YES_NO_OPTION)
        if (respuesta == JOptionPane.YES_OPTION) {
          try {
            actividades.negarPorTutorAcademico(actividad.idActividad)
            cargarDatos()
          } catch {
            case error: Exception =>
              JOptionPane.showMessageDialog(vista, s"No se pudo negar la actividad.\n\nDetalle: ${error.getMessage}")
          }
        }
      case None =>
    }
  }

  private def actividadSeleccionada(): Option[Actividad] = {
    val fila = vista.tblActividades.getSelectedRow
    if (fila < 0) {
      JOptionPane.showMessageDialog(vista, "Seleccione una actividad.")
      None
    } else {
      val id = vista.tblActividades.getValueAt(fila, 0).toString.toLong
      actividades.listarPorPractica(practica.idPractica).find(_.idActividad == id)
    }
  }

  private def regresar(): Unit = {
    vista.dispose()
    alRegresar()
  }

  private def volverInicio(): Unit = {
    vista.dispose()
    alInicio()
  }
}
