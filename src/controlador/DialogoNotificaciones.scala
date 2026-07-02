package controlador

import modelo.entidades.Usuario
import modelo.repositorios.NotificacionRepositorio

import java.awt.{BorderLayout, Component}
import javax.swing.{JDialog, JOptionPane, JScrollPane, JTable}
import javax.swing.table.DefaultTableModel

object DialogoNotificaciones {
  private val repositorio = new NotificacionRepositorio()

  def mostrar(owner: Component, usuario: Usuario): Unit = {
    try {
      val datos = repositorio.listarPorUsuario(usuario.idUsuario)
      if (datos.isEmpty) {
        JOptionPane.showMessageDialog(owner, "No tiene notificaciones.")
        return
      }

      val modelo = new DefaultTableModel(Array[AnyRef]("Fecha", "Titulo", "Mensaje", "Leida"), 0) {
        override def isCellEditable(row: Int, column: Int): Boolean = false
      }
      datos.foreach { notificacion =>
        modelo.addRow(Array[AnyRef](
          notificacion.fechaCreacion.toString.replace('T', ' '),
          notificacion.titulo,
          notificacion.mensaje,
          if (notificacion.leida) "Si" else "No"
        ))
      }

      val tabla = new JTable(modelo)
      val dialogo = new JDialog()
      dialogo.setTitle("Notificaciones")
      dialogo.setModal(true)
      dialogo.setSize(850, 420)
      dialogo.setLayout(new BorderLayout())
      dialogo.add(new JScrollPane(tabla), BorderLayout.CENTER)
      dialogo.setLocationRelativeTo(owner)
      dialogo.setVisible(true)
      repositorio.marcarLeidas(usuario.idUsuario)
    } catch {
      case error: Exception =>
        JOptionPane.showMessageDialog(owner, s"No se pudieron cargar las notificaciones.\n\nDetalle: ${error.getMessage}")
    }
  }
}
