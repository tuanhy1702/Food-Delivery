package org.example;

import java.net.*;
import java.net.http.*;
import org.json.*;
class b7 {
    public static void main(String[] args) throws Exception {
        String studentCode ="B22DCCN754";
        String qCode = "Mz3FJplt";
        String getUrl = "http://36.50.135.242:2230/api/rest/method?studentCode="+studentCode+"&qCode="+qCode;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create(getUrl)).build();
        String response = client.send(getRequest, HttpResponse.BodyHandlers.ofString()).body();
        System.out.println(response);

        JSONObject json = new JSONObject(response);
        String requestId = json.getString("requestId");


        JSONObject answer = new JSONObject();
        answer.put("status", "done");

        JSONObject body = new JSONObject();
        body.put("studentCode", studentCode);
        body.put("qCode", qCode);
        body.put("answer", answer);

        String putUrl = "http://36.50.135.242:2230/api/rest/method/"+requestId;
        HttpRequest puRequest = HttpRequest.newBuilder().uri(URI.create(putUrl))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        String response2 = client.send(puRequest, HttpResponse.BodyHandlers.ofString()).body();
        System.out.print(response2);
    }
}

