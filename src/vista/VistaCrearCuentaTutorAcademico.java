package vista;

import javax.swing.*;
import java.awt.*;

public class VistaCrearCuentaTutorAcademico extends JFrame {

    public JLabel lblTitulo;
    public JLabel lblSubtitulo;
    public JLabel lblNombres;
    public JLabel lblApellido;
    public JLabel lblCorreoElectronico;
    public JLabel lblCedula;
    public JLabel lblCarrera;
    public JLabel lblContrasenia;
    public JLabel lblConfirmarContrasenia;
    public JTextField txtNombres;
    public JTextField txtApellidos;
    public JTextField txtCorreoElectronico;
    public JTextField txtCedula;
    public JComboBox<String> cbxCarrera;
    public JPasswordField txtContrasenia;
    public JPasswordField txtConfirmarContrasenia;
    public JButton btnCancelar;
    public JButton btnGuardar;

    public VistaCrearCuentaTutorAcademico() {
        setTitle("Registrar tutor academico");
        setSize(800, 700);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        lblTitulo = new JLabel("Registrar Tutor Academico");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitulo.setBounds(180, 10, 331, 21);
        getContentPane().add(lblTitulo);

        lblSubtitulo = new JLabel("Complete los campos requeridos");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 8));
        lblSubtitulo.setBounds(180, 40, 291, 21);
        getContentPane().add(lblSubtitulo);

        lblNombres = new JLabel("Nombres:");
        lblNombres.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblNombres.setBounds(180, 80, 71, 21);
        getContentPane().add(lblNombres);

        txtNombres = new JTextField();
        txtNombres.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        txtNombres.setBounds(180, 110, 431, 31);
        getContentPane().add(txtNombres);

        lblApellido = new JLabel("Apellidos:");
        lblApellido.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblApellido.setBounds(180, 150, 71, 21);
        getContentPane().add(lblApellido);

        txtApellidos = new JTextField();
        txtApellidos.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        txtApellidos.setBounds(180, 180, 431, 31);
        getContentPane().add(txtApellidos);

        lblCorreoElectronico = new JLabel("Correo electronico:");
        lblCorreoElectronico.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCorreoElectronico.setBounds(180, 210, 131, 21);
        getContentPane().add(lblCorreoElectronico);

        txtCorreoElectronico = new JTextField();
        txtCorreoElectronico.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        txtCorreoElectronico.setBounds(180, 240, 431, 31);
        getContentPane().add(txtCorreoElectronico);

        lblCedula = new JLabel("Cedula:");
        lblCedula.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCedula.setBounds(180, 280, 91, 21);
        getContentPane().add(lblCedula);

        txtCedula = new JTextField();
        txtCedula.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        txtCedula.setBounds(180, 310, 431, 31);
        getContentPane().add(txtCedula);

        lblCarrera = new JLabel("Carrera:");
        lblCarrera.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCarrera.setBounds(180, 360, 111, 21);
        getContentPane().add(lblCarrera);

        cbxCarrera = new JComboBox<>();
        cbxCarrera.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        cbxCarrera.setBounds(180, 390, 431, 31);
        getContentPane().add(cbxCarrera);

        lblContrasenia = new JLabel("Contrasenia:");
        lblContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblContrasenia.setBounds(180, 440, 91, 21);
        getContentPane().add(lblContrasenia);

        txtContrasenia = new JPasswordField();
        txtContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        txtContrasenia.setBounds(180, 470, 431, 31);
        getContentPane().add(txtContrasenia);

        lblConfirmarContrasenia = new JLabel("Confirmar contrasenia:");
        lblConfirmarContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblConfirmarContrasenia.setBounds(180, 510, 151, 21);
        getContentPane().add(lblConfirmarContrasenia);

        txtConfirmarContrasenia = new JPasswordField();
        txtConfirmarContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        txtConfirmarContrasenia.setBounds(180, 540, 431, 31);
        getContentPane().add(txtConfirmarContrasenia);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnCancelar.setBounds(330, 600, 91, 31);
        getContentPane().add(btnCancelar);

        btnGuardar = new JButton("Guardar", new ImageIcon("./iconos/person_add.png"));
        btnGuardar.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnGuardar.setBounds(440, 600, 171, 31);
        getContentPane().add(btnGuardar);
    }
}
