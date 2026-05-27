package org.example;

import java.net.*;
import java.net.http.*;
import java.util.*;

import org.json.*;
public class b3 {
    public static void main(String[] args) throws Exception{
        String studentCode = "B22DCCN754";
        String qCode = "RYq5o8wF";

        HttpClient client = HttpClient.newHttpClient();

        String getUrl = "http://36.50.135.242:2230/api/rest/character?studentCode=B22DCCN754&qCode=RYq5o8wF";

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(getUrl))
                .GET()
                .build();

        String response = client.send(httpRequest,
                HttpResponse.BodyHandlers.ofString()).body();
        System.out.println(response);
        JSONObject body = new JSONObject(response);

        String requestId  = body.getString("requestId");
        String data = body.getString("data");

        String a[] = data.trim().split("\\s+");

        Arrays.sort(a);

        String s = String.join(" ", a);

        s= s.trim();

        JSONObject body1 = new JSONObject();

        body1.put("studentCode", studentCode);
        body1.put("qCode", qCode);
        body1.put("requestId", requestId  );
        body1.put("answer", s );

        String postUrl = "http://36.50.135.242:2230/api/rest/character/submit";

        HttpRequest httpRequest1 = HttpRequest.newBuilder()
                .uri(URI.create(postUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body1.toString()))
                .build();

        String result = client.send(httpRequest1,
                HttpResponse.BodyHandlers.ofString()).body();

    }
}
