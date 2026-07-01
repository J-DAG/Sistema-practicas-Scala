package vista;

import javax.swing.*;
import java.awt.*;

public class VistaCoordinadorReportes extends JFrame {

    public JLabel lblIcono;
    public JLabel lblTitulo;
    public JLabel lblSubTitulo;
    public JLabel lblInformacion;
    public JLabel lblNumEstudiantes;
    public JLabel lblEstudiante;
    public JLabel lblNumTA;
    public JLabel lblTA;
    public JLabel lblTE;
    public JLabel lblTE_2;
    public JLabel lblNumEmpresas;
    public JLabel lblEmpresa;
    public JLabel lblNumPostulaciones;
    public JLabel lblPostulacion;
    public JLabel lblNumPracActiva;
    public JLabel lblPracticaActiva;
    public JLabel lblNumPracFinalizada;
    public JLabel lblPracticaFinalizada;
    public JLabel lblEstadoAplicacionPracticas;
    public JLabel lblEstudiantesXciclo;
    public JButton btnInicio;
    public JButton btnReportes;
    public JButton btnEstudiantes;
    public JButton btnTutores;
    public JButton btnOfertas;
    public JButton btnEmpresa;
    public JButton btnPracticas;
    public JButton btnPostulaciones;
    public JButton btnCerrarSesion;
    public JPanel widgetGraficoPastel;
    public JPanel widgetGraficoBarras;

    public VistaCoordinadorReportes() {
        setTitle("Informacion y reportes");
        setSize(1200, 800);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        lblIcono = new JLabel(new ImageIcon("./iconos/shield.png"));
        lblIcono.setBounds(10, 20, 31, 31);
        getContentPane().add(lblIcono);

        lblTitulo = new JLabel("Coordinacion");
        lblTitulo.setFont(new Font("Dialog", Font.BOLD, 15));
        lblTitulo.setBounds(40, 20, 141, 31);
        getContentPane().add(lblTitulo);

        btnInicio = new JButton("Inicio", new ImageIcon("./iconos/home.png"));
        btnInicio.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnInicio.setBounds(180, 30, 91, 31);
        getContentPane().add(btnInicio);

        btnReportes = new JButton("Reportes", new ImageIcon("./iconos/stats.png"));
        btnReportes.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnReportes.setBounds(280, 30, 101, 31);
        getContentPane().add(btnReportes);

        btnEstudiantes = new JButton("Estudiantes", new ImageIcon("./iconos/account.png"));
        btnEstudiantes.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnEstudiantes.setBounds(390, 30, 111, 31);
        getContentPane().add(btnEstudiantes);

        btnTutores = new JButton("Tutores", new ImageIcon("./iconos/school.png"));
        btnTutores.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnTutores.setBounds(510, 30, 111, 31);
        getContentPane().add(btnTutores);

        btnOfertas = new JButton("Ofertas", new ImageIcon("./iconos/forms_add.png"));
        btnOfertas.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnOfertas.setBounds(630, 30, 101, 31);
        getContentPane().add(btnOfertas);

        btnEmpresa = new JButton("Empresas", new ImageIcon("./iconos/apartment.png"));
        btnEmpresa.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnEmpresa.setBounds(740, 30, 101, 31);
        getContentPane().add(btnEmpresa);

        btnPracticas = new JButton("Practicas", new ImageIcon("./iconos/work.png"));
        btnPracticas.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnPracticas.setBounds(850, 30, 101, 31);
        getContentPane().add(btnPracticas);

        btnPostulaciones = new JButton("Postulaciones", new ImageIcon("./iconos/lists.png"));
        btnPostulaciones.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnPostulaciones.setBounds(960, 30, 111, 31);
        getContentPane().add(btnPostulaciones);

        btnCerrarSesion = new JButton("Cerrar sesion", new ImageIcon("./iconos/logout.png"));
        btnCerrarSesion.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnCerrarSesion.setBounds(1080, 30, 111, 31);
        getContentPane().add(btnCerrarSesion);

        lblSubTitulo = new JLabel("Informacion y reportes");
        lblSubTitulo.setFont(new Font("Dialog", Font.BOLD, 18));
        lblSubTitulo.setBounds(40, 80, 291, 31);
        getContentPane().add(lblSubTitulo);

        lblInformacion = new JLabel("Panel de coordinador");
        lblInformacion.setFont(new Font("Dialog", Font.PLAIN, 10));
        lblInformacion.setBounds(40, 110, 141, 21);
        getContentPane().add(lblInformacion);

        lblNumEstudiantes = new JLabel("00");
        lblNumEstudiantes.setFont(new Font("Dialog", Font.PLAIN, 20));
        lblNumEstudiantes.setBounds(50, 160, 61, 31);
        getContentPane().add(lblNumEstudiantes);

        lblEstudiante = new JLabel("Estudiantes");
        lblEstudiante.setFont(new Font("Dialog", Font.PLAIN, 10));
        lblEstudiante.setBounds(40, 190, 71, 21);
        getContentPane().add(lblEstudiante);

        lblNumTA = new JLabel("00");
        lblNumTA.setFont(new Font("Dialog", Font.PLAIN, 20));
        lblNumTA.setBounds(170, 160, 61, 31);
        getContentPane().add(lblNumTA);

        lblTA = new JLabel("Tutores academicos");
        lblTA.setFont(new Font("Dialog", Font.PLAIN, 10));
        lblTA.setBounds(130, 190, 121, 21);
        getContentPane().add(lblTA);

        lblTE = new JLabel("00");
        lblTE.setFont(new Font("Dialog", Font.PLAIN, 20));
        lblTE.setBounds(300, 160, 61, 31);
        getContentPane().add(lblTE);

        lblTE_2 = new JLabel("Tutores empresariales");
        lblTE_2.setFont(new Font("Dialog", Font.PLAIN, 10));
        lblTE_2.setBounds(260, 190, 141, 21);
        getContentPane().add(lblTE_2);

        lblNumEmpresas = new JLabel("00");
        lblNumEmpresas.setFont(new Font("Dialog", Font.PLAIN, 20));
        lblNumEmpresas.setBounds(420, 160, 61, 31);
        getContentPane().add(lblNumEmpresas);

        lblEmpresa = new JLabel("Empresas");
        lblEmpresa.setFont(new Font("Dialog", Font.PLAIN, 10));
        lblEmpresa.setBounds(410, 190, 71, 21);
        getContentPane().add(lblEmpresa);

        lblNumPostulaciones = new JLabel("00");
        lblNumPostulaciones.setFont(new Font("Dialog", Font.PLAIN, 20));
        lblNumPostulaciones.setBounds(520, 160, 61, 31);
        getContentPane().add(lblNumPostulaciones);

        lblPostulacion = new JLabel("Postulaciones");
        lblPostulacion.setFont(new Font("Dialog", Font.PLAIN, 10));
        lblPostulacion.setBounds(500, 190, 81, 21);
        getContentPane().add(lblPostulacion);

        lblNumPracActiva = new JLabel("00");
        lblNumPracActiva.setFont(new Font("Dialog", Font.PLAIN, 20));
        lblNumPracActiva.setBounds(630, 160, 61, 31);
        getContentPane().add(lblNumPracActiva);

        lblPracticaActiva = new JLabel("Practicas activas");
        lblPracticaActiva.setFont(new Font("Dialog", Font.PLAIN, 10));
        lblPracticaActiva.setBounds(600, 190, 101, 21);
        getContentPane().add(lblPracticaActiva);

        lblNumPracFinalizada = new JLabel("00");
        lblNumPracFinalizada.setFont(new Font("Dialog", Font.PLAIN, 20));
        lblNumPracFinalizada.setBounds(750, 160, 61, 31);
        getContentPane().add(lblNumPracFinalizada);

        lblPracticaFinalizada = new JLabel("Practicas finalizadas");
        lblPracticaFinalizada.setFont(new Font("Dialog", Font.PLAIN, 10));
        lblPracticaFinalizada.setBounds(720, 190, 121, 21);
        getContentPane().add(lblPracticaFinalizada);

        lblEstadoAplicacionPracticas = new JLabel("Estado de aplicacion de practicas");
        lblEstadoAplicacionPracticas.setFont(new Font("Dialog", Font.BOLD, 18));
        lblEstadoAplicacionPracticas.setBounds(50, 270, 391, 31);
        getContentPane().add(lblEstadoAplicacionPracticas);

        widgetGraficoPastel = new JPanel(new BorderLayout());
        widgetGraficoPastel.setBounds(50, 320, 520, 320);
        getContentPane().add(widgetGraficoPastel);

        lblEstudiantesXciclo = new JLabel("Estudiantes por ciclo");
        lblEstudiantesXciclo.setFont(new Font("Dialog", Font.BOLD, 18));
        lblEstudiantesXciclo.setBounds(650, 270, 411, 31);
        getContentPane().add(lblEstudiantesXciclo);

        widgetGraficoBarras = new JPanel(new BorderLayout());
        widgetGraficoBarras.setBounds(650, 320, 520, 320);
        getContentPane().add(widgetGraficoBarras);
    }
}
