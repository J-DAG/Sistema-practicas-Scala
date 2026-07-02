package controlador

import modelo.bd.Conexion
import modelo.repositorios.UsuarioRepositorio
import modelo.seguridad.SeguridadPassword
import modelo.entidades.Usuario
import vista.VistaInicioSesion

import java.sql.SQLException
import javax.swing.{JButton, JFrame, JOptionPane}

class ControlPrincipal {
  private val vista = new VistaInicioSesion()
  private val usuarios = new UsuarioRepositorio()

  vista.btnCrearCuenta.setVisible(false)
  vista.lblPregunta.setVisible(false)
  vista.btnIniciarSesion.addActionListener(_ => iniciarSesion())

  def mostrar(): Unit =
    vista.setVisible(true)

  private def iniciarSesion(): Unit = {
    val identificador = vista.txtUsuario.getText.trim
    val contrasena = new String(vista.txtContrasenia.getPassword)

    if (identificador.isEmpty || contrasena.isEmpty) {
      JOptionPane.showMessageDialog(vista, "Ingrese usuario y contrasena.")
      return
    }

    try {
      Conexion.probar()

      val usuarioAutenticado = usuarios
        .buscarPorUsuarioOEmail(identificador)
        .filter(_.activo)
        .filter(usuario => SeguridadPassword.verificar(contrasena, usuario.passwordHash))

      usuarioAutenticado match {
        case Some(usuario) =>
          abrirPanel(usuario)
        case None =>
          JOptionPane.showMessageDialog(
            vista,
            "Conexion correcta, pero las credenciales son invalidas o la cuenta esta inactiva."
          )
      }
    } catch {
      case error: SQLException =>
        JOptionPane.showMessageDialog(
          vista,
          s"No se pudo conectar o consultar PostgreSQL.\n\nDetalle: ${error.getMessage}"
        )
      case error: IllegalStateException =>
        JOptionPane.showMessageDialog(
          vista,
          s"Error de configuracion de base de datos.\n\nDetalle: ${error.getMessage}"
        )
      case error: Exception =>
        JOptionPane.showMessageDialog(
          vista,
          s"Error inesperado al iniciar sesion.\n\nDetalle: ${error.getMessage}"
        )
    }
  }

  private def abrirPanel(usuario: Usuario): Unit = {
    val rolNormalizado = usuario.rol.trim.toLowerCase

    rolNormalizado match {
      case "administrador" | "admin" =>
        vista.setVisible(false)
        new ControlVentanaAdmin(usuario, () => mostrarLogin()).mostrar()

      case "coordinador" =>
        vista.setVisible(false)
        new ControlVentanaCoordinador(usuario, () => mostrarLogin()).mostrar()

      case "estudiante" =>
        vista.setVisible(false)
        new ControlVentanaEstudiante(usuario, () => mostrarLogin()).mostrar()

      case "tutor_academico" | "tutor academico" | "ta" =>
        vista.setVisible(false)
        new ControlVentanaTA(usuario, () => mostrarLogin()).mostrar()

      case "tutor_empresarial" | "tutor empresarial" | "te" =>
        vista.setVisible(false)
        new ControlVentanaTE(usuario, () => mostrarLogin()).mostrar()

      case _ =>
        JOptionPane.showMessageDialog(
          vista,
          s"El rol '${usuario.rol}' aun no tiene una ventana principal configurada."
        )
    }
  }

  private def configurarCerrarSesion(panel: JFrame, controles: Any*): Unit = {
    controles.foreach {
      case boton: JButton =>
        boton.addActionListener(_ => cerrarSesion(panel))
      case item: javax.swing.JMenuItem =>
        item.addActionListener(_ => cerrarSesion(panel))
      case _ =>
    }
  }

  private def mostrarPanel(panel: JFrame): Unit = {
    vista.setVisible(false)
    panel.setLocationRelativeTo(null)
    panel.setVisible(true)
  }

  private def cerrarSesion(panel: JFrame): Unit = {
    panel.dispose()
    mostrarLogin()
  }

  private def mostrarLogin(): Unit = {
    vista.txtContrasenia.setText("")
    vista.setLocationRelativeTo(null)
    vista.setVisible(true)
  }
}
