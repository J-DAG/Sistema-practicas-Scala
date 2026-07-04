package controlador

import modelo.entidades.Usuario
import modelo.repositorios.NotificacionRepositorio

import java.awt.{BorderLayout, Component, FlowLayout}
import javax.swing.{JButton, JDialog, JOptionPane, JPanel, JScrollPane, JTable}
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
      val btnMarcarLeidas = new JButton("Marcar como leidas")
      val btnCerrar = new JButton("Cerrar")
      val dialogo = new JDialog()
      dialogo.setTitle("Notificaciones")
      dialogo.setModal(true)
      dialogo.setSize(850, 420)
      dialogo.setLayout(new BorderLayout())
      dialogo.add(new JScrollPane(tabla), BorderLayout.CENTER)

      val panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT))
      panelBotones.add(btnMarcarLeidas)
      panelBotones.add(btnCerrar)
      dialogo.add(panelBotones, BorderLayout.SOUTH)

      btnMarcarLeidas.addActionListener(_ => {
        val actualizadas = repositorio.marcarLeidas(usuario.idUsuario)
        (0 until modelo.getRowCount).foreach(fila => modelo.setValueAt("Si", fila, 3))
        btnMarcarLeidas.setEnabled(false)
        JOptionPane.showMessageDialog(dialogo, s"Notificaciones marcadas como leidas: $actualizadas")
      })
      btnCerrar.addActionListener(_ => dialogo.dispose())
      btnMarcarLeidas.setEnabled(datos.exists(!_.leida))

      dialogo.setLocationRelativeTo(owner)
      dialogo.setVisible(true)
    } catch {
      case error: Exception =>
        JOptionPane.showMessageDialog(owner, s"No se pudieron cargar las notificaciones.\n\nDetalle: ${error.getMessage}")
    }
  }
}
