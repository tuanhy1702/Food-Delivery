package org.example;

import java.net.*;
import java.net.http.*;
import org.json.*;

public class b1 {
    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String getUrl = "http://36.50.135.242:2230/api/rest/data?studentCode=B22DCCN418&qCode=Jfqpizls";
        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(getUrl)).GET().build();
        String response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString()).body();
        System.out.print(response);
        JSONObject json = new JSONObject(response);
        String requestId = json.getString("requestId");
        JSONArray array = json.getJSONArray("data");
        int t =0;
        for(int i=0; i< array.length(); i++){
            t+= array.getInt(i);
        }
        String postUrl = "http://36.50.135.242:2230/api/rest/data/submit";
        JSONObject body = new JSONObject();
        body.put("studentCode", "B22DCCN418");
        body.put("qCode", "Jfqpizls");
        body.put("requestId", requestId);
        body.put("answer", t);
        HttpRequest postRequest =
                HttpRequest.newBuilder()
                        .uri(URI.create(
                                "http://36.50.135.242:2230/api/rest/data/submit"
                        ))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                        .build();
        String result =
                client.send(
                        postRequest,
                        HttpResponse.BodyHandlers.ofString()
                ).body();
        System.out.println(result);
    }


}