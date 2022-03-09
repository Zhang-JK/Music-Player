package com.jk.player.service;

import cn.hutool.json.JSONObject;
import com.jk.player.model.User;
import com.jk.player.response.PlatformFavListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class NeteaseFavService {

    @Autowired
    private NeteaseLoginService neteaseLoginService;

    public List<PlatformFavListResponse> getNeteaseFavList(User user) {
        Integer userId = neteaseLoginService.getUserId(user);

        Map<String, Object> param = new HashMap<>();
        param.put("u", userId);
        param.put("t", Instant.now().getEpochSecond());
        ResponseEntity<JSONObject> response = neteaseLoginService.neteaseRequestWithCookie("http://localhost:3000/user/playlist", "?timestamp={t}&uid={u}", param, user);
        if(Objects.requireNonNull(response.getBody()).isNull("playlist"))
            return null;

        response.getBody().getJSONArray("playlist").replaceAll(item -> {
            JSONObject obj = (JSONObject) item;
            obj.append("title", obj.getStr("name"));
            obj.append("mediaCount", obj.getStr("trackCount"));
            return obj;
        });
        return response.getBody().getJSONArray("playlist").toList(PlatformFavListResponse.class);
    }
}
