package com.jk.player.utils;

import java.util.List;

public class CookieHandler {
    public static String setCookie(List<String> cookie) {
        if (cookie != null) {
            StringBuilder sb = new StringBuilder();
            for(String s : cookie) {
                sb.append(s, 0, s.indexOf(";")+1);
            }
            return sb.toString();
        }
        return null;
    }
}
