package com.practice;

import static org.junit.Assert.assertTrue;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void firstTest() throws Exception {
        String response = "{\"response\": [{\"id\": 0,\"first_name\": \"Вася\",\"last_name\": \"Пупкин\",\"sex\": 2,\"online\": 1}]}";
        Object obj2 = new JSONParser().parse(response);

        JSONObject jo = (JSONObject) obj2;
        JSONArray jsonArray = (JSONArray) jo.get("response");
        String result = "";
        String cmp = "Имя: Вася\nФамилия: Пупкин";
        assertTrue(result.startsWith(cmp));
        //@Mock Mockito
    }
}
