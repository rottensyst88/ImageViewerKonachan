package controlador;

import excepciones.SistemaExcepcionesAPP;
import io.IOarchivo;
import modelo.Imagen;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.*;

public class ControladorSistema {
    private Imagen imagenActual;
    private static final ControladorSistema controladorSistema = new ControladorSistema();
    public static ControladorSistema getInstance() {
        return controladorSistema;
    }
    public String[][] obtenerResultados(String solicitud, int selector, int numPagina) throws SistemaExcepcionesAPP {
        ArrayList<String[]> resultados = new ArrayList<>();

        String urlString = "https://konachan.com/post.json?limit=100&page=" + numPagina + "&tags=" + solicitud;

        System.out.println(urlString);
        StringBuffer response;

        try {
            URL url = new URL(urlString);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            // CODIGO REQLIAO

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;

            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        } catch (MalformedURLException e) {
            throw new SistemaExcepcionesAPP("Error al obtener la URL");
        } catch (IOException e) {
            throw new SistemaExcepcionesAPP("Error al abrir la conexión");
        }

        JSONArray posts = new JSONArray(response.toString());

        for (int i = 0; i < posts.length(); i++) {
            JSONObject post = posts.getJSONObject(i);

            switch (selector) {
                case 0:
                    if (post.getString("rating").equals("e")) {
                        resultados.add(manejarDatosJSON(post));
                    }
                    break;
                case 1:
                    if (post.getString("rating").equals("q")) {
                        resultados.add(manejarDatosJSON(post));
                    }
                    break;
                case 2:
                    if (post.getString("rating").equals("s")) {
                        resultados.add(manejarDatosJSON(post));
                    }
                    break;
            }
        }

        String[][] resultadosArray = new String[resultados.size()][5];

        for (int i = 0; i < resultados.size(); i++) {
            String[] fila = resultados.get(i);

            resultadosArray[i][0] = fila[0];
            resultadosArray[i][1] = fila[1];
            resultadosArray[i][2] = fila[2];
            resultadosArray[i][3] = fila[3];
            resultadosArray[i][4] = fila[4];
        }

        return resultadosArray;
    }
    public void saveImagen(Imagen img) {
        imagenActual = img;
    }
    public Imagen loadImagen() {
        return imagenActual;
    }
    public void descargarImagen() throws SistemaExcepcionesAPP {
        try{
            URL url_paraDescargar = new URL(imagenActual.getUrlImagen());
            String nombre_imagen = imagenActual.getId() + "." + imagenActual.getFormato();

            IOarchivo.getInstance().descargarImagenAPC(url_paraDescargar, nombre_imagen, imagenActual.getFormato());

        } catch (MalformedURLException e){
            throw new SistemaExcepcionesAPP("Error al obtener la URL");
        } catch (SistemaExcepcionesAPP e){
            throw new SistemaExcepcionesAPP(e.getMessage());
        }
    }

    private String[] manejarDatosJSON(JSONObject post) {
        return new String[]{String.valueOf(post.getInt("id")), post.getString("file_url"),
                post.getString("tags"), post.getString("rating"),
                post.optString("source", "No disponible")};
    }
}
