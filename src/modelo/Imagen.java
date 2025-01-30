package modelo;

import controlador.ControladorSistema;

import excepciones.SistemaExcepcionesAPP;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class Imagen {
    private int id;
    private String urlImagen;
    private String etiquetas;
    private String calificacion;
    private String fuente;
    private Image image;

    public Imagen(int id, String urlImagen, String etiquetas, String calificacion, String fuente) {
        this.id = id;
        this.urlImagen = urlImagen;
        this.etiquetas = etiquetas;
        this.calificacion = calificacion;
        this.fuente = fuente;
        image = null;
    }

    public int getId() {
        return id;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public String getEtiquetas() {
        return etiquetas;
    }

    public String getCalificacion() {
        return calificacion;
    }

    public String getFuente() {
        return fuente;
    }

    public Image getImage(){
        try {
            URL url = new URL(urlImagen);
            image = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (image != null) {
            // Obtener las dimensiones originales de la imagen
            int originalWidth = image.getWidth(null);
            int originalHeight = image.getHeight(null);

            // Calcular las nuevas dimensiones para mantener la relación de aspecto
            double aspectRatio = (double) originalWidth / originalHeight;
            int newWidth = 1366; // Ancho máximo
            int newHeight = (int) (newWidth / aspectRatio); // Alto proporcional

            // Si el alto calculado es mayor que el máximo (600), ajustamos el alto
            if (newHeight > 768) {
                newHeight = 768; // Alto máximo
                newWidth = (int) (newHeight * aspectRatio); // Ancho proporcional
            }

            // Escalar la imagen manteniendo la relación de aspecto
            return image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        }

        return null;
    }
}
