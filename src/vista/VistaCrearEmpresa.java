package vista;

import javax.swing.*;
import java.awt.*;

public class VistaCrearEmpresa extends JFrame {

    public JLabel lblTitulo;
    public JLabel lblSubtitulo;
    public JLabel lblNombre;
    public JLabel lblCorreoElectrinico;
    public JLabel lblCedula;
    public JLabel lblConvenio;
    public JLabel lblContrasenia;
    public JLabel lblConfirmarContrasenia;
    public JTextField txtNombreEmpresa;
    public JTextField txtCorreoElectronico;
    public JTextField txtRuc;
    public JTextField txtSector;
    public JTextField txtUbicacion;
    public JRadioButton rbtSi;
    public JRadioButton rbtNo;
    public JButton btnCancelar;
    public JButton btnGuardar;

    public VistaCrearEmpresa() {
        setTitle("Registrar nueva empresa");
        setSize(850, 650);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        lblTitulo = new JLabel("Registrar Nueva Empresa");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitulo.setBounds(180, 10, 271, 21);
        getContentPane().add(lblTitulo);

        lblSubtitulo = new JLabel("Complete los campos requeridos");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSubtitulo.setBounds(180, 40, 251, 21);
        getContentPane().add(lblSubtitulo);

        lblNombre = new JLabel("Nombre de la empresa:");
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblNombre.setBounds(180, 60, 161, 21);
        getContentPane().add(lblNombre);

        txtNombreEmpresa = new JTextField();
        txtNombreEmpresa.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtNombreEmpresa.setBounds(180, 90, 431, 31);
        getContentPane().add(txtNombreEmpresa);

        lblCorreoElectrinico = new JLabel("Correo electronico:");
        lblCorreoElectrinico.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCorreoElectrinico.setBounds(180, 130, 131, 21);
        getContentPane().add(lblCorreoElectrinico);

        txtCorreoElectronico = new JTextField();
        txtCorreoElectronico.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtCorreoElectronico.setBounds(205, 160, 431, 31);
        getContentPane().add(txtCorreoElectronico);

        lblCedula = new JLabel("Ruc:");
        lblCedula.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCedula.setBounds(205, 200, 91, 21);
        getContentPane().add(lblCedula);

        txtRuc = new JTextField();
        txtRuc.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtRuc.setBounds(205, 230, 431, 31);
        getContentPane().add(txtRuc);

        lblConvenio = new JLabel("Tiene convenio:");
        lblConvenio.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblConvenio.setBounds(205, 270, 111, 21);
        getContentPane().add(lblConvenio);

        rbtSi = new JRadioButton("Si");
        rbtSi.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        rbtSi.setBackground(Color.WHITE);
        rbtSi.setBounds(345, 280, 101, 17);
        getContentPane().add(rbtSi);

        rbtNo = new JRadioButton("No");
        rbtNo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        rbtNo.setBackground(Color.WHITE);
        rbtNo.setBounds(475, 280, 101, 17);
        getContentPane().add(rbtNo);

        lblContrasenia = new JLabel("Sector:");
        lblContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblContrasenia.setBounds(205, 310, 91, 21);
        getContentPane().add(lblContrasenia);

        txtSector = new JTextField();
        txtSector.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtSector.setBounds(205, 340, 431, 31);
        getContentPane().add(txtSector);

        lblConfirmarContrasenia = new JLabel("Ubicacion:");
        lblConfirmarContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblConfirmarContrasenia.setBounds(205, 380, 151, 21);
        getContentPane().add(lblConfirmarContrasenia);

        txtUbicacion = new JTextField();
        txtUbicacion.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtUbicacion.setBounds(205, 410, 431, 31);
        getContentPane().add(txtUbicacion);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnCancelar.setBounds(355, 480, 91, 31);
        getContentPane().add(btnCancelar);

        btnGuardar = new JButton("Guardar y registrar", RecursoVista.icono("apartment.png"));
        btnGuardar.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnGuardar.setBounds(465, 480, 171, 31);
        getContentPane().add(btnGuardar);
    }
}
