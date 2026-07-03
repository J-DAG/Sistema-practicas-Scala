package controlador

import modelo.entidades.{Empresa, Usuario}
import modelo.repositorios.EmpresaRepositorio
import vista.{VistaCoordinadorEmpresa, VistaCrearEmpresa, VistaEditarEmpresa}

import java.sql.SQLException
import javax.swing.{ButtonGroup, JOptionPane}
import javax.swing.table.DefaultTableModel

class ControlCoordinadorEmpresas(usuarioSesion: Usuario, alInicio: () => Unit, alCerrarSesion: () => Unit) {
  private val vista = new VistaCoordinadorEmpresa()
  private val empresas = new EmpresaRepositorio()

  vista.lblInformacion.setText(s"Bienvenido ${usuarioSesion.nombreCompleto}")

  vista.btnInicio.addActionListener(_ => volverInicio())
  vista.btnEmpresa.addActionListener(_ => cargarDatos())
  vista.btnCerrarSesion.addActionListener(_ => cerrarSesion())
  vista.btnBuscar.addActionListener(_ => cargarDatos())
  vista.txtBuscar.addActionListener(_ => cargarDatos())
  vista.btnNuevoEmpresa.addActionListener(_ => crearEmpresa())
  vista.btnEditar.addActionListener(_ => editarEmpresa())
  vista.btnEliminar.addActionListener(_ => eliminarEmpresa())

  vista.btnEstudiantes.addActionListener(_ => abrirEstudiantes())
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
      llenarTabla(empresas.listar(vista.txtBuscar.getText))
    } catch {
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudo cargar empresas.\n\nDetalle: ${error.getMessage}")
    }
  }

  private def llenarTabla(datos: List[Empresa]): Unit = {
    val modelo = new DefaultTableModel(Array[AnyRef]("ID", "Nombre", "Correo", "RUC", "Sector", "Ubicacion", "Convenio", "Activa"), 0) {
      override def isCellEditable(row: Int, column: Int): Boolean = false
    }

    datos.foreach { empresa =>
      modelo.addRow(Array[AnyRef](
        Long.box(empresa.idEmpresa),
        empresa.nombre,
        empresa.correo,
        empresa.ruc,
        empresa.sector,
        empresa.ubicacion,
        Boolean.box(empresa.tieneConvenio),
        Boolean.box(empresa.activa)
      ))
    }

    vista.tblEmpresas.setModel(modelo)
  }

  private def crearEmpresa(): Unit = {
    val formulario = new VistaCrearEmpresa()
    configurarConvenio(formulario.rbtSi, formulario.rbtNo)
    formulario.rbtNo.setSelected(true)
    formulario.btnCancelar.addActionListener(_ => formulario.dispose())
    formulario.btnGuardar.addActionListener(_ => {
      leerEmpresaCrear(formulario) match {
        case Some(empresa) =>
          try {
            empresas.crear(empresa)
            formulario.dispose()
            cargarDatos()
          } catch {
            case error: SQLException =>
              JOptionPane.showMessageDialog(formulario, s"No se pudo registrar la empresa. Revise RUC/correo duplicados.\n\nDetalle: ${error.getMessage}")
            case error: Exception =>
              JOptionPane.showMessageDialog(formulario, s"No se pudo registrar la empresa.\n\nDetalle: ${error.getMessage}")
          }
        case None =>
      }
    })
    formulario.setLocationRelativeTo(vista)
    formulario.setVisible(true)
  }

  private def editarEmpresa(): Unit = {
    val fila = vista.tblEmpresas.getSelectedRow
    if (fila < 0) {
      JOptionPane.showMessageDialog(vista, "Seleccione una empresa para editar.")
      return
    }

    val idEmpresa = vista.tblEmpresas.getValueAt(fila, 0).toString.toLong
    empresas.buscarPorId(idEmpresa) match {
      case Some(empresa) =>
        val formulario = new VistaEditarEmpresa()
        configurarConvenio(formulario.rbtSi, formulario.rbtNo)
        formulario.txtNombreEmpresa.setText(empresa.nombre)
        formulario.txtCorreoElectronico.setText(empresa.correo)
        formulario.txtRuc.setText(empresa.ruc)
        formulario.txtSector.setText(empresa.sector)
        formulario.txtUbicacion.setText(empresa.ubicacion)
        formulario.rbtSi.setSelected(empresa.tieneConvenio)
        formulario.rbtNo.setSelected(!empresa.tieneConvenio)
        formulario.btnCancelar.addActionListener(_ => formulario.dispose())
        formulario.btnGuardar.addActionListener(_ => {
          leerEmpresaEditar(formulario, empresa.idEmpresa) match {
            case Some(actualizada) =>
              try {
                empresas.actualizar(actualizada)
                formulario.dispose()
                cargarDatos()
              } catch {
                case error: SQLException =>
                  JOptionPane.showMessageDialog(formulario, s"No se pudo actualizar la empresa. Revise RUC/correo duplicados.\n\nDetalle: ${error.getMessage}")
                case error: Exception =>
                  JOptionPane.showMessageDialog(formulario, s"No se pudo actualizar la empresa.\n\nDetalle: ${error.getMessage}")
              }
            case None =>
          }
        })
        formulario.setLocationRelativeTo(vista)
        formulario.setVisible(true)

      case None =>
        JOptionPane.showMessageDialog(vista, "No se encontro la empresa seleccionada.")
    }
  }

  private def eliminarEmpresa(): Unit = {
    val fila = vista.tblEmpresas.getSelectedRow
    if (fila < 0) {
      JOptionPane.showMessageDialog(vista, "Seleccione una empresa para eliminar.")
      return
    }

    val idEmpresa = vista.tblEmpresas.getValueAt(fila, 0).toString.toLong
    val respuesta = JOptionPane.showConfirmDialog(
      vista,
      "Esta accion eliminara la empresa de la base de datos. Desea continuar?",
      "Confirmar eliminacion",
      JOptionPane.YES_NO_OPTION
    )

    if (respuesta == JOptionPane.YES_OPTION) {
      try {
        empresas.eliminar(idEmpresa)
        cargarDatos()
      } catch {
        case error: SQLException =>
          JOptionPane.showMessageDialog(vista, s"No se puede eliminar porque la empresa esta relacionada con tutores, ofertas o practicas.\n\nDetalle: ${error.getMessage}")
        case error: Exception =>
          JOptionPane.showMessageDialog(vista, s"No se pudo eliminar la empresa.\n\nDetalle: ${error.getMessage}")
      }
    }
  }

  private def configurarConvenio(si: javax.swing.JRadioButton, no: javax.swing.JRadioButton): Unit = {
    val grupo = new ButtonGroup()
    grupo.add(si)
    grupo.add(no)
  }

  private def leerEmpresaCrear(formulario: VistaCrearEmpresa): Option[Empresa] =
    leerCamposEmpresa(
      formulario.txtNombreEmpresa.getText,
      formulario.txtCorreoElectronico.getText,
      formulario.txtRuc.getText,
      formulario.txtSector.getText,
      formulario.txtUbicacion.getText,
      formulario.rbtSi.isSelected
    ).map(_.copy(idEmpresa = 0L))

  private def leerEmpresaEditar(formulario: VistaEditarEmpresa, idEmpresa: Long): Option[Empresa] =
    leerCamposEmpresa(
      formulario.txtNombreEmpresa.getText,
      formulario.txtCorreoElectronico.getText,
      formulario.txtRuc.getText,
      formulario.txtSector.getText,
      formulario.txtUbicacion.getText,
      formulario.rbtSi.isSelected
    ).map(_.copy(idEmpresa = idEmpresa))

  private def leerCamposEmpresa(
      nombre: String,
      correo: String,
      ruc: String,
      sector: String,
      ubicacion: String,
      tieneConvenio: Boolean
  ): Option[Empresa] = {
    val datos = List(nombre, correo, ruc, sector, ubicacion).map(_.trim)
    if (datos.exists(_.isEmpty)) {
      JOptionPane.showMessageDialog(vista, "Complete todos los campos de la empresa.")
      None
    } else if (!datos(1).contains("@")) {
      JOptionPane.showMessageDialog(vista, "Ingrese un correo electronico valido.")
      None
    } else if (datos(2).length != 13 || !datos(2).forall(_.isDigit)) {
      JOptionPane.showMessageDialog(vista, "El RUC debe tener 13 digitos.")
      None
    } else {
      Some(Empresa(
        idEmpresa = 0L,
        nombre = datos(0),
        correo = datos(1),
        ruc = datos(2),
        sector = datos(3),
        ubicacion = datos(4),
        tieneConvenio = tieneConvenio,
        activa = true
      ))
    }
  }

  private def volverInicio(): Unit = {
    vista.dispose()
    alInicio()
  }

  private def abrirTutores(): Unit = {
    vista.dispose()
    new ControlCoordinadorTutores(usuarioSesion, alInicio, alCerrarSesion).mostrar()
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

  private def cerrarSesion(): Unit = {
    vista.dispose()
    alCerrarSesion()
  }
}
