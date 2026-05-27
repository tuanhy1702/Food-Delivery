package com.mycompany.testmaven;

import java.net.*;
import java.net.http.*;
import org.json.*;

public class b8 {
    public static void main(String[] args)throws Exception {
        String studentCode ="B22DCCN754";
        String qCode ="e92U4IY5";
        String getUrl = "http://36.50.135.242:2230/api/rest/header?studentCode="+studentCode+"&qCode="+qCode;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create(getUrl)).build();
        HttpResponse<String>  response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        String body = response.body();
        String checksum = response.headers().firstValue("X-Checksum").orElse("");
        JSONObject json = new JSONObject(body );
        String requestId = json.getString("requestId");
        String postUrl = "http://36.50.135.242:2230/api/rest/header/submit";
        JSONObject se = new JSONObject();
        se.put("studentCode", studentCode);
        se.put("qCode", qCode);
        se.put("requestId", requestId);
        HttpRequest postRequest= HttpRequest.newBuilder().uri(URI.create(postUrl))
                .headers("X-Checksum", checksum)
                .POST(HttpRequest.BodyPublishers.ofString(se.toString()))
                .build();
        String res2 = client.send(postRequest, HttpResponse.BodyHandlers.ofString()).body();
        System.out.println(res2);
    }
    
}