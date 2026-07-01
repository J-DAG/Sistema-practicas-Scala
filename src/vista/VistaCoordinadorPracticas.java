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
    public JButton btnVerActividades;
    public JTextField txtBuscar;
    public JTable tblPracticas;
    public JPanel widgetGraficoPastel;

    public VistaCoordinadorPracticas() {
        setTitle("Administracion de practicas");
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

        lblSubTitulo = new JLabel("Administracion de Practicas");
        lblSubTitulo.setFont(new Font("Dialog", Font.BOLD, 18));
        lblSubTitulo.setBounds(30, 80, 371, 31);
        getContentPane().add(lblSubTitulo);

        lblInformacion = new JLabel("Panel de coordinador");
        lblInformacion.setFont(new Font("Dialog", Font.PLAIN, 10));
        lblInformacion.setBounds(30, 110, 141, 21);
        getContentPane().add(lblInformacion);

        JLabel label = new JLabel(new ImageIcon("./iconos/search.png"));
        label.setBounds(580, 160, 31, 31);
        getContentPane().add(label);

        txtBuscar = new JTextField();
        txtBuscar.setFont(new Font("Dialog", Font.PLAIN, 10));
        txtBuscar.setBounds(610, 160, 301, 31);
        getContentPane().add(txtBuscar);

        btnBuscar = new JButton("Buscar", new ImageIcon("./iconos/search.png"));
        btnBuscar.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnBuscar.setBounds(920, 160, 111, 31);
        getContentPane().add(btnBuscar);

        btnVerActividades = new JButton("Ver actividades", new ImageIcon("./iconos/visibility.png"));
        btnVerActividades.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnVerActividades.setBounds(1040, 160, 131, 31);
        getContentPane().add(btnVerActividades);

        lblEstadoAplicacionPracticas = new JLabel("Estado de Practicas");
        lblEstadoAplicacionPracticas.setFont(new Font("Dialog", Font.BOLD, 18));
        lblEstadoAplicacionPracticas.setBounds(30, 230, 391, 31);
        getContentPane().add(lblEstadoAplicacionPracticas);

        widgetGraficoPastel = new JPanel(new BorderLayout());
        widgetGraficoPastel.setBounds(30, 280, 520, 320);
        getContentPane().add(widgetGraficoPastel);

        tblPracticas = new JTable(new DefaultTableModel());
        JScrollPane scrollPracticas = new JScrollPane(tblPracticas);
        scrollPracticas.setBounds(580, 210, 591, 501);
        getContentPane().add(scrollPracticas);
    }
}
