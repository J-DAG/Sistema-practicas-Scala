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
        setSize(800, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon("./iconos/account.png").getImage());
        getContentPane().setBackground(Color.WHITE);
        getContentPane().setLayout(null);
        initComponents();
    }

    private void initComponents() {
        lblLogoUcuenca = new JLabel();
        lblLogoUcuenca.setBounds(180, 50, 401, 121);
        ImageIcon logoIcon = new ImageIcon("/imagenes/logo_inicio_sesion.jpg");
        if (logoIcon.getIconWidth() > 0) {
            Image scaled = logoIcon.getImage().getScaledInstance(401, 121, Image.SCALE_SMOOTH);
            lblLogoUcuenca.setIcon(new ImageIcon(scaled));
        }
        lblLogoUcuenca.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblLogoUcuenca);

        lblTitulo = new JLabel("Gestión de Prácticas");
        lblTitulo.setBounds(300, 170, 201, 31);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        add(lblTitulo);

        lblUsuario = new JLabel("Usuario");
        lblUsuario.setBounds(240, 200, 61, 41);
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        add(lblUsuario);

        txtUsuario = new JTextField();
        txtUsuario.setBounds(240, 230, 311, 31);
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 8));
        add(txtUsuario);

        lblContrasenia = new JLabel("Contraseña:");
        lblContrasenia.setBounds(240, 280, 91, 31);
        lblContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        add(lblContrasenia);

        txtContrasenia = new JPasswordField();
        txtContrasenia.setBounds(240, 310, 311, 31);
        txtContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        add(txtContrasenia);

        btnIniciarSesion = new JButton("Iniciar sesion");
        btnIniciarSesion.setBounds(240, 360, 311, 31);
        btnIniciarSesion.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        add(btnIniciarSesion);

        lblPregunta = new JLabel("¿Nuevo en la plataforma?");
        lblPregunta.setBounds(330, 400, 141, 41);
        lblPregunta.setFont(new Font("Segoe UI", Font.PLAIN, 8));
        add(lblPregunta);

        btnCrearCuenta = new JButton("Crear cuenta");
        btnCrearCuenta.setBounds(240, 440, 311, 31);
        btnCrearCuenta.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        add(btnCrearCuenta);
    }
}
