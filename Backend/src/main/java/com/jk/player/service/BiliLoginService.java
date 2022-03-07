package com.jk.player.service;

import cn.hutool.json.JSONObject;
import com.jk.player.dao.UserCookieDAO;
import com.jk.player.model.User;
import com.jk.player.model.UserCookie;
import com.jk.player.result.ResponseCode;
import com.jk.player.utils.Platforms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Objects;

@Service
public class BiliLoginService {

    @Autowired
    UserCookieDAO userCookieDAO;

    RestTemplate restTemplate = new RestTemplate();

    public JSONObject getBiliLoginUrl() {
        return restTemplate.getForObject("https://passport.bilibili.com/qrcode/getLoginUrl", JSONObject.class);
    }

    public ResponseCode checkBiliLogin(String oauthKey, User user) {
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("oauthKey", oauthKey);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<JSONObject> response = restTemplate.exchange("https://passport.bilibili.com/qrcode/getLoginInfo", HttpMethod.POST, requestEntity, JSONObject.class);
        if(!Objects.requireNonNull(response.getBody()).getBool("status")) {
            switch (response.getBody().getInt("data")) {
                case -4:
                    return ResponseCode.BILI_LOGIN_NOT_SCAN;
                case -5:
                    return ResponseCode.BILI_LOGIN_NOT_CONFIRM;
                case -2:
                    return ResponseCode.BILI_LOGIN_URL_EXPIRED;
                default:
                    return ResponseCode.BILI_NOT_LOGIN;
            }
        }

        UserCookie userCookie = userCookieDAO.findByUserAndPlatform(user, Platforms.BILI.getNumVal());
        if(userCookie == null) {
            UserCookie newUserCookie = new UserCookie();
            newUserCookie.setUser(user);
            newUserCookie.setPlatform(Platforms.BILI.getNumVal());
            newUserCookie.setData(Objects.requireNonNull(response.getHeaders().get("Set-Cookie")).toString());
            newUserCookie.setUpdateTime(Instant.now());
            userCookieDAO.save(newUserCookie);
        } else {
            userCookie.setData(Objects.requireNonNull(response.getHeaders().get("Set-Cookie")).toString());
            userCookie.setUpdateTime(Instant.now());
            userCookieDAO.save(userCookie);
        }
        System.out.println(response.getHeaders().get("Set-Cookie"));
        return ResponseCode.SUCCESS;
    }
}
