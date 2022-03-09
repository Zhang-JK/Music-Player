package com.jk.player.service;

import cn.hutool.json.JSONObject;
import com.jk.player.dao.UserCookieDAO;
import com.jk.player.model.User;
import com.jk.player.model.UserCookie;
import com.jk.player.result.ResponseCode;
import com.jk.player.utils.Platforms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class NeteaseLoginService {
    @Autowired
    UserCookieDAO userCookieDAO;

    RestTemplate restTemplate = new RestTemplate();

    public JSONObject getNeteaseLoginUrl() {
        Instant instant = Instant.now();
        return restTemplate.getForObject("http://localhost:3000/login/qr/key?timestamp="+instant.toString(), JSONObject.class);
    }

    public ResponseCode checkNeteaseLogin(String oauthKey, User user) {
        Map<String, Object> param = new HashMap<>();
        param.put("oauthKey", oauthKey);
        param.put("timestamp", Instant.now().getEpochSecond());
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<JSONObject> response = restTemplate.exchange("http://localhost:3000/login/qr/check" + "?key={oauthKey}&timestamp={timestamp}", HttpMethod.GET, requestEntity, JSONObject.class, param);
        switch (Objects.requireNonNull(response.getBody()).getInt("code")) {
            case 800:
                return ResponseCode.PLATFORM_LOGIN_URL_EXPIRED;
            case 801:
                return ResponseCode.PLATFORM_LOGIN_NOT_SCAN;
            case 802:
                return ResponseCode.PLATFORM_LOGIN_NOT_CONFIRM;
            case 803:
                break;
            default:
                return ResponseCode.PLATFORM_NOT_LOGIN;
        }

        UserCookie userCookie = userCookieDAO.findByUserAndPlatform(user, Platforms.NETEASE.getNumVal());
        String cookies = response.getBody().getStr("cookie");

        if(userCookie == null) {
            UserCookie newUserCookie = new UserCookie();
            newUserCookie.setUser(user);
            newUserCookie.setPlatform(Platforms.NETEASE.getNumVal());
            newUserCookie.setData(cookies);
            newUserCookie.setUpdateTime(Instant.now());
            userCookieDAO.save(newUserCookie);
        } else {
            userCookie.setData(cookies);
            userCookie.setUpdateTime(Instant.now());
            userCookieDAO.save(userCookie);
        }

        return ResponseCode.SUCCESS;
    }

    public boolean isLogin(User user) {
        Map<String, Object> param = new HashMap<>();
        param.put("timestamp", Instant.now().getEpochSecond());
        ResponseEntity<JSONObject> response = neteaseRequestWithCookie("http://localhost:3000/user/account", "?timestamp={timestamp}", param, user);
        return !Objects.requireNonNull(response.getBody()).isNull("account");
    }

    public ResponseEntity<JSONObject> neteaseRequestWithCookie(String url, String paramList, Map<String, Object> param, User user) {
        UserCookie userCookie = userCookieDAO.findByUserAndPlatform(user, Platforms.NETEASE.getNumVal());
        HttpHeaders headers = new HttpHeaders();
        headers.add("cookie", userCookie.getData());
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("cookie", userCookie.getData());
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        if(param != null)
            return restTemplate.exchange(url+paramList, HttpMethod.POST, requestEntity, JSONObject.class, param);
        else
            return restTemplate.exchange(url, HttpMethod.POST, requestEntity, JSONObject.class);
    }

    public Integer getUserId(User user) {
        ResponseEntity<JSONObject> response = neteaseRequestWithCookie("http://localhost:3000/user/account", "", null, user);
        return Objects.requireNonNull(response.getBody()).getJSONObject("account").getInt("id");
    }
}
