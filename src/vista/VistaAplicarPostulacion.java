package vista;

import javax.swing.*;
import java.awt.*;

public class VistaAplicarPostulacion extends JFrame {

    public JLabel lblTitulo;
    public JLabel lblOferta;
    public JLabel lblOfertaEditar;
    public JLabel lblDocumento;
    public JTextField txtDocumento;
    public JButton btnSeleccionarDocumento;
    public JButton btnCancelar;
    public JButton btnEnviar;

    public VistaAplicarPostulacion() {
        setTitle("Aplicar postulacion");
        setSize(700, 300);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        lblTitulo = new JLabel("Postulacion - avance de malla");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setBounds(30, 20, 361, 31);
        getContentPane().add(lblTitulo);

        lblOferta = new JLabel("Oferta:");
        lblOferta.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblOferta.setBounds(30, 70, 81, 25);
        getContentPane().add(lblOferta);

        lblOfertaEditar = new JLabel("");
        lblOfertaEditar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblOfertaEditar.setBounds(100, 70, 501, 25);
        getContentPane().add(lblOfertaEditar);

        lblDocumento = new JLabel("Avance de malla PDF:");
        lblDocumento.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblDocumento.setBounds(30, 115, 151, 25);
        getContentPane().add(lblDocumento);

        txtDocumento = new JTextField();
        txtDocumento.setEditable(false);
        txtDocumento.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtDocumento.setBounds(180, 115, 301, 31);
        getContentPane().add(txtDocumento);

        btnSeleccionarDocumento = new JButton("Seleccionar PDF", RecursoVista.icono("visibility.png"));
        btnSeleccionarDocumento.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnSeleccionarDocumento.setBounds(490, 115, 131, 31);
        getContentPane().add(btnSeleccionarDocumento);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnCancelar.setBounds(415, 175, 101, 31);
        getContentPane().add(btnCancelar);

        btnEnviar = new JButton("Enviar postulacion", RecursoVista.icono("save.png"));
        btnEnviar.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnEnviar.setBounds(525, 175, 131, 31);
        getContentPane().add(btnEnviar);
    }
}
