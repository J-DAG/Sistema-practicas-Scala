package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaAdminUsuario extends JFrame {

    public JLabel lblIcono;
    public JLabel lblTitulo;
    public JLabel lblSubTitulo;
    public JLabel lblInformacion;
    public JButton btnInicio;
    public JButton btnUsuarios;
    public JButton btnEmpresa;
    public JButton btnCerrarSesion;
    public JButton btnBuscar;
    public JButton btnNuevoUsuario;
    public JButton btnEliminar;
    public JButton btnEditar;
    public JTextField txtBuscar;
    public JTable tblUsuarios;

    public VistaAdminUsuario() {
        setTitle("Administracion de usuarios");
        setSize(1200, 800);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        lblIcono = new JLabel(new ImageIcon("./iconos/settings.png"));
        lblIcono.setBounds(20, 20, 31, 31);
        getContentPane().add(lblIcono);

        lblTitulo = new JLabel("Administrador");
        lblTitulo.setFont(new Font("Dialog", Font.BOLD, 15));
        lblTitulo.setBounds(50, 20, 161, 31);
        getContentPane().add(lblTitulo);

        btnInicio = new JButton("Inicio", new ImageIcon("./iconos/home.png"));
        btnInicio.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnInicio.setBounds(680, 30, 111, 31);
        getContentPane().add(btnInicio);

        btnUsuarios = new JButton("Usuarios", new ImageIcon("./iconos/account.png"));
        btnUsuarios.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnUsuarios.setBounds(800, 30, 111, 31);
        getContentPane().add(btnUsuarios);

        btnEmpresa = new JButton("Empresas", new ImageIcon("./iconos/apartment.png"));
        btnEmpresa.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnEmpresa.setBounds(920, 30, 111, 31);
        getContentPane().add(btnEmpresa);

        btnCerrarSesion = new JButton("Cerrar sesion", new ImageIcon("./iconos/logout.png"));
        btnCerrarSesion.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnCerrarSesion.setBounds(1040, 30, 111, 31);
        getContentPane().add(btnCerrarSesion);

        lblSubTitulo = new JLabel("Gestion de usuarios");
        lblSubTitulo.setFont(new Font("Dialog", Font.BOLD, 18));
        lblSubTitulo.setBounds(20, 80, 241, 21);
        getContentPane().add(lblSubTitulo);

        lblInformacion = new JLabel("Administracion de estudiantes y tutores");
        lblInformacion.setFont(new Font("Dialog", Font.PLAIN, 10));
        lblInformacion.setBounds(20, 110, 391, 21);
        getContentPane().add(lblInformacion);

        JLabel lblIcono_2 = new JLabel(new ImageIcon("./iconos/search.png"));
        lblIcono_2.setBounds(90, 160, 31, 31);
        getContentPane().add(lblIcono_2);

        txtBuscar = new JTextField();
        txtBuscar.setFont(new Font("Dialog", Font.PLAIN, 10));
        txtBuscar.setBounds(120, 160, 741, 31);
        getContentPane().add(txtBuscar);

        btnBuscar = new JButton("Buscar", new ImageIcon("./iconos/search.png"));
        btnBuscar.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnBuscar.setBounds(870, 160, 111, 31);
        getContentPane().add(btnBuscar);

        btnNuevoUsuario = new JButton("Nuevo usuario", new ImageIcon("./iconos/add_24.png"));
        btnNuevoUsuario.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnNuevoUsuario.setBounds(990, 160, 131, 31);
        getContentPane().add(btnNuevoUsuario);

        tblUsuarios = new JTable(new DefaultTableModel());
        JScrollPane scrollUsuarios = new JScrollPane(tblUsuarios);
        scrollUsuarios.setBounds(70, 210, 1071, 501);
        getContentPane().add(scrollUsuarios);

        btnEliminar = new JButton("Eliminar", new ImageIcon("./iconos/delete.png"));
        btnEliminar.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnEliminar.setBounds(870, 720, 131, 31);
        getContentPane().add(btnEliminar);

        btnEditar = new JButton("Editar", new ImageIcon("./iconos/edit.png"));
        btnEditar.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnEditar.setBounds(1010, 720, 131, 31);
        getContentPane().add(btnEditar);
    }
}
