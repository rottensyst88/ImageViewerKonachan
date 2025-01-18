package controlador;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

public class ControladorSistema {
    private static ControladorSistema controladorSistema = new ControladorSistema();

    public static ControladorSistema getInstance() {
        return controladorSistema;
    }

    private String urlImagenActual = "";

    public String[][] obtenerResultados(String solicitud, int selector) throws Exception{
        ArrayList<String[]> resultados = new ArrayList<>();

        String urlString = "https://konachan.com/post.json?tags=" + solicitud + "&limit=100";
        URL url = new URL(urlString);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        // Extraido de chatgpt
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONArray posts = new JSONArray(response.toString());

        for (int i = 0; i < posts.length(); i++) {
            JSONObject post = posts.getJSONObject(i);

            switch (selector){
                case 0:
                    if(post.getString("rating").equals("e")){
                        System.out.println("ID: " + post.getInt("id"));
                        System.out.println("URL de la imagen: " + post.getString("file_url"));
                        System.out.println("Etiquetas: " + post.getString("tags"));
                        System.out.println("Calificación: " + post.getString("rating"));
                        System.out.println("Fuente: " + post.optString("source", "No disponible"));
                        System.out.println("----");

                        String[] fila = {String.valueOf(post.getInt("id")), post.getString("file_url"),
                                post.getString("tags"), post.getString("rating"),
                                post.optString("source", "No disponible")};

                        resultados.add(fila);
                    }
                    break;
                case 1:
                    if(post.getString("rating").equals("q")){
                        System.out.println("ID: " + post.getInt("id"));
                        System.out.println("URL de la imagen: " + post.getString("file_url"));
                        System.out.println("Etiquetas: " + post.getString("tags"));
                        System.out.println("Calificación: " + post.getString("rating"));
                        System.out.println("Fuente: " + post.optString("source", "No disponible"));
                        System.out.println("----");

                        String[] fila = {String.valueOf(post.getInt("id")), post.getString("file_url"),
                                post.getString("tags"), post.getString("rating"),
                                post.optString("source", "No disponible")};

                        resultados.add(fila);
                    }
                    break;
                case 2:
                    if(post.getString("rating").equals("s")){
                        System.out.println("ID: " + post.getInt("id"));
                        System.out.println("URL de la imagen: " + post.getString("file_url"));
                        System.out.println("Etiquetas: " + post.getString("tags"));
                        System.out.println("Calificación: " + post.getString("rating"));
                        System.out.println("Fuente: " + post.optString("source", "No disponible"));
                        System.out.println("----");

                        String[] fila = {String.valueOf(post.getInt("id")), post.getString("file_url"),
                                post.getString("tags"), post.getString("rating"),
                                post.optString("source", "No disponible")};

                        resultados.add(fila);
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

    public void setUrlImagenActual(String url) {
        urlImagenActual = url;
    }

    public String getUrlImagenActual() {
        return urlImagenActual;
    }
}
