package org.example;


import java.net.*;
import java.net.http.*;
import org.json.*;
public class b14 {
    public static void main(String[] args) throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        String studentCode = "B22DCCN754";
        String qCode = "GJFd5a1m";

        String getUrl = "http://36.50.135.242:2230/api/rest/path?studentCode=B22DCCN754&qCode=GJFd5a1m";
        HttpRequest getRequest = HttpRequest.newBuilder(URI.create(getUrl)).GET().build();
        String response = client.send(getRequest, HttpResponse.BodyHandlers.ofString()).body();
        System.out.println(response);

        JSONObject body = new JSONObject(response);
        String requestId = body.getString("requestId");
        JSONArray data  = body.getJSONArray("data");

        int invoiceId = data.getJSONObject(0).getInt("id");

        String getUrl1 = "http://36.50.135.242:2230/api/rest/path/" +
                invoiceId +
                "?studentCode=" +
                studentCode +
                "&qCode=" + qCode +
                "&requestId=" +requestId +
                "&currency=USD";
        HttpRequest httpRequest1 = HttpRequest.newBuilder()
                .uri(URI.create(getUrl1))
                .GET()
                .build();
        String response1 = client.send(
                httpRequest1,
                HttpResponse.BodyHandlers.ofString()
        ).body();
        System.out.println(response1);
    }
}
