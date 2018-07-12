package com.practice.Vk;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Api {
    public static String AUTH_URL = "https://oauth.vk.com/authorize?client_id=5536965&display=popup&redirect_uri=https://oauth.vk.com/blank.html&scope=friends,&response_type=token&v=5.80";

    private static String URL = "https://api.vk.com/method/";
    private static String FIELDS_FULL = "&fields=photo_100,sex,bdate,city,country,home_town,online,domain,has_mobile,contacts,site,education,universities,schools,status,last_seen,followers_count,common_count,occupation,nickname,relatives,relation,personal,connections,exports,wall_comments,activities,interests,music,movies,tv,books,games,about,quotes,timezone,career,military&access_token=";
    private static String FIELDS_SHORT = "&fields=photo_100,nickname,bdate&access_token=";
    private static String VERSION = "&v=5.80";

    private String token;
    private String owner_id;
    public String getToken() {
        return  token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getOwnerId() {
        return owner_id;
    }

    public void parseToken(String url) {
        int index = url.indexOf("access_token=") + 13;
        token = url.substring(index, index + 85);
        owner_id = url.substring(url.indexOf("user_id=") + 8);
        System.out.println(owner_id);
    }

    public User[] getUsersInfo(String username, boolean isFull) throws Exception {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(URL).append("users.get?user_ids=").append(username);
        if (isFull) stringBuffer.append(FIELDS_FULL).append(token).append(VERSION);
        else stringBuffer.append(FIELDS_SHORT).append(token).append(VERSION);
        StringBuffer response = new StringBuffer();
        java.net.URL obj = new URL(stringBuffer.toString());
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);
        in.close();
        return parseUsers(parseResponse(response));
    }

    private JSONArray parseResponse(StringBuffer stringBuffer) throws Exception {
        Object obj2 = new JSONParser().parse(stringBuffer.toString());
        JSONObject jo = (JSONObject) obj2;
        if (jo.containsKey("error")) {
            System.out.println("Ошибка API");
            jo = (JSONObject) jo.get("error");
            System.out.println("Код ошибки: " + jo.get("error_code"));
            System.out.println("Подробности: " + jo.get("error_msg"));
        } else return (JSONArray) jo.get("response");
        return null;
    }

    private User[] parseUsers(JSONArray jsonArray) {
        if (jsonArray != null) {
            User[] users = new User[jsonArray.size()];
            for (int i = 0; i < jsonArray.size(); i++)
                users[i] = new User((JSONObject) jsonArray.get(i));
            return users;
        }
        return null;
    }

    public User[] getMutualFriends(String id1, String id2) throws Exception {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(URL).append("friends.getMutual?source_uid=").append(id1).append("&target_uid=").append(id2).append("&access_token=").append(token).append(VERSION);
        StringBuffer response = new StringBuffer();
        URL obj = new URL(stringBuffer.toString());
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);
        in.close();
        JSONArray jsonArray = parseResponse(response);
        if (jsonArray!=null) {
            StringBuffer ids;
            User[] users = new User[jsonArray.size()];
            for (int i = 0; i < jsonArray.size(); i += 100) {
                System.out.println(i);
                ids = new StringBuffer();
                for (int j = 0; (j + i) < jsonArray.size() && j < 100; j++)
                    ids.append(jsonArray.get(j + i).toString()).append(",");
                ids.delete(ids.length() - 2, ids.length() - 1);
                User[] tmp = getUsersInfo(ids.toString(), false);
                Thread.sleep(330);
                for (int j = 0; j < tmp.length; j++)
                    users[i + j] = tmp[j];
            }
            return users;
        }
        return null;
    }
}
