package controlador

import modelo.entidades.{PracticaResumen, Usuario}
import modelo.repositorios.PracticaRepositorio
import vista.VistaMainTutorAcademico

import javax.swing.JOptionPane
import javax.swing.table.DefaultTableModel

class ControlVentanaTA(usuario: Usuario, alCerrarSesion: () => Unit) {
  private val vista = new VistaMainTutorAcademico()
  private val practicas = new PracticaRepositorio()

  vista.lblInformacion.setText(s"Bienvenido ${usuario.nombreCompleto}")

  vista.btnEnProgreso.addActionListener(_ => abrirPracticasProgreso())
  vista.mniEnProgreso.addActionListener(_ => abrirPracticasProgreso())
  vista.btnCompletadas.addActionListener(_ => abrirPracticasCompletadas())
  vista.mniCompletadas.addActionListener(_ => abrirPracticasCompletadas())
  vista.btnNotificaciones.addActionListener(_ => verNotificaciones())
  vista.btnVerNotificaciones.addActionListener(_ => verNotificaciones())
  vista.mniNotificaciones.addActionListener(_ => verNotificaciones())
  vista.btnCerrarSesion.addActionListener(_ => cerrarSesion())
  vista.btnCerrarSesion_2.addActionListener(_ => cerrarSesion())
  vista.mniCerrarSesion.addActionListener(_ => cerrarSesion())

  def mostrar(): Unit = {
    cargarResumen()
    vista.setLocationRelativeTo(null)
    vista.setVisible(true)
  }

  private def cargarResumen(): Unit = {
    try {
      val activas = practicas.listarPorTutorAcademico(usuario.idUsuario, Set("activa"), "")
      vista.lblNumEstudiantesEditar.setText(formatear(practicas.contarEstudiantesPorTutorAcademico(usuario.idUsuario)))
      vista.lblNumPracticasProgresoEditar.setText(formatear(practicas.contarPorTutorAcademico(usuario.idUsuario, "activa")))
      vista.lblNumPracComprelatasEditar.setText(formatear(
        practicas.contarPorTutorAcademico(usuario.idUsuario, "finalizada") + practicas.contarPorTutorAcademico(usuario.idUsuario, "completada")
      ))
      llenarTabla(activas)
    } catch {
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudo cargar el panel del tutor academico.\n\nDetalle: ${error.getMessage}")
    }
  }

  private def llenarTabla(datos: List[PracticaResumen]): Unit = {
    val modelo = new DefaultTableModel(Array[AnyRef]("Practica", "Estudiante", "Cedula", "Empresa", "Oferta", "Area", "Horas", "Progreso"), 0) {
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
        s"${practica.horasCumplidas}/240",
        s"${practica.porcentaje}%"
      ))
    }
    vista.tblEstudiantes.setModel(modelo)
  }

  private def abrirPracticasProgreso(): Unit = {
    vista.dispose()
    new ControlTAPracticasProgreso(usuario, () => new ControlVentanaTA(usuario, alCerrarSesion).mostrar(), alCerrarSesion).mostrar()
  }

  private def abrirPracticasCompletadas(): Unit = {
    vista.dispose()
    new ControlTAPracticasCompletadas(usuario, () => new ControlVentanaTA(usuario, alCerrarSesion).mostrar(), alCerrarSesion).mostrar()
  }

  private def verNotificaciones(): Unit =
    DialogoNotificaciones.mostrar(vista, usuario)

  private def cerrarSesion(): Unit = {
    vista.dispose()
    alCerrarSesion()
  }

  private def formatear(valor: Int): String = f"$valor%02d"
}
