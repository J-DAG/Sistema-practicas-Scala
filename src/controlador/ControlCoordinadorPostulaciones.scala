package controlador

import modelo.entidades.{PostulacionDetalle, PostulacionResumen, Usuario}
import modelo.repositorios.{PostulacionRepositorio, UsuarioRepositorio}
import modelo.servicios.PostulacionServicio
import vista.{SelectorFechaDialog, VistaCoordinadorPostulaciones}

import java.awt.Desktop
import java.io.File
import java.awt.GridLayout
import java.time.{LocalDate, ZoneId}
import java.util.Date
import javax.swing.{JButton, JComboBox, JLabel, JOptionPane, JPanel}
import javax.swing.table.DefaultTableModel

class ControlCoordinadorPostulaciones(usuarioSesion: Usuario, alInicio: () => Unit, alCerrarSesion: () => Unit) {
  private val vista = new VistaCoordinadorPostulaciones()
  private val postulaciones = new PostulacionRepositorio()
  private val usuarios = new UsuarioRepositorio()
  private val servicio = new PostulacionServicio()

  vista.lblInformacion.setText(s"Bienvenido ${usuarioSesion.nombreCompleto}")

  vista.btnInicio.addActionListener(_ => volverInicio())
  vista.btnPostulaciones.addActionListener(_ => cargarDatos())
  vista.btnBuscar.addActionListener(_ => cargarDatos())
  vista.txtBuscar.addActionListener(_ => cargarDatos())
  vista.btnRevisar.addActionListener(_ => revisarPostulacion())
  vista.btnCerrarSesion.addActionListener(_ => cerrarSesion())

  vista.btnEmpresa.addActionListener(_ => abrirEmpresas())
  vista.btnEstudiantes.addActionListener(_ => abrirEstudiantes())
  vista.btnTutores.addActionListener(_ => abrirTutores())
  vista.btnOfertas.addActionListener(_ => abrirOfertas())
  vista.btnPracticas.addActionListener(_ => moduloPendiente("Practicas"))
  vista.btnReportes.addActionListener(_ => moduloPendiente("Reportes"))

  def mostrar(): Unit = {
    cargarDatos()
    vista.setLocationRelativeTo(null)
    vista.setVisible(true)
  }

  private def cargarDatos(): Unit = {
    try {
      llenarTabla(postulaciones.listarResumen(vista.txtBuscar.getText))
    } catch {
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudo cargar postulaciones.\n\nDetalle: ${error.getMessage}")
    }
  }

  private def llenarTabla(datos: List[PostulacionResumen]): Unit = {
    val modelo = new DefaultTableModel(Array[AnyRef]("ID", "Estudiante", "Cedula", "Empresa", "Oferta", "Area", "Fecha", "Estado", "Documento"), 0) {
      override def isCellEditable(row: Int, column: Int): Boolean = false
    }

    datos.foreach { postulacion =>
      modelo.addRow(Array[AnyRef](
        Long.box(postulacion.idPostulacion),
        postulacion.estudiante,
        postulacion.cedula,
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

  private def revisarPostulacion(): Unit = {
    postulacionSeleccionada() match {
      case Some(detalle) =>
        val formulario = construirFormularioRevision(detalle)
        val opciones = opcionesRevision(detalle)
        val respuesta = JOptionPane.showOptionDialog(
          vista,
          formulario.panel,
          "Revisar postulacion",
          JOptionPane.DEFAULT_OPTION,
          JOptionPane.QUESTION_MESSAGE,
          null,
          opciones,
          opciones(0)
        )

        if (respuesta >= 0 && respuesta < opciones.length) {
          opciones(respuesta).toString match {
            case "Aprobar postulacion" => aprobarPostulacion(detalle, formulario)
            case "Negar postulacion" => negarPostulacion(detalle)
            case "Cancelar aprobacion" => cancelarAprobacion(detalle)
            case _ =>
          }
        }
      case None =>
    }
  }

  private def opcionesRevision(detalle: PostulacionDetalle): Array[AnyRef] =
    detalle.estado.trim.toLowerCase match {
      case "pendiente" => Array[AnyRef]("Aprobar postulacion", "Negar postulacion", "Cancelar")
      case "aprobada" => Array[AnyRef]("Cancelar aprobacion", "Negar postulacion", "Cerrar")
      case "negada" => Array[AnyRef]("Aprobar postulacion", "Cerrar")
      case _ => Array[AnyRef]("Cerrar")
    }

  private def aprobarPostulacion(detalle: PostulacionDetalle, formulario: RevisionPostulacion): Unit = {
    if (detalle.cuposOferta <= 0) {
      JOptionPane.showMessageDialog(vista, "La oferta no tiene cupos disponibles.")
      return
    }

    val confirmar = JOptionPane.showConfirmDialog(
      vista,
      s"Desea aprobar la postulacion de ${detalle.estudiante} y crear la practica?",
      "Confirmar aprobacion",
      JOptionPane.YES_NO_OPTION
    )
    if (confirmar != JOptionPane.YES_OPTION) {
      return
    }

    formulario.asignacion() match {
      case Some(asignacion) =>
        try {
          val idPractica = servicio.aprobar(
            idPostulacion = detalle.idPostulacion,
            idTutorAcademico = asignacion.idTutorAcademico,
            idTutorEmpresarial = asignacion.idTutorEmpresarial,
            idCoordinador = usuarioSesion.idUsuario,
            fechaInicio = asignacion.fechaInicio,
            fechaFin = asignacion.fechaFin
          )
          JOptionPane.showMessageDialog(vista, s"Postulacion aprobada. Practica creada con codigo $idPractica.")
          cargarDatos()
        } catch {
          case error: Exception =>
            JOptionPane.showMessageDialog(vista, s"No se pudo aprobar la postulacion.\n\nDetalle: ${error.getMessage}")
        }
      case None =>
    }
  }

  private def negarPostulacion(detalle: PostulacionDetalle): Unit = {
    val mensaje =
      if (detalle.estado.equalsIgnoreCase("aprobada")) {
        s"La postulacion de ${detalle.estudiante} ya fue aprobada.\nSi la niega, se eliminara la practica activa asociada siempre que no tenga actividades registradas.\nDesea continuar?"
      } else {
        s"Desea negar la postulacion de ${detalle.estudiante}?"
      }
    val respuesta = JOptionPane.showConfirmDialog(
      vista,
      mensaje,
      "Confirmar negacion",
      JOptionPane.YES_NO_OPTION
    )
    if (respuesta != JOptionPane.YES_OPTION) {
      return
    }

    try {
      servicio.negar(detalle.idPostulacion, usuarioSesion.idUsuario)
      JOptionPane.showMessageDialog(vista, "Postulacion negada correctamente.")
      cargarDatos()
    } catch {
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudo negar la postulacion.\n\nDetalle: ${error.getMessage}")
    }
  }

  private def cancelarAprobacion(detalle: PostulacionDetalle): Unit = {
    val respuesta = JOptionPane.showConfirmDialog(
      vista,
      s"Desea cancelar la aprobacion de la postulacion de ${detalle.estudiante}?\nLa practica activa se eliminara solo si no tiene actividades registradas y el cupo sera devuelto.",
      "Confirmar cancelacion de aprobacion",
      JOptionPane.YES_NO_OPTION
    )
    if (respuesta != JOptionPane.YES_OPTION) {
      return
    }

    try {
      servicio.cancelarAprobacion(detalle.idPostulacion, usuarioSesion.idUsuario)
      JOptionPane.showMessageDialog(vista, "Aprobacion cancelada correctamente. La postulacion volvio a estado pendiente.")
      cargarDatos()
    } catch {
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudo cancelar la aprobacion.\n\nDetalle: ${error.getMessage}")
    }
  }

  private case class AsignacionPractica(
      idTutorAcademico: Long,
      idTutorEmpresarial: Long,
      fechaInicio: LocalDate,
      fechaFin: LocalDate
  )

  private case class RevisionPostulacion(panel: JPanel, asignacion: () => Option[AsignacionPractica])

  private def construirFormularioRevision(detalle: PostulacionDetalle): RevisionPostulacion = {
    val tutoresAcademicos = usuarios.listarTutoresAcademicosActivos()
    val tutoresEmpresariales = usuarios.listarTutoresEmpresarialesActivosPorEmpresa(detalle.idEmpresa)
    val permiteAsignar = detalle.estado.equalsIgnoreCase("pendiente") || detalle.estado.equalsIgnoreCase("negada")

    val cbxTA = new JComboBox[String]()
    tutoresAcademicos.foreach(t => cbxTA.addItem(s"${t.idUsuario} - ${t.nombreCompleto}"))
    cbxTA.setEnabled(permiteAsignar && tutoresAcademicos.nonEmpty)

    val cbxTE = new JComboBox[String]()
    tutoresEmpresariales.foreach(t => cbxTE.addItem(s"${t.idUsuario} - ${t.nombreCompleto}"))
    cbxTE.setEnabled(permiteAsignar && tutoresEmpresariales.nonEmpty)

    var fechaInicioSeleccionada = detalle.fechaInicioPractica
      .map(fecha => Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant))
      .getOrElse(new Date())
    var fechaFinSeleccionada = detalle.fechaFinPractica
      .map(fecha => Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant))
      .getOrElse(Date.from(LocalDate.now().plusMonths(6).atStartOfDay(ZoneId.systemDefault()).toInstant))

    val lblFechaInicio = new JLabel(formatearFecha(fechaInicioSeleccionada))
    val lblFechaFin = new JLabel(formatearFecha(fechaFinSeleccionada))

    val btnFechaInicio = new JButton("Seleccionar en calendario")
    btnFechaInicio.setEnabled(permiteAsignar)
    btnFechaInicio.addActionListener(_ => {
      val seleccion = SelectorFechaDialog.seleccionar(vista, fechaInicioSeleccionada)
      if (seleccion != null) {
        fechaInicioSeleccionada = seleccion
        lblFechaInicio.setText(formatearFecha(seleccion))
      }
    })

    val btnFechaFin = new JButton("Seleccionar en calendario")
    btnFechaFin.setEnabled(permiteAsignar)
    btnFechaFin.addActionListener(_ => {
      val seleccion = SelectorFechaDialog.seleccionar(vista, fechaFinSeleccionada)
      if (seleccion != null) {
        fechaFinSeleccionada = seleccion
        lblFechaFin.setText(formatearFecha(seleccion))
      }
    })

    val btnVerDocumento = new JButton("Ver avance de malla")
    btnVerDocumento.addActionListener(_ => verDocumentoAdjunto(detalle))
    btnVerDocumento.setEnabled(detalle.rutaDocumentoMalla.exists(_.trim.nonEmpty))

    val panel = new JPanel(new GridLayout(0, 1, 4, 4))
    panel.add(new JLabel(s"Estudiante: ${detalle.estudiante}"))
    panel.add(new JLabel(s"Empresa: ${detalle.empresa}"))
    panel.add(new JLabel(s"Oferta: ${detalle.oferta}"))
    panel.add(new JLabel(s"Area: ${detalle.area}"))
    panel.add(new JLabel(s"Estado: ${detalle.estado}"))
    panel.add(new JLabel(s"Cupos disponibles: ${detalle.cuposOferta}"))
    panel.add(new JLabel(s"Convenio: ${if (detalle.empresaTieneConvenio) "Si" else "No - se notificara carta compromiso"}"))
    panel.add(new JLabel("Documento de avance de malla:"))
    panel.add(btnVerDocumento)
    panel.add(new JLabel("Tutor academico:"))
    if (!permiteAsignar) {
      panel.add(new JLabel(detalle.tutorAcademico.getOrElse("No registrado")))
    } else if (tutoresAcademicos.isEmpty) {
      panel.add(new JLabel("No existen tutores academicos activos para asignar."))
    } else {
      panel.add(cbxTA)
    }
    panel.add(new JLabel("Tutor empresarial de la empresa:"))
    if (!permiteAsignar) {
      panel.add(new JLabel(detalle.tutorEmpresarial.getOrElse("No registrado")))
    } else if (tutoresEmpresariales.isEmpty) {
      panel.add(new JLabel("No existen tutores empresariales activos en la empresa de la oferta."))
    } else {
      panel.add(cbxTE)
    }
    panel.add(new JLabel("Fecha inicio:"))
    panel.add(lblFechaInicio)
    if (permiteAsignar) {
      panel.add(btnFechaInicio)
    }
    panel.add(new JLabel("Fecha fin:"))
    panel.add(lblFechaFin)
    if (permiteAsignar) {
      panel.add(btnFechaFin)
    }

    RevisionPostulacion(
      panel = panel,
      asignacion = () => {
        if (tutoresAcademicos.isEmpty) {
          JOptionPane.showMessageDialog(vista, "No existen tutores academicos activos para asignar.")
          None
        } else if (tutoresEmpresariales.isEmpty) {
          JOptionPane.showMessageDialog(vista, "No existen tutores empresariales activos en la empresa de la oferta.")
          None
        } else {
          Some(AsignacionPractica(
            idTutorAcademico = tutoresAcademicos(cbxTA.getSelectedIndex).idUsuario,
            idTutorEmpresarial = tutoresEmpresariales(cbxTE.getSelectedIndex).idUsuario,
            fechaInicio = fechaDate(fechaInicioSeleccionada),
            fechaFin = fechaDate(fechaFinSeleccionada)
          ))
        }
      }
    )
  }

  private def postulacionSeleccionada(): Option[PostulacionDetalle] = {
    val fila = vista.tblPostulaciones.getSelectedRow
    if (fila < 0) {
      JOptionPane.showMessageDialog(vista, "Seleccione una postulacion.")
      None
    } else {
      val idPostulacion = vista.tblPostulaciones.getValueAt(fila, 0).toString.toLong
      postulaciones.buscarDetalle(idPostulacion) match {
        case Some(detalle) => Some(detalle)
        case None =>
          JOptionPane.showMessageDialog(vista, "No se encontro la postulacion seleccionada.")
          None
      }
    }
  }

  private def verDocumentoAdjunto(detalle: PostulacionDetalle): Unit = {
    detalle.rutaDocumentoMalla match {
      case Some(ruta) if ruta.trim.nonEmpty =>
        abrirDocumento(ruta)
      case _ =>
        JOptionPane.showMessageDialog(vista, "La postulacion no tiene documento adjunto registrado.")
    }
  }

  private def abrirDocumento(ruta: String): Unit = {
    try {
      val archivo = new File(ruta)
      if (!archivo.exists()) {
        JOptionPane.showMessageDialog(vista, s"No se encontro el documento:\n$ruta")
      } else if (!Desktop.isDesktopSupported) {
        JOptionPane.showMessageDialog(vista, s"No se puede abrir automaticamente en este equipo.\nRuta:\n$ruta")
      } else {
        Desktop.getDesktop.open(archivo)
      }
    } catch {
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudo abrir el documento.\n\nDetalle: ${error.getMessage}")
    }
  }

  private def fechaDate(valor: Date): LocalDate =
    valor.toInstant.atZone(ZoneId.systemDefault()).toLocalDate

  private def formatearFecha(fecha: Date): String = {
    val local = fechaDate(fecha)
    f"${local.getDayOfMonth}%02d/${local.getMonthValue}%02d/${local.getYear}%04d"
  }

  private def abrirEmpresas(): Unit = {
    vista.dispose()
    new ControlCoordinadorEmpresas(usuarioSesion, alInicio, alCerrarSesion).mostrar()
  }

  private def abrirTutores(): Unit = {
    vista.dispose()
    new ControlCoordinadorTutores(usuarioSesion, alInicio, alCerrarSesion).mostrar()
  }

  private def abrirEstudiantes(): Unit = {
    vista.dispose()
    new ControlCoordinadorEstudiantes(usuarioSesion, alInicio, alCerrarSesion).mostrar()
  }

  private def abrirOfertas(): Unit = {
    vista.dispose()
    new ControlCoordinadorOfertas(usuarioSesion, alInicio, alCerrarSesion).mostrar()
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
