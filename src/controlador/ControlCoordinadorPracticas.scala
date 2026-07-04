package controlador

import controlador.util.ModelosTabla
import modelo.entidades.{Actividad, PracticaCoordinadorResumen, Usuario}
import modelo.reportes.AnalisisReportes
import modelo.repositorios.{ActividadRepositorio, PracticaRepositorio}
import vista.{VistaCoordinadorPracticas, VistaListaActividades}

import java.awt.{Color, Font, GridLayout}
import java.awt.event.{MouseAdapter, MouseEvent}
import javax.swing.{BorderFactory, JLabel, JOptionPane, JPanel, SwingConstants}
import javax.swing.table.DefaultTableModel

class ControlCoordinadorPracticas(usuarioSesion: Usuario, alInicio: () => Unit, alCerrarSesion: () => Unit) {
  private val vista = new VistaCoordinadorPracticas()
  private val practicas = new PracticaRepositorio()
  private val actividades = new ActividadRepositorio()

  vista.lblInformacion.setText(s"Bienvenido ${usuarioSesion.nombreCompleto}")

  vista.btnInicio.addActionListener(_ => volverInicio())
  vista.btnPracticas.addActionListener(_ => cargarDatos())
  vista.btnBuscar.addActionListener(_ => cargarDatos())
  vista.txtBuscar.addActionListener(_ => cargarDatos())
  vista.btnVerDetalle.addActionListener(_ => verDetalle())
  vista.btnVerActividades.addActionListener(_ => verActividades())
  vista.btnCerrarSesion.addActionListener(_ => cerrarSesion())
  vista.tblPracticas.addMouseListener(new MouseAdapter {
    override def mouseClicked(event: MouseEvent): Unit = {
      if (event.getClickCount == 2) {
        verDetalle()
      }
    }
  })

  vista.btnEmpresa.addActionListener(_ => abrirEmpresas())
  vista.btnEstudiantes.addActionListener(_ => abrirEstudiantes())
  vista.btnTutores.addActionListener(_ => abrirTutores())
  vista.btnOfertas.addActionListener(_ => abrirOfertas())
  vista.btnPostulaciones.addActionListener(_ => abrirPostulaciones())
  vista.btnReportes.addActionListener(_ => abrirReportes())

  def mostrar(): Unit = {
    cargarDatos()
    vista.setLocationRelativeTo(null)
    vista.setVisible(true)
  }

  private def cargarDatos(): Unit = {
    try {
      val datos = practicas.listarCoordinador(vista.txtBuscar.getText)
      llenarTabla(datos)
      cargarGrafico(datos)
    } catch {
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudieron cargar las practicas.\n\nDetalle: ${error.getMessage}")
    }
  }

  private def llenarTabla(datos: List[PracticaCoordinadorResumen]): Unit =
    vista.tblPracticas.setModel(ModelosTabla.practicasCoordinador(datos))

  private def cargarGrafico(datos: List[PracticaCoordinadorResumen]): Unit = {
    val segmentos = AnalisisReportes.resumenPracticas(datos)
    val total = datos.size

    vista.widgetGraficoPastel.removeAll()
    vista.widgetGraficoPastel.setBorder(BorderFactory.createTitledBorder("Practicas por estado"))

    val panel = new JPanel(new GridLayout(0, 1, 6, 6))
    panel.setBackground(Color.WHITE)

    segmentos.foreach { segmento =>
      val etiqueta = new JLabel(segmento.texto, SwingConstants.CENTER)
      etiqueta.setOpaque(true)
      etiqueta.setBackground(colorEstado(segmento.clave))
      etiqueta.setForeground(Color.WHITE)
      etiqueta.setFont(new Font("Dialog", Font.BOLD, 14))
      panel.add(etiqueta)
    }

    val totalLabel = new JLabel(s"Total practicas: $total", SwingConstants.CENTER)
    totalLabel.setFont(new Font("Dialog", Font.BOLD, 16))
    totalLabel.setForeground(new Color(45, 45, 45))
    panel.add(totalLabel)

    vista.widgetGraficoPastel.add(panel)
    vista.widgetGraficoPastel.revalidate()
    vista.widgetGraficoPastel.repaint()
  }

  private def verActividades(): Unit = {
    practicaSeleccionada() match {
      case Some(practica) =>
        try {
          val ventana = new VistaListaActividades()
          ventana.lblIDestudianteEditar.setText(practica.idEstudiante.toString)
          ventana.lblIDpracticaEditar.setText(practica.idPractica.toString)
          ventana.lblNombresEstudianteEditar.setText(practica.estudiante)
          ventana.lblNombreTAEditar.setText(practica.tutorAcademico)
          ventana.lblNombreTEEditar.setText(practica.tutorEmpresarial)
          ventana.tblListaActividades.setModel(modeloActividades(actividades.listarPorPractica(practica.idPractica)))
          ventana.btnSalir.addActionListener(_ => ventana.dispose())
          ventana.setLocationRelativeTo(vista)
          ventana.setVisible(true)
        } catch {
          case error: Exception =>
            JOptionPane.showMessageDialog(vista, s"No se pudieron cargar las actividades.\n\nDetalle: ${error.getMessage}")
        }
      case None =>
    }
  }

  private def verDetalle(): Unit = {
    practicaSeleccionada() match {
      case Some(practica) =>
        val actividadesPractica = actividades.listarPorPractica(practica.idPractica)
        val completadas = actividadesPractica.count(_.estado.equalsIgnoreCase("completada"))
        val aprobadas = actividadesPractica.count(_.estado.equalsIgnoreCase("aprobada"))
        val pendientes = actividadesPractica.count(_.estado.equalsIgnoreCase("pendiente aprobacion"))
        val negadas = actividadesPractica.count(_.estado.equalsIgnoreCase("negada"))

        val mensaje =
          s"""Codigo de practica: ${practica.idPractica}
             |Estado: ${practica.estado}
             |Horas cumplidas: ${practica.horasCumplidas}/240 (${practica.porcentaje}%)
             |Calificacion: ${practica.calificacion.map(c => s"$c/100").getOrElse("Sin calificar")}
             |
             |Estudiante: ${practica.estudiante}
             |Cedula: ${practica.cedula}
             |
             |Empresa: ${practica.empresa}
             |Oferta: ${practica.oferta}
             |Area: ${practica.area}
             |
             |Tutor academico: ${practica.tutorAcademico}
             |Tutor empresarial: ${practica.tutorEmpresarial}
             |
             |Fecha inicio: ${practica.fechaInicio}
             |Fecha fin planificada: ${practica.fechaFin}
             |
             |Actividades registradas: ${actividadesPractica.size}
             |Completadas: $completadas
             |Aprobadas pendientes de completar: $aprobadas
             |Pendientes de aprobacion: $pendientes
             |Negadas: $negadas
             |""".stripMargin

        JOptionPane.showMessageDialog(vista, mensaje, "Detalle de practica", JOptionPane.INFORMATION_MESSAGE)
      case None =>
    }
  }

  private def modeloActividades(datos: List[Actividad]): DefaultTableModel = {
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
    modelo
  }

  private def practicaSeleccionada(): Option[PracticaCoordinadorResumen] = {
    val fila = vista.tblPracticas.getSelectedRow
    if (fila < 0) {
      JOptionPane.showMessageDialog(vista, "Seleccione una practica.")
      None
    } else {
      val id = vista.tblPracticas.getValueAt(fila, 0).toString.toLong
      practicas.listarCoordinador().find(_.idPractica == id)
    }
  }

  private def colorEstado(estado: String): Color =
    estado match {
      case "activa" => new Color(54, 117, 181)
      case "finalizada" => new Color(181, 132, 42)
      case "completada" => new Color(48, 140, 92)
      case "cerrada" => new Color(120, 90, 160)
      case _ => new Color(110, 110, 110)
    }

  private def abrirEmpresas(): Unit = {
    vista.dispose()
    new ControlCoordinadorEmpresas(usuarioSesion, alInicio, alCerrarSesion).mostrar()
  }

  private def abrirEstudiantes(): Unit = {
    vista.dispose()
    new ControlCoordinadorEstudiantes(usuarioSesion, alInicio, alCerrarSesion).mostrar()
  }

  private def abrirTutores(): Unit = {
    vista.dispose()
    new ControlCoordinadorTutores(usuarioSesion, alInicio, alCerrarSesion).mostrar()
  }

  private def abrirOfertas(): Unit = {
    vista.dispose()
    new ControlCoordinadorOfertas(usuarioSesion, alInicio, alCerrarSesion).mostrar()
  }

  private def abrirPostulaciones(): Unit = {
    vista.dispose()
    new ControlCoordinadorPostulaciones(usuarioSesion, alInicio, alCerrarSesion).mostrar()
  }

  private def abrirReportes(): Unit = {
    vista.dispose()
    new ControlCoordinadorReportes(usuarioSesion, alInicio, alCerrarSesion).mostrar()
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
