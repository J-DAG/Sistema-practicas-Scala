package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaTEPracticasProgreso extends JFrame {

    public JLabel lblIcono;
    public JLabel lblTitulo;
    public JLabel lblSubTitulo;
    public JLabel lblInformacion;
    public JLabel lblIconoBuscar;
    public JButton btnCerrarSesion;
    public JButton btnInico;
    public JButton btnPracticasProgreso;
    public JButton btnPracticasCompletadas;
    public JButton btnNotificaciones;
    public JButton btnBuscar;
    public JButton btnActividades;
    public JTextField txtBuscar;
    public JTable tblEstudiantesTE;

    public VistaTEPracticasProgreso() {
        setTitle("Practicas en progreso");
        setSize(1200, 800);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        lblIcono = new JLabel(new ImageIcon("./iconos/apartment.png"));
        lblIcono.setBounds(30, 30, 31, 31);
        getContentPane().add(lblIcono);

        lblTitulo = new JLabel("Tutor Empresarial");
        lblTitulo.setFont(new Font("Dialog", Font.BOLD, 15));
        lblTitulo.setBounds(60, 30, 191, 31);
        getContentPane().add(lblTitulo);

        btnInico = new JButton("Inicio", new ImageIcon("./iconos/home.png"));
        btnInico.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnInico.setBounds(390, 30, 171, 31);
        getContentPane().add(btnInico);

        btnPracticasProgreso = new JButton("Practicas en progreso", new ImageIcon("./iconos/hourglass_top.png"));
        btnPracticasProgreso.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnPracticasProgreso.setBounds(570, 30, 171, 31);
        getContentPane().add(btnPracticasProgreso);

        btnPracticasCompletadas = new JButton("Practicas completadas", new ImageIcon("./iconos/check_circle.png"));
        btnPracticasCompletadas.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnPracticasCompletadas.setBounds(750, 30, 171, 31);
        getContentPane().add(btnPracticasCompletadas);

        btnNotificaciones = new JButton("Notificaciones", new ImageIcon("./iconos/notification.png"));
        btnNotificaciones.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnNotificaciones.setBounds(930, 30, 141, 31);
        getContentPane().add(btnNotificaciones);

        btnCerrarSesion = new JButton("Cerrar sesion", new ImageIcon("./iconos/logout.png"));
        btnCerrarSesion.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnCerrarSesion.setBounds(1080, 30, 111, 31);
        getContentPane().add(btnCerrarSesion);

        lblSubTitulo = new JLabel("Progreso de estudiantes a cargo");
        lblSubTitulo.setFont(new Font("Dialog", Font.BOLD, 18));
        lblSubTitulo.setBounds(50, 90, 431, 31);
        getContentPane().add(lblSubTitulo);

        lblInformacion = new JLabel("Supervisa el progreso de tus estudiantes");
        lblInformacion.setFont(new Font("Dialog", Font.PLAIN, 10));
        lblInformacion.setBounds(50, 120, 461, 21);
        getContentPane().add(lblInformacion);

        lblIconoBuscar = new JLabel(new ImageIcon("./iconos/search.png"));
        lblIconoBuscar.setBounds(70, 170, 31, 31);
        getContentPane().add(lblIconoBuscar);

        txtBuscar = new JTextField();
        txtBuscar.setFont(new Font("Dialog", Font.PLAIN, 10));
        txtBuscar.setBounds(100, 170, 761, 31);
        getContentPane().add(txtBuscar);

        btnBuscar = new JButton("Buscar", new ImageIcon("./iconos/search.png"));
        btnBuscar.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnBuscar.setBounds(870, 170, 111, 31);
        getContentPane().add(btnBuscar);

        btnActividades = new JButton("Ver actividades", new ImageIcon("./iconos/lists.png"));
        btnActividades.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnActividades.setBounds(990, 170, 141, 31);
        getContentPane().add(btnActividades);

        tblEstudiantesTE = new JTable(new DefaultTableModel());
        JScrollPane scrollEstudiantes = new JScrollPane(tblEstudiantesTE);
        scrollEstudiantes.setBounds(60, 220, 1081, 501);
        getContentPane().add(scrollEstudiantes);
    }
}
