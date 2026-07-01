package controlador

import modelo.entidades.Usuario
import modelo.repositorios.{EmpresaRepositorio, UsuarioRepositorio}
import vista.{VistaAdminEmpresa, VistaAdminUsuario, VistaMainAdmin}

import javax.swing.JOptionPane
import javax.swing.table.DefaultTableModel

class ControlVentanaAdmin(usuario: Usuario, alCerrarSesion: () => Unit) {
  private val vista = new VistaMainAdmin()
  private val usuarios = new UsuarioRepositorio()
  private val empresas = new EmpresaRepositorio()

  vista.lblInformacion.setText(s"Bienvenido ${usuario.nombreCompleto}")

  vista.btnUsuarios.addActionListener(_ => abrirUsuarios())
  vista.mniUsuarios.addActionListener(_ => abrirUsuarios())
  vista.btnEmpresa.addActionListener(_ => abrirEmpresas())
  vista.mniEmpresa.addActionListener(_ => abrirEmpresas())
  vista.btnCerrarSesion.addActionListener(_ => cerrarSesion())
  vista.btnCerrarSesion_1.addActionListener(_ => cerrarSesion())
  vista.mniCerrarSesion.addActionListener(_ => cerrarSesion())
  vista.btnAcercaDe.addActionListener(_ => mostrarAcercaDe())
  vista.mniAcercaDe.addActionListener(_ => mostrarAcercaDe())

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
      vista.lblNumCoordinadores.setText(formatear(usuarios.contarPorRol("coordinador")))
      vista.lblNumAdministradores.setText(formatear(usuarios.contarPorRol("administrador")))
      vista.lblNumEmpresas.setText(formatear(empresas.contarActivas()))
      llenarTablaUsuarios(usuarios.listar().take(20))
    } catch {
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudo cargar el panel.\n\nDetalle: ${error.getMessage}")
    }
  }

  private def llenarTablaUsuarios(datos: List[Usuario]): Unit = {
    val modelo = new DefaultTableModel(Array[AnyRef]("ID", "Nombres", "Apellidos", "Cedula", "Email", "Rol", "Activo"), 0) {
      override def isCellEditable(row: Int, column: Int): Boolean = false
    }

    datos.foreach { usuario =>
      modelo.addRow(Array[AnyRef](
        Long.box(usuario.idUsuario),
        usuario.nombres,
        usuario.apellidos,
        usuario.cedula,
        usuario.email,
        usuario.rol,
        Boolean.box(usuario.activo)
      ))
    }

    vista.tblUsuarios.setModel(modelo)
  }

  private def abrirUsuarios(): Unit = {
    vista.dispose()
    new ControlAdminUsuarios(usuario, () => new ControlVentanaAdmin(usuario, alCerrarSesion).mostrar(), alCerrarSesion).mostrar()
  }

  private def abrirEmpresas(): Unit = {
    vista.dispose()
    new ControlAdminEmpresas(usuario, () => new ControlVentanaAdmin(usuario, alCerrarSesion).mostrar(), alCerrarSesion).mostrar()
  }

  private def mostrarAcercaDe(): Unit =
    JOptionPane.showMessageDialog(vista, "Sistema de gestion de practicas preprofesionales.")

  private def cerrarSesion(): Unit = {
    vista.dispose()
    alCerrarSesion()
  }

  private def formatear(valor: Int): String = f"$valor%02d"
}
