package vista;

import javax.swing.*;
import java.awt.*;

public class VistaMainAdmin extends JFrame {

    // Central widget components
    public JLabel lblTitulo;
    public JLabel lblIcono;
    public JLabel lblSubTitulo;
    public JLabel lblInformacion;
    public JButton btnCerrarSesion_1;
    public JLabel lblEstudiante;
    public JLabel lblTA;
    public JLabel lblTE_2;
    public JLabel lblCoordinador;
    public JLabel lblAdministrador;
    public JLabel lblNumEstudiantes;
    public JLabel lblNumTA;
    public JLabel lblEmpresa;
    public JLabel lblTE;
    public JLabel lblNumCoordinadores;
    public JLabel lblNumAdministradores;
    public JLabel lblNumEmpresas;
    public JTable tblUsuarios;
    public JLabel lblRegistrosRecientes;

    // Toolbar buttons
    public JButton btnEmpresa;
    public JButton btnUsuarios;
    public JButton btnAcercaDe;
    public JButton btnCerrarSesion;

    // Menu items
    public JMenuItem mniEmpresa;
    public JMenuItem mniUsuarios;
    public JMenuItem mniCerrarSesion;
    public JMenuItem mniAcercaDe;

    // Menus
    public JMenu menuUsuarios;
    public JMenu menuAyuda;

    public VistaMainAdmin() {
        setTitle("::Administrador");
        setSize(1200, 800);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        initComponents();
    }

    private void initComponents() {
        // Menu bar
        JMenuBar menuBar = new JMenuBar();

        menuUsuarios = new JMenu("Inicio");
        mniEmpresa = new JMenuItem("Empresas", new ImageIcon("./iconos/apartment.png"));
        mniUsuarios = new JMenuItem("Usuarios", new ImageIcon("./iconos/account.png"));
        mniCerrarSesion = new JMenuItem("Cerrar sesion", new ImageIcon("./iconos/logout.png"));
        menuUsuarios.add(mniEmpresa);
        menuUsuarios.add(mniUsuarios);
        menuUsuarios.add(mniCerrarSesion);

        menuAyuda = new JMenu("Ayuda");
        mniAcercaDe = new JMenuItem("Acerca de", new ImageIcon("./iconos/help.png"));
        menuAyuda.add(mniAcercaDe);

        menuBar.add(menuUsuarios);
        menuBar.add(menuAyuda);
        setJMenuBar(menuBar);

        // Toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        btnEmpresa = new JButton("Empresas", new ImageIcon("./iconos/apartment.png"));
        btnUsuarios = new JButton("Usuarios", new ImageIcon("./iconos/account.png"));
        btnAcercaDe = new JButton("Acerca de", new ImageIcon("./iconos/help.png"));
        btnCerrarSesion = new JButton("Cerrar sesion", new ImageIcon("./iconos/logout.png"));

        toolBar.add(btnEmpresa);
        toolBar.add(btnUsuarios);
        toolBar.add(btnAcercaDe);
        toolBar.add(btnCerrarSesion);

        // Layout
        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel, BorderLayout.CENTER);

        // Central widget components
        lblIcono = new JLabel(new ImageIcon("./iconos/settings.png"));
        lblIcono.setBounds(20, 20, 31, 31);
        mainPanel.add(lblIcono);

        lblTitulo = new JLabel("Administrador");
        lblTitulo.setBounds(50, 20, 161, 31);
        lblTitulo.setFont(new Font("Dialog", Font.BOLD, 15));
        mainPanel.add(lblTitulo);

        lblSubTitulo = new JLabel("Gestión de practicas");
        lblSubTitulo.setBounds(40, 80, 251, 31);
        lblSubTitulo.setFont(new Font("Dialog", Font.BOLD, 18));
        mainPanel.add(lblSubTitulo);

        lblInformacion = new JLabel("Panel del Administrador");
        lblInformacion.setBounds(40, 110, 141, 21);
        lblInformacion.setFont(new Font("Dialog", Font.PLAIN, 10));
        mainPanel.add(lblInformacion);

        btnCerrarSesion_1 = new JButton("Cerrar sesion",
                new ImageIcon("./iconos/logout.png"));
        btnCerrarSesion_1.setBounds(1030, 30, 111, 31);
        btnCerrarSesion_1.setFont(new Font("Dialog", Font.PLAIN, 10));
        mainPanel.add(btnCerrarSesion_1);

        // Statistics labels
        lblNumEstudiantes = new JLabel("00");
        lblNumEstudiantes.setBounds(50, 160, 61, 31);
        lblNumEstudiantes.setFont(new Font("Dialog", Font.PLAIN, 20));
        mainPanel.add(lblNumEstudiantes);

        lblEstudiante = new JLabel("Estudiantes");
        lblEstudiante.setBounds(50, 180, 71, 51);
        lblEstudiante.setFont(new Font("Dialog", Font.PLAIN, 10));
        mainPanel.add(lblEstudiante);

        lblNumTA = new JLabel("00");
        lblNumTA.setBounds(180, 160, 61, 31);
        lblNumTA.setFont(new Font("Dialog", Font.PLAIN, 20));
        mainPanel.add(lblNumTA);

        lblTA = new JLabel("Tutores académicos");
        lblTA.setBounds(150, 180, 121, 51);
        lblTA.setFont(new Font("Dialog", Font.PLAIN, 10));
        mainPanel.add(lblTA);

        lblTE = new JLabel("00");
        lblTE.setBounds(330, 160, 61, 31);
        lblTE.setFont(new Font("Dialog", Font.PLAIN, 20));
        mainPanel.add(lblTE);

        lblTE_2 = new JLabel("Tutores empresariales");
        lblTE_2.setBounds(290, 180, 141, 51);
        lblTE_2.setFont(new Font("Dialog", Font.PLAIN, 10));
        mainPanel.add(lblTE_2);

        lblNumCoordinadores = new JLabel("00");
        lblNumCoordinadores.setBounds(470, 160, 61, 31);
        lblNumCoordinadores.setFont(new Font("Dialog", Font.PLAIN, 20));
        mainPanel.add(lblNumCoordinadores);

        lblCoordinador = new JLabel("Coordinadores");
        lblCoordinador.setBounds(450, 180, 91, 51);
        lblCoordinador.setFont(new Font("Dialog", Font.PLAIN, 10));
        mainPanel.add(lblCoordinador);

        lblNumAdministradores = new JLabel("00");
        lblNumAdministradores.setBounds(600, 160, 61, 31);
        lblNumAdministradores.setFont(new Font("Dialog", Font.PLAIN, 20));
        mainPanel.add(lblNumAdministradores);

        lblAdministrador = new JLabel("Administradores");
        lblAdministrador.setBounds(570, 180, 101, 51);
        lblAdministrador.setFont(new Font("Dialog", Font.PLAIN, 10));
        mainPanel.add(lblAdministrador);

        lblNumEmpresas = new JLabel("00");
        lblNumEmpresas.setBounds(710, 160, 61, 31);
        lblNumEmpresas.setFont(new Font("Dialog", Font.PLAIN, 20));
        mainPanel.add(lblNumEmpresas);

        lblEmpresa = new JLabel("Empresas");
        lblEmpresa.setBounds(700, 180, 71, 51);
        lblEmpresa.setFont(new Font("Dialog", Font.PLAIN, 10));
        mainPanel.add(lblEmpresa);

        lblRegistrosRecientes = new JLabel("Registros Recientes");
        lblRegistrosRecientes.setBounds(40, 240, 251, 31);
        lblRegistrosRecientes.setFont(new Font("Dialog", Font.BOLD, 14));
        mainPanel.add(lblRegistrosRecientes);

        tblUsuarios = new JTable();
        JScrollPane spUsuarios = new JScrollPane(tblUsuarios);
        spUsuarios.setBounds(40, 280, 1111, 401);
        mainPanel.add(spUsuarios);
    }
}
