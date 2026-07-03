package controlador

import modelo.entidades.{Usuario, UsuarioFactory}
import modelo.repositorios.{EmpresaRepositorio, UsuarioRepositorio}
import modelo.validaciones.CedulaEcuador
import vista.{
  VistaAdminUsuario,
  VistaCrearCuentaAdministrador,
  VistaCrearCuentaCoordinador,
  VistaCrearCuentaEstudiante,
  VistaCrearCuentaTutorAcademico,
  VistaCrearCuentaTutorEmpresarial,
  VistaEditarCuentaAdministrador,
  VistaEditarCuentaCoordinador,
  VistaEditarCuentaEstudiante,
  VistaEditarCuentaTutorAcademico,
  VistaEditarCuentaTutorEmpresarial
}

import java.sql.SQLException
import javax.swing.{JComboBox, JOptionPane}
import javax.swing.table.DefaultTableModel

class ControlAdminUsuarios(usuarioSesion: Usuario, alInicio: () => Unit, alCerrarSesion: () => Unit) {
  private val vista = new VistaAdminUsuario()
  private val usuarios = new UsuarioRepositorio()
  private val empresas = new EmpresaRepositorio()
  private val carreras = Array("Software", "Tecnologias de la informacion", "Sistemas", "Administracion", "Contabilidad")
  private val ciclos = (1 to 10).map(_.toString).toArray

  vista.btnInicio.addActionListener(_ => volverInicio())
  vista.btnEmpresa.addActionListener(_ => abrirEmpresas())
  vista.btnCerrarSesion.addActionListener(_ => cerrarSesion())
  vista.btnBuscar.addActionListener(_ => cargarDatos())
  vista.txtBuscar.addActionListener(_ => cargarDatos())
  vista.btnEliminar.addActionListener(_ => eliminarUsuario())
  vista.btnNuevoUsuario.addActionListener(_ => nuevoUsuario())
  vista.btnEditar.addActionListener(_ => editarUsuario())

  def mostrar(): Unit = {
    cargarDatos()
    vista.setLocationRelativeTo(null)
    vista.setVisible(true)
  }

  private def cargarDatos(): Unit = {
    try {
      llenarTabla(usuarios.listar(vista.txtBuscar.getText))
    } catch {
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudo cargar usuarios.\n\nDetalle: ${error.getMessage}")
    }
  }

  private def llenarTabla(datos: List[Usuario]): Unit = {
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

  private def eliminarUsuario(): Unit = {
    val fila = vista.tblUsuarios.getSelectedRow
    if (fila < 0) {
      JOptionPane.showMessageDialog(vista, "Seleccione un usuario para eliminar.")
      return
    }

    val idUsuario = vista.tblUsuarios.getValueAt(fila, 0).toString.toLong
    if (idUsuario == usuarioSesion.idUsuario) {
      JOptionPane.showMessageDialog(vista, "No puede eliminar el usuario con la sesion iniciada.")
      return
    }

    val usuarioEliminar = usuarios.buscarPorId(idUsuario) match {
      case Some(usuario) => usuario
      case None =>
        JOptionPane.showMessageDialog(vista, "No se encontro el usuario seleccionado.")
        return
    }

    val idReemplazo = reemplazoSiAplica(usuarioEliminar) match {
      case Some(resultado) => resultado
      case None => return
    }

    val mensaje =
      if (usuarioEliminar.rol.equalsIgnoreCase("estudiante")) {
        "Esta accion eliminara el estudiante y sus postulaciones, practicas y actividades relacionadas. Desea continuar?"
      } else {
        "Esta accion eliminara el usuario de la base de datos. Desea continuar?"
      }

    val respuesta = JOptionPane.showConfirmDialog(
      vista,
      mensaje,
      "Confirmar eliminacion",
      JOptionPane.YES_NO_OPTION
    )

    if (respuesta == JOptionPane.YES_OPTION) {
      try {
        usuarios.eliminarConReasignacion(idUsuario, idReemplazo)
        JOptionPane.showMessageDialog(vista, "Usuario eliminado correctamente.")
        cargarDatos()
      } catch {
        case error: SQLException =>
          JOptionPane.showMessageDialog(vista, s"No se puede eliminar porque el usuario esta relacionado con otros registros.\n\nDetalle: ${error.getMessage}")
        case error: Exception =>
          JOptionPane.showMessageDialog(vista, s"No se pudo eliminar el usuario.\n\nDetalle: ${error.getMessage}")
      }
    }
  }

  private def reemplazoSiAplica(usuario: Usuario): Option[Option[Long]] = {
    val rol = usuario.rol.trim.toLowerCase
    val esTutor = rol == "tutor_academico" || rol == "tutor_empresarial"
    if (!esTutor || !usuarios.requiereReasignacionTutor(usuario.idUsuario)) {
      return Some(None)
    }

    val candidatos = usuarios.candidatosReemplazoTutor(usuario.idUsuario)
    if (candidatos.isEmpty) {
      val tipo = if (rol == "tutor_academico") "academico" else "empresarial de la misma empresa"
      JOptionPane.showMessageDialog(vista, s"No existe un tutor $tipo activo de reemplazo. Cree uno antes de eliminar.")
      return None
    }

    val opciones = candidatos.map(tutor => s"${tutor.idUsuario} - ${tutor.nombreCompleto}").toArray
    val seleccion = JOptionPane.showInputDialog(
      vista,
      "El tutor tiene practicas relacionadas. Seleccione el tutor de reemplazo:",
      "Reasignar tutor",
      JOptionPane.QUESTION_MESSAGE,
      null,
      opciones.asInstanceOf[Array[AnyRef]],
      opciones.head
    )

    if (seleccion == null) {
      None
    } else {
      val indice = opciones.indexOf(seleccion.toString)
      candidatos.lift(indice).map(tutor => Some(tutor.idUsuario))
    }
  }

  private def nuevoUsuario(): Unit = {
    val opciones = Array("Estudiante", "Tutor academico", "Tutor empresarial", "Coordinador", "Administrador")
    val seleccion = JOptionPane.showInputDialog(
      vista,
      "Seleccione el tipo de usuario:",
      "Nuevo usuario",
      JOptionPane.QUESTION_MESSAGE,
      null,
      opciones.asInstanceOf[Array[AnyRef]],
      opciones.head
    )

    seleccion match {
      case "Estudiante" => abrirCrearEstudiante()
      case "Tutor academico" => abrirCrearTutorAcademico()
      case "Tutor empresarial" => abrirCrearTutorEmpresarial()
      case "Coordinador" => abrirCrearCoordinador()
      case "Administrador" => abrirCrearAdministrador()
      case _ =>
    }
  }

  private def editarUsuario(): Unit = {
    val fila = vista.tblUsuarios.getSelectedRow
    if (fila < 0) {
      JOptionPane.showMessageDialog(vista, "Seleccione un usuario para editar.")
      return
    }

    val idUsuario = vista.tblUsuarios.getValueAt(fila, 0).toString.toLong
    usuarios.buscarPorId(idUsuario) match {
      case Some(usuario) =>
        usuario.rol.trim.toLowerCase match {
          case "estudiante" => abrirEditarEstudiante(usuario)
          case "tutor_academico" | "tutor academico" | "ta" => abrirEditarTutorAcademico(usuario)
          case "tutor_empresarial" | "tutor empresarial" | "te" => abrirEditarTutorEmpresarial(usuario)
          case "coordinador" => abrirEditarCoordinador(usuario)
          case "administrador" | "admin" => abrirEditarAdministrador(usuario)
          case _ => JOptionPane.showMessageDialog(vista, s"No hay formulario configurado para el rol ${usuario.rol}.")
        }
      case None =>
        JOptionPane.showMessageDialog(vista, "No se encontro el usuario seleccionado.")
    }
  }

  private def abrirCrearEstudiante(): Unit = {
    val formulario = new VistaCrearCuentaEstudiante()
    cargarCarreras(formulario.cbxCarrera)
    cargarCiclos(formulario.cbsCicloActual)
    formulario.btnCancelar.addActionListener(_ => formulario.dispose())
    formulario.btnGuardar.addActionListener(_ => {
      leerEstudiante(
        idUsuario = 0L,
        nombres = formulario.txtNombres.getText,
        apellidos = formulario.txtApellidos.getText,
        email = formulario.txtCorreoElectronico.getText,
        cedula = formulario.txtCedula.getText,
        carrera = Option(formulario.cbxCarrera.getSelectedItem).map(_.toString),
        ciclo = Option(formulario.cbsCicloActual.getSelectedItem).map(_.toString.toInt),
        password = new String(formulario.txtContrasenia.getPassword),
        confirmar = new String(formulario.txtConfirmarContrasenia.getPassword),
        passwordActual = ""
      ).foreach { usuario =>
        guardarNuevo(formulario, usuario)
      }
    })
    formulario.setLocationRelativeTo(vista)
    formulario.setVisible(true)
  }

  private def abrirCrearTutorAcademico(): Unit = {
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
      ).foreach { usuario =>
        guardarNuevo(formulario, usuario)
      }
    })
    formulario.setLocationRelativeTo(vista)
    formulario.setVisible(true)
  }

  private def abrirCrearTutorEmpresarial(): Unit = {
    val formulario = new VistaCrearCuentaTutorEmpresarial()
    val listaEmpresas = cargarEmpresas(formulario.cbxListaEmpresa)
    formulario.btnCancelar.addActionListener(_ => formulario.dispose())
    formulario.btnGuardar.addActionListener(_ => {
      val indiceEmpresa = formulario.cbxListaEmpresa.getSelectedIndex
      val idEmpresa = listaEmpresas.lift(indiceEmpresa).map(_.idEmpresa)
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
      ).foreach { usuario =>
        guardarNuevo(formulario, usuario)
      }
    })
    formulario.setLocationRelativeTo(vista)
    formulario.setVisible(true)
  }

  private def abrirCrearCoordinador(): Unit = {
    val formulario = new VistaCrearCuentaCoordinador()
    formulario.btnCancelar.addActionListener(_ => formulario.dispose())
    formulario.btnGuardar.addActionListener(_ => {
      leerUsuarioBase(
        idUsuario = 0L,
        nombres = formulario.txtNombres.getText,
        apellidos = formulario.txtApellidos.getText,
        email = formulario.txtCorreoElectronico.getText,
        cedula = formulario.txtCedula.getText,
        rol = "coordinador",
        password = new String(formulario.txtContrasenia.getPassword),
        confirmar = new String(formulario.txtConfirmarContrasenia.getPassword),
        passwordActual = ""
      ).foreach(usuario => guardarNuevo(formulario, usuario))
    })
    formulario.setLocationRelativeTo(vista)
    formulario.setVisible(true)
  }

  private def abrirCrearAdministrador(): Unit = {
    val formulario = new VistaCrearCuentaAdministrador()
    formulario.btnCancelar.addActionListener(_ => formulario.dispose())
    formulario.btnGuardar.addActionListener(_ => {
      leerUsuarioBase(
        idUsuario = 0L,
        nombres = formulario.txtNombres.getText,
        apellidos = formulario.txtApellidos.getText,
        email = formulario.txtCorreoElectronico.getText,
        cedula = formulario.txtCedula.getText,
        rol = "administrador",
        password = new String(formulario.txtContrasenia.getPassword),
        confirmar = new String(formulario.txtConfirmarContrasenia.getPassword),
        passwordActual = ""
      ).foreach(usuario => guardarNuevo(formulario, usuario))
    })
    formulario.setLocationRelativeTo(vista)
    formulario.setVisible(true)
  }

  private def abrirEditarEstudiante(usuario: Usuario): Unit = {
    val formulario = new VistaEditarCuentaEstudiante()
    cargarCarreras(formulario.cbxCarrera)
    cargarCiclos(formulario.cbsCicloActual)
    formulario.txtNombres.setText(usuario.nombres)
    formulario.txtApellidos.setText(usuario.apellidos)
    formulario.txtCorreoElectronico.setText(usuario.email)
    formulario.txtCedula.setText(usuario.cedula)
    usuario.carrera.foreach(formulario.cbxCarrera.setSelectedItem)
    usuario.cicloActual.foreach(ciclo => formulario.cbsCicloActual.setSelectedItem(ciclo.toString))
    formulario.btnCancelar.addActionListener(_ => formulario.dispose())
    formulario.btnGuardar.addActionListener(_ => {
      leerEstudiante(
        idUsuario = usuario.idUsuario,
        nombres = formulario.txtNombres.getText,
        apellidos = formulario.txtApellidos.getText,
        email = formulario.txtCorreoElectronico.getText,
        cedula = formulario.txtCedula.getText,
        carrera = Option(formulario.cbxCarrera.getSelectedItem).map(_.toString),
        ciclo = Option(formulario.cbsCicloActual.getSelectedItem).map(_.toString.toInt),
        password = new String(formulario.txtContrasenia.getPassword),
        confirmar = new String(formulario.txtConfirmarContrasenia.getPassword),
        passwordActual = usuario.passwordHash
      ).foreach { actualizado =>
        guardarEdicion(formulario, actualizado, new String(formulario.txtContrasenia.getPassword).nonEmpty)
      }
    })
    formulario.setLocationRelativeTo(vista)
    formulario.setVisible(true)
  }

  private def abrirEditarTutorAcademico(usuario: Usuario): Unit = {
    val formulario = new VistaEditarCuentaTutorAcademico()
    cargarCarreras(formulario.cbxCarrera)
    formulario.txtNombres.setText(usuario.nombres)
    formulario.txtApellidos.setText(usuario.apellidos)
    formulario.txtCorreoElectronico.setText(usuario.email)
    formulario.txtCedula.setText(usuario.cedula)
    usuario.carrera.foreach(formulario.cbxCarrera.setSelectedItem)
    formulario.btnCancelar.addActionListener(_ => formulario.dispose())
    formulario.btnGuardar.addActionListener(_ => {
      leerTutorAcademico(
        idUsuario = usuario.idUsuario,
        nombres = formulario.txtNombres.getText,
        apellidos = formulario.txtApellidos.getText,
        email = formulario.txtCorreoElectronico.getText,
        cedula = formulario.txtCedula.getText,
        carrera = Option(formulario.cbxCarrera.getSelectedItem).map(_.toString),
        password = new String(formulario.txtContrasenia.getPassword),
        confirmar = new String(formulario.txtConfirmarContrasenia.getPassword),
        passwordActual = usuario.passwordHash
      ).foreach { actualizado =>
        guardarEdicion(formulario, actualizado, new String(formulario.txtContrasenia.getPassword).nonEmpty)
      }
    })
    formulario.setLocationRelativeTo(vista)
    formulario.setVisible(true)
  }

  private def abrirEditarTutorEmpresarial(usuario: Usuario): Unit = {
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
      val indiceEmpresa = formulario.cbxListaEmpresa.getSelectedIndex
      val idEmpresa = listaEmpresas.lift(indiceEmpresa).map(_.idEmpresa)
      leerTutorEmpresarial(
        idUsuario = usuario.idUsuario,
        nombres = formulario.txtNombres.getText,
        apellidos = formulario.txtApellidos.getText,
        email = formulario.txtCorreoElectronico.getText,
        cedula = formulario.txtCedula.getText,
        idEmpresa = idEmpresa,
        cargo = formulario.txtCargo.getText,
        password = new String(formulario.txtContrasenia.getPassword),
        confirmar = new String(formulario.txtConfirmarContrasenia.getPassword),
        passwordActual = usuario.passwordHash
      ).foreach { actualizado =>
        guardarEdicion(formulario, actualizado, new String(formulario.txtContrasenia.getPassword).nonEmpty)
      }
    })
    formulario.setLocationRelativeTo(vista)
    formulario.setVisible(true)
  }

  private def abrirEditarCoordinador(usuario: Usuario): Unit = {
    val formulario = new VistaEditarCuentaCoordinador()
    formulario.txtNombres.setText(usuario.nombres)
    formulario.txtApellidos.setText(usuario.apellidos)
    formulario.txtCorreoElectronico.setText(usuario.email)
    formulario.txtCedula.setText(usuario.cedula)
    formulario.btnCancelar.addActionListener(_ => formulario.dispose())
    formulario.btnGuardar.addActionListener(_ => {
      val password = new String(formulario.txtContrasenia.getPassword)
      leerUsuarioBase(
        idUsuario = usuario.idUsuario,
        nombres = formulario.txtNombres.getText,
        apellidos = formulario.txtApellidos.getText,
        email = formulario.txtCorreoElectronico.getText,
        cedula = formulario.txtCedula.getText,
        rol = "coordinador",
        password = password,
        confirmar = new String(formulario.txtConfirmarContrasenia.getPassword),
        passwordActual = usuario.passwordHash
      ).foreach(actualizado => guardarEdicion(formulario, actualizado, password.nonEmpty))
    })
    formulario.setLocationRelativeTo(vista)
    formulario.setVisible(true)
  }

  private def abrirEditarAdministrador(usuario: Usuario): Unit = {
    val formulario = new VistaEditarCuentaAdministrador()
    formulario.txtNombres.setText(usuario.nombres)
    formulario.txtApellidos.setText(usuario.apellidos)
    formulario.txtCorreoElectronico.setText(usuario.email)
    formulario.txtCedula.setText(usuario.cedula)
    formulario.btnCancelar.addActionListener(_ => formulario.dispose())
    formulario.btnGuardar.addActionListener(_ => {
      val password = new String(formulario.txtContrasenia.getPassword)
      leerUsuarioBase(
        idUsuario = usuario.idUsuario,
        nombres = formulario.txtNombres.getText,
        apellidos = formulario.txtApellidos.getText,
        email = formulario.txtCorreoElectronico.getText,
        cedula = formulario.txtCedula.getText,
        rol = "administrador",
        password = password,
        confirmar = new String(formulario.txtConfirmarContrasenia.getPassword),
        passwordActual = usuario.passwordHash
      ).foreach(actualizado => guardarEdicion(formulario, actualizado, password.nonEmpty))
    })
    formulario.setLocationRelativeTo(vista)
    formulario.setVisible(true)
  }

  private def leerEstudiante(
      idUsuario: Long,
      nombres: String,
      apellidos: String,
      email: String,
      cedula: String,
      carrera: Option[String],
      ciclo: Option[Int],
      password: String,
      confirmar: String,
      passwordActual: String
  ): Option[Usuario] =
    leerUsuarioBase(idUsuario, nombres, apellidos, email, cedula, "estudiante", password, confirmar, passwordActual)
      .map(usuario => UsuarioFactory.especializar(usuario.copy(carrera = carrera, cicloActual = ciclo)))

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
      JOptionPane.showMessageDialog(vista, "Debe seleccionar una empresa para el tutor empresarial.")
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
    } else if (!datos(2).contains("@") && rol != "administrador") {
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
      if (ventana != vista) ventana.dispose()
      cargarDatos()
    } catch {
      case error: SQLException =>
        JOptionPane.showMessageDialog(vista, s"No se pudo crear el usuario. Revise cedula o correo duplicado.\n\nDetalle: ${error.getMessage}")
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudo crear el usuario.\n\nDetalle: ${error.getMessage}")
    }
  }

  private def guardarEdicion(ventana: java.awt.Window, usuario: Usuario, cambiarPassword: Boolean): Unit = {
    try {
      usuarios.actualizar(usuario, cambiarPassword)
      if (ventana != vista) ventana.dispose()
      cargarDatos()
    } catch {
      case error: SQLException =>
        JOptionPane.showMessageDialog(vista, s"No se pudo actualizar el usuario. Revise cedula o correo duplicado.\n\nDetalle: ${error.getMessage}")
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudo actualizar el usuario.\n\nDetalle: ${error.getMessage}")
    }
  }

  private def cargarCarreras(combo: JComboBox[String]): Unit = {
    combo.removeAllItems()
    carreras.foreach(combo.addItem)
  }

  private def cargarCiclos(combo: JComboBox[String]): Unit = {
    combo.removeAllItems()
    ciclos.foreach(combo.addItem)
  }

  private def cargarEmpresas(combo: JComboBox[String]) = {
    val lista = empresas.listar().filter(_.activa)
    combo.removeAllItems()
    lista.foreach(empresa => combo.addItem(s"${empresa.idEmpresa} - ${empresa.nombre}"))
    lista
  }

  private def abrirEmpresas(): Unit = {
    vista.dispose()
    new ControlAdminEmpresas(usuarioSesion, alInicio, alCerrarSesion).mostrar()
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
