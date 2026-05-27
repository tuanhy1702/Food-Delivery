package com.mycompany.testmaven;

import java.net.*;
import java.net.http.*;
import org.json.*;

public class b9 {
    public static void main(String[] args) throws Exception {
        String studentCode ="B22DCCN754";
        String qCode="DLk58STA";
        String getUrl = "http://36.50.135.242:2230/api/rest/path?studentCode="+studentCode+"&qCode="+qCode;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create(getUrl)).build();
        String response = client.send(getRequest, HttpResponse.BodyHandlers.ofString()).body();
        System.out.println(response);
        JSONObject json = new JSONObject(response);
        String requestId = json.getString("requestId");
        JSONArray data = json.getJSONArray("data");
        Integer productId= data.getJSONObject(0).getInt("id");
        JSONObject body = new JSONObject();
        body.put("studentCode", studentCode);
        body.put("qCode", qCode);
        String url2 =
                "http://36.50.135.242:2230/api/rest/path/"
                        + productId
                        + "?studentCode=" + studentCode
                        + "&qCode=" + qCode
                        + "&requestId=" + requestId
                        + "&currency=USD";        
        HttpRequest getRequest2 = HttpRequest.newBuilder().uri(URI.create(url2)).build();
        String response2 = client.send(getRequest2, HttpResponse.BodyHandlers.ofString()).body();
        System.out.println(response2);
        
    }
}