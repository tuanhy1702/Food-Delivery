package org.example;

import java.net.*;
import java.net.http.*;
import org.json.*;
public class b5 {
    public static void main(String[] args) throws Exception {
        String studentCode ="B22DCCN754";
        String qCode = "ozBLGOXO";
        String getUrl = "http://36.50.135.242:2230/api/rest/object?studentCode="+studentCode+"&qCode="+qCode;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request =HttpRequest.newBuilder().uri(URI.create(getUrl)).build();
        String response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        JSONObject jSONObject = new JSONObject(response);
        System.out.println(response);
        String requestId = jSONObject.getString("requestId");
        JSONObject data = jSONObject.getJSONObject("data");
        double price = data.getDouble("price");
        double taxRate = data.getDouble("taxRate");
        double discount = data.getDouble("discount");
        double finalPrice = price * (1 + taxRate / 100) * (1 - discount / 100);

        JSONObject answer = new JSONObject();
        answer.put("finalPrice", finalPrice);
        JSONObject body = new JSONObject();
        body.put("studentCode", studentCode);
        body.put("qCode", qCode);
        body.put("requestId", requestId);
        body.put("answer",answer );

        String postUrl = "http://36.50.135.242:2230/api/rest/object/submit";
        HttpRequest postRequest = HttpRequest.newBuilder().uri(URI.create(postUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();
        String response2 = client.send(postRequest, HttpResponse.BodyHandlers.ofString()).body();
        System.out.print(response2);
    }
}
