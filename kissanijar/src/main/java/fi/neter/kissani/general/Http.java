package fi.neter.kissani.general;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonHttpParser;

public class Http {
    
    
    public static String fetch(String string) throws IOException {
            URL url = new URL(string);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int result = connection.getResponseCode();
            
            if ((result == 200) || (result == 201) || (result == 202)) {
                connection.connect();

                // read the output from the server
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                  stringBuilder.append(line + "\n");
                }
                return stringBuilder.toString();
            }
            System.out.println("Error response code: " + result);
            if (result == 400) {
                connection.connect();

                // read the output from the server
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                  stringBuilder.append(line + "\n");
                }
                System.out.println("Error: " + stringBuilder.toString());
            }
            return null;
    }

    public static <T> T googleFetch(String string, Class<T> classType) throws IOException {
        HttpTransport transport = new HttpTransport();
        transport.defaultHeaders.put("GData-Version", "2");
        JsonHttpParser parser = new JsonHttpParser();
        parser.contentType = "text/javascript";
        transport.addParser(parser);
        // build the HTTP GET request and URL
        HttpRequest request = transport.buildGetRequest();
        string = string.replaceAll("\\|", "%7C"); // A hack to workaround broken FB API.
        GenericUrl url = new GenericUrl(string);
        request.url = url;
        HttpResponse response = request.execute();
        try {
        	if (response.getContent() == null) {
        		return null;
        	}
            return response.parseAs(classType);
        } catch (IllegalArgumentException e) {
        	System.out.println("Exception: " + e.getMessage() + " for response: " + response.parseAsString()
        			+ " from URL: " + string);
        	throw e;
        }
    }

}
