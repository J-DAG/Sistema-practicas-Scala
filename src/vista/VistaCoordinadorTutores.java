package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaCoordinadorTutores extends JFrame {

    public JLabel lblIcono;
    public JLabel lblTitulo;
    public JLabel lblSubTitulo;
    public JLabel lblInformacion;
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
    public JButton btnNuevoTA;
    public JButton btnNuevoTE;
    public JButton btnEliminar;
    public JButton btnEditar;
    public JTextField txtBuscar;
    public JTable tblTutores;

    public VistaCoordinadorTutores() {
        setTitle("Administracion de tutores");
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

        lblSubTitulo = new JLabel("Administracion de tutores");
        lblSubTitulo.setFont(new Font("Dialog", Font.BOLD, 18));
        lblSubTitulo.setBounds(30, 80, 321, 31);
        getContentPane().add(lblSubTitulo);

        lblInformacion = new JLabel("Panel de coordinador");
        lblInformacion.setFont(new Font("Dialog", Font.PLAIN, 11));
        lblInformacion.setBounds(30, 110, 141, 21);
        getContentPane().add(lblInformacion);

        JLabel lblIcono_2 = new JLabel(RecursoVista.icono("search.png"));
        lblIcono_2.setBounds(110, 160, 31, 31);
        getContentPane().add(lblIcono_2);

        txtBuscar = new JTextField();
        txtBuscar.setFont(new Font("Dialog", Font.PLAIN, 11));
        txtBuscar.setBounds(140, 160, 551, 31);
        getContentPane().add(txtBuscar);

        btnBuscar = new JButton("Buscar", RecursoVista.icono("search.png"));
        btnBuscar.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnBuscar.setBounds(700, 160, 111, 31);
        getContentPane().add(btnBuscar);

        btnNuevoTA = new JButton("Nuevo tutor academico", RecursoVista.icono("school.png"));
        btnNuevoTA.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnNuevoTA.setBounds(830, 160, 181, 31);
        getContentPane().add(btnNuevoTA);

        btnNuevoTE = new JButton("Nuevo tutor empresarial", RecursoVista.icono("apartment.png"));
        btnNuevoTE.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnNuevoTE.setBounds(1020, 160, 181, 31);
        getContentPane().add(btnNuevoTE);

        tblTutores = new JTable(new DefaultTableModel());
        JScrollPane scrollTutores = new JScrollPane(tblTutores);
        scrollTutores.setBounds(100, 210, 1101, 501);
        getContentPane().add(scrollTutores);

        btnEliminar = new JButton("Eliminar", RecursoVista.icono("delete.png"));
        btnEliminar.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnEliminar.setBounds(930, 720, 131, 31);
        getContentPane().add(btnEliminar);

        btnEditar = new JButton("Editar", RecursoVista.icono("edit.png"));
        btnEditar.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnEditar.setBounds(1070, 720, 131, 31);
        getContentPane().add(btnEditar);
    }
}
