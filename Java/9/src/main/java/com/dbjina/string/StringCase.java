package com.dbjina.string;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;


import java.util.List;
import java.util.Map;

public class StringCase {
    
    private StringCase() {
        
    }
    
    private static final Gson gson = new Gson();

    /**
     * str 을 Map 으로 변환
     * @param str JSON String
     * @return Map 반환
     */
    public static Map<String, Object> convertStringToMap(String str) {
        return gson.fromJson(str, new TypeToken<Map<String, Object>>(){}.getType());
    }

    /**
     * str 을 List&lt;Map&gt; 으로 반환
     * @param str JSON String
     * @return List&lt;Map&gt; 반환
     */
    public static List<Map<String, Object>> convertStringToList(String str) {
        return gson.fromJson(str, new TypeToken<List<Map<String, Object>>>(){}.getType());
    }


    /**
     * str 을 DateTime 으로 반환
     * @param str 날짜 포맷의 String
     * @return DateTime 반환
     */
    public static DateTime convertStringToDateTime(String str) {
        if(str == null)
            return null;
        
        String pattern = "yyyyMMddHHmmss";
        String removedStr = str.replaceAll("[\\D]", "");
        
        if(removedStr.length() >= 8) {
            removedStr = Strings.padEnd(removedStr, 14, '0');
        }
        
        return DateTimeFormat.forPattern(pattern).parseDateTime(removedStr);
    }
}
