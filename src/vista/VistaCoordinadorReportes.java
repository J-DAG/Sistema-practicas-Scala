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
        setSize(1300, 840);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        lblIcono = new JLabel(RecursoVista.icono("shield.png"));
        lblIcono.setBounds(10, 20, 31, 31);
        getContentPane().add(lblIcono);

        lblTitulo = new JLabel("Coordinacion");
        lblTitulo.setFont(new Font("Dialog", Font.BOLD, 15));
        lblTitulo.setBounds(40, 20, 141, 31);
        getContentPane().add(lblTitulo);

        btnInicio = new JButton("Inicio", RecursoVista.icono("home.png"));
        btnInicio.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnInicio.setBounds(170, 30, 90, 31);
        getContentPane().add(btnInicio);

        btnReportes = new JButton("Reportes", RecursoVista.icono("stats.png"));
        btnReportes.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnReportes.setBounds(270, 30, 105, 31);
        getContentPane().add(btnReportes);

        btnEstudiantes = new JButton("Estudiantes", RecursoVista.icono("account.png"));
        btnEstudiantes.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnEstudiantes.setBounds(385, 30, 125, 31);
        getContentPane().add(btnEstudiantes);

        btnTutores = new JButton("Tutores", RecursoVista.icono("school.png"));
        btnTutores.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnTutores.setBounds(520, 30, 105, 31);
        getContentPane().add(btnTutores);

        btnOfertas = new JButton("Ofertas", RecursoVista.icono("forms_add.png"));
        btnOfertas.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnOfertas.setBounds(635, 30, 100, 31);
        getContentPane().add(btnOfertas);

        btnEmpresa = new JButton("Empresas", RecursoVista.icono("apartment.png"));
        btnEmpresa.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnEmpresa.setBounds(745, 30, 115, 31);
        getContentPane().add(btnEmpresa);

        btnPracticas = new JButton("Practicas", RecursoVista.icono("work.png"));
        btnPracticas.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnPracticas.setBounds(870, 30, 115, 31);
        getContentPane().add(btnPracticas);

        btnPostulaciones = new JButton("Postulaciones", RecursoVista.icono("lists.png"));
        btnPostulaciones.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnPostulaciones.setBounds(995, 30, 130, 31);
        getContentPane().add(btnPostulaciones);

        btnCerrarSesion = new JButton("Cerrar sesion", RecursoVista.icono("logout.png"));
        btnCerrarSesion.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnCerrarSesion.setBounds(1135, 30, 135, 31);
        getContentPane().add(btnCerrarSesion);

        lblSubTitulo = new JLabel("Informacion y reportes");
        lblSubTitulo.setFont(new Font("Dialog", Font.BOLD, 18));
        lblSubTitulo.setBounds(40, 80, 291, 31);
        getContentPane().add(lblSubTitulo);

        lblInformacion = new JLabel("Panel de coordinador");
        lblInformacion.setFont(new Font("Dialog", Font.PLAIN, 11));
        lblInformacion.setBounds(40, 110, 141, 21);
        getContentPane().add(lblInformacion);

        lblNumEstudiantes = new JLabel("00");
        lblNumEstudiantes.setFont(new Font("Dialog", Font.PLAIN, 20));
        lblNumEstudiantes.setBounds(100, 160, 61, 31);
        getContentPane().add(lblNumEstudiantes);

        lblEstudiante = new JLabel("Estudiantes");
        lblEstudiante.setFont(new Font("Dialog", Font.PLAIN, 11));
        lblEstudiante.setBounds(90, 190, 71, 21);
        getContentPane().add(lblEstudiante);

        lblNumTA = new JLabel("00");
        lblNumTA.setFont(new Font("Dialog", Font.PLAIN, 20));
        lblNumTA.setBounds(220, 160, 61, 31);
        getContentPane().add(lblNumTA);

        lblTA = new JLabel("Tutores academicos");
        lblTA.setFont(new Font("Dialog", Font.PLAIN, 11));
        lblTA.setBounds(180, 190, 121, 21);
        getContentPane().add(lblTA);

        lblTE = new JLabel("00");
        lblTE.setFont(new Font("Dialog", Font.PLAIN, 20));
        lblTE.setBounds(350, 160, 61, 31);
        getContentPane().add(lblTE);

        lblTE_2 = new JLabel("Tutores empresariales");
        lblTE_2.setFont(new Font("Dialog", Font.PLAIN, 11));
        lblTE_2.setBounds(310, 190, 141, 21);
        getContentPane().add(lblTE_2);

        lblNumEmpresas = new JLabel("00");
        lblNumEmpresas.setFont(new Font("Dialog", Font.PLAIN, 20));
        lblNumEmpresas.setBounds(470, 160, 61, 31);
        getContentPane().add(lblNumEmpresas);

        lblEmpresa = new JLabel("Empresas");
        lblEmpresa.setFont(new Font("Dialog", Font.PLAIN, 11));
        lblEmpresa.setBounds(460, 190, 71, 21);
        getContentPane().add(lblEmpresa);

        lblNumPostulaciones = new JLabel("00");
        lblNumPostulaciones.setFont(new Font("Dialog", Font.PLAIN, 20));
        lblNumPostulaciones.setBounds(570, 160, 61, 31);
        getContentPane().add(lblNumPostulaciones);

        lblPostulacion = new JLabel("Postulaciones");
        lblPostulacion.setFont(new Font("Dialog", Font.PLAIN, 11));
        lblPostulacion.setBounds(550, 190, 81, 21);
        getContentPane().add(lblPostulacion);

        lblNumPracActiva = new JLabel("00");
        lblNumPracActiva.setFont(new Font("Dialog", Font.PLAIN, 20));
        lblNumPracActiva.setBounds(680, 160, 61, 31);
        getContentPane().add(lblNumPracActiva);

        lblPracticaActiva = new JLabel("Practicas activas");
        lblPracticaActiva.setFont(new Font("Dialog", Font.PLAIN, 11));
        lblPracticaActiva.setBounds(650, 190, 101, 21);
        getContentPane().add(lblPracticaActiva);

        lblNumPracFinalizada = new JLabel("00");
        lblNumPracFinalizada.setFont(new Font("Dialog", Font.PLAIN, 20));
        lblNumPracFinalizada.setBounds(800, 160, 61, 31);
        getContentPane().add(lblNumPracFinalizada);

        lblPracticaFinalizada = new JLabel("Practicas finalizadas");
        lblPracticaFinalizada.setFont(new Font("Dialog", Font.PLAIN, 11));
        lblPracticaFinalizada.setBounds(770, 190, 121, 21);
        getContentPane().add(lblPracticaFinalizada);

        lblEstadoAplicacionPracticas = new JLabel("Estado de aplicacion de practicas");
        lblEstadoAplicacionPracticas.setFont(new Font("Dialog", Font.BOLD, 18));
        lblEstadoAplicacionPracticas.setBounds(100, 270, 391, 31);
        getContentPane().add(lblEstadoAplicacionPracticas);

        widgetGraficoPastel = new JPanel(new BorderLayout());
        widgetGraficoPastel.setBounds(100, 320, 520, 320);
        getContentPane().add(widgetGraficoPastel);

        lblEstudiantesXciclo = new JLabel("Estudiantes por ciclo");
        lblEstudiantesXciclo.setFont(new Font("Dialog", Font.BOLD, 18));
        lblEstudiantesXciclo.setBounds(700, 270, 411, 31);
        getContentPane().add(lblEstudiantesXciclo);

        widgetGraficoBarras = new JPanel(new BorderLayout());
        widgetGraficoBarras.setBounds(700, 320, 520, 320);
        getContentPane().add(widgetGraficoBarras);
    }
}
