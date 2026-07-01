package controlador

import modelo.entidades.{TutorResumen, Usuario, UsuarioFactory}
import modelo.repositorios.{EmpresaRepositorio, UsuarioRepositorio}
import modelo.validaciones.CedulaEcuador
import vista.{
  VistaCoordinadorTutores,
  VistaCrearCuentaTutorAcademico,
  VistaCrearCuentaTutorEmpresarial,
  VistaEditarCuentaTutorAcademico,
  VistaEditarCuentaTutorEmpresarial
}

import java.sql.SQLException
import javax.swing.{JComboBox, JOptionPane}
import javax.swing.table.DefaultTableModel

class ControlCoordinadorTutores(usuarioSesion: Usuario, alInicio: () => Unit, alCerrarSesion: () => Unit) {
  private val vista = new VistaCoordinadorTutores()
  private val usuarios = new UsuarioRepositorio()
  private val empresas = new EmpresaRepositorio()
  private val carreras = Array("Software", "Tecnologias de la informacion", "Sistemas", "Administracion", "Contabilidad")

  vista.lblInformacion.setText(s"Bienvenido ${usuarioSesion.nombreCompleto}")

  vista.btnInicio.addActionListener(_ => volverInicio())
  vista.btnTutores.addActionListener(_ => cargarDatos())
  vista.btnBuscar.addActionListener(_ => cargarDatos())
  vista.txtBuscar.addActionListener(_ => cargarDatos())
  vista.btnNuevoTA.addActionListener(_ => nuevoTutorAcademico())
  vista.btnNuevoTE.addActionListener(_ => nuevoTutorEmpresarial())
  vista.btnEditar.addActionListener(_ => editarTutor())
  vista.btnEliminar.addActionListener(_ => eliminarTutor())
  vista.btnCerrarSesion.addActionListener(_ => cerrarSesion())

  vista.btnEmpresa.addActionListener(_ => abrirEmpresas())
  vista.btnEstudiantes.addActionListener(_ => abrirEstudiantes())
  vista.btnOfertas.addActionListener(_ => abrirOfertas())
  vista.btnPracticas.addActionListener(_ => moduloPendiente("Practicas"))
  vista.btnPostulaciones.addActionListener(_ => abrirPostulaciones())
  vista.btnReportes.addActionListener(_ => moduloPendiente("Reportes"))

  def mostrar(): Unit = {
    cargarDatos()
    vista.setLocationRelativeTo(null)
    vista.setVisible(true)
  }

  private def cargarDatos(): Unit = {
    try {
      llenarTabla(usuarios.listarTutores(vista.txtBuscar.getText))
    } catch {
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudo cargar tutores.\n\nDetalle: ${error.getMessage}")
    }
  }

  private def llenarTabla(datos: List[TutorResumen]): Unit = {
    val modelo = new DefaultTableModel(Array[AnyRef]("ID", "Nombres", "Apellidos", "Cedula", "Email", "Rol", "Carrera", "Empresa", "Cargo", "Activo"), 0) {
      override def isCellEditable(row: Int, column: Int): Boolean = false
    }

    datos.foreach { tutor =>
      modelo.addRow(Array[AnyRef](
        Long.box(tutor.idUsuario),
        tutor.nombres,
        tutor.apellidos,
        tutor.cedula,
        tutor.email,
        tutor.rol,
        tutor.carrera.getOrElse(""),
        tutor.empresa.getOrElse(""),
        tutor.cargo.getOrElse(""),
        Boolean.box(tutor.activo)
      ))
    }

    vista.tblTutores.setModel(modelo)
  }

  private def nuevoTutorAcademico(): Unit = {
    val formulario = new VistaCrearCuentaTutorAcademico()
    cargarCarreras(formulario.cbxCarrera)
    formulario.btnCancelar.addActionListener(_ => formulario.dispose())
    formulario.btnGuardar.addActionListener(_ => {
      leerTutorAcademico(
        idUsuario = 0L,
        nombres = formulario.txtNombres.getText,
        apellidos = formulario.txtApellidos.getText,
        email = formulario.txtCorreoElectronico.getText,
        cedula = formulario.txtCedula.getText,
        carrera = Option(formulario.cbxCarrera.getSelectedItem).map(_.toString),
        password = new String(formulario.txtContrasenia.getPassword),
        confirmar = new String(formulario.txtConfirmarContrasenia.getPassword),
        passwordActual = ""
      ).foreach(usuario => guardarNuevo(formulario, usuario))
    })
    formulario.setLocationRelativeTo(vista)
    formulario.setVisible(true)
  }

  private def nuevoTutorEmpresarial(): Unit = {
    val formulario = new VistaCrearCuentaTutorEmpresarial()
    val listaEmpresas = cargarEmpresas(formulario.cbxListaEmpresa)
    formulario.btnCancelar.addActionListener(_ => formulario.dispose())
    formulario.btnGuardar.addActionListener(_ => {
      val idEmpresa = listaEmpresas.lift(formulario.cbxListaEmpresa.getSelectedIndex).map(_.idEmpresa)
      leerTutorEmpresarial(
        idUsuario = 0L,
        nombres = formulario.txtNombres.getText,
        apellidos = formulario.txtApellidos.getText,
        email = formulario.txtCorreoElectronico.getText,
        cedula = formulario.txtCedula.getText,
        idEmpresa = idEmpresa,
        cargo = formulario.txtCargo.getText,
        password = new String(formulario.txtContrasenia.getPassword),
        confirmar = new String(formulario.txtConfirmarContrasenia.getPassword),
        passwordActual = ""
      ).foreach(usuario => guardarNuevo(formulario, usuario))
    })
    formulario.setLocationRelativeTo(vista)
    formulario.setVisible(true)
  }

  private def editarTutor(): Unit = {
    val fila = vista.tblTutores.getSelectedRow
    if (fila < 0) {
      JOptionPane.showMessageDialog(vista, "Seleccione un tutor para editar.")
      return
    }

    val idUsuario = vista.tblTutores.getValueAt(fila, 0).toString.toLong
    usuarios.buscarPorId(idUsuario) match {
      case Some(usuario) if usuario.rol == "tutor_academico" => editarTutorAcademico(usuario)
      case Some(usuario) if usuario.rol == "tutor_empresarial" => editarTutorEmpresarial(usuario)
      case Some(_) => JOptionPane.showMessageDialog(vista, "El usuario seleccionado no es un tutor.")
      case None => JOptionPane.showMessageDialog(vista, "No se encontro el tutor seleccionado.")
    }
  }

  private def editarTutorAcademico(usuario: Usuario): Unit = {
    val formulario = new VistaEditarCuentaTutorAcademico()
    cargarCarreras(formulario.cbxCarrera)
    formulario.txtNombres.setText(usuario.nombres)
    formulario.txtApellidos.setText(usuario.apellidos)
    formulario.txtCorreoElectronico.setText(usuario.email)
    formulario.txtCedula.setText(usuario.cedula)
    usuario.carrera.foreach(formulario.cbxCarrera.setSelectedItem)
    formulario.btnCancelar.addActionListener(_ => formulario.dispose())
    formulario.btnGuardar.addActionListener(_ => {
      val password = new String(formulario.txtContrasenia.getPassword)
      leerTutorAcademico(
        idUsuario = usuario.idUsuario,
        nombres = formulario.txtNombres.getText,
        apellidos = formulario.txtApellidos.getText,
        email = formulario.txtCorreoElectronico.getText,
        cedula = formulario.txtCedula.getText,
        carrera = Option(formulario.cbxCarrera.getSelectedItem).map(_.toString),
        password = password,
        confirmar = new String(formulario.txtConfirmarContrasenia.getPassword),
        passwordActual = usuario.passwordHash
      ).foreach(actualizado => guardarEdicion(formulario, actualizado, password.nonEmpty))
    })
    formulario.setLocationRelativeTo(vista)
    formulario.setVisible(true)
  }

  private def editarTutorEmpresarial(usuario: Usuario): Unit = {
    val formulario = new VistaEditarCuentaTutorEmpresarial()
    val listaEmpresas = cargarEmpresas(formulario.cbxListaEmpresa)
    formulario.txtNombres.setText(usuario.nombres)
    formulario.txtApellidos.setText(usuario.apellidos)
    formulario.txtCorreoElectronico.setText(usuario.email)
    formulario.txtCedula.setText(usuario.cedula)
    formulario.txtCargo.setText(usuario.cargo.getOrElse(""))
    usuario.idEmpresa.foreach { idEmpresa =>
      val indice = listaEmpresas.indexWhere(_.idEmpresa == idEmpresa)
      if (indice >= 0) formulario.cbxListaEmpresa.setSelectedIndex(indice)
    }
    formulario.btnCancelar.addActionListener(_ => formulario.dispose())
    formulario.btnGuardar.addActionListener(_ => {
      val password = new String(formulario.txtContrasenia.getPassword)
      val idEmpresa = listaEmpresas.lift(formulario.cbxListaEmpresa.getSelectedIndex).map(_.idEmpresa)
      leerTutorEmpresarial(
        idUsuario = usuario.idUsuario,
        nombres = formulario.txtNombres.getText,
        apellidos = formulario.txtApellidos.getText,
        email = formulario.txtCorreoElectronico.getText,
        cedula = formulario.txtCedula.getText,
        idEmpresa = idEmpresa,
        cargo = formulario.txtCargo.getText,
        password = password,
        confirmar = new String(formulario.txtConfirmarContrasenia.getPassword),
        passwordActual = usuario.passwordHash
      ).foreach(actualizado => guardarEdicion(formulario, actualizado, password.nonEmpty))
    })
    formulario.setLocationRelativeTo(vista)
    formulario.setVisible(true)
  }

  private def eliminarTutor(): Unit = {
    val fila = vista.tblTutores.getSelectedRow
    if (fila < 0) {
      JOptionPane.showMessageDialog(vista, "Seleccione un tutor para eliminar.")
      return
    }

    val idUsuario = vista.tblTutores.getValueAt(fila, 0).toString.toLong
    val respuesta = JOptionPane.showConfirmDialog(
      vista,
      "Si el tutor tiene practicas relacionadas, debera reasignarse antes de eliminar. Desea continuar?",
      "Confirmar eliminacion",
      JOptionPane.YES_NO_OPTION
    )

    if (respuesta == JOptionPane.YES_OPTION) {
      try {
        usuarios.eliminar(idUsuario)
        cargarDatos()
      } catch {
        case error: SQLException =>
          JOptionPane.showMessageDialog(vista, s"No se puede eliminar el tutor porque tiene registros relacionados. Primero debe reasignarse.\n\nDetalle: ${error.getMessage}")
        case error: Exception =>
          JOptionPane.showMessageDialog(vista, s"No se pudo eliminar el tutor.\n\nDetalle: ${error.getMessage}")
      }
    }
  }

  private def leerTutorAcademico(
      idUsuario: Long,
      nombres: String,
      apellidos: String,
      email: String,
      cedula: String,
      carrera: Option[String],
      password: String,
      confirmar: String,
      passwordActual: String
  ): Option[Usuario] =
    leerUsuarioBase(idUsuario, nombres, apellidos, email, cedula, "tutor_academico", password, confirmar, passwordActual)
      .map(usuario => UsuarioFactory.especializar(usuario.copy(carrera = carrera)))

  private def leerTutorEmpresarial(
      idUsuario: Long,
      nombres: String,
      apellidos: String,
      email: String,
      cedula: String,
      idEmpresa: Option[Long],
      cargo: String,
      password: String,
      confirmar: String,
      passwordActual: String
  ): Option[Usuario] = {
    if (idEmpresa.isEmpty) {
      JOptionPane.showMessageDialog(vista, "Debe seleccionar una empresa.")
      None
    } else if (cargo.trim.isEmpty) {
      JOptionPane.showMessageDialog(vista, "Ingrese el cargo del tutor empresarial.")
      None
    } else {
      leerUsuarioBase(idUsuario, nombres, apellidos, email, cedula, "tutor_empresarial", password, confirmar, passwordActual)
        .map(usuario => UsuarioFactory.especializar(usuario.copy(idEmpresa = idEmpresa, cargo = Some(cargo.trim))))
    }
  }

  private def leerUsuarioBase(
      idUsuario: Long,
      nombres: String,
      apellidos: String,
      email: String,
      cedula: String,
      rol: String,
      password: String,
      confirmar: String,
      passwordActual: String
  ): Option[Usuario] = {
    val datos = List(nombres, apellidos, email, cedula).map(_.trim)
    if (datos.exists(_.isEmpty)) {
      JOptionPane.showMessageDialog(vista, "Complete todos los campos obligatorios.")
      None
    } else if (!datos(2).contains("@")) {
      JOptionPane.showMessageDialog(vista, "Ingrese un correo electronico valido.")
      None
    } else if (!CedulaEcuador.esValida(datos(3))) {
      JOptionPane.showMessageDialog(vista, "Ingrese una cedula ecuatoriana valida.")
      None
    } else if (idUsuario == 0L && password.isEmpty) {
      JOptionPane.showMessageDialog(vista, "Ingrese una contrasenia.")
      None
    } else if (password != confirmar) {
      JOptionPane.showMessageDialog(vista, "Las contrasenias no coinciden.")
      None
    } else {
      Some(UsuarioFactory.crear(
        idUsuario = idUsuario,
        nombres = datos(0),
        apellidos = datos(1),
        cedula = datos(3),
        email = datos(2),
        passwordHash = if (password.nonEmpty) password else passwordActual,
        rol = rol,
        activo = true
      ))
    }
  }

  private def guardarNuevo(ventana: java.awt.Window, usuario: Usuario): Unit = {
    try {
      usuarios.crear(usuario)
      ventana.dispose()
      cargarDatos()
    } catch {
      case error: SQLException =>
        JOptionPane.showMessageDialog(vista, s"No se pudo crear el tutor. Revise cedula o correo duplicado.\n\nDetalle: ${error.getMessage}")
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudo crear el tutor.\n\nDetalle: ${error.getMessage}")
    }
  }

  private def guardarEdicion(ventana: java.awt.Window, usuario: Usuario, cambiarPassword: Boolean): Unit = {
    try {
      usuarios.actualizar(usuario, cambiarPassword)
      ventana.dispose()
      cargarDatos()
    } catch {
      case error: SQLException =>
        JOptionPane.showMessageDialog(vista, s"No se pudo actualizar el tutor. Revise cedula o correo duplicado.\n\nDetalle: ${error.getMessage}")
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudo actualizar el tutor.\n\nDetalle: ${error.getMessage}")
    }
  }

  private def cargarCarreras(combo: JComboBox[String]): Unit = {
    combo.removeAllItems()
    carreras.foreach(combo.addItem)
  }

  private def cargarEmpresas(combo: JComboBox[String]) = {
    val lista = empresas.listar().filter(_.activa)
    combo.removeAllItems()
    lista.foreach(empresa => combo.addItem(s"${empresa.idEmpresa} - ${empresa.nombre}"))
    lista
  }

  private def abrirEmpresas(): Unit = {
    vista.dispose()
    new ControlCoordinadorEmpresas(usuarioSesion, alInicio, alCerrarSesion).mostrar()
  }

  private def abrirEstudiantes(): Unit = {
    vista.dispose()
    new ControlCoordinadorEstudiantes(usuarioSesion, alInicio, alCerrarSesion).mostrar()
  }

  private def abrirOfertas(): Unit = {
    vista.dispose()
    new ControlCoordinadorOfertas(usuarioSesion, alInicio, alCerrarSesion).mostrar()
  }

  private def abrirPostulaciones(): Unit = {
    vista.dispose()
    new ControlCoordinadorPostulaciones(usuarioSesion, alInicio, alCerrarSesion).mostrar()
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
