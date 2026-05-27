package org.example;

import java.net.*;
import java.net.http.*;
import org.json.*;

public class b6 {
    public static void main(String[] args)throws Exception {
        String studentCode ="B22DCCN754";
        String qCode = "zVbAM0tO";
        String getUrl = "http://36.50.135.242:2230/api/rest/object?studentCode="+studentCode+"&qCode="+qCode;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create(getUrl)).build();
        String response = client.send(getRequest, HttpResponse.BodyHandlers.ofString()).body();
        System.out.println(response);
        JSONObject json = new JSONObject(response);
        String requestId = json.getString("requestId");
        JSONObject data = json.getJSONObject("data");
        Double weightKg = data.getDouble("weightKg");
        int  maxEtaDays = data.getInt("maxEtaDays");
        JSONArray quotes = data.getJSONArray("quotes");
        Integer t=0;
        String c="";
        Double b=1.0 *0;
        Double f=1e9*1.0;
        Double r=0.0;
        for(int i=0; i<quotes.length(); i++){
            JSONObject x = quotes.getJSONObject(i);
            Integer etaDays = x.getInt("etaDays");
            if(etaDays<= maxEtaDays){
                double totalFee=Math.round((x.getDouble("baseFee")+weightKg*x.getDouble("perKgFee"))*100)/100.0;
                if(totalFee<=f && x.getDouble("reliability")>r){
                    f = totalFee;
                    t= etaDays;
                    c=x.getString("carrier");
                    r=x.getDouble("reliability");
                }
            }}
        JSONObject body = new JSONObject();
        body.put("carrier", c);
        body.put("totalFee", f);
        body.put("etaDays", t);
        JSONObject o = new JSONObject();
        o.put("requestId", requestId);
        o.put("studentCode", studentCode);
        o.put("qCode", qCode);
        o.put("answer", body);
        String postUrl = "http://36.50.135.242:2230/api/rest/object/submit";
        HttpRequest postRequest = HttpRequest.newBuilder().uri(URI.create(postUrl))
                .header("Content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(o.toString()))
                .build();
        String res2 = client.send(postRequest,HttpResponse.BodyHandlers.ofString()).body();
        System.out.println(res2);

    }
}