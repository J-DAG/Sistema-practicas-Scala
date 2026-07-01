package controlador

import modelo.entidades.{Oferta, Usuario}
import modelo.repositorios.{OfertaRepositorio, PostulacionRepositorio}
import vista.VistaEstOfertas

import java.nio.file.{Files, Path, StandardCopyOption}
import javax.swing.{JFileChooser, JOptionPane}
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing.table.DefaultTableModel

class ControlEstudianteOfertas(usuario: Usuario, alInicio: () => Unit, alCerrarSesion: () => Unit) {
  private val vista = new VistaEstOfertas()
  private val ofertas = new OfertaRepositorio()
  private val postulaciones = new PostulacionRepositorio()

  vista.btnInico.addActionListener(_ => volverInicio())
  vista.btnOfertaLaboral.addActionListener(_ => cargarDatos())
  vista.btnBuscar.addActionListener(_ => cargarDatos())
  vista.txtBuscar.addActionListener(_ => cargarDatos())
  vista.btnAplicarPostulacion.addActionListener(_ => aplicarPostulacion())
  vista.btnCancelarPostulacion.addActionListener(_ => abrirPostulaciones())
  vista.btnMisPostulaciones.addActionListener(_ => abrirPostulaciones())
  vista.btnCerrarSesion.addActionListener(_ => cerrarSesion())
  vista.btnMiPractica.addActionListener(_ => moduloPendiente("Mi practica"))
  vista.btnMisFormularios.addActionListener(_ => moduloPendiente("Formularios"))

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
    seleccionarPdf() match {
      case Some(rutaDocumento) =>
        try {
          postulaciones.crear(usuario.idUsuario, idOferta, rutaDocumento)
          JOptionPane.showMessageDialog(vista, "Postulacion enviada correctamente.")
          abrirPostulaciones()
        } catch {
          case error: Exception =>
            JOptionPane.showMessageDialog(vista, s"No se pudo enviar la postulacion. Verifique si ya postulo a esta oferta.\n\nDetalle: ${error.getMessage}")
        }
      case None =>
    }
  }

  private def seleccionarPdf(): Option[String] = {
    val selector = new JFileChooser()
    selector.setDialogTitle("Seleccione el PDF de avance de malla")
    selector.setFileFilter(new FileNameExtensionFilter("Archivos PDF", "pdf"))

    if (selector.showOpenDialog(vista) == JFileChooser.APPROVE_OPTION) {
      val archivo = selector.getSelectedFile
      if (!archivo.getName.toLowerCase.endsWith(".pdf")) {
        JOptionPane.showMessageDialog(vista, "Debe seleccionar un archivo PDF.")
        None
      } else {
        val carpeta = Path.of("documentos_postulacion").toAbsolutePath
        Files.createDirectories(carpeta)
        val destino = carpeta.resolve(s"${usuario.idUsuario}_${System.currentTimeMillis()}_${archivo.getName}")
        Files.copy(archivo.toPath, destino, StandardCopyOption.REPLACE_EXISTING)
        Some(destino.toString)
      }
    } else {
      None
    }
  }

  private def abrirPostulaciones(): Unit = {
    vista.dispose()
    new ControlEstudiantePostulaciones(usuario, alInicio, alCerrarSesion).mostrar()
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
