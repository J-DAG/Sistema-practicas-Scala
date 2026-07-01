package vista;

import javax.swing.*;
import java.awt.*;

public class VistaMainCoordinador extends JFrame {

    // Central widget components
    public JLabel lblTitulo;
    public JLabel lblIcono;
    public JLabel lblSubTitulo;
    public JLabel lblInformacion;
    public JButton btnCerrarSesion_2;
    public JButton btnVerNotificaciones;
    public JLabel lblEstudiante;
    public JLabel lblTA;
    public JLabel lblTE_2;
    public JLabel lblNumEstudiantes;
    public JLabel lblNumTA;
    public JLabel lblEmpresa;
    public JLabel lblTE;
    public JLabel lblNumEmpresas;
    public JLabel lblPostulacion;
    public JLabel lblNumPostulaciones;
    public JLabel lblPracticaActiva;
    public JLabel lblNumPracActiva;
    public JTable tblPostulaciones;
    public JLabel lblRegistrosRecientes;

    // Toolbar buttons
    public JButton btnEstudiantes;
    public JButton btnTutores;
    public JButton btnEmpresa;
    public JButton btnPostulacion;
    public JButton btnReportes;
    public JButton btnAcercaDe;
    public JButton btnNotificaciones;
    public JButton btnCerrarSesion;

    // Menu items
    public JMenuItem mniNotificaciones;
    public JMenuItem mniCerrarSesion;
    public JMenuItem mniAcercaDe;
    public JMenuItem mniEmpresa;
    public JMenuItem mniEstudiantes;
    public JMenuItem mniTutores;
    public JMenuItem mniReportes;
    public JMenuItem mniCrearOferta;
    public JMenuItem mniPracticas;
    public JMenuItem mniPostulacion;

    // Menus
    public JMenu menuUsuarios;
    public JMenu menuAyuda;
    public JMenu menuCrear;
    public JMenu menuPracticas;

    public VistaMainCoordinador() {
        setTitle("::Menu Coordinador");
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
        mniNotificaciones = new JMenuItem("Notificaciones", new ImageIcon("./iconos/notification.png"));
        mniCerrarSesion = new JMenuItem("Cerrar sesion", new ImageIcon("./iconos/logout.png"));
        menuUsuarios.add(mniNotificaciones);
        menuUsuarios.add(mniCerrarSesion);

        menuAyuda = new JMenu("Ayuda");
        mniAcercaDe = new JMenuItem("Acerca de", new ImageIcon("./iconos/help.png"));
        menuAyuda.add(mniAcercaDe);

        menuCrear = new JMenu("Administración");
        mniEmpresa = new JMenuItem("Empresas", new ImageIcon("./iconos/apartment.png"));
        mniEstudiantes = new JMenuItem("Estudiantes", new ImageIcon("./iconos/account.png"));
        mniTutores = new JMenuItem("Tutores", new ImageIcon("./iconos/school.png"));
        mniReportes = new JMenuItem("Reportes", new ImageIcon("./iconos/stats.png"));
        menuCrear.add(mniEmpresa);
        menuCrear.add(mniEstudiantes);
        menuCrear.add(mniTutores);
        menuCrear.add(mniReportes);

        menuPracticas = new JMenu("Prácticas");
        mniCrearOferta = new JMenuItem("Crear oferta", new ImageIcon("./iconos/forms_add.png"));
        mniPracticas = new JMenuItem("Practicas", new ImageIcon("./iconos/work.png"));
        mniPostulacion = new JMenuItem("Postulaciones", new ImageIcon("./iconos/forms_add.png"));
        menuPracticas.add(mniCrearOferta);
        menuPracticas.add(mniPracticas);
        menuPracticas.add(mniPostulacion);

        menuBar.add(menuUsuarios);
        menuBar.add(menuCrear);
        menuBar.add(menuPracticas);
        menuBar.add(menuAyuda);
        setJMenuBar(menuBar);

        // Toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        btnEstudiantes = new JButton("Estudiantes", new ImageIcon("./iconos/account.png"));
        btnTutores = new JButton("Tutores", new ImageIcon("./iconos/school.png"));
        btnEmpresa = new JButton("Empresas", new ImageIcon("./iconos/apartment.png"));
        btnPostulacion = new JButton("Postulaciones", new ImageIcon("./iconos/forms_add.png"));
        btnReportes = new JButton("Reportes", new ImageIcon("./iconos/stats.png"));
        btnAcercaDe = new JButton("Acerca de", new ImageIcon("./iconos/help.png"));
        btnNotificaciones = new JButton("Notificaciones", new ImageIcon("./iconos/notification.png"));
        btnCerrarSesion = new JButton("Cerrar sesion", new ImageIcon("./iconos/logout.png"));

        toolBar.add(btnEstudiantes);
        toolBar.add(btnTutores);
        toolBar.add(btnEmpresa);
        toolBar.add(btnPostulacion);
        toolBar.add(btnReportes);
        toolBar.add(btnAcercaDe);
        toolBar.add(btnNotificaciones);
        toolBar.add(btnCerrarSesion);

        // Layout
        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel, BorderLayout.CENTER);

        // Central widget components
        lblIcono = new JLabel(new ImageIcon("./iconos/shield.png"));
        lblIcono.setBounds(20, 20, 31, 31);
        mainPanel.add(lblIcono);

        lblTitulo = new JLabel("Coordinación");
        lblTitulo.setBounds(50, 20, 161, 31);
        lblTitulo.setFont(new Font("Dialog", Font.BOLD, 15));
        mainPanel.add(lblTitulo);

        lblSubTitulo = new JLabel("Gestión de practicas");
        lblSubTitulo.setBounds(40, 80, 251, 31);
        lblSubTitulo.setFont(new Font("Dialog", Font.BOLD, 18));
        mainPanel.add(lblSubTitulo);

        lblInformacion = new JLabel("Panel de coordinador");
        lblInformacion.setBounds(40, 110, 141, 21);
        lblInformacion.setFont(new Font("Dialog", Font.PLAIN, 10));
        mainPanel.add(lblInformacion);

        btnVerNotificaciones = new JButton("Ver notificaciones",
                new ImageIcon("./iconos/notification.png"));
        btnVerNotificaciones.setBounds(900, 30, 141, 31);
        btnVerNotificaciones.setFont(new Font("Dialog", Font.PLAIN, 10));
        mainPanel.add(btnVerNotificaciones);

        btnCerrarSesion_2 = new JButton("Cerrar sesion",
                new ImageIcon("./iconos/logout.png"));
        btnCerrarSesion_2.setBounds(1050, 30, 111, 31);
        btnCerrarSesion_2.setFont(new Font("Dialog", Font.PLAIN, 10));
        mainPanel.add(btnCerrarSesion_2);

        // Statistics
        lblNumEstudiantes = new JLabel("00");
        lblNumEstudiantes.setBounds(60, 160, 61, 31);
        lblNumEstudiantes.setFont(new Font("Dialog", Font.PLAIN, 20));
        mainPanel.add(lblNumEstudiantes);

        lblEstudiante = new JLabel("Estudiantes");
        lblEstudiante.setBounds(50, 190, 71, 21);
        lblEstudiante.setFont(new Font("Dialog", Font.PLAIN, 10));
        mainPanel.add(lblEstudiante);

        lblNumTA = new JLabel("00");
        lblNumTA.setBounds(180, 160, 61, 31);
        lblNumTA.setFont(new Font("Dialog", Font.PLAIN, 20));
        mainPanel.add(lblNumTA);

        lblTA = new JLabel("Tutores académicos");
        lblTA.setBounds(140, 190, 121, 21);
        lblTA.setFont(new Font("Dialog", Font.PLAIN, 10));
        mainPanel.add(lblTA);

        lblTE = new JLabel("00");
        lblTE.setBounds(310, 160, 61, 31);
        lblTE.setFont(new Font("Dialog", Font.PLAIN, 20));
        mainPanel.add(lblTE);

        lblTE_2 = new JLabel("Tutores empresariales");
        lblTE_2.setBounds(270, 190, 141, 21);
        lblTE_2.setFont(new Font("Dialog", Font.PLAIN, 10));
        mainPanel.add(lblTE_2);

        lblNumEmpresas = new JLabel("00");
        lblNumEmpresas.setBounds(430, 160, 61, 31);
        lblNumEmpresas.setFont(new Font("Dialog", Font.PLAIN, 20));
        mainPanel.add(lblNumEmpresas);

        lblEmpresa = new JLabel("Empresas");
        lblEmpresa.setBounds(420, 190, 71, 21);
        lblEmpresa.setFont(new Font("Dialog", Font.PLAIN, 10));
        mainPanel.add(lblEmpresa);

        lblNumPostulaciones = new JLabel("00");
        lblNumPostulaciones.setBounds(530, 160, 61, 31);
        lblNumPostulaciones.setFont(new Font("Dialog", Font.PLAIN, 20));
        mainPanel.add(lblNumPostulaciones);

        lblPostulacion = new JLabel("Postulaciones");
        lblPostulacion.setBounds(510, 190, 81, 21);
        lblPostulacion.setFont(new Font("Dialog", Font.PLAIN, 10));
        mainPanel.add(lblPostulacion);

        lblNumPracActiva = new JLabel("00");
        lblNumPracActiva.setBounds(640, 160, 61, 31);
        lblNumPracActiva.setFont(new Font("Dialog", Font.PLAIN, 20));
        mainPanel.add(lblNumPracActiva);

        lblPracticaActiva = new JLabel("Practicas activas");
        lblPracticaActiva.setBounds(610, 190, 101, 21);
        lblPracticaActiva.setFont(new Font("Dialog", Font.PLAIN, 10));
        mainPanel.add(lblPracticaActiva);

        lblRegistrosRecientes = new JLabel("Postulaciones Recientes");
        lblRegistrosRecientes.setBounds(80, 230, 251, 31);
        lblRegistrosRecientes.setFont(new Font("Dialog", Font.BOLD, 14));
        mainPanel.add(lblRegistrosRecientes);

        tblPostulaciones = new JTable();
        JScrollPane spPostulaciones = new JScrollPane(tblPostulaciones);
        spPostulaciones.setBounds(80, 270, 1031, 401);
        mainPanel.add(spPostulaciones);
    }
}
