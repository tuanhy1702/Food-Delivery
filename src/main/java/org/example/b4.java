package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

public class b4 {
    public static void main(String[] args) throws Exception {
        String studentCode = "B22DCCN754";
        String qcode = "YdwXuLxi";
        String getUrl =
                "http://36.50.135.242:2230/api/rest/character"
                        + "?studentCode=" + studentCode
                        + "&qCode=" + qcode;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getUrl))
                .GET()
                .build();
        String response =
                client.send(request, HttpResponse.BodyHandlers.ofString())
                        .body();
        System.out.println(response);

        JSONObject jsonObject = new JSONObject(response);
        String requestId = jsonObject.getString("requestId");
        String data = jsonObject.getString("data");
        String[] logs = data.split("\\|\\|");
        String result = "";
        for (int i = 0; i < logs.length; i++) {
            String line = logs[i];
            String[] words = line.split("\\s+");
            String newLine = "";
            for (String word : words) {
                if (word.startsWith("user=")) {
                    newLine += "user=[EMAIL] ";
                }
                else if (word.startsWith("phone=")) {
                    newLine += "phone=[PHONE] ";
                }
                else if (word.startsWith("token=")) {
                    newLine += "token=[TOKEN] ";
                }
                else {
                    newLine += word + " ";
                }
            }
            newLine = newLine.trim();
            result +=newLine;
            if (i != logs.length - 1) {
                result += "||";
            }
        }
        System.out.println(result);
        JSONObject body = new JSONObject();
        body.put("studentCode", studentCode);
        body.put("qCode", qcode);
        body.put("requestId", requestId);
        body.put("answer", result);
        HttpRequest post = HttpRequest.newBuilder()
                .uri(
                        URI.create(
                                "http://36.50.135.242:2230/api/rest/character/submit"
                        )
                )
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        String response2 =
                client.send(post, HttpResponse.BodyHandlers.ofString())
                        .body();

        System.out.println(response2);
    }
}