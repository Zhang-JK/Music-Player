package com.jk.player.utils;

import com.jk.player.model.User;
import com.jk.player.model.UserCookie;

import java.time.Instant;
import java.util.List;

public class CookieHandler {
    public static String setCookie(List<String> cookie) {
        if (cookie != null) {
            StringBuilder sb = new StringBuilder();
            for (String s : cookie) {
                sb.append(s, 0, s.indexOf(";") + 1);
            }
            return sb.toString();
        }
        return null;
    }

    public static UserCookie setCookie(List<String> cookie, UserCookie userCookie, User user, Platforms platform) {
//        String data = platform==Platforms.NETEASE ? cookie.toString().replace("[", "").replace("]", "") : setCookie(cookie);
        if (userCookie == null) {
            UserCookie newUserCookie = new UserCookie();
            newUserCookie.setUser(user);
            newUserCookie.setPlatform(platform.getNumVal());
            newUserCookie.setData(setCookie(cookie));
            newUserCookie.setUpdateTime(Instant.now());
            return newUserCookie;
        } else {
            userCookie.setData(setCookie(cookie));
            userCookie.setUpdateTime(Instant.now());
            return userCookie;
        }
    }
}
