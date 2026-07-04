package controlador.util

import modelo.entidades.{PostulacionDetalle, Usuario}
import vista.SelectorFechaDialog

import java.awt.{GridLayout, Window}
import java.time.{LocalDate, ZoneId}
import java.util.Date
import javax.swing.{JButton, JComboBox, JLabel, JOptionPane, JPanel}

final case class AsignacionPracticaFormulario(
    idTutorAcademico: Long,
    idTutorEmpresarial: Long,
    fechaInicio: LocalDate,
    fechaFin: LocalDate
)

final case class RevisionPostulacionFormulario(
    panel: JPanel,
    asignacion: () => Option[AsignacionPracticaFormulario]
)

object RevisionPostulacionFormulario {
  def construir(
      owner: Window,
      detalle: PostulacionDetalle,
      tutoresAcademicos: List[Usuario],
      tutoresEmpresariales: List[Usuario],
      verDocumento: () => Unit
  ): RevisionPostulacionFormulario = {
    val permiteAsignar = detalle.estado.equalsIgnoreCase("pendiente") || detalle.estado.equalsIgnoreCase("negada")

    val cbxTA = comboTutores(tutoresAcademicos)
    cbxTA.setEnabled(permiteAsignar && tutoresAcademicos.nonEmpty)

    val cbxTE = comboTutores(tutoresEmpresariales)
    cbxTE.setEnabled(permiteAsignar && tutoresEmpresariales.nonEmpty)

    var fechaInicioSeleccionada = fechaInicial(detalle.fechaInicioPractica, LocalDate.now())
    var fechaFinSeleccionada = fechaInicial(detalle.fechaFinPractica, LocalDate.now().plusMonths(6))

    val lblFechaInicio = new JLabel(formatearFecha(fechaInicioSeleccionada))
    val lblFechaFin = new JLabel(formatearFecha(fechaFinSeleccionada))

    val btnFechaInicio = botonFecha(owner, permiteAsignar, fechaInicioSeleccionada) { seleccion =>
      fechaInicioSeleccionada = seleccion
      lblFechaInicio.setText(formatearFecha(seleccion))
    }

    val btnFechaFin = botonFecha(owner, permiteAsignar, fechaFinSeleccionada) { seleccion =>
      fechaFinSeleccionada = seleccion
      lblFechaFin.setText(formatearFecha(seleccion))
    }

    val btnVerDocumento = new JButton("Ver avance de malla")
    btnVerDocumento.addActionListener(_ => verDocumento())
    btnVerDocumento.setEnabled(detalle.rutaDocumentoMalla.exists(_.trim.nonEmpty))

    val panel = new JPanel(new GridLayout(0, 1, 4, 4))
    etiquetasDetalle(detalle).foreach(texto => panel.add(new JLabel(texto)))
    panel.add(new JLabel("Documento de avance de malla:"))
    panel.add(btnVerDocumento)
    agregarTutor(panel, "Tutor academico:", permiteAsignar, detalle.tutorAcademico, tutoresAcademicos, cbxTA)
    agregarTutor(panel, "Tutor empresarial de la empresa:", permiteAsignar, detalle.tutorEmpresarial, tutoresEmpresariales, cbxTE)
    agregarFecha(panel, "Fecha inicio:", lblFechaInicio, btnFechaInicio, permiteAsignar)
    agregarFecha(panel, "Fecha fin:", lblFechaFin, btnFechaFin, permiteAsignar)

    RevisionPostulacionFormulario(
      panel = panel,
      asignacion = () => asignacionSeleccionada(owner, tutoresAcademicos, tutoresEmpresariales, cbxTA, cbxTE, fechaInicioSeleccionada, fechaFinSeleccionada)
    )
  }

  private def etiquetasDetalle(detalle: PostulacionDetalle): List[String] =
    List(
      s"Estudiante: ${detalle.estudiante}",
      s"Empresa: ${detalle.empresa}",
      s"Oferta: ${detalle.oferta}",
      s"Area: ${detalle.area}",
      s"Estado: ${detalle.estado}",
      s"Cupos disponibles: ${detalle.cuposOferta}",
      s"Convenio: ${if (detalle.empresaTieneConvenio) "Si" else "No - se notificara carta compromiso"}"
    )

  private def comboTutores(tutores: List[Usuario]): JComboBox[String] = {
    val combo = new JComboBox[String]()
    tutores.map(tutor => s"${tutor.idUsuario} - ${tutor.nombreCompleto}").foreach(combo.addItem)
    combo
  }

  private def agregarTutor(
      panel: JPanel,
      titulo: String,
      permiteAsignar: Boolean,
      tutorActual: Option[String],
      tutores: List[Usuario],
      combo: JComboBox[String]
  ): Unit = {
    panel.add(new JLabel(titulo))
    if (!permiteAsignar) {
      panel.add(new JLabel(tutorActual.getOrElse("No registrado")))
    } else if (tutores.isEmpty) {
      panel.add(new JLabel("No existen tutores activos para asignar."))
    } else {
      panel.add(combo)
    }
  }

  private def agregarFecha(panel: JPanel, titulo: String, etiqueta: JLabel, boton: JButton, permiteAsignar: Boolean): Unit = {
    panel.add(new JLabel(titulo))
    panel.add(etiqueta)
    if (permiteAsignar) {
      panel.add(boton)
    }
  }

  private def botonFecha(owner: Window, habilitado: Boolean, fechaActual: Date)(actualizar: Date => Unit): JButton = {
    val boton = new JButton("Seleccionar en calendario")
    boton.setEnabled(habilitado)
    boton.addActionListener(_ => Option(SelectorFechaDialog.seleccionar(owner, fechaActual)).foreach(actualizar))
    boton
  }

  private def asignacionSeleccionada(
      owner: Window,
      tutoresAcademicos: List[Usuario],
      tutoresEmpresariales: List[Usuario],
      cbxTA: JComboBox[String],
      cbxTE: JComboBox[String],
      fechaInicio: Date,
      fechaFin: Date
  ): Option[AsignacionPracticaFormulario] =
    if (tutoresAcademicos.isEmpty) {
      JOptionPane.showMessageDialog(owner, "No existen tutores academicos activos para asignar.")
      None
    } else if (tutoresEmpresariales.isEmpty) {
      JOptionPane.showMessageDialog(owner, "No existen tutores empresariales activos en la empresa de la oferta.")
      None
    } else {
      Some(AsignacionPracticaFormulario(
        idTutorAcademico = tutoresAcademicos(cbxTA.getSelectedIndex).idUsuario,
        idTutorEmpresarial = tutoresEmpresariales(cbxTE.getSelectedIndex).idUsuario,
        fechaInicio = fechaDate(fechaInicio),
        fechaFin = fechaDate(fechaFin)
      ))
    }

  private def fechaInicial(valor: Option[LocalDate], porDefecto: LocalDate): Date =
    Date.from(valor.getOrElse(porDefecto).atStartOfDay(ZoneId.systemDefault()).toInstant)

  private def fechaDate(valor: Date): LocalDate =
    valor.toInstant.atZone(ZoneId.systemDefault()).toLocalDate

  private def formatearFecha(fecha: Date): String = {
    val local = fechaDate(fecha)
    f"${local.getDayOfMonth}%02d/${local.getMonthValue}%02d/${local.getYear}%04d"
  }
}
