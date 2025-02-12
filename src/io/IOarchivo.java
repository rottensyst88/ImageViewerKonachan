package io;

import excepciones.SistemaExcepcionesAPP;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class IOarchivo {
    private static IOarchivo ioarchivo = new IOarchivo();

    public static IOarchivo getInstance() {
        return ioarchivo;
    }

    public void descargarImagenAPC(URL url, String nombreArchivo, String formato) throws SistemaExcepcionesAPP {
        try{
            String dirDescargas = System.getProperty("user.home");
            File descargas = new File(dirDescargas + File.separator + "Downloads");

            File arch = new File(descargas, nombreArchivo);
            BufferedImage image = ImageIO.read(url);
            ImageIO.write(image, formato, arch);
            System.out.println(arch.getAbsolutePath());

        }catch(Exception e){
            throw new SistemaExcepcionesAPP("Error al descargar la imagen");
        }
    }
}
