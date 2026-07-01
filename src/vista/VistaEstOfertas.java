package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaEstOfertas extends JFrame {

    public JLabel lblIcono;
    public JLabel lblTitulo;
    public JLabel lblSubTitulo;
    public JLabel lblIconoBuscar;
    public JButton btnCerrarSesion;
    public JButton btnInico;
    public JButton btnOfertaLaboral;
    public JButton btnMisFormularios;
    public JButton btnMiPractica;
    public JButton btnMisPostulaciones;
    public JButton btnBuscar;
    public JButton btnAplicarPostulacion;
    public JButton btnCancelarPostulacion;
    public JTextField txtBuscar;
    public JTable tblOfertasLaborales;

    public VistaEstOfertas() {
        setTitle("Ofertas laborales");
        setSize(1200, 800);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        lblIcono = new JLabel(new ImageIcon("./iconos/school.png"));
        lblIcono.setBounds(30, 30, 31, 31);
        getContentPane().add(lblIcono);

        lblTitulo = new JLabel("Estudiante");
        lblTitulo.setFont(new Font("Dialog", Font.BOLD, 15));
        lblTitulo.setBounds(60, 30, 191, 31);
        getContentPane().add(lblTitulo);

        btnInico = new JButton("Inicio", new ImageIcon("./iconos/home.png"));
        btnInico.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnInico.setBounds(370, 30, 101, 31);
        getContentPane().add(btnInico);

        btnMiPractica = new JButton("Mi practica", new ImageIcon("./iconos/stats.png"));
        btnMiPractica.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnMiPractica.setBounds(480, 30, 141, 31);
        getContentPane().add(btnMiPractica);

        btnMisPostulaciones = new JButton("Mis postulaciones", new ImageIcon("./iconos/lists.png"));
        btnMisPostulaciones.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnMisPostulaciones.setBounds(630, 30, 141, 31);
        getContentPane().add(btnMisPostulaciones);

        btnOfertaLaboral = new JButton("Oferta laboral", new ImageIcon("./iconos/work.png"));
        btnOfertaLaboral.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnOfertaLaboral.setBounds(780, 30, 141, 31);
        getContentPane().add(btnOfertaLaboral);

        btnMisFormularios = new JButton("Mis formularios", new ImageIcon("./iconos/forms_add.png"));
        btnMisFormularios.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnMisFormularios.setBounds(930, 30, 141, 31);
        getContentPane().add(btnMisFormularios);

        btnCerrarSesion = new JButton("Cerrar sesion", new ImageIcon("./iconos/logout.png"));
        btnCerrarSesion.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnCerrarSesion.setBounds(1080, 30, 111, 31);
        getContentPane().add(btnCerrarSesion);

        lblSubTitulo = new JLabel("Ofertas laborales");
        lblSubTitulo.setFont(new Font("Dialog", Font.BOLD, 18));
        lblSubTitulo.setBounds(50, 90, 561, 31);
        getContentPane().add(lblSubTitulo);

        lblIconoBuscar = new JLabel(new ImageIcon("./iconos/search.png"));
        lblIconoBuscar.setBounds(70, 170, 31, 31);
        getContentPane().add(lblIconoBuscar);

        txtBuscar = new JTextField();
        txtBuscar.setFont(new Font("Dialog", Font.PLAIN, 10));
        txtBuscar.setBounds(100, 170, 681, 31);
        getContentPane().add(txtBuscar);

        btnBuscar = new JButton("Buscar", new ImageIcon("./iconos/search.png"));
        btnBuscar.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnBuscar.setBounds(790, 170, 111, 31);
        getContentPane().add(btnBuscar);

        tblOfertasLaborales = new JTable(new DefaultTableModel());
        JScrollPane scrollOfertas = new JScrollPane(tblOfertasLaborales);
        scrollOfertas.setBounds(60, 220, 921, 501);
        getContentPane().add(scrollOfertas);

        btnAplicarPostulacion = new JButton("Aplicar postulacion", new ImageIcon("./iconos/add_24.png"));
        btnAplicarPostulacion.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnAplicarPostulacion.setBounds(1000, 220, 161, 31);
        getContentPane().add(btnAplicarPostulacion);

        btnCancelarPostulacion = new JButton("Cancelar postulacion", new ImageIcon("./iconos/close.png"));
        btnCancelarPostulacion.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnCancelarPostulacion.setBounds(1000, 260, 161, 31);
        getContentPane().add(btnCancelarPostulacion);
    }
}
