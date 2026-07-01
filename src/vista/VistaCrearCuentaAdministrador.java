package vista;

import javax.swing.*;
import java.awt.*;

public class VistaCrearCuentaAdministrador extends JFrame {

    public JLabel lblTitulo;
    public JLabel lblSubtitulo;
    public JLabel lblNombres;
    public JLabel lblApellido;
    public JLabel lblCorreoElectronico;
    public JLabel lblCedula;
    public JLabel lblContrasenia;
    public JLabel lblConfirmarContrasenia;
    public JTextField txtNombres;
    public JTextField txtApellidos;
    public JTextField txtCorreoElectronico;
    public JTextField txtCedula;
    public JPasswordField txtContrasenia;
    public JPasswordField txtConfirmarContrasenia;
    public JButton btnCancelar;
    public JButton btnGuardar;

    public VistaCrearCuentaAdministrador() {
        setTitle("Registrar administrador");
        setSize(800, 620);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        lblTitulo = new JLabel("Registrar Administrador");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitulo.setBounds(180, 20, 331, 21);
        getContentPane().add(lblTitulo);

        lblSubtitulo = new JLabel("Complete los campos requeridos");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 8));
        lblSubtitulo.setBounds(180, 50, 291, 21);
        getContentPane().add(lblSubtitulo);

        lblNombres = new JLabel("Nombres:");
        lblNombres.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblNombres.setBounds(180, 90, 71, 21);
        getContentPane().add(lblNombres);

        txtNombres = new JTextField();
        txtNombres.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        txtNombres.setBounds(180, 120, 431, 31);
        getContentPane().add(txtNombres);

        lblApellido = new JLabel("Apellidos:");
        lblApellido.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblApellido.setBounds(180, 160, 71, 21);
        getContentPane().add(lblApellido);

        txtApellidos = new JTextField();
        txtApellidos.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        txtApellidos.setBounds(180, 190, 431, 31);
        getContentPane().add(txtApellidos);

        lblCorreoElectronico = new JLabel("Usuario / correo:");
        lblCorreoElectronico.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCorreoElectronico.setBounds(180, 230, 131, 21);
        getContentPane().add(lblCorreoElectronico);

        txtCorreoElectronico = new JTextField();
        txtCorreoElectronico.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        txtCorreoElectronico.setBounds(180, 260, 431, 31);
        getContentPane().add(txtCorreoElectronico);

        lblCedula = new JLabel("Cedula:");
        lblCedula.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCedula.setBounds(180, 300, 91, 21);
        getContentPane().add(lblCedula);

        txtCedula = new JTextField();
        txtCedula.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        txtCedula.setBounds(180, 330, 431, 31);
        getContentPane().add(txtCedula);

        lblContrasenia = new JLabel("Contrasenia:");
        lblContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblContrasenia.setBounds(180, 370, 91, 21);
        getContentPane().add(lblContrasenia);

        txtContrasenia = new JPasswordField();
        txtContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        txtContrasenia.setBounds(180, 400, 431, 31);
        getContentPane().add(txtContrasenia);

        lblConfirmarContrasenia = new JLabel("Confirmar contrasenia:");
        lblConfirmarContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblConfirmarContrasenia.setBounds(180, 440, 151, 21);
        getContentPane().add(lblConfirmarContrasenia);

        txtConfirmarContrasenia = new JPasswordField();
        txtConfirmarContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        txtConfirmarContrasenia.setBounds(180, 470, 431, 31);
        getContentPane().add(txtConfirmarContrasenia);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnCancelar.setBounds(330, 530, 91, 31);
        getContentPane().add(btnCancelar);

        btnGuardar = new JButton("Guardar", new ImageIcon("./iconos/person_add.png"));
        btnGuardar.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnGuardar.setBounds(440, 530, 171, 31);
        getContentPane().add(btnGuardar);
    }
}
