package controlador

import modelo.entidades.{EstudianteResumen, Usuario}
import modelo.reportes.{AnalisisReportes, ResumenCiclo}
import modelo.repositorios.{EmpresaRepositorio, PostulacionRepositorio, PracticaRepositorio, UsuarioRepositorio}
import vista.VistaCoordinadorReportes

import java.awt.{Color, Font, GridLayout}
import javax.swing.{BorderFactory, JLabel, JOptionPane, JPanel, SwingConstants}

class ControlCoordinadorReportes(usuarioSesion: Usuario, alInicio: () => Unit, alCerrarSesion: () => Unit) {
  private val vista = new VistaCoordinadorReportes()
  private val usuarios = new UsuarioRepositorio()
  private val empresas = new EmpresaRepositorio()
  private val postulaciones = new PostulacionRepositorio()
  private val practicas = new PracticaRepositorio()

  vista.lblInformacion.setText(s"Bienvenido ${usuarioSesion.nombreCompleto}")

  vista.btnInicio.addActionListener(_ => volverInicio())
  vista.btnReportes.addActionListener(_ => cargarDatos())
  vista.btnCerrarSesion.addActionListener(_ => cerrarSesion())

  vista.btnEmpresa.addActionListener(_ => abrirEmpresas())
  vista.btnEstudiantes.addActionListener(_ => abrirEstudiantes())
  vista.btnTutores.addActionListener(_ => abrirTutores())
  vista.btnOfertas.addActionListener(_ => abrirOfertas())
  vista.btnPracticas.addActionListener(_ => abrirPracticas())
  vista.btnPostulaciones.addActionListener(_ => abrirPostulaciones())

  def mostrar(): Unit = {
    cargarDatos()
    vista.setLocationRelativeTo(null)
    vista.setVisible(true)
  }

  private def cargarDatos(): Unit = {
    try {
      val estudiantes = usuarios.listarEstudiantes()
      val estadosPracticas = practicas.contarEstados()

      vista.lblNumEstudiantes.setText(formatear(usuarios.contarPorRol("estudiante")))
      vista.lblNumTA.setText(formatear(usuarios.contarPorRol("tutor_academico")))
      vista.lblTE.setText(formatear(usuarios.contarPorRol("tutor_empresarial")))
      vista.lblNumEmpresas.setText(formatear(empresas.contarActivas()))
      vista.lblNumPostulaciones.setText(formatear(postulaciones.contarPorEstado("pendiente")))
      vista.lblNumPracActiva.setText(formatear(estadosPracticas.getOrElse("activa", 0)))
      vista.lblNumPracFinalizada.setText(formatear(estadosPracticas.getOrElse("finalizada", 0) + estadosPracticas.getOrElse("completada", 0)))

      cargarGraficoPracticas(estadosPracticas)
      cargarGraficoCiclos(estudiantes)
    } catch {
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudieron cargar los reportes.\n\nDetalle: ${error.getMessage}")
    }
  }

  private def cargarGraficoPracticas(conteos: Map[String, Int]): Unit = {
    val segmentos = AnalisisReportes.resumenPracticasDesdeConteos(conteos)
    val total = segmentos.headOption.map(_.total).getOrElse(0)

    vista.widgetGraficoPastel.removeAll()
    vista.widgetGraficoPastel.setBorder(BorderFactory.createTitledBorder("Practicas por estado"))
    vista.widgetGraficoPastel.add(panelResumen(
      segmentos.map(segmento => (segmento.texto, colorEstadoPractica(segmento.clave))) :+
        (s"Total practicas: $total", new Color(45, 45, 45)),
      fondoTotal = false
    ))
    vista.widgetGraficoPastel.revalidate()
    vista.widgetGraficoPastel.repaint()
  }

  private def cargarGraficoCiclos(estudiantes: List[EstudianteResumen]): Unit = {
    val ciclos = AnalisisReportes.resumenCiclos(estudiantes)

    vista.widgetGraficoBarras.removeAll()
    vista.widgetGraficoBarras.setBorder(BorderFactory.createTitledBorder("Practicantes por ciclo"))

    val panel = new JPanel(new GridLayout(0, 1, 4, 4))
    panel.setBackground(Color.WHITE)

    if (ciclos.isEmpty) {
      panel.add(etiqueta("No hay estudiantes registrados.", new Color(110, 110, 110)))
    } else {
      ciclos.foreach(ciclo => agregarResumenCiclo(panel, ciclo))
    }

    vista.widgetGraficoBarras.add(panel)
    vista.widgetGraficoBarras.revalidate()
    vista.widgetGraficoBarras.repaint()
  }

  private def agregarResumenCiclo(panel: JPanel, resumen: ResumenCiclo): Unit =
    panel.add(etiqueta(resumen.texto, colorCiclo(resumen.ciclo.getOrElse(0))))

  private def panelResumen(items: Seq[(String, Color)], fondoTotal: Boolean): JPanel = {
    val panel = new JPanel(new GridLayout(0, 1, 6, 6))
    panel.setBackground(Color.WHITE)
    items.zipWithIndex.foreach { case ((texto, color), indice) =>
      val ultimo = indice == items.size - 1
      val label = etiqueta(texto, color)
      if (ultimo && !fondoTotal) {
        label.setOpaque(false)
        label.setForeground(color)
      }
      panel.add(label)
    }
    panel
  }

  private def etiqueta(texto: String, color: Color): JLabel = {
    val label = new JLabel(texto, SwingConstants.CENTER)
    label.setOpaque(true)
    label.setBackground(color)
    label.setForeground(Color.WHITE)
    label.setFont(new Font("Dialog", Font.BOLD, 13))
    label
  }

  private def colorEstadoPractica(estado: String): Color =
    estado match {
      case "activa" => new Color(54, 117, 181)
      case "finalizada" => new Color(181, 132, 42)
      case "completada" => new Color(48, 140, 92)
      case "cerrada" => new Color(120, 90, 160)
      case _ => new Color(110, 110, 110)
    }

  private def colorCiclo(ciclo: Int): Color = {
    val colores = Array(
      new Color(54, 117, 181),
      new Color(48, 140, 92),
      new Color(181, 132, 42),
      new Color(120, 90, 160),
      new Color(70, 130, 120)
    )
    colores(Math.abs(ciclo) % colores.length)
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

  private def abrirPracticas(): Unit = {
    vista.dispose()
    new ControlCoordinadorPracticas(usuarioSesion, alInicio, alCerrarSesion).mostrar()
  }

  private def abrirPostulaciones(): Unit = {
    vista.dispose()
    new ControlCoordinadorPostulaciones(usuarioSesion, alInicio, alCerrarSesion).mostrar()
  }

  private def volverInicio(): Unit = {
    vista.dispose()
    alInicio()
  }

  private def cerrarSesion(): Unit = {
    vista.dispose()
    alCerrarSesion()
  }

  private def formatear(valor: Int): String = f"$valor%02d"
}
