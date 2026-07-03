package controlador

import modelo.entidades.{Oferta, Usuario}
import modelo.repositorios.{OfertaRepositorio, PostulacionRepositorio, PracticaRepositorio}
import vista.{VistaAplicarPostulacion, VistaEstOfertas}

import java.nio.file.{Files, Path, StandardCopyOption}
import java.io.File
import javax.swing.{JFileChooser, JOptionPane}
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing.table.DefaultTableModel

class ControlEstudianteOfertas(usuario: Usuario, alInicio: () => Unit, alCerrarSesion: () => Unit) {
  private val vista = new VistaEstOfertas()
  private val ofertas = new OfertaRepositorio()
  private val postulaciones = new PostulacionRepositorio()
  private val practicas = new PracticaRepositorio()

  vista.btnInico.addActionListener(_ => volverInicio())
  vista.btnOfertaLaboral.addActionListener(_ => cargarDatos())
  vista.btnBuscar.addActionListener(_ => cargarDatos())
  vista.txtBuscar.addActionListener(_ => cargarDatos())
  vista.btnAplicarPostulacion.addActionListener(_ => aplicarPostulacion())
  vista.btnCancelarPostulacion.addActionListener(_ => cancelarPostulacionOferta())
  vista.btnMisPostulaciones.addActionListener(_ => abrirPostulaciones())
  vista.btnCerrarSesion.addActionListener(_ => cerrarSesion())
  vista.btnMiPractica.addActionListener(_ => abrirPractica())
  vista.btnMisFormularios.addActionListener(_ => abrirFormularios())

  def mostrar(): Unit = {
    cargarDatos()
    vista.setLocationRelativeTo(null)
    vista.setVisible(true)
  }

  private def cargarDatos(): Unit = {
    try {
      llenarTabla(ofertas.listarAbiertas(vista.txtBuscar.getText))
    } catch {
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudo cargar ofertas.\n\nDetalle: ${error.getMessage}")
    }
  }

  private def llenarTabla(datos: List[Oferta]): Unit = {
    val modelo = new DefaultTableModel(Array[AnyRef]("ID", "Empresa", "Titulo", "Descripcion", "Area", "Cupos", "Cierre"), 0) {
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
        oferta.fechaCierre.toString
      ))
    }

    vista.tblOfertasLaborales.setModel(modelo)
  }

  private def aplicarPostulacion(): Unit = {
    val fila = vista.tblOfertasLaborales.getSelectedRow
    if (fila < 0) {
      JOptionPane.showMessageDialog(vista, "Seleccione una oferta para postular.")
      return
    }

    val idOferta = vista.tblOfertasLaborales.getValueAt(fila, 0).toString.toLong
    if (!puedePostular()) {
      return
    }
    abrirFormularioPostulacion(idOferta, fila)
  }

  private def cancelarPostulacionOferta(): Unit = {
    val fila = vista.tblOfertasLaborales.getSelectedRow
    if (fila < 0) {
      JOptionPane.showMessageDialog(vista, "Seleccione la oferta cuya postulacion desea cancelar.")
      return
    }

    val idOferta = vista.tblOfertasLaborales.getValueAt(fila, 0).toString.toLong
    val titulo = vista.tblOfertasLaborales.getValueAt(fila, 2).toString
    val respuesta = JOptionPane.showConfirmDialog(
      vista,
      s"Desea cancelar su postulacion pendiente a la oferta '$titulo'?",
      "Confirmar cancelacion",
      JOptionPane.YES_NO_OPTION
    )

    if (respuesta == JOptionPane.YES_OPTION) {
      try {
        val eliminadas = postulaciones.cancelarPendientePorOferta(usuario.idUsuario, idOferta)
        if (eliminadas > 0) {
          JOptionPane.showMessageDialog(vista, "Postulacion cancelada y eliminada correctamente.")
          cargarDatos()
        } else {
          JOptionPane.showMessageDialog(vista, "No existe una postulacion pendiente para esta oferta o ya fue gestionada.")
        }
      } catch {
        case error: Exception =>
          JOptionPane.showMessageDialog(vista, s"No se pudo cancelar la postulacion.\n\nDetalle: ${error.getMessage}")
      }
    }
  }

  private def puedePostular(): Boolean = {
    try {
      if (practicas.estudianteTienePracticaActiva(usuario.idUsuario)) {
        JOptionPane.showMessageDialog(vista, "No puede postular porque ya tiene una practica activa o pendiente de cierre.")
        false
      } else if (practicas.estudianteYaTuvoPractica(usuario.idUsuario)) {
        JOptionPane.showMessageDialog(vista, "No puede postular porque ya completo una practica preprofesional.")
        false
      } else {
        true
      }
    } catch {
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudo validar si puede postular.\n\nDetalle: ${error.getMessage}")
        false
    }
  }

  private def abrirFormularioPostulacion(idOferta: Long, fila: Int): Unit = {
    val formulario = new VistaAplicarPostulacion()
    val empresa = vista.tblOfertasLaborales.getValueAt(fila, 1).toString
    val titulo = vista.tblOfertasLaborales.getValueAt(fila, 2).toString
    val area = vista.tblOfertasLaborales.getValueAt(fila, 4).toString
    formulario.lblOfertaEditar.setText(s"$titulo - $empresa ($area)")

    var archivoSeleccionado: Option[File] = None

    formulario.btnSeleccionarDocumento.addActionListener(_ => {
      seleccionarPdf() match {
        case Some(archivo) =>
          archivoSeleccionado = Some(archivo)
          formulario.txtDocumento.setText(archivo.getAbsolutePath)
        case None =>
      }
    })

    formulario.btnCancelar.addActionListener(_ => formulario.dispose())
    formulario.btnEnviar.addActionListener(_ => {
      archivoSeleccionado match {
        case Some(archivo) =>
          try {
            val rutaDocumento = guardarDocumento(archivo)
            postulaciones.crear(usuario.idUsuario, idOferta, rutaDocumento)
            JOptionPane.showMessageDialog(formulario, "Postulacion enviada correctamente.")
            formulario.dispose()
            abrirPostulaciones()
          } catch {
            case error: Exception =>
              JOptionPane.showMessageDialog(formulario, s"No se pudo enviar la postulacion. Verifique si ya postulo a esta oferta.\n\nDetalle: ${error.getMessage}")
          }
        case None =>
          JOptionPane.showMessageDialog(formulario, "Seleccione el PDF de avance de malla para continuar.")
      }
    })

    formulario.setLocationRelativeTo(vista)
    formulario.setVisible(true)
  }

  private def seleccionarPdf(): Option[File] = {
    val selector = new JFileChooser()
    selector.setDialogTitle("Seleccione el PDF de avance de malla")
    selector.setFileFilter(new FileNameExtensionFilter("Archivos PDF", "pdf"))

    if (selector.showOpenDialog(vista) == JFileChooser.APPROVE_OPTION) {
      val archivo = selector.getSelectedFile
      if (!archivo.getName.toLowerCase.endsWith(".pdf")) {
        JOptionPane.showMessageDialog(vista, "Debe seleccionar un archivo PDF.")
        None
      } else {
        Some(archivo)
      }
    } else {
      None
    }
  }

  private def guardarDocumento(archivo: File): String = {
    val carpeta = Path.of("documentos_postulacion").toAbsolutePath
    Files.createDirectories(carpeta)
    val destino = carpeta.resolve(s"${usuario.idUsuario}_${System.currentTimeMillis()}_${archivo.getName}")
    Files.copy(archivo.toPath, destino, StandardCopyOption.REPLACE_EXISTING)
    destino.toString
  }

  private def abrirPostulaciones(): Unit = {
    vista.dispose()
    new ControlEstudiantePostulaciones(usuario, alInicio, alCerrarSesion).mostrar()
  }

  private def abrirPractica(): Unit = {
    vista.dispose()
    new ControlEstudiantePractica(usuario, alInicio, alCerrarSesion).mostrar()
  }

  private def abrirFormularios(): Unit = {
    vista.dispose()
    new ControlEstudianteFormularios(usuario, alInicio, alCerrarSesion).mostrar()
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
