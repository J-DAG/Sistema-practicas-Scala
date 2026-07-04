package controlador

import modelo.entidades.{EstudianteResumen, Usuario}
import modelo.reportes.AnalisisReportes
import modelo.repositorios.UsuarioRepositorio
import vista.VistaCoordinadorEstudiante

import java.awt.{Color, Font, GridLayout}
import javax.swing.{BorderFactory, JLabel, JOptionPane, JPanel, SwingConstants}
import javax.swing.table.DefaultTableModel

class ControlCoordinadorEstudiantes(usuarioSesion: Usuario, alInicio: () => Unit, alCerrarSesion: () => Unit) {
  private val vista = new VistaCoordinadorEstudiante()
  private val usuarios = new UsuarioRepositorio()

  vista.lblInformacion.setText(s"Bienvenido ${usuarioSesion.nombreCompleto}")

  vista.btnInicio.addActionListener(_ => volverInicio())
  vista.btnEstudiantes.addActionListener(_ => cargarDatos())
  vista.btnBuscar.addActionListener(_ => cargarDatos())
  vista.txtBuscar.addActionListener(_ => cargarDatos())
  vista.btnCerrarSesion.addActionListener(_ => cerrarSesion())

  vista.btnEmpresa.addActionListener(_ => abrirEmpresas())
  vista.btnTutores.addActionListener(_ => abrirTutores())
  vista.btnOfertas.addActionListener(_ => abrirOfertas())
  vista.btnPracticas.addActionListener(_ => abrirPracticas())
  vista.btnPostulaciones.addActionListener(_ => abrirPostulaciones())
  vista.btnReportes.addActionListener(_ => abrirReportes())

  def mostrar(): Unit = {
    cargarDatos()
    vista.setLocationRelativeTo(null)
    vista.setVisible(true)
  }

  private def cargarDatos(): Unit = {
    try {
      val estudiantes = usuarios.listarEstudiantes(vista.txtBuscar.getText)
      llenarTabla(estudiantes)
      cargarResumen(estudiantes)
    } catch {
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudo cargar estudiantes.\n\nDetalle: ${error.getMessage}")
    }
  }

  private def llenarTabla(datos: List[EstudianteResumen]): Unit = {
    val modelo = new DefaultTableModel(Array[AnyRef]("ID", "Estudiante", "Cedula", "Email", "Carrera", "Ciclo", "Estado", "Activo"), 0) {
      override def isCellEditable(row: Int, column: Int): Boolean = false
    }

    datos.foreach { estudiante =>
      modelo.addRow(Array[AnyRef](
        Long.box(estudiante.idUsuario),
        estudiante.nombreCompleto,
        estudiante.cedula,
        estudiante.email,
        estudiante.carrera.getOrElse(""),
        estudiante.cicloActual.map(_.toString).getOrElse(""),
        estudiante.estadoPractica,
        Boolean.box(estudiante.activo)
      ))
    }

    vista.tblEstudiantes.setModel(modelo)
  }

  private def cargarResumen(estudiantes: List[EstudianteResumen]): Unit = {
    val segmentos = AnalisisReportes.resumenEstudiantes(estudiantes)
    val total = estudiantes.size

    vista.widgetGraficoPastel.removeAll()
    vista.widgetGraficoPastel.setBorder(BorderFactory.createTitledBorder("Estado de aplicacion"))

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

    val totalLabel = new JLabel(s"Total estudiantes: $total", SwingConstants.CENTER)
    totalLabel.setFont(new Font("Dialog", Font.BOLD, 16))
    totalLabel.setForeground(new Color(45, 45, 45))
    panel.add(totalLabel)

    vista.widgetGraficoPastel.add(panel)
    vista.widgetGraficoPastel.revalidate()
    vista.widgetGraficoPastel.repaint()
  }

  private def colorEstado(estado: String): Color =
    estado match {
      case "En practica" => new Color(54, 117, 181)
      case "Finalizada" => new Color(181, 132, 42)
      case "Completada" => new Color(48, 140, 92)
      case "Postulando" => new Color(181, 132, 42)
      case _ => new Color(110, 110, 110)
    }

  private def abrirEmpresas(): Unit = {
    vista.dispose()
    new ControlCoordinadorEmpresas(usuarioSesion, alInicio, alCerrarSesion).mostrar()
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

  private def abrirPracticas(): Unit = {
    vista.dispose()
    new ControlCoordinadorPracticas(usuarioSesion, alInicio, alCerrarSesion).mostrar()
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
