package controlador

import modelo.entidades.{PostulacionResumen, Usuario}
import modelo.repositorios.{EmpresaRepositorio, PostulacionRepositorio, PracticaRepositorio, UsuarioRepositorio}
import vista.{
  VistaCoordinadorEstudiante,
  VistaCoordinadorPostulaciones,
  VistaCoordinadorPracticas,
  VistaCoordinadorReportes,
  VistaCoordinadorTutores,
  VistaMainCoordinador
}

import javax.swing.{JFrame, JOptionPane}
import javax.swing.table.DefaultTableModel

class ControlVentanaCoordinador(usuario: Usuario, alCerrarSesion: () => Unit) {
  private val vista = new VistaMainCoordinador()
  private val usuarios = new UsuarioRepositorio()
  private val empresas = new EmpresaRepositorio()
  private val postulaciones = new PostulacionRepositorio()
  private val practicas = new PracticaRepositorio()

  vista.lblInformacion.setText(s"Bienvenido ${usuario.nombreCompleto}")

  vista.btnCerrarSesion.addActionListener(_ => cerrarSesion())
  vista.btnCerrarSesion_2.addActionListener(_ => cerrarSesion())
  vista.mniCerrarSesion.addActionListener(_ => cerrarSesion())
  vista.btnAcercaDe.addActionListener(_ => acercaDe())
  vista.mniAcercaDe.addActionListener(_ => acercaDe())
  vista.btnNotificaciones.addActionListener(_ => verNotificaciones())
  vista.btnVerNotificaciones.addActionListener(_ => verNotificaciones())
  vista.mniNotificaciones.addActionListener(_ => verNotificaciones())

  vista.btnEmpresa.addActionListener(_ => abrirEmpresas())
  vista.mniEmpresa.addActionListener(_ => abrirEmpresas())
  vista.btnEstudiantes.addActionListener(_ => abrirEstudiantes())
  vista.mniEstudiantes.addActionListener(_ => abrirEstudiantes())
  vista.btnTutores.addActionListener(_ => abrirTutores())
  vista.mniTutores.addActionListener(_ => abrirTutores())
  vista.btnPostulacion.addActionListener(_ => abrirPostulaciones())
  vista.mniPostulacion.addActionListener(_ => abrirPostulaciones())
  vista.btnReportes.addActionListener(_ => abrirPanel(new VistaCoordinadorReportes()))
  vista.mniReportes.addActionListener(_ => abrirPanel(new VistaCoordinadorReportes()))
  vista.mniCrearOferta.addActionListener(_ => abrirOfertas())
  vista.mniPracticas.addActionListener(_ => abrirPanel(new VistaCoordinadorPracticas()))

  def mostrar(): Unit = {
    cargarDatos()
    vista.setLocationRelativeTo(null)
    vista.setVisible(true)
  }

  private def cargarDatos(): Unit = {
    try {
      vista.lblNumEstudiantes.setText(formatear(usuarios.contarPorRol("estudiante")))
      vista.lblNumTA.setText(formatear(usuarios.contarPorRol("tutor_academico")))
      vista.lblTE.setText(formatear(usuarios.contarPorRol("tutor_empresarial")))
      vista.lblNumEmpresas.setText(formatear(empresas.contarActivas()))
      vista.lblNumPostulaciones.setText(formatear(postulaciones.contarPorEstado("pendiente")))
      vista.lblNumPracActiva.setText(formatear(practicas.contarPorEstado("activa")))
      llenarTablaPostulaciones(postulaciones.listarResumen(limite = 20))
    } catch {
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudo cargar el panel de coordinador.\n\nDetalle: ${error.getMessage}")
    }
  }

  private def llenarTablaPostulaciones(datos: List[PostulacionResumen]): Unit = {
    val modelo = new DefaultTableModel(Array[AnyRef]("ID", "Estudiante", "Cedula", "Empresa", "Oferta", "Area", "Fecha", "Estado"), 0) {
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
        postulacion.estado
      ))
    }

    vista.tblPostulaciones.setModel(modelo)
  }

  private def abrirPanel(panel: JFrame): Unit = {
    JOptionPane.showMessageDialog(vista, "Esta ventana ya esta importada. La logica interna se conectara en el siguiente paso.")
    panel.setLocationRelativeTo(vista)
    panel.setVisible(true)
  }

  private def abrirEmpresas(): Unit = {
    vista.dispose()
    new ControlCoordinadorEmpresas(usuario, () => new ControlVentanaCoordinador(usuario, alCerrarSesion).mostrar(), alCerrarSesion).mostrar()
  }

  private def abrirTutores(): Unit = {
    vista.dispose()
    new ControlCoordinadorTutores(usuario, () => new ControlVentanaCoordinador(usuario, alCerrarSesion).mostrar(), alCerrarSesion).mostrar()
  }

  private def abrirEstudiantes(): Unit = {
    vista.dispose()
    new ControlCoordinadorEstudiantes(usuario, () => new ControlVentanaCoordinador(usuario, alCerrarSesion).mostrar(), alCerrarSesion).mostrar()
  }

  private def abrirOfertas(): Unit = {
    vista.dispose()
    new ControlCoordinadorOfertas(usuario, () => new ControlVentanaCoordinador(usuario, alCerrarSesion).mostrar(), alCerrarSesion).mostrar()
  }

  private def abrirPostulaciones(): Unit = {
    vista.dispose()
    new ControlCoordinadorPostulaciones(usuario, () => new ControlVentanaCoordinador(usuario, alCerrarSesion).mostrar(), alCerrarSesion).mostrar()
  }

  private def verNotificaciones(): Unit =
    JOptionPane.showMessageDialog(vista, "Las notificaciones del coordinador se conectaran con el repositorio de notificaciones.")

  private def acercaDe(): Unit =
    JOptionPane.showMessageDialog(vista, "Sistema de gestion de practicas preprofesionales.")

  private def cerrarSesion(): Unit = {
    vista.dispose()
    alCerrarSesion()
  }

  private def formatear(valor: Int): String = f"$valor%02d"
}
