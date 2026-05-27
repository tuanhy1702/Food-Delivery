package org.example;

import java.net.*;
import java.net.http.*;
import org.json.*;

public class b2 {
    public static void main (String[] args)throws Exception  {
        HttpClient client = HttpClient.newHttpClient();
        String studentCode = "B22DCCN754";
        String qCode = "8efCsUXv";
        String getUrl =
                "http://36.50.135.242:2230/api/rest/data"
                        + "?studentCode=" + studentCode
                        + "&qCode=" + qCode;
        HttpRequest get = HttpRequest.newBuilder()
                .uri(URI.create(getUrl))
                .GET()
                .build();

        String response =
                client.send(get, HttpResponse.BodyHandlers.ofString())
                        .body();

        System.out.println(response);

        JSONObject a = new JSONObject(response);
        String id = a.getString("requestId");
        JSONArray data = a.getJSONArray("data");
        double cap =0;
        double ref =0;
        int fail = 0;
        for(int i=0; i<data.length(); i++){
            JSONObject tran = data.getJSONObject(i);
            double amout = tran.getDouble("amount");
            String status = tran.getString("status");
            if(status.equals("CAPTURED")) cap+=amout;
            else if(status.equals("REFUNDED")) ref+=amout;
            else if(status.equals("FAILED")) fail++;
        }
        double net = cap -ref;
        JSONObject answer = new JSONObject();
        answer.put("capturedTotal", cap);
        answer.put("refundedTotal", ref);
        answer.put("netTotal", net);
        answer.put("failedCount", fail);

        JSONObject body = new JSONObject();
        body.put("studentCode", studentCode);
        body.put("qCode", qCode);
        body.put("requestId", id);
        body.put("answer", answer);

        String postUrl = "http://36.50.135.242:2230/api/rest/data/submit";
        HttpRequest post = HttpRequest.newBuilder().uri(URI.create(postUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();
        String response2 = client.send(post, HttpResponse.BodyHandlers.ofString()).body();
        System.out.println(response2);
    }
}
