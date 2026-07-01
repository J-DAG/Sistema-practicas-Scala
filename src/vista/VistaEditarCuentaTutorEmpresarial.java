package vista;

import javax.swing.*;
import java.awt.*;

public class VistaEditarCuentaTutorEmpresarial extends JFrame {

    public JLabel lblTitulo;
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

    public VistaEditarCuentaTutorEmpresarial() {
        setTitle("Editar informacion tutor empresarial");
        setSize(800, 700);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        lblTitulo = new JLabel("Editar Informacion de Tutor Empresarial");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitulo.setBounds(180, 10, 401, 31);
        getContentPane().add(lblTitulo);

        lblNombres = new JLabel("Nombres:");
        lblNombres.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblNombres.setBounds(180, 50, 71, 21);
        getContentPane().add(lblNombres);

        txtNombres = new JTextField();
        txtNombres.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        txtNombres.setBounds(180, 80, 431, 31);
        getContentPane().add(txtNombres);

        lblApellido = new JLabel("Apellidos:");
        lblApellido.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblApellido.setBounds(180, 120, 71, 21);
        getContentPane().add(lblApellido);

        txtApellidos = new JTextField();
        txtApellidos.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        txtApellidos.setBounds(180, 150, 431, 31);
        getContentPane().add(txtApellidos);

        lblCorreoElectronico = new JLabel("Correo electronico:");
        lblCorreoElectronico.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCorreoElectronico.setBounds(180, 190, 131, 21);
        getContentPane().add(lblCorreoElectronico);

        txtCorreoElectronico = new JTextField();
        txtCorreoElectronico.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        txtCorreoElectronico.setBounds(180, 220, 431, 31);
        getContentPane().add(txtCorreoElectronico);

        lblCedula = new JLabel("Cedula:");
        lblCedula.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCedula.setBounds(180, 260, 91, 21);
        getContentPane().add(lblCedula);

        txtCedula = new JTextField();
        txtCedula.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        txtCedula.setBounds(180, 290, 431, 31);
        getContentPane().add(txtCedula);

        lblEmpresa = new JLabel("Empresa:");
        lblEmpresa.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblEmpresa.setBounds(180, 340, 111, 21);
        getContentPane().add(lblEmpresa);

        cbxListaEmpresa = new JComboBox<>();
        cbxListaEmpresa.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        cbxListaEmpresa.setBounds(180, 370, 431, 31);
        getContentPane().add(cbxListaEmpresa);

        lblCargo = new JLabel("Cargo en la empresa:");
        lblCargo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCargo.setBounds(180, 410, 151, 21);
        getContentPane().add(lblCargo);

        txtCargo = new JTextField();
        txtCargo.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        txtCargo.setBounds(180, 440, 431, 31);
        getContentPane().add(txtCargo);

        lblContrasenia = new JLabel("Contrasenia:");
        lblContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblContrasenia.setBounds(180, 480, 91, 21);
        getContentPane().add(lblContrasenia);

        txtContrasenia = new JPasswordField();
        txtContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        txtContrasenia.setBounds(180, 510, 431, 31);
        getContentPane().add(txtContrasenia);

        lblConfirmarContrasenia = new JLabel("Confirmar contrasenia:");
        lblConfirmarContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblConfirmarContrasenia.setBounds(180, 550, 151, 21);
        getContentPane().add(lblConfirmarContrasenia);

        txtConfirmarContrasenia = new JPasswordField();
        txtConfirmarContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        txtConfirmarContrasenia.setBounds(180, 580, 431, 31);
        getContentPane().add(txtConfirmarContrasenia);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnCancelar.setBounds(330, 630, 91, 31);
        getContentPane().add(btnCancelar);

        btnGuardar = new JButton("Guardar", new ImageIcon("./iconos/person_add.png"));
        btnGuardar.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnGuardar.setBounds(440, 630, 171, 31);
        getContentPane().add(btnGuardar);
    }
}
