package controlador

import modelo.entidades.{Actividad, PracticaResumen, Usuario}
import modelo.repositorios.ActividadRepositorio
import vista.{VistaEditarActividad, VistaRegistrarActividad, VistaTEActividades}

import javax.swing.JOptionPane
import javax.swing.table.DefaultTableModel

class ControlTEActividades(usuario: Usuario, practica: PracticaResumen, alRegresar: () => Unit, alInicio: () => Unit) {
  private val vista = new VistaTEActividades()
  private val actividades = new ActividadRepositorio()

  vista.lblNombresEstudianteEditar.setText(practica.estudiante)
  vista.btnInicio.addActionListener(_ => volverInicio())
  vista.btnRegresar.addActionListener(_ => regresar())
  vista.btnBuscar.addActionListener(_ => cargarDatos())
  vista.txtBuscar.addActionListener(_ => cargarDatos())
  vista.btnNuevaActividad.addActionListener(_ => nuevaActividad())
  vista.btnEditar.addActionListener(_ => editarActividad())
  vista.btnEliminar.addActionListener(_ => eliminarActividad())
  vista.btnMarcarCompletada.addActionListener(_ => marcarCompletada())
  vista.btnMarcarIncompletada.addActionListener(_ => marcarPendiente())

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

  private def nuevaActividad(): Unit = {
    val formulario = new VistaRegistrarActividad()
    formulario.sbxHoras.setValue(1)
    formulario.btnCancelar.addActionListener(_ => formulario.dispose())
    formulario.btnGuardar.addActionListener(_ => {
      val descripcion = formulario.txtDescripcion.getText.trim
      val horas = formulario.sbxHoras.getValue.toString.toInt
      if (descripcion.isEmpty) {
        JOptionPane.showMessageDialog(formulario, "Ingrese la descripcion de la actividad.")
      } else if (horas <= 0) {
        JOptionPane.showMessageDialog(formulario, "Las horas deben ser mayores a cero.")
      } else {
        try {
          actividades.crear(practica.idPractica, descripcion, horas)
          formulario.dispose()
          cargarDatos()
        } catch {
          case error: Exception =>
            JOptionPane.showMessageDialog(formulario, s"No se pudo registrar la actividad.\n\nDetalle: ${error.getMessage}")
        }
      }
    })
    formulario.setLocationRelativeTo(vista)
    formulario.setVisible(true)
  }

  private def editarActividad(): Unit = {
    actividadSeleccionada() match {
      case Some(actividad) =>
        val formulario = new VistaEditarActividad()
        formulario.txtDescripcion.setText(actividad.descripcion)
        formulario.sbxHoras.setValue(actividad.horas)
        formulario.btnCancelar.addActionListener(_ => formulario.dispose())
        formulario.btnGuardar.addActionListener(_ => {
          val descripcion = formulario.txtDescripcion.getText.trim
          val horas = formulario.sbxHoras.getValue.toString.toInt
          if (descripcion.isEmpty) {
            JOptionPane.showMessageDialog(formulario, "Ingrese la descripcion de la actividad.")
          } else if (horas <= 0) {
            JOptionPane.showMessageDialog(formulario, "Las horas deben ser mayores a cero.")
          } else {
            try {
              actividades.actualizar(actividad.idActividad, descripcion, horas)
              formulario.dispose()
              cargarDatos()
            } catch {
              case error: Exception =>
                JOptionPane.showMessageDialog(formulario, s"No se pudo actualizar la actividad.\n\nDetalle: ${error.getMessage}")
            }
          }
        })
        formulario.setLocationRelativeTo(vista)
        formulario.setVisible(true)
      case None =>
    }
  }

  private def eliminarActividad(): Unit = {
    actividadSeleccionada() match {
      case Some(actividad) =>
        val respuesta = JOptionPane.showConfirmDialog(
          vista,
          "Desea eliminar la actividad seleccionada?",
          "Confirmar eliminacion",
          JOptionPane.YES_NO_OPTION
        )
        if (respuesta == JOptionPane.YES_OPTION) {
          try {
            actividades.eliminar(actividad.idActividad)
            cargarDatos()
          } catch {
            case error: Exception =>
              JOptionPane.showMessageDialog(vista, s"No se pudo eliminar la actividad.\n\nDetalle: ${error.getMessage}")
          }
        }
      case None =>
    }
  }

  private def marcarCompletada(): Unit = {
    actividadSeleccionada() match {
      case Some(actividad) =>
        val respuesta = JOptionPane.showConfirmDialog(
          vista,
          "Desea marcar esta actividad como completada?",
          "Confirmar actividad completada",
          JOptionPane.YES_NO_OPTION
        )
        if (respuesta == JOptionPane.YES_OPTION) {
          try {
            actividades.marcarCompletada(actividad.idActividad, usuario.idUsuario)
            cargarDatos()
          } catch {
            case error: Exception =>
              JOptionPane.showMessageDialog(vista, s"No se pudo completar la actividad.\n\nDetalle: ${error.getMessage}")
          }
        }
      case None =>
    }
  }

  private def marcarPendiente(): Unit = {
    actividadSeleccionada() match {
      case Some(actividad) =>
        val respuesta = JOptionPane.showConfirmDialog(
          vista,
          "Desea marcar esta actividad nuevamente como pendiente?",
          "Confirmar cambio",
          JOptionPane.YES_NO_OPTION
        )
        if (respuesta == JOptionPane.YES_OPTION) {
          try {
            actividades.marcarPendiente(actividad.idActividad)
            cargarDatos()
          } catch {
            case error: Exception =>
              JOptionPane.showMessageDialog(vista, s"No se pudo cambiar la actividad.\n\nDetalle: ${error.getMessage}")
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
      actividades.listarPorPractica(practica.idPractica).find(_.idActividad == id) match {
        case Some(actividad) => Some(actividad)
        case None =>
          JOptionPane.showMessageDialog(vista, "No se encontro la actividad seleccionada.")
          None
      }
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
