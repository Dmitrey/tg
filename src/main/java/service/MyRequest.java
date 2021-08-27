package service;

import entity.Transport;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.sound.midi.Track;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyRequest {

    static String req = "https://yandex.by/maps/api/masstransit/getStopInfo?ajax=1&csrfToken=4759b402f2a2bcc0db80807b73ff428b2ecfe6a3%3A1630046719&id=stop__10045041&lang=ru&locale=ru_UA&mode=prognosis&s=2655929855&sessionId=1630046705526_262509&uri=ymapsbm1%3A%2F%2Ftransit%2Fstop%3Fid%3Dstop__10045041";

    public static List<Transport> getTransportList() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(request());
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject data = jsonObject.optJSONObject("data");
        JSONArray transports = data.getJSONArray("transports");
        List<Transport> transportList = new ArrayList<>();
        for (int i = 0; i < transports.length(); i++) {
            String time = "n/a";
            JSONObject obj = transports.getJSONObject(i);
            JSONArray threads = obj.getJSONArray("threads");
//            System.out.println(threads);
            JSONObject threads0 = threads.getJSONObject(0);
            JSONObject briefSchedule = threads0.getJSONObject("BriefSchedule");
            JSONArray events = briefSchedule.getJSONArray("Events");
//            System.out.println(events);
            if (events.length() > 0) {
                JSONObject events0 = events.getJSONObject(0);
                if (events0.has("Estimated")) {
                    JSONObject estimated = events0.getJSONObject("Estimated");
                    time = estimated.getString("text");
                }
            }

//            System.out.println(time);

            Transport transport = new Transport(obj.optString("name"), obj.optString("type"), time);
            transportList.add(transport);
        }
        return transportList;
    }

    public static String request() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String res = null;
        try {

            HttpGet request = new HttpGet(req);

            // add request headers
//            request.addHeader("custom-key", "mkyong");
            request.addHeader("cookie", "maps_los=1; is_gdpr=0; is_gdpr_b=CMToPBCJQigC; _yasc=dBKHxVPg+JxqVtKmED7dXkg+xXOlHaoScy+z8mArpn+p6kdyiuo=; gdpr=0; _ym_uid=1629724033575602670; _ym_d=1630046708; yandexuid=723070781627883139; yuidss=723070781627883139; i=GJLbAKK6yG91cycf8b43UjQIMCM61UimUDyB0ykm/xy2orb9Y34FjTCRqedIEudPQ8jtYsljH3Eu4CLi/hfDCpCVs0o=; yp=1630133113.yu.723070781627883139; ymex=1632638713.oyu.723070781627883139#1945406707.yrts.1630046707#1945406708.yrtsi.1630046708");

            CloseableHttpResponse response = httpClient.execute(request);

            try {

                // Get HttpResponse Status
//                System.out.println(response.getProtocolVersion());              // HTTP/1.1
//                System.out.println(response.getStatusLine().getStatusCode());   // 200
//                System.out.println(response.getStatusLine().getReasonPhrase()); // OK
//                System.out.println(response.getStatusLine().toString());        // HTTP/1.1 200 OK

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    String result = EntityUtils.toString(entity);
                    res = result;
//                    System.out.println(result);
                }

            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.close();
        }
        return res;
    }
}
