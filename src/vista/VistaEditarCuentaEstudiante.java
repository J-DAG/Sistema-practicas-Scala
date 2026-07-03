package vista;

import javax.swing.*;
import java.awt.*;

public class VistaEditarCuentaEstudiante extends JFrame {

    public JLabel lblTitulo;
    public JLabel lblNombres;
    public JLabel lblApellido;
    public JLabel lblCorreoElectrinico;
    public JLabel lblCedula;
    public JLabel lblCarrera;
    public JLabel lblCicloActual;
    public JLabel lblContrasenia;
    public JLabel lblConfirmarContrasenia;
    public JTextField txtNombres;
    public JTextField txtApellidos;
    public JTextField txtCorreoElectronico;
    public JTextField txtCedula;
    public JComboBox<String> cbxCarrera;
    public JComboBox<String> cbsCicloActual;
    public JPasswordField txtContrasenia;
    public JPasswordField txtConfirmarContrasenia;
    public JButton btnCancelar;
    public JButton btnGuardar;

    public VistaEditarCuentaEstudiante() {
        setTitle("Editar informacion del estudiante");
        setSize(850, 790);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        lblTitulo = new JLabel("Editar Informacion del Estudiante");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitulo.setBounds(180, 10, 361, 21);
        getContentPane().add(lblTitulo);

        lblNombres = new JLabel("Nombres:");
        lblNombres.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblNombres.setBounds(190, 60, 71, 21);
        getContentPane().add(lblNombres);

        txtNombres = new JTextField();
        txtNombres.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtNombres.setBounds(190, 90, 431, 31);
        getContentPane().add(txtNombres);

        lblApellido = new JLabel("Apellidos:");
        lblApellido.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblApellido.setBounds(190, 130, 71, 21);
        getContentPane().add(lblApellido);

        txtApellidos = new JTextField();
        txtApellidos.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtApellidos.setBounds(215, 160, 431, 31);
        getContentPane().add(txtApellidos);

        lblCorreoElectrinico = new JLabel("Correo electronico:");
        lblCorreoElectrinico.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCorreoElectrinico.setBounds(215, 200, 131, 21);
        getContentPane().add(lblCorreoElectrinico);

        txtCorreoElectronico = new JTextField();
        txtCorreoElectronico.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtCorreoElectronico.setBounds(215, 230, 431, 31);
        getContentPane().add(txtCorreoElectronico);

        lblCedula = new JLabel("Cedula:");
        lblCedula.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCedula.setBounds(215, 270, 91, 21);
        getContentPane().add(lblCedula);

        txtCedula = new JTextField();
        txtCedula.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtCedula.setBounds(215, 300, 431, 31);
        getContentPane().add(txtCedula);

        lblCarrera = new JLabel("Carrera:");
        lblCarrera.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCarrera.setBounds(215, 350, 111, 21);
        getContentPane().add(lblCarrera);

        cbxCarrera = new JComboBox<>();
        cbxCarrera.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        cbxCarrera.setBounds(215, 380, 431, 31);
        getContentPane().add(cbxCarrera);

        lblCicloActual = new JLabel("Ciclo actual:");
        lblCicloActual.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCicloActual.setBounds(215, 420, 91, 21);
        getContentPane().add(lblCicloActual);

        cbsCicloActual = new JComboBox<>();
        cbsCicloActual.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        cbsCicloActual.setBounds(215, 450, 431, 31);
        getContentPane().add(cbsCicloActual);

        lblContrasenia = new JLabel("Contrasenia:");
        lblContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblContrasenia.setBounds(215, 490, 91, 21);
        getContentPane().add(lblContrasenia);

        txtContrasenia = new JPasswordField();
        txtContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtContrasenia.setBounds(215, 520, 431, 31);
        getContentPane().add(txtContrasenia);

        lblConfirmarContrasenia = new JLabel("Confirmar contrasenia:");
        lblConfirmarContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblConfirmarContrasenia.setBounds(215, 560, 151, 21);
        getContentPane().add(lblConfirmarContrasenia);

        txtConfirmarContrasenia = new JPasswordField();
        txtConfirmarContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtConfirmarContrasenia.setBounds(215, 590, 431, 31);
        getContentPane().add(txtConfirmarContrasenia);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnCancelar.setBounds(365, 650, 91, 31);
        getContentPane().add(btnCancelar);

        btnGuardar = new JButton("Guardar", RecursoVista.icono("person_add.png"));
        btnGuardar.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnGuardar.setBounds(475, 650, 171, 31);
        getContentPane().add(btnGuardar);
    }
}
