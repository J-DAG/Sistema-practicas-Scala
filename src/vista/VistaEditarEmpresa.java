package vista;

import javax.swing.*;
import java.awt.*;

public class VistaEditarEmpresa extends JFrame {

    public JLabel lblTitulo;
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

    public VistaEditarEmpresa() {
        setTitle("Editar informacion de la empresa");
        setSize(800, 610);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        lblTitulo = new JLabel("Editar Informacion de Empresa");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitulo.setBounds(180, 10, 311, 21);
        getContentPane().add(lblTitulo);

        lblNombre = new JLabel("Nombre de la empresa:");
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblNombre.setBounds(180, 60, 161, 21);
        getContentPane().add(lblNombre);

        txtNombreEmpresa = new JTextField();
        txtNombreEmpresa.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        txtNombreEmpresa.setBounds(180, 90, 431, 31);
        getContentPane().add(txtNombreEmpresa);

        lblCorreoElectrinico = new JLabel("Correo electronico:");
        lblCorreoElectrinico.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCorreoElectrinico.setBounds(180, 130, 131, 21);
        getContentPane().add(lblCorreoElectrinico);

        txtCorreoElectronico = new JTextField();
        txtCorreoElectronico.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        txtCorreoElectronico.setBounds(180, 160, 431, 31);
        getContentPane().add(txtCorreoElectronico);

        lblCedula = new JLabel("Ruc:");
        lblCedula.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCedula.setBounds(180, 200, 91, 21);
        getContentPane().add(lblCedula);

        txtRuc = new JTextField();
        txtRuc.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        txtRuc.setBounds(180, 230, 431, 31);
        getContentPane().add(txtRuc);

        lblConvenio = new JLabel("Tiene convenio:");
        lblConvenio.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblConvenio.setBounds(180, 270, 111, 21);
        getContentPane().add(lblConvenio);

        rbtSi = new JRadioButton("Si");
        rbtSi.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        rbtSi.setBackground(Color.WHITE);
        rbtSi.setBounds(320, 280, 101, 17);
        getContentPane().add(rbtSi);

        rbtNo = new JRadioButton("No");
        rbtNo.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        rbtNo.setBackground(Color.WHITE);
        rbtNo.setBounds(450, 280, 101, 17);
        getContentPane().add(rbtNo);

        lblContrasenia = new JLabel("Sector:");
        lblContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblContrasenia.setBounds(180, 310, 91, 21);
        getContentPane().add(lblContrasenia);

        txtSector = new JTextField();
        txtSector.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        txtSector.setBounds(180, 340, 431, 31);
        getContentPane().add(txtSector);

        lblConfirmarContrasenia = new JLabel("Ubicacion:");
        lblConfirmarContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblConfirmarContrasenia.setBounds(180, 380, 151, 21);
        getContentPane().add(lblConfirmarContrasenia);

        txtUbicacion = new JTextField();
        txtUbicacion.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        txtUbicacion.setBounds(180, 410, 431, 31);
        getContentPane().add(txtUbicacion);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnCancelar.setBounds(330, 480, 91, 31);
        getContentPane().add(btnCancelar);

        btnGuardar = new JButton("Guardar", new ImageIcon("./iconos/save.png"));
        btnGuardar.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnGuardar.setBounds(440, 480, 171, 31);
        getContentPane().add(btnGuardar);
    }
}
