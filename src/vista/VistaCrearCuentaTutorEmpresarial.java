package vista;

import javax.swing.*;
import java.awt.*;

public class VistaCrearCuentaTutorEmpresarial extends JFrame {

    public JLabel lblTitulo;
    public JLabel lblSubtitulo;
    public JLabel lblNombres;
    public JLabel lblApellido;
    public JLabel lblCorreoElectronico;
    public JLabel lblCedula;
    public JLabel lblEmpresa;
    public JLabel lblCargo;
    public JLabel lblContrasenia;
    public JLabel lblConfirmarContrasenia;
    public JTextField txtNombres;
    public JTextField txtApellidos;
    public JTextField txtCorreoElectronico;
    public JTextField txtCedula;
    public JTextField txtCargo;
    public JComboBox<String> cbxListaEmpresa;
    public JPasswordField txtContrasenia;
    public JPasswordField txtConfirmarContrasenia;
    public JButton btnCancelar;
    public JButton btnGuardar;

    public VistaCrearCuentaTutorEmpresarial() {
        setTitle("Registrar tutor empresarial");
        setSize(850, 740);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        lblTitulo = new JLabel("Registrar Tutor Empresarial");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitulo.setBounds(180, 10, 331, 21);
        getContentPane().add(lblTitulo);

        lblSubtitulo = new JLabel("Complete los campos requeridos");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSubtitulo.setBounds(180, 40, 291, 21);
        getContentPane().add(lblSubtitulo);

        lblNombres = new JLabel("Nombres:");
        lblNombres.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblNombres.setBounds(180, 70, 71, 21);
        getContentPane().add(lblNombres);

        txtNombres = new JTextField();
        txtNombres.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtNombres.setBounds(180, 100, 431, 31);
        getContentPane().add(txtNombres);

        lblApellido = new JLabel("Apellidos:");
        lblApellido.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblApellido.setBounds(180, 140, 71, 21);
        getContentPane().add(lblApellido);

        txtApellidos = new JTextField();
        txtApellidos.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtApellidos.setBounds(205, 170, 431, 31);
        getContentPane().add(txtApellidos);

        lblCorreoElectronico = new JLabel("Correo electronico:");
        lblCorreoElectronico.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCorreoElectronico.setBounds(205, 210, 131, 21);
        getContentPane().add(lblCorreoElectronico);

        txtCorreoElectronico = new JTextField();
        txtCorreoElectronico.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtCorreoElectronico.setBounds(205, 240, 431, 31);
        getContentPane().add(txtCorreoElectronico);

        lblCedula = new JLabel("Cedula:");
        lblCedula.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCedula.setBounds(205, 280, 91, 21);
        getContentPane().add(lblCedula);

        txtCedula = new JTextField();
        txtCedula.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtCedula.setBounds(205, 310, 431, 31);
        getContentPane().add(txtCedula);

        lblEmpresa = new JLabel("Empresa:");
        lblEmpresa.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblEmpresa.setBounds(205, 360, 111, 21);
        getContentPane().add(lblEmpresa);

        cbxListaEmpresa = new JComboBox<>();
        cbxListaEmpresa.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        cbxListaEmpresa.setBounds(205, 390, 431, 31);
        getContentPane().add(cbxListaEmpresa);

        lblCargo = new JLabel("Cargo en la empresa:");
        lblCargo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCargo.setBounds(205, 430, 151, 21);
        getContentPane().add(lblCargo);

        txtCargo = new JTextField();
        txtCargo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtCargo.setBounds(205, 460, 431, 31);
        getContentPane().add(txtCargo);

        lblContrasenia = new JLabel("Contrasenia:");
        lblContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblContrasenia.setBounds(205, 500, 91, 21);
        getContentPane().add(lblContrasenia);

        txtContrasenia = new JPasswordField();
        txtContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtContrasenia.setBounds(205, 530, 431, 31);
        getContentPane().add(txtContrasenia);

        lblConfirmarContrasenia = new JLabel("Confirmar contrasenia:");
        lblConfirmarContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblConfirmarContrasenia.setBounds(205, 570, 151, 21);
        getContentPane().add(lblConfirmarContrasenia);

        txtConfirmarContrasenia = new JPasswordField();
        txtConfirmarContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtConfirmarContrasenia.setBounds(205, 600, 431, 31);
        getContentPane().add(txtConfirmarContrasenia);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnCancelar.setBounds(355, 650, 91, 31);
        getContentPane().add(btnCancelar);

        btnGuardar = new JButton("Guardar", RecursoVista.icono("person_add.png"));
        btnGuardar.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnGuardar.setBounds(465, 650, 171, 31);
        getContentPane().add(btnGuardar);
    }
}
