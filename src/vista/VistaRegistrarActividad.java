package vista;

import javax.swing.*;
import java.awt.*;

public class VistaRegistrarActividad extends JFrame {

    public JLabel lblTitulo;
    public JLabel lblDescripcion;
    public JLabel lblHoras;
    public JTextField txtDescripcion;
    public JSpinner sbxHoras;
    public JButton btnCancelar;
    public JButton btnGuardar;

    public VistaRegistrarActividad() {
        setTitle("Registrar nueva actividad");
        setSize(500, 225);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        lblTitulo = new JLabel("Ingrese la Informacion de la Actividad");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitulo.setBounds(70, 10, 381, 31);
        getContentPane().add(lblTitulo);

        lblDescripcion = new JLabel("Descripcion:");
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDescripcion.setBounds(20, 70, 81, 31);
        getContentPane().add(lblDescripcion);

        txtDescripcion = new JTextField();
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        txtDescripcion.setBounds(110, 70, 361, 31);
        getContentPane().add(txtDescripcion);

        lblHoras = new JLabel("Horas:");
        lblHoras.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblHoras.setBounds(60, 110, 41, 31);
        getContentPane().add(lblHoras);

        sbxHoras = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        sbxHoras.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        sbxHoras.setBounds(110, 110, 101, 31);
        getContentPane().add(sbxHoras);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnCancelar.setBounds(140, 160, 91, 31);
        getContentPane().add(btnCancelar);

        btnGuardar = new JButton("Guardar", new ImageIcon("./iconos/save.png"));
        btnGuardar.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnGuardar.setBounds(260, 160, 101, 31);
        getContentPane().add(btnGuardar);
    }
}
