package controlador

import modelo.entidades.{Actividad, PracticaEstudianteDetalle, Usuario}
import modelo.repositorios.{ActividadRepositorio, PracticaRepositorio}
import vista.VistaEstFormularios

import java.awt.{BorderLayout, GridLayout}
import javax.swing.{JLabel, JOptionPane, JPanel, JScrollPane, JTextArea}

class ControlEstudianteFormularios(usuario: Usuario, alInicio: () => Unit, alCerrarSesion: () => Unit) {
  private val vista = new VistaEstFormularios()
  private val practicas = new PracticaRepositorio()
  private val actividades = new ActividadRepositorio()
  private var practicaActual: Option[PracticaEstudianteDetalle] = None

  vista.btnInico.addActionListener(_ => volverInicio())
  vista.btnMiPractica.addActionListener(_ => abrirPractica())
  vista.btnMisPostulaciones.addActionListener(_ => abrirPostulaciones())
  vista.btnOfertaLaboral.addActionListener(_ => abrirOfertas())
  vista.btnMisFormularios.addActionListener(_ => cargarDatos())
  vista.btnCartaCompromiso.addActionListener(_ => verCartaCompromiso())
  vista.btnCerrarSesion.addActionListener(_ => cerrarSesion())

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
          llenarFormularioInicial(practica)
          llenarFormularioFinal(practica)
          vista.btnCartaCompromiso.setEnabled(!practica.empresaTieneConvenio)
        case None =>
          limpiarPaneles("Aun no tiene una practica para generar formularios.")
          vista.btnCartaCompromiso.setEnabled(false)
      }
    } catch {
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudieron cargar formularios.\n\nDetalle: ${error.getMessage}")
    }
  }

  private def llenarFormularioInicial(practica: PracticaEstudianteDetalle): Unit = {
    val contenido = panelTexto(
      "Formulario 1 - Inicio de practica",
      List(
        s"Estudiante: ${usuario.nombreCompleto}",
        s"Codigo de practica: ${practica.idPractica}",
        s"Empresa: ${practica.empresa}",
        s"Fecha de inicio: ${practica.fechaInicio}",
        s"Fecha de fin planificada: ${practica.fechaFin}",
        s"Tutor academico: ${practica.tutorAcademico}",
        s"Tutor empresarial: ${practica.tutorEmpresarial}",
        s"Oferta: ${practica.oferta}",
        s"Area: ${practica.area}",
        s"Convenio: ${if (practica.empresaTieneConvenio) "Si" else "No"}"
      )
    )
    reemplazar(vista.widgetF1, contenido)
  }

  private def llenarFormularioFinal(practica: PracticaEstudianteDetalle): Unit = {
    if (!practica.formulariosFinalesEnviados || practica.calificacion.isEmpty) {
      reemplazar(vista.widgetF2, panelTexto("Formulario 2 - Final", List("Disponible cuando la practica sea calificada por el tutor academico.")))
      return
    }

    val listaActividades = actividades
      .listarPorPractica(practica.idPractica)
      .filter(_.estado.equalsIgnoreCase("completada"))

    val lineas = List(
      s"Estudiante: ${usuario.nombreCompleto}",
      s"Codigo de practica: ${practica.idPractica}",
      s"Empresa: ${practica.empresa}",
      s"Fecha de inicio: ${practica.fechaInicio}",
      s"Fecha de fin: ${practica.fechaFin}",
      s"Tutor academico: ${practica.tutorAcademico}",
      s"Tutor empresarial: ${practica.tutorEmpresarial}",
      s"Oferta: ${practica.oferta}",
      s"Area: ${practica.area}",
      s"Calificacion: ${practica.calificacion.get}/100",
      "",
      "Actividades desarrolladas:"
    ) ++ actividadesTexto(listaActividades)

    reemplazar(vista.widgetF2, panelTexto("Formulario 2 - Practica finalizada", lineas))
  }

  private def verCartaCompromiso(): Unit = {
    practicaActual match {
      case Some(practica) if !practica.empresaTieneConvenio =>
        val mensaje =
          s"""Carta compromiso
             |
             |Estudiante: ${usuario.nombreCompleto}
             |Empresa: ${practica.empresa}
             |Practica: ${practica.idPractica}
             |Oferta: ${practica.oferta}
             |Area: ${practica.area}
             |
             |La empresa no cuenta con convenio registrado. Se emite carta compromiso estandar para respaldar el desarrollo de la practica preprofesional.
             |
             |Documento enviado al correo del estudiante: ${usuario.email}
             |""".stripMargin
        JOptionPane.showMessageDialog(vista, mensaje, "Carta compromiso", JOptionPane.INFORMATION_MESSAGE)
      case Some(_) =>
        JOptionPane.showMessageDialog(vista, "La empresa tiene convenio. No se requiere carta compromiso.")
      case None =>
        JOptionPane.showMessageDialog(vista, "No tiene practica asociada.")
    }
  }

  private def limpiarPaneles(mensaje: String): Unit = {
    val panel = panelTexto("Sin formularios", List(mensaje))
    reemplazar(vista.widgetF1, panel)
    reemplazar(vista.widgetF2, panelTexto("Formulario final", List(mensaje)))
  }

  private def panelTexto(titulo: String, lineas: List[String]): JPanel = {
    val panel = new JPanel(new BorderLayout(6, 6))
    val encabezado = new JLabel(titulo)
    val texto = new JTextArea(lineas.mkString("\n"))
    texto.setEditable(false)
    texto.setLineWrap(true)
    texto.setWrapStyleWord(true)
    panel.add(encabezado, BorderLayout.NORTH)
    panel.add(new JScrollPane(texto), BorderLayout.CENTER)
    panel
  }

  private def actividadesTexto(datos: List[Actividad]): List[String] =
    if (datos.isEmpty) {
      List("No hay actividades completadas registradas.")
    } else {
      datos.map(a => s"- ${a.descripcion} (${a.horas} horas, ${a.fecha})")
    }

  private def reemplazar(destino: JPanel, contenido: JPanel): Unit = {
    destino.removeAll()
    destino.setLayout(new GridLayout(1, 1))
    destino.add(contenido)
    destino.revalidate()
    destino.repaint()
  }

  private def abrirOfertas(): Unit = {
    vista.dispose()
    new ControlEstudianteOfertas(usuario, alInicio, alCerrarSesion).mostrar()
  }

  private def abrirPostulaciones(): Unit = {
    vista.dispose()
    new ControlEstudiantePostulaciones(usuario, alInicio, alCerrarSesion).mostrar()
  }

  private def abrirPractica(): Unit = {
    vista.dispose()
    new ControlEstudiantePractica(usuario, alInicio, alCerrarSesion).mostrar()
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
