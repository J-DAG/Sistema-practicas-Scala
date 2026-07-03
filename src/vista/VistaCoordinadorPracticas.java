package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaCoordinadorPracticas extends JFrame {

    public JLabel lblIcono;
    public JLabel lblTitulo;
    public JLabel lblSubTitulo;
    public JLabel lblInformacion;
    public JLabel lblEstadoAplicacionPracticas;
    public JButton btnInicio;
    public JButton btnReportes;
    public JButton btnEstudiantes;
    public JButton btnTutores;
    public JButton btnOfertas;
    public JButton btnEmpresa;
    public JButton btnPracticas;
    public JButton btnPostulaciones;
    public JButton btnCerrarSesion;
    public JButton btnBuscar;
    public JButton btnVerDetalle;
    public JButton btnVerActividades;
    public JTextField txtBuscar;
    public JTable tblPracticas;
    public JPanel widgetGraficoPastel;

    public VistaCoordinadorPracticas() {
        setTitle("Administracion de practicas");
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

        lblSubTitulo = new JLabel("Administracion de Practicas");
        lblSubTitulo.setFont(new Font("Dialog", Font.BOLD, 18));
        lblSubTitulo.setBounds(30, 80, 371, 31);
        getContentPane().add(lblSubTitulo);

        lblInformacion = new JLabel("Panel de coordinador");
        lblInformacion.setFont(new Font("Dialog", Font.PLAIN, 11));
        lblInformacion.setBounds(30, 110, 141, 21);
        getContentPane().add(lblInformacion);

        JLabel label = new JLabel(RecursoVista.icono("search.png"));
        label.setBounds(630, 160, 31, 31);
        getContentPane().add(label);

        txtBuscar = new JTextField();
        txtBuscar.setFont(new Font("Dialog", Font.PLAIN, 11));
        txtBuscar.setBounds(660, 160, 181, 31);
        getContentPane().add(txtBuscar);

        btnBuscar = new JButton("Buscar", RecursoVista.icono("search.png"));
        btnBuscar.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnBuscar.setBounds(850, 160, 111, 31);
        getContentPane().add(btnBuscar);

        btnVerDetalle = new JButton("Ver detalle", RecursoVista.icono("visibility.png"));
        btnVerDetalle.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnVerDetalle.setBounds(970, 160, 111, 31);
        getContentPane().add(btnVerDetalle);

        btnVerActividades = new JButton("Ver actividades", RecursoVista.icono("visibility.png"));
        btnVerActividades.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnVerActividades.setBounds(1090, 160, 131, 31);
        getContentPane().add(btnVerActividades);

        lblEstadoAplicacionPracticas = new JLabel("Estado de Practicas");
        lblEstadoAplicacionPracticas.setFont(new Font("Dialog", Font.BOLD, 18));
        lblEstadoAplicacionPracticas.setBounds(80, 230, 391, 31);
        getContentPane().add(lblEstadoAplicacionPracticas);

        widgetGraficoPastel = new JPanel(new BorderLayout());
        widgetGraficoPastel.setBounds(80, 280, 520, 320);
        getContentPane().add(widgetGraficoPastel);

        tblPracticas = new JTable(new DefaultTableModel());
        JScrollPane scrollPracticas = new JScrollPane(tblPracticas);
        scrollPracticas.setBounds(630, 210, 591, 501);
        getContentPane().add(scrollPracticas);
    }
}
