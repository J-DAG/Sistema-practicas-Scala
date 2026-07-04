package controlador

import controlador.util.ModelosTabla
import controlador.util.RevisionPostulacionFormulario
import modelo.entidades.{PostulacionDetalle, PostulacionResumen, Usuario}
import modelo.repositorios.{PostulacionRepositorio, UsuarioRepositorio}
import modelo.servicios.PostulacionServicio
import vista.VistaCoordinadorPostulaciones

import java.awt.Desktop
import java.io.File
import javax.swing.JOptionPane

class ControlCoordinadorPostulaciones(usuarioSesion: Usuario, alInicio: () => Unit, alCerrarSesion: () => Unit) {
  private val vista = new VistaCoordinadorPostulaciones()
  private val postulaciones = new PostulacionRepositorio()
  private val usuarios = new UsuarioRepositorio()
  private val servicio = new PostulacionServicio()

  vista.lblInformacion.setText(s"Bienvenido ${usuarioSesion.nombreCompleto}")

  vista.btnInicio.addActionListener(_ => volverInicio())
  vista.btnPostulaciones.addActionListener(_ => cargarDatos())
  vista.btnBuscar.addActionListener(_ => cargarDatos())
  vista.txtBuscar.addActionListener(_ => cargarDatos())
  vista.btnRevisar.addActionListener(_ => revisarPostulacion())
  vista.btnCerrarSesion.addActionListener(_ => cerrarSesion())

  vista.btnEmpresa.addActionListener(_ => abrirEmpresas())
  vista.btnEstudiantes.addActionListener(_ => abrirEstudiantes())
  vista.btnTutores.addActionListener(_ => abrirTutores())
  vista.btnOfertas.addActionListener(_ => abrirOfertas())
  vista.btnPracticas.addActionListener(_ => abrirPracticas())
  vista.btnReportes.addActionListener(_ => abrirReportes())

  def mostrar(): Unit = {
    cargarDatos()
    vista.setLocationRelativeTo(null)
    vista.setVisible(true)
  }

  private def cargarDatos(): Unit = {
    try {
      llenarTabla(postulaciones.listarResumen(vista.txtBuscar.getText))
    } catch {
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudo cargar postulaciones.\n\nDetalle: ${error.getMessage}")
    }
  }

  private def llenarTabla(datos: List[PostulacionResumen]): Unit =
    vista.tblPostulaciones.setModel(ModelosTabla.postulaciones(datos))

  private def revisarPostulacion(): Unit = {
    postulacionSeleccionada() match {
      case Some(detalle) =>
        val formulario = RevisionPostulacionFormulario.construir(
          owner = vista,
          detalle = detalle,
          tutoresAcademicos = usuarios.listarTutoresAcademicosActivos(),
          tutoresEmpresariales = usuarios.listarTutoresEmpresarialesActivosPorEmpresa(detalle.idEmpresa),
          verDocumento = () => verDocumentoAdjunto(detalle)
        )
        val opciones = opcionesRevision(detalle)
        val respuesta = JOptionPane.showOptionDialog(
          vista,
          formulario.panel,
          "Revisar postulacion",
          JOptionPane.DEFAULT_OPTION,
          JOptionPane.QUESTION_MESSAGE,
          null,
          opciones,
          opciones(0)
        )

        if (respuesta >= 0 && respuesta < opciones.length) {
          opciones(respuesta).toString match {
            case "Aprobar postulacion" => aprobarPostulacion(detalle, formulario)
            case "Negar postulacion" => negarPostulacion(detalle)
            case "Cancelar aprobacion" => cancelarAprobacion(detalle)
            case _ =>
          }
        }
      case None =>
    }
  }

  private def opcionesRevision(detalle: PostulacionDetalle): Array[AnyRef] =
    detalle.estado.trim.toLowerCase match {
      case "pendiente" => Array[AnyRef]("Aprobar postulacion", "Negar postulacion", "Cancelar")
      case "aprobada" => Array[AnyRef]("Cancelar aprobacion", "Negar postulacion", "Cerrar")
      case "negada" => Array[AnyRef]("Aprobar postulacion", "Cerrar")
      case _ => Array[AnyRef]("Cerrar")
    }

  private def aprobarPostulacion(detalle: PostulacionDetalle, formulario: RevisionPostulacionFormulario): Unit = {
    if (detalle.cuposOferta <= 0) {
      JOptionPane.showMessageDialog(vista, "La oferta no tiene cupos disponibles.")
      return
    }

    val confirmar = JOptionPane.showConfirmDialog(
      vista,
      s"Desea aprobar la postulacion de ${detalle.estudiante} y crear la practica?",
      "Confirmar aprobacion",
      JOptionPane.YES_NO_OPTION
    )
    if (confirmar != JOptionPane.YES_OPTION) {
      return
    }

    formulario.asignacion() match {
      case Some(asignacion) =>
        try {
          val idPractica = servicio.aprobar(
            idPostulacion = detalle.idPostulacion,
            idTutorAcademico = asignacion.idTutorAcademico,
            idTutorEmpresarial = asignacion.idTutorEmpresarial,
            idCoordinador = usuarioSesion.idUsuario,
            fechaInicio = asignacion.fechaInicio,
            fechaFin = asignacion.fechaFin
          )
          JOptionPane.showMessageDialog(vista, s"Postulacion aprobada. Practica creada con codigo $idPractica.")
          cargarDatos()
        } catch {
          case error: Exception =>
            JOptionPane.showMessageDialog(vista, s"No se pudo aprobar la postulacion.\n\nDetalle: ${error.getMessage}")
        }
      case None =>
    }
  }

  private def negarPostulacion(detalle: PostulacionDetalle): Unit = {
    val mensaje =
      if (detalle.estado.equalsIgnoreCase("aprobada")) {
        s"La postulacion de ${detalle.estudiante} ya fue aprobada.\nSi la niega, se eliminara la practica activa asociada siempre que no tenga actividades registradas.\nDesea continuar?"
      } else {
        s"Desea negar la postulacion de ${detalle.estudiante}?"
      }
    val respuesta = JOptionPane.showConfirmDialog(
      vista,
      mensaje,
      "Confirmar negacion",
      JOptionPane.YES_NO_OPTION
    )
    if (respuesta != JOptionPane.YES_OPTION) {
      return
    }

    try {
      servicio.negar(detalle.idPostulacion, usuarioSesion.idUsuario)
      JOptionPane.showMessageDialog(vista, "Postulacion negada correctamente.")
      cargarDatos()
    } catch {
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudo negar la postulacion.\n\nDetalle: ${error.getMessage}")
    }
  }

  private def cancelarAprobacion(detalle: PostulacionDetalle): Unit = {
    val respuesta = JOptionPane.showConfirmDialog(
      vista,
      s"Desea cancelar la aprobacion de la postulacion de ${detalle.estudiante}?\nLa practica activa se eliminara solo si no tiene actividades registradas y el cupo sera devuelto.",
      "Confirmar cancelacion de aprobacion",
      JOptionPane.YES_NO_OPTION
    )
    if (respuesta != JOptionPane.YES_OPTION) {
      return
    }

    try {
      servicio.cancelarAprobacion(detalle.idPostulacion, usuarioSesion.idUsuario)
      JOptionPane.showMessageDialog(vista, "Aprobacion cancelada correctamente. La postulacion volvio a estado pendiente.")
      cargarDatos()
    } catch {
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudo cancelar la aprobacion.\n\nDetalle: ${error.getMessage}")
    }
  }

  private def postulacionSeleccionada(): Option[PostulacionDetalle] = {
    val fila = vista.tblPostulaciones.getSelectedRow
    if (fila < 0) {
      JOptionPane.showMessageDialog(vista, "Seleccione una postulacion.")
      None
    } else {
      val idPostulacion = vista.tblPostulaciones.getValueAt(fila, 0).toString.toLong
      postulaciones.buscarDetalle(idPostulacion) match {
        case Some(detalle) => Some(detalle)
        case None =>
          JOptionPane.showMessageDialog(vista, "No se encontro la postulacion seleccionada.")
          None
      }
    }
  }

  private def verDocumentoAdjunto(detalle: PostulacionDetalle): Unit = {
    detalle.rutaDocumentoMalla match {
      case Some(ruta) if ruta.trim.nonEmpty =>
        abrirDocumento(ruta)
      case _ =>
        JOptionPane.showMessageDialog(vista, "La postulacion no tiene documento adjunto registrado.")
    }
  }

  private def abrirDocumento(ruta: String): Unit = {
    try {
      val archivo = new File(ruta)
      if (!archivo.exists()) {
        JOptionPane.showMessageDialog(vista, s"No se encontro el documento:\n$ruta")
      } else if (!Desktop.isDesktopSupported) {
        JOptionPane.showMessageDialog(vista, s"No se puede abrir automaticamente en este equipo.\nRuta:\n$ruta")
      } else {
        Desktop.getDesktop.open(archivo)
      }
    } catch {
      case error: Exception =>
        JOptionPane.showMessageDialog(vista, s"No se pudo abrir el documento.\n\nDetalle: ${error.getMessage}")
    }
  }

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

  private def abrirOfertas(): Unit = {
    vista.dispose()
    new ControlCoordinadorOfertas(usuarioSesion, alInicio, alCerrarSesion).mostrar()
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
