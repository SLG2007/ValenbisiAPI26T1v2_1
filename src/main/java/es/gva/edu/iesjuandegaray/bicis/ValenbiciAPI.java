package es.gva.edu.iesjuandegaray.bicis;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;

import java.io.IOException;

public class ValenbiciAPI {

	
	// https://geoportal.valencia.es/server/rest/services/OPENDATA/Trafico/MapServer/228/query?where=1=1&outFields=*&f=json
    private static final String API_URL =
            "https://geoportal.valencia.es/server/rest/services/OPENDATA/Trafico/MapServer/228/query"
            + "?where=1%3D1"
            + "&outFields=*"
            + "&returnGeometry=true"
            + "&f=json";

    public static void main(String[] args) {

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            HttpGet request = new HttpGet(API_URL);
            HttpResponse response = httpClient.execute(request);

            HttpEntity entity = response.getEntity();

            if (entity != null) {

                String result = EntityUtils.toString(entity);

                // Convertimos a JSON
                JSONObject jsonObject = new JSONObject(result);

                // Obtenemos el array "features"
                JSONArray features = jsonObject.getJSONArray("features");

                System.out.println("Número de estaciones: " + features.length());
                System.out.println();

                // BUCLE RECORRE VECTOR FEATURES MOSTRANDO LOS DATOS SOLICITADOS.
                
                for (int i=0; i<features.length(); i++) {
                	JSONObject estacion = (JSONObject) features.get(i);
                	JSONObject atributos = estacion.getJSONObject("attributes");
                	JSONObject geometria = estacion.getJSONObject("geometry");
                	String name = atributos.getString("name");
                	int available = atributos.getInt("available");
                	int free = atributos.getInt("free");
                	double x = geometria.getInt("x");
                	double y = geometria.getInt("y");
                	Geometry ubicacion = new Geometry();
                	ubicacion.x = x;
                	ubicacion.y = y;
                	System.out.println("Estación: " + name + "\n\t  Bicis disponibles: " + available + "\n\t  Espacios disponibles: " + free + "\n\t" + "  Coordenadas: x=" + ubicacion.x + ", y=" + ubicacion.y + "\n");
                	
                	
                }
                
                    
                
                
            }

        } catch (IOException e) {
            System.out.println("Error en la petición HTTP:");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error procesando JSON:");
            e.printStackTrace();
        }
    }
}