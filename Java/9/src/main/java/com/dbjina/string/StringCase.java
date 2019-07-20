package com.dbjina.string;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class StringCase {
    private static final Gson gson = new Gson();
    
    Map<String, Object> convertStringToMap(String str) throws Exception{
        return gson.fromJson(str, new TypeToken<Map<String, Object>>(){}.getType());
    }

    List<Map<String, Object>> convertStringToList(String str) throws Exception{
        return gson.fromJson(str, new TypeToken<List<Map<String, Object>>>(){}.getType());
    }
}
