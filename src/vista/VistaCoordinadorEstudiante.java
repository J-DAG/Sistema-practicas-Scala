package vista;

import javax.swing.*;
import java.awt.*;

public class VistaCoordinadorEstudiante extends JFrame {

    // Nav bar buttons
    public JButton btnInicio;
    public JButton btnReportes;
    public JButton btnEstudiantes;
    public JButton btnTutores;
    public JButton btnOfertas;
    public JButton btnEmpresa;
    public JButton btnPracticas;
    public JButton btnPostulaciones;
    public JButton btnCerrarSesion;

    // Header labels
    public JLabel lblIcono;
    public JLabel lblTitulo;
    public JLabel lblSubTitulo;
    public JLabel lblInformacion;

    // Content
    public JLabel lblEstadoAplicacionPracticas;
    public JPanel widgetGraficoPastel;
    public JLabel lblIconoBuscar;
    public JTextField txtBuscar;
    public JButton btnBuscar;
    public JTable tblEstudiantes;

    public VistaCoordinadorEstudiante() {
        setTitle("Administración de estudiantes");
        setSize(1300, 840);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        getContentPane().setLayout(null);
        initComponents();
    }

    private void initComponents() {
        // Navigation bar buttons (y=30)
        lblIcono = new JLabel(RecursoVista.icono("shield.png"));
        lblIcono.setBounds(10, 20, 31, 31);
        add(lblIcono);

        lblTitulo = new JLabel("Coordinación");
        lblTitulo.setBounds(40, 20, 141, 31);
        lblTitulo.setFont(new Font("Dialog", Font.BOLD, 15));
        add(lblTitulo);

        btnInicio = new JButton("Inicio", RecursoVista.icono("home.png"));
        btnInicio.setBounds(170, 30, 90, 31);
        btnInicio.setFont(new Font("Dialog", Font.PLAIN, 10));
        add(btnInicio);

        btnReportes = new JButton("Reportes", RecursoVista.icono("stats.png"));
        btnReportes.setBounds(270, 30, 105, 31);
        btnReportes.setFont(new Font("Dialog", Font.PLAIN, 10));
        add(btnReportes);

        btnEstudiantes = new JButton("Estudiantes", RecursoVista.icono("account.png"));
        btnEstudiantes.setBounds(385, 30, 125, 31);
        btnEstudiantes.setFont(new Font("Dialog", Font.PLAIN, 10));
        add(btnEstudiantes);

        btnTutores = new JButton("Tutores", RecursoVista.icono("school.png"));
        btnTutores.setBounds(520, 30, 105, 31);
        btnTutores.setFont(new Font("Dialog", Font.PLAIN, 10));
        add(btnTutores);

        btnOfertas = new JButton("Ofertas", RecursoVista.icono("forms_add.png"));
        btnOfertas.setBounds(635, 30, 100, 31);
        btnOfertas.setFont(new Font("Dialog", Font.PLAIN, 10));
        add(btnOfertas);

        btnEmpresa = new JButton("Empresas", RecursoVista.icono("apartment.png"));
        btnEmpresa.setBounds(745, 30, 115, 31);
        btnEmpresa.setFont(new Font("Dialog", Font.PLAIN, 10));
        add(btnEmpresa);

        btnPracticas = new JButton("Practicas", RecursoVista.icono("work.png"));
        btnPracticas.setBounds(870, 30, 115, 31);
        btnPracticas.setFont(new Font("Dialog", Font.PLAIN, 10));
        add(btnPracticas);

        btnPostulaciones = new JButton("Postulaciones", RecursoVista.icono("lists.png"));
        btnPostulaciones.setBounds(995, 30, 130, 31);
        btnPostulaciones.setFont(new Font("Dialog", Font.PLAIN, 10));
        add(btnPostulaciones);

        btnCerrarSesion = new JButton("Cerrar sesion", RecursoVista.icono("logout.png"));
        btnCerrarSesion.setBounds(1135, 30, 135, 31);
        btnCerrarSesion.setFont(new Font("Dialog", Font.PLAIN, 10));
        add(btnCerrarSesion);

        // Header info
        lblInformacion = new JLabel("Panel de coordinador");
        lblInformacion.setBounds(30, 110, 141, 21);
        lblInformacion.setFont(new Font("Dialog", Font.PLAIN, 11));
        add(lblInformacion);

        lblSubTitulo = new JLabel("Administración de estudiantes");
        lblSubTitulo.setBounds(30, 80, 371, 31);
        lblSubTitulo.setFont(new Font("Dialog", Font.BOLD, 18));
        add(lblSubTitulo);

        // Content area
        lblEstadoAplicacionPracticas = new JLabel("Estado de Aplicación a Prácticas");
        lblEstadoAplicacionPracticas.setBounds(80, 190, 391, 31);
        lblEstadoAplicacionPracticas.setFont(new Font("Dialog", Font.BOLD, 18));
        add(lblEstadoAplicacionPracticas);

        widgetGraficoPastel = new JPanel(new BorderLayout());
        widgetGraficoPastel.setBounds(80, 240, 520, 320);
        widgetGraficoPastel.setBackground(Color.WHITE);
        widgetGraficoPastel.setBorder(BorderFactory.createTitledBorder("Gráfico de Pastel"));
        JLabel lblChartPlaceholder = new JLabel("Gráfico", SwingConstants.CENTER);
        widgetGraficoPastel.add(lblChartPlaceholder, BorderLayout.CENTER);
        add(widgetGraficoPastel);

        // Search area
        lblIconoBuscar = new JLabel(RecursoVista.icono("search.png"));
        lblIconoBuscar.setBounds(630, 190, 31, 31);
        add(lblIconoBuscar);

        txtBuscar = new JTextField();
        txtBuscar.setBounds(670, 190, 431, 31);
        txtBuscar.setFont(new Font("Dialog", Font.PLAIN, 11));
        add(txtBuscar);

        btnBuscar = new JButton("Buscar", RecursoVista.icono("search.png"));
        btnBuscar.setBounds(1110, 190, 111, 31);
        btnBuscar.setFont(new Font("Dialog", Font.PLAIN, 10));
        add(btnBuscar);

        // Table
        tblEstudiantes = new JTable();
        JScrollPane spEstudiantes = new JScrollPane(tblEstudiantes);
        spEstudiantes.setBounds(640, 240, 591, 501);
        add(spEstudiantes);
    }
}
