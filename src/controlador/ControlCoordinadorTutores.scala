package controlador

import controlador.util.ModelosTabla
import modelo.entidades.{TutorResumen, Usuario}
import modelo.repositorios.{EmpresaRepositorio, UsuarioRepositorio}
import modelo.servicios.{DatosUsuarioFormulario, UsuarioFormularioServicio}
import vista.{
  VistaCoordinadorTutores,
  VistaCrearCuentaTutorAcademico,
  VistaCrearCuentaTutorEmpresarial,
  VistaEditarCuentaTutorAcademico,
  VistaEditarCuentaTutorEmpresarial
}

import java.sql.SQLException
import javax.swing.{JComboBox, JOptionPane}

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
      llenarTabla(usuarios.listarTutores(vista.txtBuscar.getText))
    } catch {
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudo cargar tutores.\n\nDetalle: ${error.getMessage}")
    }
  }

  private def llenarTabla(datos: List[TutorResumen]): Unit =
    vista.tblTutores.setModel(ModelosTabla.tutores(datos))

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
    val tutor = usuarios.buscarPorId(idUsuario) match {
      case Some(usuario) => usuario
      case None =>
        JOptionPane.showMessageDialog(vista, "No se encontro el tutor seleccionado.")
        return
    }

    val idReemplazo = reemplazoSiAplica(tutor) match {
      case Some(resultado) => resultado
      case None => return
    }

    val respuesta = JOptionPane.showConfirmDialog(
      vista,
      "Esta accion eliminara el tutor de la base de datos. Desea continuar?",
      "Confirmar eliminacion",
      JOptionPane.YES_NO_OPTION
    )

    if (respuesta == JOptionPane.YES_OPTION) {
      try {
        usuarios.eliminarConReasignacion(idUsuario, idReemplazo)
        JOptionPane.showMessageDialog(vista, "Tutor eliminado correctamente.")
        cargarDatos()
      } catch {
        case error: SQLException =>
          JOptionPane.showMessageDialog(vista, s"No se puede eliminar el tutor porque tiene registros relacionados. Primero debe reasignarse.\n\nDetalle: ${error.getMessage}")
        case error: Exception =>
          JOptionPane.showMessageDialog(vista, s"No se pudo eliminar el tutor.\n\nDetalle: ${error.getMessage}")
      }
    }
  }

  private def reemplazoSiAplica(tutor: Usuario): Option[Option[Long]] = {
    if (!usuarios.requiereReasignacionTutor(tutor.idUsuario)) {
      return Some(None)
    }

    val candidatos = usuarios.candidatosReemplazoTutor(tutor.idUsuario)
    if (candidatos.isEmpty) {
      val tipo =
        if (tutor.rol.equalsIgnoreCase("tutor_academico")) "academico"
        else "empresarial de la misma empresa"
      JOptionPane.showMessageDialog(vista, s"No existe un tutor $tipo activo de reemplazo. Cree uno antes de eliminar.")
      return None
    }

    val opciones = candidatos.map(reemplazo => s"${reemplazo.idUsuario} - ${reemplazo.nombreCompleto}").toArray
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
      candidatos.lift(indice).map(reemplazo => Some(reemplazo.idUsuario))
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
    resultadoUsuario(UsuarioFormularioServicio.tutorAcademico(
      datosUsuario(idUsuario, nombres, apellidos, email, cedula, "tutor_academico", password, confirmar, passwordActual),
      carrera
    ))

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
  ): Option[Usuario] =
    resultadoUsuario(UsuarioFormularioServicio.tutorEmpresarial(
      datosUsuario(idUsuario, nombres, apellidos, email, cedula, "tutor_empresarial", password, confirmar, passwordActual),
      idEmpresa,
      cargo
    ))

  private def datosUsuario(
      idUsuario: Long,
      nombres: String,
      apellidos: String,
      email: String,
      cedula: String,
      rol: String,
      password: String,
      confirmar: String,
      passwordActual: String
  ): DatosUsuarioFormulario =
    DatosUsuarioFormulario(idUsuario, nombres, apellidos, email, cedula, rol, password, confirmar, passwordActual)

  private def resultadoUsuario(resultado: Either[String, Usuario]): Option[Usuario] =
    resultado.fold(
      mensaje => {
        JOptionPane.showMessageDialog(vista, mensaje)
        None
      },
      Some(_)
    )

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
