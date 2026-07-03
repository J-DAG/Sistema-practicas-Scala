package controlador

import modelo.entidades.{Empresa, Oferta, Usuario}
import modelo.repositorios.{EmpresaRepositorio, OfertaRepositorio}
import vista.{VistaCoordinadorOfertas, VistaCrearOferta, VistaEditarOferta}

import java.sql.SQLException
import java.time.{LocalDate, ZoneId}
import java.util.Date
import javax.swing.{JComboBox, JOptionPane}
import javax.swing.table.DefaultTableModel

class ControlCoordinadorOfertas(usuarioSesion: Usuario, alInicio: () => Unit, alCerrarSesion: () => Unit) {
  private val vista = new VistaCoordinadorOfertas()
  private val ofertas = new OfertaRepositorio()
  private val empresas = new EmpresaRepositorio()

  vista.lblInformacion.setText(s"Bienvenido ${usuarioSesion.nombreCompleto}")

  vista.btnInicio.addActionListener(_ => volverInicio())
  vista.btnOfertas.addActionListener(_ => cargarDatos())
  vista.btnBuscar.addActionListener(_ => cargarDatos())
  vista.txtBuscar.addActionListener(_ => cargarDatos())
  vista.btnNuevaOferta.addActionListener(_ => nuevaOferta())
  vista.btnEditar.addActionListener(_ => editarOferta())
  vista.btnEliminar.addActionListener(_ => eliminarOferta())
  vista.btnCerrarSesion.addActionListener(_ => cerrarSesion())

  vista.btnEmpresa.addActionListener(_ => abrirEmpresas())
  vista.btnEstudiantes.addActionListener(_ => abrirEstudiantes())
  vista.btnTutores.addActionListener(_ => abrirTutores())
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
      llenarTabla(ofertas.listar(vista.txtBuscar.getText))
    } catch {
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudo cargar ofertas.\n\nDetalle: ${error.getMessage}")
    }
  }

  private def llenarTabla(datos: List[Oferta]): Unit = {
    val modelo = new DefaultTableModel(Array[AnyRef]("ID", "Empresa", "Titulo", "Descripcion", "Area", "Cupos", "Publicacion", "Cierre", "Estado"), 0) {
      override def isCellEditable(row: Int, column: Int): Boolean = false
    }

    datos.foreach { oferta =>
      modelo.addRow(Array[AnyRef](
        Long.box(oferta.idOferta),
        oferta.empresa.getOrElse(""),
        oferta.titulo,
        oferta.descripcion,
        oferta.area,
        Int.box(oferta.cupos),
        oferta.fechaPublicacion.toString,
        oferta.fechaCierre.toString,
        oferta.estado
      ))
    }

    vista.tblOfertas.setModel(modelo)
  }

  private def nuevaOferta(): Unit = {
    val formulario = new VistaCrearOferta()
    val listaEmpresas = cargarEmpresas(formulario.cbxListaEmpresas)
    formulario.sbxNumCupos.setValue(1)
    formulario.btnCancelar.addActionListener(_ => formulario.dispose())
    formulario.btnGuardar.addActionListener(_ => {
      val idEmpresa = listaEmpresas.lift(formulario.cbxListaEmpresas.getSelectedIndex).map(_.idEmpresa)
      leerOferta(
        idOferta = 0L,
        idEmpresa = idEmpresa,
        titulo = formulario.txtTitulo.getText,
        descripcion = formulario.txtDescripcion.getText,
        area = formulario.txtArea.getText,
        cupos = formulario.sbxNumCupos.getValue.asInstanceOf[Int],
        fechaCierre = fechaSpinner(formulario.dtFecha.getValue),
        fechaPublicacion = LocalDate.now(),
        estado = "abierta"
      ).foreach { oferta =>
        try {
          ofertas.crear(oferta)
          formulario.dispose()
          cargarDatos()
        } catch {
          case error: SQLException =>
            JOptionPane.showMessageDialog(formulario, s"No se pudo registrar la oferta.\n\nDetalle: ${error.getMessage}")
          case error: Exception =>
            JOptionPane.showMessageDialog(formulario, s"No se pudo registrar la oferta.\n\nDetalle: ${error.getMessage}")
        }
      }
    })
    formulario.setLocationRelativeTo(vista)
    formulario.setVisible(true)
  }

  private def editarOferta(): Unit = {
    val fila = vista.tblOfertas.getSelectedRow
    if (fila < 0) {
      JOptionPane.showMessageDialog(vista, "Seleccione una oferta para editar.")
      return
    }

    val idOferta = vista.tblOfertas.getValueAt(fila, 0).toString.toLong
    ofertas.buscarPorId(idOferta) match {
      case Some(oferta) =>
        val formulario = new VistaEditarOferta()
        val listaEmpresas = cargarEmpresas(formulario.cbxListaEmpresas)
        val indiceEmpresa = listaEmpresas.indexWhere(_.idEmpresa == oferta.idEmpresa)
        if (indiceEmpresa >= 0) formulario.cbxListaEmpresas.setSelectedIndex(indiceEmpresa)
        formulario.sbxNumCupos.setValue(oferta.cupos)
        formulario.txtTitulo.setText(oferta.titulo)
        formulario.txtDescripcion.setText(oferta.descripcion)
        formulario.txtArea.setText(oferta.area)
        formulario.establecerFecha(Date.from(oferta.fechaCierre.atStartOfDay(ZoneId.systemDefault()).toInstant))
        formulario.btnCancelar.addActionListener(_ => formulario.dispose())
        formulario.btnGuardar.addActionListener(_ => {
          val idEmpresa = listaEmpresas.lift(formulario.cbxListaEmpresas.getSelectedIndex).map(_.idEmpresa)
          leerOferta(
            idOferta = oferta.idOferta,
            idEmpresa = idEmpresa,
            titulo = formulario.txtTitulo.getText,
            descripcion = formulario.txtDescripcion.getText,
            area = formulario.txtArea.getText,
            cupos = formulario.sbxNumCupos.getValue.asInstanceOf[Int],
            fechaCierre = fechaSpinner(formulario.dtFecha.getValue),
            fechaPublicacion = oferta.fechaPublicacion,
            estado = estadoSegunFecha(fechaSpinner(formulario.dtFecha.getValue), oferta.estado)
          ).foreach { actualizada =>
            try {
              ofertas.actualizar(actualizada)
              formulario.dispose()
              cargarDatos()
            } catch {
              case error: SQLException =>
                JOptionPane.showMessageDialog(formulario, s"No se pudo actualizar la oferta.\n\nDetalle: ${error.getMessage}")
              case error: Exception =>
                JOptionPane.showMessageDialog(formulario, s"No se pudo actualizar la oferta.\n\nDetalle: ${error.getMessage}")
            }
          }
        })
        formulario.setLocationRelativeTo(vista)
        formulario.setVisible(true)

      case None =>
        JOptionPane.showMessageDialog(vista, "No se encontro la oferta seleccionada.")
    }
  }

  private def eliminarOferta(): Unit = {
    val fila = vista.tblOfertas.getSelectedRow
    if (fila < 0) {
      JOptionPane.showMessageDialog(vista, "Seleccione una oferta para eliminar.")
      return
    }

    val idOferta = vista.tblOfertas.getValueAt(fila, 0).toString.toLong
    val respuesta = JOptionPane.showConfirmDialog(
      vista,
      "Si la oferta tiene postulaciones relacionadas, no podra eliminarse. Desea continuar?",
      "Confirmar eliminacion",
      JOptionPane.YES_NO_OPTION
    )

    if (respuesta == JOptionPane.YES_OPTION) {
      try {
        ofertas.eliminar(idOferta)
        cargarDatos()
      } catch {
        case error: SQLException =>
          JOptionPane.showMessageDialog(vista, s"No se puede eliminar porque la oferta tiene postulaciones relacionadas.\n\nDetalle: ${error.getMessage}")
        case error: Exception =>
          JOptionPane.showMessageDialog(vista, s"No se pudo eliminar la oferta.\n\nDetalle: ${error.getMessage}")
      }
    }
  }

  private def leerOferta(
      idOferta: Long,
      idEmpresa: Option[Long],
      titulo: String,
      descripcion: String,
      area: String,
      cupos: Int,
      fechaCierre: LocalDate,
      fechaPublicacion: LocalDate,
      estado: String
  ): Option[Oferta] = {
    val datos = List(titulo, descripcion, area).map(_.trim)
    if (idEmpresa.isEmpty) {
      JOptionPane.showMessageDialog(vista, "Seleccione una empresa activa.")
      None
    } else if (datos.exists(_.isEmpty)) {
      JOptionPane.showMessageDialog(vista, "Complete titulo, descripcion y area.")
      None
    } else if (cupos <= 0) {
      JOptionPane.showMessageDialog(vista, "El numero de cupos debe ser mayor a cero.")
      None
    } else if (fechaCierre.isBefore(LocalDate.now())) {
      JOptionPane.showMessageDialog(vista, "La fecha de cierre no puede estar en el pasado.")
      None
    } else {
      Some(Oferta(
        idOferta = idOferta,
        idEmpresa = idEmpresa.get,
        empresa = None,
        titulo = datos(0),
        descripcion = datos(1),
        requisitos = datos(1),
        area = datos(2),
        cupos = cupos,
        fechaPublicacion = fechaPublicacion,
        fechaCierre = fechaCierre,
        estado = estado
      ))
    }
  }

  private def cargarEmpresas(combo: JComboBox[String]): List[Empresa] = {
    val lista = empresas.listar().filter(_.activa)
    combo.removeAllItems()
    lista.foreach(empresa => combo.addItem(s"${empresa.idEmpresa} - ${empresa.nombre}"))
    lista
  }

  private def fechaSpinner(valor: Any): LocalDate =
    valor.asInstanceOf[Date].toInstant.atZone(ZoneId.systemDefault()).toLocalDate

  private def estadoSegunFecha(fechaCierre: LocalDate, estadoActual: String): String =
    if (fechaCierre.isBefore(LocalDate.now())) "cerrada" else estadoActual

  private def abrirEmpresas(): Unit = {
    vista.dispose()
    new ControlCoordinadorEmpresas(usuarioSesion, alInicio, alCerrarSesion).mostrar()
  }

  private def abrirTutores(): Unit = {
    vista.dispose()
    new ControlCoordinadorTutores(usuarioSesion, alInicio, alCerrarSesion).mostrar()
  }

  private def abrirEstudiantes(): Unit = {
    vista.dispose()
    new ControlCoordinadorEstudiantes(usuarioSesion, alInicio, alCerrarSesion).mostrar()
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
