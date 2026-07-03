package vista;

import javax.swing.*;
import java.awt.*;

public class VistaInicioSesion extends JFrame {

    public JLabel lblLogoUcuenca;
    public JLabel lblTitulo;
    public JLabel lblUsuario;
    public JTextField txtUsuario;
    public JLabel lblContrasenia;
    public JPasswordField txtContrasenia;
    public JButton btnIniciarSesion;
    public JLabel lblPregunta;
    public JButton btnCrearCuenta;

    public VistaInicioSesion() {
        setTitle("Inicio de Sesion");
        setSize(850, 640);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(RecursoVista.icono("account.png").getImage());
        getContentPane().setBackground(Color.WHITE);
        getContentPane().setLayout(null);
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        lblLogoUcuenca = new JLabel();
        lblLogoUcuenca.setBounds(225, 55, 401, 121);
        ImageIcon logoIcon = RecursoVista.imagen("logo_inicio_sesion.jpg");
        if (logoIcon.getIconWidth() > 0) {
            Image scaled = logoIcon.getImage().getScaledInstance(401, 121, Image.SCALE_SMOOTH);
            lblLogoUcuenca.setIcon(new ImageIcon(scaled));
        }
        lblLogoUcuenca.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblLogoUcuenca);

        lblTitulo = new JLabel("Gestión de Prácticas");
        lblTitulo.setBounds(325, 180, 201, 31);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        add(lblTitulo);

        lblUsuario = new JLabel("Usuario");
        lblUsuario.setBounds(295, 210, 61, 41);
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        add(lblUsuario);

        txtUsuario = new JTextField();
        txtUsuario.setBounds(295, 240, 311, 31);
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        add(txtUsuario);

        lblContrasenia = new JLabel("Contraseña:");
        lblContrasenia.setBounds(295, 290, 91, 31);
        lblContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        add(lblContrasenia);

        txtContrasenia = new JPasswordField();
        txtContrasenia.setBounds(295, 320, 311, 31);
        txtContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        add(txtContrasenia);

        btnIniciarSesion = new JButton("Iniciar sesion");
        btnIniciarSesion.setBounds(295, 370, 311, 31);
        btnIniciarSesion.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        add(btnIniciarSesion);

        lblPregunta = new JLabel("¿Nuevo en la plataforma?");
        lblPregunta.setBounds(380, 410, 141, 41);
        lblPregunta.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        add(lblPregunta);

        btnCrearCuenta = new JButton("Crear cuenta");
        btnCrearCuenta.setBounds(295, 450, 311, 31);
        btnCrearCuenta.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        add(btnCrearCuenta);
    }
}
