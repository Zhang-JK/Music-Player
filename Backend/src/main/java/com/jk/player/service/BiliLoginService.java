package com.jk.player.service;

import cn.hutool.json.JSONObject;
import com.jk.player.dao.UserCookieDAO;
import com.jk.player.model.User;
import com.jk.player.model.UserCookie;
import com.jk.player.result.ResponseCode;
import com.jk.player.utils.CookieHandler;
import com.jk.player.utils.Platforms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
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
        MultiValueMap<String, Object> request = new LinkedMultiValueMap<>();
        request.add("oauthKey", oauthKey);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<JSONObject> response = restTemplate.exchange("https://passport.bilibili.com/qrcode/getLoginInfo", HttpMethod.POST, requestEntity, JSONObject.class);
        if(!Objects.requireNonNull(response.getBody()).getBool("status")) {
            switch (response.getBody().getInt("data")) {
                case -4:
                    return ResponseCode.PLATFORM_LOGIN_NOT_SCAN;
                case -5:
                    return ResponseCode.PLATFORM_LOGIN_NOT_CONFIRM;
                case -2:
                    return ResponseCode.PLATFORM_LOGIN_URL_EXPIRED;
                default:
                    return ResponseCode.PLATFORM_NOT_LOGIN;
            }
        }

        UserCookie userCookie = userCookieDAO.findByUserAndPlatform(user, Platforms.BILI.getNumVal());
        List<String> cookies = response.getHeaders().get("Set-Cookie");

        userCookieDAO.save(Objects.requireNonNull(CookieHandler.setCookie(cookies, userCookie, user, Platforms.BILI)));

        return ResponseCode.SUCCESS;
    }

    public boolean isLogin(User user) {
        ResponseEntity<JSONObject> response = biliRequestWithCookie("https://api.bilibili.com/x/web-interface/nav", HttpMethod.GET, null, user);
        return !Objects.requireNonNull(response.getBody()).getStr("code").equals("-101");
    }

    public ResponseEntity<JSONObject> biliRequestWithCookie(String url, HttpMethod method, LinkedMultiValueMap<String, Object> request, User user) {
        UserCookie userCookie = userCookieDAO.findByUserAndPlatform(user, Platforms.BILI.getNumVal());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", userCookie.getData());
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(request, headers);

        return restTemplate.exchange(url, method, requestEntity, JSONObject.class);
    }
}
