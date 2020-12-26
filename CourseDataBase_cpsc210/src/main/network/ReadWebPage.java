package network;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;

public class ReadWebPage {
    private static int currenttemp;
    private static int highesttemp;
    private static int lowesttemp;
    private static int humidity;
    private static String description;

    private static final Integer kelvin = 273;



    public ReadWebPage() {
        BufferedReader br = null;
        try {

//            String apikey = "a5efa349-cc03-4bcd-b387-24c6b530c877"; //fill this in with the API key they email you
//            String londonweatherquery = "https://api.thecatapi.com/v1/images/search";
//            String theURL = londonweatherquery + "?api_key=" + apikey;//this can point to any URL
//
//            URL url = new URL(theURL);
//            br = new BufferedReader(new InputStreamReader(url.openStream()));
//            String line;
//
//            StringBuilder sb = new StringBuilder();
//
//            while ((line = br.readLine()) != null) {
//
//                sb.append(line);
//                sb.append(System.lineSeparator());
//            }
//
//            System.out.println(sb);
//            JSONArray cat = new JSONArray(sb.toString());
//            URL imageurl = new URL(cat.getJSONObject(0).getString("url"));
//            BufferedImage catimage = ImageIO.read(imageurl);
//            System.out.println(catimage);

            JSONObject data = null;
            try {
                data = new JSONObject(getJsondata(br));
            } catch (IOException e) {
                e.printStackTrace();
            }


            JSONArray weather = data.getJSONArray("weather");
            description = weather.getJSONObject(0).getString("main");

            JSONObject temp = data.getJSONObject("main");
            currenttemp = temp.getInt("temp") - kelvin;
            highesttemp = temp.getInt("temp_max") - kelvin;
            lowesttemp = temp.getInt("temp_min") - kelvin;
            humidity = temp.getInt("humidity");



//            JSONArray cat = new JSONArray(sb.toString());
//            URL imageurl = new URL(cat.getJSONObject(0).getString("url"));
//            BufferedImage catimage = ImageIO.read(imageurl);
//            System.out.println(catimage);
//
//

        } finally {

            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String getJsondata(BufferedReader br) throws IOException {

        String apikey = "13c98cac0b5c33c1f0a72ab0039e02c0"; //fill this in with the API key they email you
        String londonweatherquery = "https://api.openweathermap.org/data/2.5/weather?q=Vancouver,ca&APPID=";
        String theURL = londonweatherquery + apikey;


        URL url = new URL(theURL);
        br = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;

        StringBuilder sb = new StringBuilder();

        while ((line = br.readLine()) != null) {

            sb.append(line);
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    //MODIFIES:nothing
    //EFFECTS：return the html form of Vancouver Weather data.
    public String printdata() {
        Date d = new Date();
        return ("<html>Today's weather in Vancouver:"
                + "<br/>Status: " + description + ";" + "<br/>Current temperature: " + currenttemp + "°C;"
                + "<br/>Highest temperature: " + highesttemp + "°C;" + "<br/>Lowest temperature: " + lowesttemp + "°C;"
                + "<br/>Humidity: " + humidity + "%."
                + "<br/>Last update: " + d.toString() + "<html>");
    }
}
