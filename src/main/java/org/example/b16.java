package org.example;

import java.net.*;
import java.net.http.*;
import org.json.*;
public class b16 {
    public static void main(String[] args) throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        String studentCode = "B22DCCN754";
        String qCode = "jMlgkrT5";
        String getUrl = "http://36.50.135.242:2230/api/rest/path?studentCode=B22DCCN754&qCode=jMlgkrT5";
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(getUrl))
                .GET()
                .build();

        String response = client.send(httpRequest,
                HttpResponse.BodyHandlers.ofString()).body();

        JSONObject body = new JSONObject(response);
        String requestId = body.getString("requestId");
        JSONArray data = body.getJSONArray("data");

        String customerId = "";
        int overdueAmount=0;
        int page =0;

        for(int i =0; i<data.length();i++){
            JSONObject x = data.getJSONObject(i);
            if(x.getInt("overdueAmount")>overdueAmount && x.getString("status").equals("OVERDUE")){
                page = x.getInt("page");
                overdueAmount = x.getInt("overdueAmount");
                customerId = x.getString("customerId");
            }
        }

        String getUrl1 = "http://36.50.135.242:2230/api/rest/path/"+customerId+
                "?studentCode="+studentCode+
                "&qCode=" + qCode+
                "&requestId="+requestId+
                "&status=OVERDUE&page="+page;
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
