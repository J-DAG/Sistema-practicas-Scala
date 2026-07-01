package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaTAActividades extends JFrame {

    public JLabel lblIcono;
    public JLabel lblTitulo;
    public JLabel lblSubTitulo;
    public JLabel lblIconoBuscar;
    public JLabel lblEstudiante;
    public JLabel lblNombresEstudianteEditar;
    public JButton btnRegresar;
    public JButton btnInicio;
    public JButton btnBuscar;
    public JButton btnAprobar;
    public JButton btnNegar;
    public JTextField txtBuscar;
    public JTable tblActividades;

    public VistaTAActividades() {
        setTitle("Lista de actividades");
        setSize(1200, 800);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        lblIcono = new JLabel(new ImageIcon("./iconos/apartment.png"));
        lblIcono.setBounds(30, 30, 31, 31);
        getContentPane().add(lblIcono);

        lblTitulo = new JLabel("Tutor Academico");
        lblTitulo.setFont(new Font("Dialog", Font.BOLD, 15));
        lblTitulo.setBounds(60, 30, 191, 31);
        getContentPane().add(lblTitulo);

        btnInicio = new JButton("Inicio", new ImageIcon("./iconos/home.png"));
        btnInicio.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnInicio.setBounds(950, 30, 111, 31);
        getContentPane().add(btnInicio);

        btnRegresar = new JButton("Regresar", new ImageIcon("./iconos/logout.png"));
        btnRegresar.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnRegresar.setBounds(1070, 30, 111, 31);
        getContentPane().add(btnRegresar);

        lblSubTitulo = new JLabel("Lista de actividades");
        lblSubTitulo.setFont(new Font("Dialog", Font.BOLD, 18));
        lblSubTitulo.setBounds(50, 90, 251, 31);
        getContentPane().add(lblSubTitulo);

        lblEstudiante = new JLabel("Estudiante:");
        lblEstudiante.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblEstudiante.setBounds(50, 130, 91, 16);
        getContentPane().add(lblEstudiante);

        lblNombresEstudianteEditar = new JLabel("nombre_estudiante");
        lblNombresEstudianteEditar.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblNombresEstudianteEditar.setBounds(140, 130, 281, 16);
        getContentPane().add(lblNombresEstudianteEditar);

        lblIconoBuscar = new JLabel(new ImageIcon("./iconos/search.png"));
        lblIconoBuscar.setBounds(70, 170, 31, 31);
        getContentPane().add(lblIconoBuscar);

        txtBuscar = new JTextField();
        txtBuscar.setFont(new Font("Dialog", Font.PLAIN, 10));
        txtBuscar.setBounds(100, 170, 861, 31);
        getContentPane().add(txtBuscar);

        btnBuscar = new JButton("Buscar", new ImageIcon("./iconos/search.png"));
        btnBuscar.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnBuscar.setBounds(980, 170, 111, 31);
        getContentPane().add(btnBuscar);

        tblActividades = new JTable(new DefaultTableModel());
        JScrollPane scrollActividades = new JScrollPane(tblActividades);
        scrollActividades.setBounds(60, 220, 1081, 501);
        getContentPane().add(scrollActividades);

        btnAprobar = new JButton("Aprobar", new ImageIcon("./iconos/check.png"));
        btnAprobar.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnAprobar.setBounds(730, 740, 201, 31);
        getContentPane().add(btnAprobar);

        btnNegar = new JButton("Negar", new ImageIcon("./iconos/close.png"));
        btnNegar.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnNegar.setBounds(940, 740, 201, 31);
        getContentPane().add(btnNegar);
    }
}
