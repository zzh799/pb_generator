package utils;

import java.util.Locale;

public class StringUtils {
    public static String lowerFirstCase(String str) {
        String firstCase = str.substring(0, 1);
        return firstCase.toLowerCase() + str.substring(1);
    }

    public static String upperFirstCase(String str) {
        String firstCase = str.substring(0, 1);
        return firstCase.toUpperCase() + str.substring(1);
    }

    public static String lowerAllCase(String str){
        return str.toLowerCase();
    }

    public static String upperAllCase(String str){
        return str.toUpperCase();
    }
}
