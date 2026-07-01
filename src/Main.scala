
import controlador.ControlPrincipal
import javax.swing.SwingUtilities

object Main {
  def main(args: Array[String]): Unit = {
    SwingUtilities.invokeLater(() => new ControlPrincipal().mostrar())
  }
}
