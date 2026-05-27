package com.mycompany.testmaven;
import java.net.*;
import java.net.http.*;
import org.json.*;
public class b11 {
    public static void main(String[] args) throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        String studentCode = "B22DCCN754";
        String qCode = "rkgCZEsq";
        
        String getUrl ="http://36.50.135.242:2230/api/rest/method"
                +"?studentCode="+studentCode
                +"&qCode="+qCode;
        
        HttpRequest httpRequest= HttpRequest.newBuilder()
                .uri(URI.create(getUrl))
                .GET()
                .build();
        
        String response = client.send(httpRequest, 
                HttpResponse.BodyHandlers.ofString()).body();
        
        System.out.println(response);
        JSONObject body = new JSONObject(response);
        
        String requestId = body.getString("requestId");
        JSONObject data = body.getJSONObject("data");
        String etag = data.getString("etag");
       
        JSONObject answer = new JSONObject();
        answer.put("status", "RESOLVED");
        
        JSONObject body2 = new JSONObject();
        body2.put("studentCode", studentCode);
        body2.put("qCode", qCode );
        body2.put("answer", answer );
        
        String getUrl1 = "http://36.50.135.242:2230/api/rest/method/"+requestId;
        HttpRequest httpRequest1 = HttpRequest.newBuilder()
                .uri(URI.create(getUrl1))
                .header("If-Match", etag)
                .method("PATCH", HttpRequest.BodyPublishers.ofString(body2.toString()))
                .header("Content-Type", "application/json")
                .build();
        
        String result = client.send(httpRequest1, 
                HttpResponse.BodyHandlers.ofString()).body();
        System.out.println(result);
    }
}
