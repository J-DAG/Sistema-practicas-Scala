package vista;

import javax.swing.*;
import javax.swing.SpinnerDateModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VistaEditarOferta extends JFrame {

    public JLabel lblTitulo;
    public JLabel lblNombre;
    public JLabel lblCupos;
    public JLabel lblCorreoElectrinico;
    public JLabel lblDescripcion;
    public JLabel lblArea;
    public JLabel lblFechaCierre;
    public JComboBox<String> cbxListaEmpresas;
    public JSpinner sbxNumCupos;
    public JTextField txtTitulo;
    public JTextField txtDescripcion;
    public JTextField txtArea;
    public JSpinner dtFecha;
    public JButton btnSeleccionarFecha;
    public JLabel lblFechaSeleccionada;
    public JButton btnCancelar;
    public JButton btnGuardar;

    public VistaEditarOferta() {
        setTitle("Edicion de oferta");
        setSize(850, 740);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        lblTitulo = new JLabel("Editar Oferta");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitulo.setBounds(330, 20, 131, 21);
        getContentPane().add(lblTitulo);

        lblNombre = new JLabel("Empresa:");
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblNombre.setBounds(180, 60, 71, 21);
        getContentPane().add(lblNombre);

        cbxListaEmpresas = new JComboBox<>();
        cbxListaEmpresas.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        cbxListaEmpresas.setBounds(180, 90, 261, 31);
        getContentPane().add(cbxListaEmpresas);

        lblCupos = new JLabel("Numero de cupos:");
        lblCupos.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCupos.setBounds(490, 60, 91, 21);
        getContentPane().add(lblCupos);

        sbxNumCupos = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        sbxNumCupos.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sbxNumCupos.setBounds(490, 90, 71, 31);
        getContentPane().add(sbxNumCupos);

        lblCorreoElectrinico = new JLabel("Titulo:");
        lblCorreoElectrinico.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCorreoElectrinico.setBounds(180, 130, 131, 21);
        getContentPane().add(lblCorreoElectrinico);

        txtTitulo = new JTextField();
        txtTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtTitulo.setBounds(205, 160, 431, 31);
        getContentPane().add(txtTitulo);

        lblDescripcion = new JLabel("Descripcion:");
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDescripcion.setBounds(205, 200, 91, 21);
        getContentPane().add(lblDescripcion);

        txtDescripcion = new JTextField();
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtDescripcion.setBounds(205, 230, 431, 31);
        getContentPane().add(txtDescripcion);

        lblArea = new JLabel("Area:");
        lblArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblArea.setBounds(205, 270, 51, 21);
        getContentPane().add(lblArea);

        txtArea = new JTextField();
        txtArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtArea.setBounds(205, 300, 431, 31);
        getContentPane().add(txtArea);

        lblFechaCierre = new JLabel("Fecha de cierre:");
        lblFechaCierre.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFechaCierre.setBounds(205, 340, 111, 21);
        getContentPane().add(lblFechaCierre);

        dtFecha = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dtFecha, "dd/MM/yyyy");
        dtFecha.setEditor(dateEditor);
        dtFecha.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        dtFecha.setBounds(205, 405, 351, 31);
        dtFecha.setVisible(false);
        getContentPane().add(dtFecha);

        btnSeleccionarFecha = new JButton("Seleccionar en calendario");
        btnSeleccionarFecha.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnSeleccionarFecha.setBounds(205, 370, 181, 31);
        btnSeleccionarFecha.addActionListener(e -> seleccionarFecha());
        getContentPane().add(btnSeleccionarFecha);

        lblFechaSeleccionada = new JLabel();
        lblFechaSeleccionada.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblFechaSeleccionada.setBounds(405, 370, 231, 31);
        getContentPane().add(lblFechaSeleccionada);
        establecerFecha(new Date());

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnCancelar.setBounds(355, 590, 91, 31);
        getContentPane().add(btnCancelar);

        btnGuardar = new JButton("Guardar y registrar", RecursoVista.icono("forms_add.png"));
        btnGuardar.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnGuardar.setBounds(455, 590, 171, 31);
        getContentPane().add(btnGuardar);
    }

    public void establecerFecha(Date fecha) {
        dtFecha.setValue(fecha);
        lblFechaSeleccionada.setText(new SimpleDateFormat("dd/MM/yyyy").format(fecha));
    }

    private void seleccionarFecha() {
        Date seleccion = SelectorFechaDialog.seleccionar(this, (Date) dtFecha.getValue());
        if (seleccion != null) {
            establecerFecha(seleccion);
        }
    }
}
