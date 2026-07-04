package vista;

import javax.swing.*;
import java.awt.*;

public class VistaEstFormularios extends JFrame {

    public JLabel lblIcono;
    public JLabel lblTitulo;
    public JLabel lblSubTitulo;
    public JLabel lblTitulo_2;
    public JLabel lblTitulo_3;
    public JButton btnCerrarSesion;
    public JButton btnInico;
    public JButton btnOfertaLaboral;
    public JButton btnMisFormularios;
    public JButton btnMiPractica;
    public JButton btnMisPostulaciones;
    public JButton btnCartaCompromiso;
    public JPanel widgetF1;
    public JPanel widgetF2;

    public VistaEstFormularios() {
        setTitle("Mis formularios");
        setSize(1250, 840);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        lblIcono = new JLabel(RecursoVista.icono("school.png"));
        lblIcono.setBounds(30, 30, 31, 31);
        getContentPane().add(lblIcono);

        lblTitulo = new JLabel("Estudiante");
        lblTitulo.setFont(new Font("Dialog", Font.BOLD, 15));
        lblTitulo.setBounds(60, 30, 191, 31);
        getContentPane().add(lblTitulo);

        btnInico = new JButton("Inicio", RecursoVista.icono("home.png"));
        btnInico.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnInico.setBounds(370, 30, 101, 31);
        getContentPane().add(btnInico);

        btnMiPractica = new JButton("Mi practica", RecursoVista.icono("stats.png"));
        btnMiPractica.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnMiPractica.setBounds(480, 30, 141, 31);
        getContentPane().add(btnMiPractica);

        btnMisPostulaciones = new JButton("Mis postulaciones", RecursoVista.icono("lists.png"));
        btnMisPostulaciones.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnMisPostulaciones.setBounds(630, 30, 141, 31);
        getContentPane().add(btnMisPostulaciones);

        btnOfertaLaboral = new JButton("Oferta laboral", RecursoVista.icono("work.png"));
        btnOfertaLaboral.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnOfertaLaboral.setBounds(780, 30, 141, 31);
        getContentPane().add(btnOfertaLaboral);

        btnMisFormularios = new JButton("Mis formularios", RecursoVista.icono("forms_add.png"));
        btnMisFormularios.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnMisFormularios.setBounds(930, 30, 141, 31);
        getContentPane().add(btnMisFormularios);

        btnCerrarSesion = new JButton("Cerrar sesion", RecursoVista.icono("logout.png"));
        btnCerrarSesion.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnCerrarSesion.setBounds(1080, 30, 111, 31);
        getContentPane().add(btnCerrarSesion);

        lblSubTitulo = new JLabel("Mis formularios");
        lblSubTitulo.setFont(new Font("Dialog", Font.BOLD, 18));
        lblSubTitulo.setBounds(50, 90, 561, 31);
        getContentPane().add(lblSubTitulo);

        btnCartaCompromiso = new JButton("Carta compromiso", RecursoVista.icono("forms_add.png"));
        btnCartaCompromiso.setFont(new Font("Dialog", Font.PLAIN, 10));
        btnCartaCompromiso.setBounds(970, 130, 161, 31);
        getContentPane().add(btnCartaCompromiso);

        lblTitulo_2 = new JLabel("Formulario 1:");
        lblTitulo_2.setFont(new Font("Dialog", Font.BOLD, 15));
        lblTitulo_2.setBounds(185, 160, 191, 31);
        getContentPane().add(lblTitulo_2);

        lblTitulo_3 = new JLabel("Formulario 2:");
        lblTitulo_3.setFont(new Font("Dialog", Font.BOLD, 15));
        lblTitulo_3.setBounds(655, 160, 191, 31);
        getContentPane().add(lblTitulo_3);

        widgetF1 = new JPanel(new BorderLayout());
        widgetF1.setBounds(105, 200, 521, 551);
        getContentPane().add(widgetF1);

        widgetF2 = new JPanel(new BorderLayout());
        widgetF2.setBounds(655, 200, 501, 551);
        getContentPane().add(widgetF2);
    }
}
