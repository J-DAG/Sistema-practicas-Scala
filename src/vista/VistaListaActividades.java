package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaListaActividades extends JFrame {

    public JLabel lblTitulo;
    public JLabel lblEstudiante;
    public JLabel lblIDestudianteEditar;
    public JLabel lblPracticaId;
    public JLabel lblIDpracticaEditar;
    public JLabel lblTA;
    public JLabel lblNombreTAEditar;
    public JLabel lblNombres;
    public JLabel lblNombresEstudianteEditar;
    public JLabel lblTE;
    public JLabel lblNombreTEEditar;
    public JButton btnSalir;
    public JTable tblListaActividades;

    public VistaListaActividades() {
        setTitle("Lista de actividades");
        setSize(800, 700);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        lblTitulo = new JLabel("Lista de actividades");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitulo.setBounds(300, 10, 201, 21);
        getContentPane().add(lblTitulo);

        btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnSalir.setBounds(690, 10, 91, 31);
        getContentPane().add(btnSalir);

        lblEstudiante = new JLabel("Estudiante ID:");
        lblEstudiante.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblEstudiante.setBounds(20, 50, 101, 21);
        getContentPane().add(lblEstudiante);

        lblIDestudianteEditar = new JLabel("ID_estudiante");
        lblIDestudianteEditar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblIDestudianteEditar.setBounds(130, 50, 111, 21);
        getContentPane().add(lblIDestudianteEditar);

        lblPracticaId = new JLabel("Practica ID:");
        lblPracticaId.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblPracticaId.setBounds(240, 50, 91, 21);
        getContentPane().add(lblPracticaId);

        lblIDpracticaEditar = new JLabel("");
        lblIDpracticaEditar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblIDpracticaEditar.setBounds(330, 50, 101, 21);
        getContentPane().add(lblIDpracticaEditar);

        lblTA = new JLabel("TA:");
        lblTA.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTA.setBounds(460, 50, 31, 21);
        getContentPane().add(lblTA);

        lblNombreTAEditar = new JLabel("");
        lblNombreTAEditar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblNombreTAEditar.setBounds(500, 50, 261, 21);
        getContentPane().add(lblNombreTAEditar);

        lblNombres = new JLabel("Nombre del estudiante:");
        lblNombres.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblNombres.setBounds(20, 80, 171, 21);
        getContentPane().add(lblNombres);

        lblNombresEstudianteEditar = new JLabel("");
        lblNombresEstudianteEditar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblNombresEstudianteEditar.setBounds(200, 80, 241, 21);
        getContentPane().add(lblNombresEstudianteEditar);

        lblTE = new JLabel("TE:");
        lblTE.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTE.setBounds(460, 80, 31, 21);
        getContentPane().add(lblTE);

        lblNombreTEEditar = new JLabel("");
        lblNombreTEEditar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblNombreTEEditar.setBounds(500, 80, 261, 21);
        getContentPane().add(lblNombreTEEditar);

        tblListaActividades = new JTable(new DefaultTableModel());
        JScrollPane scrollActividades = new JScrollPane(tblListaActividades);
        scrollActividades.setBounds(30, 120, 731, 541);
        getContentPane().add(scrollActividades);
    }
}
