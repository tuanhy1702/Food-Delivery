package com.mycompany.testmaven;

import java.net.*;
import java.net.http.*;

import org.json.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class b13 {

    public static String hmasSHA256(String data, String key) throws Exception {

        Mac mac = Mac.getInstance("HmacSHA256");

        SecretKeySpec secret =
                new SecretKeySpec(key.getBytes(), "HmacSHA256");

        mac.init(secret);

        byte[] hashBytes = mac.doFinal(data.getBytes());

        StringBuilder sb = new StringBuilder();

        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

    public static void main(String[] args) throws Exception {

        String studentCode = "B22DCCN754";
        String qCode = "eYT6GRNN";

        String baseUrl =
                "http://36.50.135.242:2230/api/rest/header";

        String getUrl =
                baseUrl
                        + "?studentCode=" + studentCode
                        + "&qCode=" + qCode;

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create(getUrl))
                        .GET()
                        .build();

        String response =
                client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                ).body();

        System.out.println(response);

        JSONObject a = new JSONObject(response);

        String requestId = a.getString("requestId");

        JSONObject data = a.getJSONObject("data");

        String nonce = data.getString("nonce");

        String signingKey = data.getString("signingKey");

        JSONArray events = data.getJSONArray("events");


        String s = nonce + ":";

        for (int i = 0; i < events.length(); i++) {

            s += events.getString(i);

            if (i != events.length() - 1) {
                s += "|";
            }
        }

        s += ":" + studentCode.toUpperCase();

        System.out.println("PAYLOAD = " + s);

        String signature =
                hmasSHA256(s, signingKey);

        System.out.println("SIGNATURE = " + signature);

        JSONObject obj = new JSONObject();

        obj.put("studentCode", studentCode);
        obj.put("qCode", qCode);
        obj.put("requestId", requestId);

        HttpRequest submitRequest =
                HttpRequest.newBuilder()
                        .uri(URI.create(baseUrl + "/submit"))
                        .header("X-Signature", signature)
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(obj.toString()))
                        .build();

        String submitResponse =
                client.send(
                        submitRequest,
                        HttpResponse.BodyHandlers.ofString()
                ).body();

        System.out.println(submitResponse);
    }
}