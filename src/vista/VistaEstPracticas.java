package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaEstPracticas extends JFrame {

    public JLabel lblIcono;
    public JLabel lblTitulo;
    public JLabel lblSubTitulo;
    public JLabel lblIconoBuscar;
    public JLabel lblEmpresa;
    public JLabel lblTA;
    public JLabel lblTE;
    public JLabel lblNumHoras;
    public JLabel lblActCompletadas;
    public JLabel lblEmpresaEditar;
    public JLabel lblTAEditar;
    public JLabel lblTEEditar;
    public JLabel lblNumHorasEditar;
    public JLabel lblActCompletadasEditar;
    public JButton btnCerrarSesion;
    public JButton btnInico;
    public JButton btnOfertaLaboral;
    public JButton btnMisFormularios;
    public JButton btnMiPractica;
    public JButton btnMisPostulaciones;
    public JButton btnBuscar;
    public JTextField txtBuscar;
    public JRadioButton rbtAprobadas;
    public JRadioButton rbtAprobadas_2;
    public JRadioButton rbtAprobadas_3;
    public JTable tblListaActividades;

    public VistaEstPracticas() {
        setTitle("Mi practica");
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

        lblSubTitulo = new JLabel("Practica - Lista de actividades");
        lblSubTitulo.setFont(new Font("Dialog", Font.BOLD, 18));
        lblSubTitulo.setBounds(50, 90, 561, 31);
        getContentPane().add(lblSubTitulo);

        lblEmpresa = new JLabel("Empresa:");
        lblEmpresa.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblEmpresa.setBounds(50, 140, 81, 31);
        getContentPane().add(lblEmpresa);

        lblEmpresaEditar = new JLabel("nombre empresa");
        lblEmpresaEditar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblEmpresaEditar.setBounds(130, 140, 421, 31);
        getContentPane().add(lblEmpresaEditar);

        lblTA = new JLabel("Tutor academico:");
        lblTA.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTA.setBounds(50, 170, 131, 31);
        getContentPane().add(lblTA);

        lblTAEditar = new JLabel("");
        lblTAEditar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTAEditar.setBounds(180, 170, 421, 31);
        getContentPane().add(lblTAEditar);

        lblTE = new JLabel("Tutor empresarial:");
        lblTE.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTE.setBounds(50, 200, 131, 31);
        getContentPane().add(lblTE);

        lblTEEditar = new JLabel("");
        lblTEEditar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTEEditar.setBounds(190, 200, 421, 31);
        getContentPane().add(lblTEEditar);

        lblNumHoras = new JLabel("Numero de horas completadas:");
        lblNumHoras.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblNumHoras.setBounds(50, 230, 221, 31);
        getContentPane().add(lblNumHoras);

        lblNumHorasEditar = new JLabel("0");
        lblNumHorasEditar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblNumHorasEditar.setBounds(280, 230, 61, 31);
        getContentPane().add(lblNumHorasEditar);

        lblActCompletadas = new JLabel("Actividades completadas:");
        lblActCompletadas.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblActCompletadas.setBounds(50, 260, 181, 31);
        getContentPane().add(lblActCompletadas);

        lblActCompletadasEditar = new JLabel("0");
        lblActCompletadasEditar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblActCompletadasEditar.setBounds(240, 260, 61, 31);
        getContentPane().add(lblActCompletadasEditar);

        lblIconoBuscar = new JLabel(new ImageIcon("./iconos/search.png"));
        lblIconoBuscar.setBounds(60, 310, 31, 31);
        getContentPane().add(lblIconoBuscar);

        txtBuscar = new JTextField();
        txtBuscar.setFont(new Font("Dialog", Font.PLAIN, 10));
        txtBuscar.setBounds(90, 310, 521, 31);
        getContentPane().add(txtBuscar);

        btnBuscar = new JButton("Buscar", new ImageIcon("./iconos/search.png"));
        btnBuscar.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnBuscar.setBounds(630, 310, 111, 31);
        getContentPane().add(btnBuscar);

        rbtAprobadas = new JRadioButton("Aprobada");
        rbtAprobadas.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        rbtAprobadas.setBounds(750, 320, 85, 17);
        getContentPane().add(rbtAprobadas);

        rbtAprobadas_2 = new JRadioButton("Pendiente de aprobacion");
        rbtAprobadas_2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        rbtAprobadas_2.setBounds(860, 320, 191, 17);
        getContentPane().add(rbtAprobadas_2);

        rbtAprobadas_3 = new JRadioButton("Completada");
        rbtAprobadas_3.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        rbtAprobadas_3.setBounds(1040, 320, 101, 17);
        getContentPane().add(rbtAprobadas_3);

        tblListaActividades = new JTable(new DefaultTableModel());
        JScrollPane scrollActividades = new JScrollPane(tblListaActividades);
        scrollActividades.setBounds(60, 360, 1081, 411);
        getContentPane().add(scrollActividades);
    }
}
