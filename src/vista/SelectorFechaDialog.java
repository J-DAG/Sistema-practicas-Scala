package vista;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SelectorFechaDialog extends JDialog {

    private Date fechaSeleccionada;
    private final Calendar calendario;
    private final JLabel lblMesAnio;
    private final JPanel pnlDias;

    private SelectorFechaDialog(Window owner, Date fechaInicial) {
        super(owner, "Seleccionar fecha", ModalityType.APPLICATION_MODAL);
        setSize(460, 460);
        setResizable(false);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(8, 8));

        calendario = Calendar.getInstance();
        calendario.setTime(fechaInicial != null ? fechaInicial : new Date());

        JPanel pnlSuperior = new JPanel(new BorderLayout());
        JButton btnAnterior = new JButton("<");
        JButton btnSiguiente = new JButton(">");
        lblMesAnio = new JLabel("", SwingConstants.CENTER);
        lblMesAnio.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnAnterior.addActionListener(e -> cambiarMes(-1));
        btnSiguiente.addActionListener(e -> cambiarMes(1));

        pnlSuperior.add(btnAnterior, BorderLayout.WEST);
        pnlSuperior.add(lblMesAnio, BorderLayout.CENTER);
        pnlSuperior.add(btnSiguiente, BorderLayout.EAST);
        add(pnlSuperior, BorderLayout.NORTH);

        pnlDias = new JPanel(new GridLayout(7, 7, 4, 4));
        pnlDias.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(pnlDias, BorderLayout.CENTER);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        JPanel pnlInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlInferior.add(btnCancelar);
        add(pnlInferior, BorderLayout.SOUTH);

        cargarDias();
    }

    public static Date seleccionar(Window owner, Date fechaInicial) {
        SelectorFechaDialog dialogo = new SelectorFechaDialog(owner, fechaInicial);
        dialogo.setVisible(true);
        return dialogo.fechaSeleccionada;
    }

    private void cambiarMes(int cambio) {
        calendario.add(Calendar.MONTH, cambio);
        cargarDias();
    }

    private void cargarDias() {
        pnlDias.removeAll();
        SimpleDateFormat formatoMes = new SimpleDateFormat("MMMM yyyy", new Locale("es", "EC"));
        lblMesAnio.setText(formatoMes.format(calendario.getTime()));

        String[] diasSemana = {"Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom"};
        for (String dia : diasSemana) {
            JLabel etiqueta = new JLabel(dia, SwingConstants.CENTER);
            etiqueta.setFont(new Font("Segoe UI", Font.BOLD, 11));
            etiqueta.setOpaque(true);
            etiqueta.setBackground(new Color(235, 238, 242));
            pnlDias.add(etiqueta);
        }

        Calendar base = (Calendar) calendario.clone();
        base.set(Calendar.DAY_OF_MONTH, 1);
        int primerDiaSemana = base.get(Calendar.DAY_OF_WEEK);
        int espacios = primerDiaSemana == Calendar.SUNDAY ? 6 : primerDiaSemana - 2;

        int celdasUsadas = 7;
        for (int i = 0; i < espacios; i++) {
            pnlDias.add(new JLabel(""));
            celdasUsadas++;
        }

        int maximoDia = base.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int dia = 1; dia <= maximoDia; dia++) {
            JButton botonDia = new JButton(String.valueOf(dia));
            botonDia.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            botonDia.setMargin(new Insets(2, 2, 2, 2));
            int diaSeleccionado = dia;
            botonDia.addActionListener(e -> seleccionarDia(diaSeleccionado));
            pnlDias.add(botonDia);
            celdasUsadas++;
        }

        while (celdasUsadas < 49) {
            pnlDias.add(new JLabel(""));
            celdasUsadas++;
        }

        pnlDias.revalidate();
        pnlDias.repaint();
    }

    private void seleccionarDia(int dia) {
        calendario.set(Calendar.DAY_OF_MONTH, dia);
        fechaSeleccionada = calendario.getTime();
        dispose();
    }
}
