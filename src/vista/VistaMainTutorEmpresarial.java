package vista;

import javax.swing.*;
import java.awt.*;

public class VistaMainTutorEmpresarial extends JFrame {

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

    public VistaMainTutorEmpresarial() {
        setTitle("::Menu de tutor empresarial");
        setSize(1250, 840);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        initComponents();
    }

    private void initComponents() {
        // Menu bar
        JMenuBar menuBar = new JMenuBar();

        menuInicio = new JMenu("Inicio");
        mniNotificaciones = new JMenuItem("Notificaciones", RecursoVista.icono("notification.png"));
        mniCerrarSesion = new JMenuItem("Cerrar sesion", RecursoVista.icono("logout.png"));
        menuInicio.add(mniNotificaciones);
        menuInicio.add(mniCerrarSesion);

        menuPracticas = new JMenu("Practicas");
        mniEnProgreso = new JMenuItem("En progreso", RecursoVista.icono("hourglass_top.png"));
        mniCompletadas = new JMenuItem("Completadas", RecursoVista.icono("check_circle.png"));
        menuPracticas.add(mniEnProgreso);
        menuPracticas.add(mniCompletadas);

        menuBar.add(menuInicio);
        menuBar.add(menuPracticas);
        setJMenuBar(menuBar);

        // Toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        btnCompletadas = new JButton(RecursoVista.icono("check_circle.png"));
        btnCompletadas.setToolTipText("Completadas");
        btnCompletadas.setFocusable(false);
        btnEnProgreso = new JButton(RecursoVista.icono("hourglass_top.png"));
        btnEnProgreso.setToolTipText("En progreso");
        btnEnProgreso.setFocusable(false);
        btnNotificaciones = new JButton(RecursoVista.icono("notification.png"));
        btnNotificaciones.setToolTipText("Notificaciones");
        btnNotificaciones.setFocusable(false);
        btnCerrarSesion = new JButton(RecursoVista.icono("logout.png"));
        btnCerrarSesion.setToolTipText("Cerrar sesion");
        btnCerrarSesion.setFocusable(false);

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
                RecursoVista.icono("notification.png"));
        btnVerNotificaciones.setBounds(860, 30, 170, 31);
        btnVerNotificaciones.setFont(new Font("Dialog", Font.PLAIN, 10));
        mainPanel.add(btnVerNotificaciones);

        btnCerrarSesion_2 = new JButton("Cerrar sesion",
                RecursoVista.icono("logout.png"));
        btnCerrarSesion_2.setBounds(1040, 30, 140, 31);
        btnCerrarSesion_2.setFont(new Font("Dialog", Font.PLAIN, 10));
        mainPanel.add(btnCerrarSesion_2);

        lblIcono = new JLabel(RecursoVista.icono("apartment.png"));
        lblIcono.setBounds(20, 20, 31, 31);
        mainPanel.add(lblIcono);

        lblTitulo = new JLabel("Tutor empresarial");
        lblTitulo.setBounds(50, 20, 221, 31);
        lblTitulo.setFont(new Font("Dialog", Font.BOLD, 15));
        mainPanel.add(lblTitulo);

        lblSubTitulo = new JLabel("Gestión de practicas");
        lblSubTitulo.setBounds(40, 80, 251, 31);
        lblSubTitulo.setFont(new Font("Dialog", Font.BOLD, 18));
        mainPanel.add(lblSubTitulo);

        lblInformacion = new JLabel("Panel de tutor empresarial");
        lblInformacion.setBounds(40, 110, 461, 21);
        lblInformacion.setFont(new Font("Dialog", Font.PLAIN, 11));
        mainPanel.add(lblInformacion);

        lblNumEstudiantesEditar = new JLabel("00");
        lblNumEstudiantesEditar.setBounds(85, 160, 61, 31);
        lblNumEstudiantesEditar.setFont(new Font("Dialog", Font.PLAIN, 20));
        mainPanel.add(lblNumEstudiantesEditar);

        lblEstudiante = new JLabel("Estudiantes");
        lblEstudiante.setBounds(75, 190, 71, 21);
        lblEstudiante.setFont(new Font("Dialog", Font.PLAIN, 11));
        mainPanel.add(lblEstudiante);

        lblNumPracticasProgresoEditar = new JLabel("00");
        lblNumPracticasProgresoEditar.setBounds(205, 160, 61, 31);
        lblNumPracticasProgresoEditar.setFont(new Font("Dialog", Font.PLAIN, 20));
        mainPanel.add(lblNumPracticasProgresoEditar);

        lblPracticaProgreso = new JLabel("Practicas en progreso");
        lblPracticaProgreso.setBounds(165, 190, 131, 21);
        lblPracticaProgreso.setFont(new Font("Dialog", Font.PLAIN, 11));
        mainPanel.add(lblPracticaProgreso);

        lblNumPracComprelatasEditar = new JLabel("00");
        lblNumPracComprelatasEditar.setBounds(335, 160, 61, 31);
        lblNumPracComprelatasEditar.setFont(new Font("Dialog", Font.PLAIN, 20));
        mainPanel.add(lblNumPracComprelatasEditar);

        lblPracticaCompletadas = new JLabel("Practicas completas");
        lblPracticaCompletadas.setBounds(305, 190, 121, 21);
        lblPracticaCompletadas.setFont(new Font("Dialog", Font.PLAIN, 11));
        mainPanel.add(lblPracticaCompletadas);

        lblRegistrosRecientes = new JLabel("Lista de estudiantes asignados");
        lblRegistrosRecientes.setBounds(65, 250, 331, 31);
        lblRegistrosRecientes.setFont(new Font("Dialog", Font.BOLD, 14));
        mainPanel.add(lblRegistrosRecientes);

        tblEstudiantes = new JTable();
        JScrollPane spEstudiantes = new JScrollPane(tblEstudiantes);
        spEstudiantes.setBounds(55, 290, 1131, 401);
        mainPanel.add(spEstudiantes);
    }
}
