package vista;

import javax.swing.ImageIcon;
import java.io.File;
import java.net.URL;

public final class RecursoVista {
    private RecursoVista() {
    }

    public static ImageIcon icono(String nombreArchivo) {
        return cargar("iconos/" + nombreArchivo);
    }

    public static ImageIcon imagen(String nombreArchivo) {
        return cargar("imagenes/" + nombreArchivo);
    }

    private static ImageIcon cargar(String ruta) {
        URL recurso = RecursoVista.class.getClassLoader().getResource(ruta);
        if (recurso != null) {
            return new ImageIcon(recurso);
        }

        File archivo = new File(ruta);
        if (archivo.exists()) {
            return new ImageIcon(archivo.getAbsolutePath());
        }

        archivo = new File("src/vista/" + ruta);
        if (archivo.exists()) {
            return new ImageIcon(archivo.getAbsolutePath());
        }

        return new ImageIcon();
    }
}
