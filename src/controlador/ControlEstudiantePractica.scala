package controlador

import modelo.entidades.{Actividad, PracticaEstudianteDetalle, Usuario}
import modelo.repositorios.{ActividadRepositorio, PracticaRepositorio}
import vista.VistaEstPracticas

import javax.swing.{JOptionPane, JRadioButton}
import javax.swing.table.DefaultTableModel

class ControlEstudiantePractica(usuario: Usuario, alInicio: () => Unit, alCerrarSesion: () => Unit) {
  private val vista = new VistaEstPracticas()
  private val practicas = new PracticaRepositorio()
  private val actividades = new ActividadRepositorio()
  private var practicaActual: Option[PracticaEstudianteDetalle] = None
  private var filtroSeleccionado: Option[JRadioButton] = None

  vista.btnInico.addActionListener(_ => volverInicio())
  vista.btnMiPractica.addActionListener(_ => cargarDatos())
  vista.btnMisPostulaciones.addActionListener(_ => abrirPostulaciones())
  vista.btnOfertaLaboral.addActionListener(_ => abrirOfertas())
  vista.btnMisFormularios.addActionListener(_ => abrirFormularios())
  vista.btnCerrarSesion.addActionListener(_ => cerrarSesion())
  vista.btnBuscar.addActionListener(_ => cargarActividades())
  vista.txtBuscar.addActionListener(_ => cargarActividades())
  configurarFiltro(vista.rbtAprobadas)
  configurarFiltro(vista.rbtAprobadas_2)
  configurarFiltro(vista.rbtAprobadas_3)

  def mostrar(): Unit = {
    cargarDatos()
    vista.setLocationRelativeTo(null)
    vista.setVisible(true)
  }

  private def cargarDatos(): Unit = {
    try {
      practicaActual = practicas.buscarActualPorEstudiante(usuario.idUsuario)
      practicaActual match {
        case Some(practica) =>
          llenarResumen(practica)
          cargarActividades()
        case None =>
          limpiarResumen()
          llenarTabla(Nil)
          JOptionPane.showMessageDialog(vista, "Aun no tiene una practica activa, finalizada o completada.")
      }
    } catch {
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudo cargar la practica.\n\nDetalle: ${error.getMessage}")
    }
  }

  private def llenarResumen(practica: PracticaEstudianteDetalle): Unit = {
    vista.lblSubTitulo.setText(s"Practica ${practica.idPractica} - ${practica.estado}")
    vista.lblEmpresaEditar.setText(s"${practica.empresa} / ${practica.oferta} / ${practica.area}")
    vista.lblTAEditar.setText(practica.tutorAcademico)
    vista.lblTEEditar.setText(practica.tutorEmpresarial)
    vista.lblNumHorasEditar.setText(s"${practica.horasCumplidas}/240 (${practica.porcentaje}%)")
    vista.lblActCompletadasEditar.setText(contarCompletadas(practica.idPractica).toString)
  }

  private def limpiarResumen(): Unit = {
    vista.lblSubTitulo.setText("Practica - Lista de actividades")
    vista.lblEmpresaEditar.setText("Sin practica")
    vista.lblTAEditar.setText("")
    vista.lblTEEditar.setText("")
    vista.lblNumHorasEditar.setText("0/240")
    vista.lblActCompletadasEditar.setText("0")
  }

  private def cargarActividades(): Unit = {
    practicaActual match {
      case Some(practica) =>
        try {
          val texto = vista.txtBuscar.getText.trim
          val datos = actividades.listarPorPractica(practica.idPractica, texto)
          llenarTabla(aplicarFiltro(datos))
          vista.lblActCompletadasEditar.setText(datos.count(_.estado.equalsIgnoreCase("completada")).toString)
        } catch {
          case error: Exception =>
            JOptionPane.showMessageDialog(vista, s"No se pudo cargar actividades.\n\nDetalle: ${error.getMessage}")
        }
      case None =>
        llenarTabla(Nil)
    }
  }

  private def aplicarFiltro(datos: List[Actividad]): List[Actividad] =
    filtroSeleccionado match {
      case Some(boton) if boton == vista.rbtAprobadas =>
        datos.filter(_.estado.equalsIgnoreCase("aprobada"))
      case Some(boton) if boton == vista.rbtAprobadas_2 =>
        datos.filter(_.estado.equalsIgnoreCase("pendiente aprobacion"))
      case Some(boton) if boton == vista.rbtAprobadas_3 =>
        datos.filter(_.estado.equalsIgnoreCase("completada"))
      case _ =>
        datos
    }

  private def llenarTabla(datos: List[Actividad]): Unit = {
    val modelo = new DefaultTableModel(Array[AnyRef]("ID", "Descripcion", "Horas", "Fecha", "Estado"), 0) {
      override def isCellEditable(row: Int, column: Int): Boolean = false
    }

    datos.foreach { actividad =>
      modelo.addRow(Array[AnyRef](
        Long.box(actividad.idActividad),
        actividad.descripcion,
        Int.box(actividad.horas),
        actividad.fecha.toString,
        actividad.estado
      ))
    }

    vista.tblListaActividades.setModel(modelo)
  }

  private def contarCompletadas(idPractica: Long): Int =
    actividades.listarPorPractica(idPractica).count(_.estado.equalsIgnoreCase("completada"))

  private def configurarFiltro(boton: JRadioButton): Unit = {
    boton.addActionListener(_ => {
      if (filtroSeleccionado.contains(boton)) {
        boton.setSelected(false)
        filtroSeleccionado = None
      } else {
        List(vista.rbtAprobadas, vista.rbtAprobadas_2, vista.rbtAprobadas_3).foreach { otro =>
          if (otro != boton) otro.setSelected(false)
        }
        boton.setSelected(true)
        filtroSeleccionado = Some(boton)
      }
      cargarActividades()
    })
  }

  private def abrirOfertas(): Unit = {
    vista.dispose()
    new ControlEstudianteOfertas(usuario, alInicio, alCerrarSesion).mostrar()
  }

  private def abrirPostulaciones(): Unit = {
    vista.dispose()
    new ControlEstudiantePostulaciones(usuario, alInicio, alCerrarSesion).mostrar()
  }

  private def abrirFormularios(): Unit = {
    vista.dispose()
    new ControlEstudianteFormularios(usuario, alInicio, alCerrarSesion).mostrar()
  }

  private def volverInicio(): Unit = {
    vista.dispose()
    alInicio()
  }

  private def cerrarSesion(): Unit = {
    vista.dispose()
    alCerrarSesion()
  }
}
