package vista;

import javax.swing.*;
import java.awt.*;

public class VistaCrearCuentaEstudiante extends JFrame {

    public JLabel lblTitulo;
    public JLabel lblSubtitulo;
    public JLabel lblNombres;
    public JTextField txtNombres;
    public JLabel lblApellido;
    public JTextField txtApellidos;
    public JLabel lblCorreoElectrinico;
    public JTextField txtCorreoElectronico;
    public JLabel lblCedula;
    public JTextField txtCedula;
    public JLabel lblCarrera;
    public JComboBox<String> cbxCarrera;
    public JLabel lblCicloActual;
    public JComboBox<String> cbsCicloActual;
    public JLabel lblContrasenia;
    public JPasswordField txtContrasenia;
    public JLabel lblConfirmarContrasenia;
    public JPasswordField txtConfirmarContrasenia;
    public JButton btnCancelar;
    public JButton btnGuardar;

    public VistaCrearCuentaEstudiante() {
        setTitle("Registar nuevo estudiante");
        setSize(850, 790);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        getContentPane().setLayout(null);
        initComponents();
    }

    private void initComponents() {
        lblTitulo = new JLabel("Registrar Nuevo Estudiante");
        lblTitulo.setBounds(180, 10, 271, 21);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        add(lblTitulo);

        lblSubtitulo = new JLabel("Crea una cuenta en el sistema para estudiantes.");
        lblSubtitulo.setBounds(180, 40, 251, 21);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        add(lblSubtitulo);

        lblNombres = new JLabel("Nombres:");
        lblNombres.setBounds(180, 80, 71, 21);
        lblNombres.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        add(lblNombres);

        txtNombres = new JTextField();
        txtNombres.setBounds(180, 110, 431, 31);
        txtNombres.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        add(txtNombres);

        lblApellido = new JLabel("Apellidos:");
        lblApellido.setBounds(205, 150, 71, 21);
        lblApellido.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        add(lblApellido);

        txtApellidos = new JTextField();
        txtApellidos.setBounds(205, 180, 431, 31);
        txtApellidos.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        add(txtApellidos);

        lblCorreoElectrinico = new JLabel("Correo electronico:");
        lblCorreoElectrinico.setBounds(205, 220, 131, 21);
        lblCorreoElectrinico.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        add(lblCorreoElectrinico);

        txtCorreoElectronico = new JTextField();
        txtCorreoElectronico.setBounds(205, 250, 431, 31);
        txtCorreoElectronico.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        add(txtCorreoElectronico);

        lblCedula = new JLabel("Cedula:");
        lblCedula.setBounds(205, 290, 91, 21);
        lblCedula.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        add(lblCedula);

        txtCedula = new JTextField();
        txtCedula.setBounds(205, 320, 431, 31);
        txtCedula.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        add(txtCedula);

        lblCarrera = new JLabel("Carrera:");
        lblCarrera.setBounds(205, 370, 111, 21);
        lblCarrera.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        add(lblCarrera);

        cbxCarrera = new JComboBox<>();
        cbxCarrera.setBounds(205, 400, 431, 31);
        add(cbxCarrera);

        lblCicloActual = new JLabel("Ciclo actual:");
        lblCicloActual.setBounds(205, 440, 91, 21);
        lblCicloActual.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        add(lblCicloActual);

        cbsCicloActual = new JComboBox<>();
        cbsCicloActual.setBounds(205, 470, 431, 31);
        add(cbsCicloActual);

        lblContrasenia = new JLabel("Contraseña:");
        lblContrasenia.setBounds(205, 510, 91, 21);
        lblContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        add(lblContrasenia);

        txtContrasenia = new JPasswordField();
        txtContrasenia.setBounds(205, 540, 431, 31);
        txtContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        add(txtContrasenia);

        lblConfirmarContrasenia = new JLabel("Confirmar contraseña");
        lblConfirmarContrasenia.setBounds(205, 580, 151, 21);
        lblConfirmarContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        add(lblConfirmarContrasenia);

        txtConfirmarContrasenia = new JPasswordField();
        txtConfirmarContrasenia.setBounds(205, 610, 431, 31);
        txtConfirmarContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        add(txtConfirmarContrasenia);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(355, 670, 91, 31);
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        add(btnCancelar);

        btnGuardar = new JButton("Guardar", RecursoVista.icono("person_add.png"));
        btnGuardar.setBounds(465, 670, 171, 31);
        btnGuardar.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        add(btnGuardar);
    }
}
