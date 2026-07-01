package vista;

import javax.swing.*;
import java.awt.*;

public class VistaMainTutorAcademico extends JFrame {

    // Central widget components
    public JButton btnVerNotificaciones;
    public JButton btnCerrarSesion_2;
    public JLabel lblInformacion;
    public JLabel lblIcono;
    public JLabel lblSubTitulo;
    public JLabel lblTitulo;
    public JLabel lblPracticaCompletadas;
    public JLabel lblPracticaProgreso;
    public JLabel lblNumEstudiantesEditar;
    public JLabel lblEstudiante;
    public JLabel lblNumPracticasProgresoEditar;
    public JLabel lblNumPracComprelatasEditar;
    public JTable tblEstudiantes;
    public JLabel lblRegistrosRecientes;

    // Toolbar buttons
    public JButton btnCompletadas;
    public JButton btnEnProgreso;
    public JButton btnNotificaciones;
    public JButton btnCerrarSesion;

    // Menu items
    public JMenuItem mniNotificaciones;
    public JMenuItem mniCerrarSesion;
    public JMenuItem mniEnProgreso;
    public JMenuItem mniCompletadas;

    // Menus
    public JMenu menuInicio;
    public JMenu menuPracticas;

    public VistaMainTutorAcademico() {
        setTitle("::Menu de tutor academico");
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
        mniNotificaciones = new JMenuItem("Notificaciones", new ImageIcon("./iconos/notification.png"));
        mniCerrarSesion = new JMenuItem("Cerrar sesion", new ImageIcon("./iconos/logout.png"));
        menuInicio.add(mniNotificaciones);
        menuInicio.add(mniCerrarSesion);

        menuPracticas = new JMenu("Practicas");
        mniEnProgreso = new JMenuItem("En progreso", new ImageIcon("./iconos/hourglass_top.png"));
        mniCompletadas = new JMenuItem("Completadas", new ImageIcon("./iconos/check_circle.png"));
        menuPracticas.add(mniEnProgreso);
        menuPracticas.add(mniCompletadas);

        menuBar.add(menuInicio);
        menuBar.add(menuPracticas);
        setJMenuBar(menuBar);

        // Toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        btnCompletadas = new JButton("Completadas", new ImageIcon("./iconos/check_circle.png"));
        btnEnProgreso = new JButton("En progreso", new ImageIcon("./iconos/hourglass_top.png"));
        btnNotificaciones = new JButton("Notificaciones", new ImageIcon("./iconos/notification.png"));
        btnCerrarSesion = new JButton("Cerrar sesion", new ImageIcon("./iconos/logout.png"));

        toolBar.add(btnCompletadas);
        toolBar.add(btnEnProgreso);
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

        lblTitulo = new JLabel("Tutor Academico");
        lblTitulo.setBounds(50, 20, 191, 31);
        lblTitulo.setFont(new Font("Dialog", Font.BOLD, 15));
        mainPanel.add(lblTitulo);

        lblSubTitulo = new JLabel("Gestión de practicas");
        lblSubTitulo.setBounds(40, 80, 251, 31);
        lblSubTitulo.setFont(new Font("Dialog", Font.BOLD, 18));
        mainPanel.add(lblSubTitulo);

        lblInformacion = new JLabel("Panel de tutor academico");
        lblInformacion.setBounds(40, 110, 461, 21);
        lblInformacion.setFont(new Font("Dialog", Font.PLAIN, 10));
        mainPanel.add(lblInformacion);

        lblNumEstudiantesEditar = new JLabel("00");
        lblNumEstudiantesEditar.setBounds(60, 160, 61, 31);
        lblNumEstudiantesEditar.setFont(new Font("Dialog", Font.PLAIN, 20));
        mainPanel.add(lblNumEstudiantesEditar);

        lblEstudiante = new JLabel("Estudiantes");
        lblEstudiante.setBounds(50, 190, 71, 21);
        lblEstudiante.setFont(new Font("Dialog", Font.PLAIN, 10));
        mainPanel.add(lblEstudiante);

        lblNumPracticasProgresoEditar = new JLabel("00");
        lblNumPracticasProgresoEditar.setBounds(180, 160, 61, 31);
        lblNumPracticasProgresoEditar.setFont(new Font("Dialog", Font.PLAIN, 20));
        mainPanel.add(lblNumPracticasProgresoEditar);

        lblPracticaProgreso = new JLabel("Practicas en progreso");
        lblPracticaProgreso.setBounds(140, 190, 131, 21);
        lblPracticaProgreso.setFont(new Font("Dialog", Font.PLAIN, 10));
        mainPanel.add(lblPracticaProgreso);

        lblNumPracComprelatasEditar = new JLabel("00");
        lblNumPracComprelatasEditar.setBounds(310, 160, 61, 31);
        lblNumPracComprelatasEditar.setFont(new Font("Dialog", Font.PLAIN, 20));
        mainPanel.add(lblNumPracComprelatasEditar);

        lblPracticaCompletadas = new JLabel("Practicas completas");
        lblPracticaCompletadas.setBounds(280, 190, 121, 21);
        lblPracticaCompletadas.setFont(new Font("Dialog", Font.PLAIN, 10));
        mainPanel.add(lblPracticaCompletadas);

        lblRegistrosRecientes = new JLabel("Lista de estudiantes asignados");
        lblRegistrosRecientes.setBounds(40, 250, 311, 31);
        lblRegistrosRecientes.setFont(new Font("Dialog", Font.BOLD, 14));
        mainPanel.add(lblRegistrosRecientes);

        tblEstudiantes = new JTable();
        JScrollPane spEstudiantes = new JScrollPane(tblEstudiantes);
        spEstudiantes.setBounds(30, 290, 1121, 401);
        mainPanel.add(spEstudiantes);
    }
}
