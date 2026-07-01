package vista;

import javax.swing.*;
import java.awt.*;

public class VistaMainEstudiante extends JFrame {

    // Central widget components
    public JButton btnVerNotificaciones;
    public JButton btnCerrarSesion_2;
    public JLabel lblInformacion;
    public JLabel lblIcono;
    public JLabel lblSubTitulo;
    public JLabel lblTitulo;
    public JTable tblListaOfertasLaborales;
    public JLabel lblRegistrosRecientes;

    // Toolbar buttons
    public JButton btnOfertas;
    public JButton btnFormularios;
    public JButton btnEnProgreso;
    public JButton btnPostulaciones;
    public JButton btnNotificaciones;
    public JButton btnCerrarSesion;

    // Menu items
    public JMenuItem mniNotificaciones;
    public JMenuItem mniCerrarSesion;
    public JMenuItem mniEnProgreso;
    public JMenuItem mniFormularios;
    public JMenuItem mniPostulaciones;
    public JMenuItem mniOfertas;

    // Menus
    public JMenu menuInicio;
    public JMenu menuPracticas;

    public VistaMainEstudiante() {
        setTitle("::Menu de estudiante");
        setSize(1200, 800);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        initComponents();
    }

    private void initComponents() {
        // Menu bar
        JMenuBar menuBar = new JMenuBar();

        menuInicio = new JMenu("Inicio");
        mniNotificaciones = new JMenuItem("Notificaciones",
                new ImageIcon("./iconos/notification.png"));
        mniCerrarSesion = new JMenuItem("Cerrar sesion",
                new ImageIcon("./iconos/logout.png"));
        menuInicio.add(mniNotificaciones);
        menuInicio.add(mniCerrarSesion);

        menuPracticas = new JMenu("Practicas");
        mniEnProgreso = new JMenuItem("Mi progreso",
                new ImageIcon("./iconos/hourglass_top.png"));
        mniFormularios = new JMenuItem("Mis formularios",
                new ImageIcon("./iconos/forms_add.png"));
        mniPostulaciones = new JMenuItem("Postulaciones",
                new ImageIcon("./iconos/lists.png"));
        mniOfertas = new JMenuItem("Ofertas laborales",
                new ImageIcon("./iconos/work.png"));
        menuPracticas.add(mniEnProgreso);
        menuPracticas.add(mniFormularios);
        menuPracticas.add(mniPostulaciones);
        menuPracticas.add(mniOfertas);

        menuBar.add(menuInicio);
        menuBar.add(menuPracticas);
        setJMenuBar(menuBar);

        // Toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        btnOfertas = new JButton("Ofertas laborales", new ImageIcon("./iconos/work.png"));
        btnFormularios = new JButton("Mis formularios", new ImageIcon("./iconos/forms_add.png"));
        btnEnProgreso = new JButton("Mi progreso", new ImageIcon("./iconos/hourglass_top.png"));
        btnPostulaciones = new JButton("Postulaciones", new ImageIcon("./iconos/lists.png"));
        btnNotificaciones = new JButton("Notificaciones", new ImageIcon("./iconos/notification.png"));
        btnCerrarSesion = new JButton("Cerrar sesion", new ImageIcon("./iconos/logout.png"));

        toolBar.add(btnOfertas);
        toolBar.add(btnFormularios);
        toolBar.add(btnEnProgreso);
        toolBar.add(btnPostulaciones);
        toolBar.add(btnNotificaciones);
        toolBar.add(btnCerrarSesion);

        // Layout
        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel, BorderLayout.CENTER);

        // Central widget components
        btnVerNotificaciones = new JButton("Ver notificaciones",
                new ImageIcon("./iconos/notification.png"));
        btnVerNotificaciones.setBounds(920, 20, 141, 31);
        btnVerNotificaciones.setFont(new Font("Dialog", Font.PLAIN, 10));
        mainPanel.add(btnVerNotificaciones);

        btnCerrarSesion_2 = new JButton("Cerrar sesion",
                new ImageIcon("./iconos/logout.png"));
        btnCerrarSesion_2.setBounds(1070, 20, 111, 31);
        btnCerrarSesion_2.setFont(new Font("Dialog", Font.PLAIN, 10));
        mainPanel.add(btnCerrarSesion_2);

        lblIcono = new JLabel(new ImageIcon("./iconos/school.png"));
        lblIcono.setBounds(20, 20, 31, 31);
        mainPanel.add(lblIcono);

        lblTitulo = new JLabel("Estudiante");
        lblTitulo.setBounds(50, 20, 221, 31);
        lblTitulo.setFont(new Font("Dialog", Font.BOLD, 15));
        mainPanel.add(lblTitulo);

        lblSubTitulo = new JLabel("Gestión de practicas");
        lblSubTitulo.setBounds(40, 80, 251, 31);
        lblSubTitulo.setFont(new Font("Dialog", Font.BOLD, 18));
        mainPanel.add(lblSubTitulo);

        lblInformacion = new JLabel("Panel estudiantil");
        lblInformacion.setBounds(40, 110, 461, 21);
        lblInformacion.setFont(new Font("Dialog", Font.PLAIN, 10));
        mainPanel.add(lblInformacion);

        lblRegistrosRecientes = new JLabel("Lista de ofertas laborales");
        lblRegistrosRecientes.setBounds(60, 200, 301, 31);
        lblRegistrosRecientes.setFont(new Font("Dialog", Font.BOLD, 14));
        mainPanel.add(lblRegistrosRecientes);

        tblListaOfertasLaborales = new JTable();
        JScrollPane spOfertas = new JScrollPane(tblListaOfertasLaborales);
        spOfertas.setBounds(60, 240, 1081, 451);
        mainPanel.add(spOfertas);
    }
}