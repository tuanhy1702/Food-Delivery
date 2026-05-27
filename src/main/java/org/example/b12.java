
package com.mycompany.testmaven;
import java.net.*;
import java.net.http.*;
import org.json.*;
public class b12 {
    public static void main(String[] args) throws Exception {
        String qCode ="BRR5yCDa";
        String studentCode ="B22DCCN754";
        String getUrl = "http://36.50.135.242:2230/api/rest/header?studentCode="+studentCode+"&qCode="+qCode;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create(getUrl)).build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        String x = response.body();
        System.out.println(x);
        String x_Checksum = response.headers().firstValue("X-Checksum").orElse("");
        JSONObject json = new JSONObject(x);
        String requestId = json.getString("requestId");
        JSONObject body = new JSONObject();
        body.put("studentCode", studentCode);
        body.put("qCode", qCode);
        body.put("requestId", requestId);
        String postUrl ="http://36.50.135.242:2230/api/rest/header/submit";
        HttpRequest potRequest= HttpRequest.newBuilder().uri(URI.create(postUrl))
                .header("X-Checksum", x_Checksum)
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();
        String res2 = client.send(potRequest, HttpResponse.BodyHandlers.ofString()).body();
        System.out.println(res2);
    }
}