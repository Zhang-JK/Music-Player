package com.jk.player.service;

import cn.hutool.json.JSONObject;
import com.jk.player.dao.UserCookieDAO;
import com.jk.player.model.Song;
import com.jk.player.model.User;
import com.jk.player.response.PlatformFavListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class BiliFavService {

    @Autowired
    UserCookieDAO userCookieDAO;

    @Autowired
    BiliLoginService biliLoginService;

    public List<PlatformFavListResponse> getBiliFavList(User user) {
        ResponseEntity<JSONObject> info = biliLoginService.biliRequestWithCookie("http://api.bilibili.com/x/web-interface/nav", HttpMethod.GET, null, user);
        Integer mid = Objects.requireNonNull(info.getBody()).getJSONObject("data").getInt("mid");

        ResponseEntity<JSONObject> response = biliLoginService.biliRequestWithCookie("http://api.bilibili.com/x/v3/fav/folder/created/list-all"+"?up_mid="+mid.toString(), HttpMethod.GET, null, user);
        if(Objects.requireNonNull(response.getBody()).isNull("data"))
            return null;

        return response.getBody().getJSONObject("data").getJSONArray("list").toList(PlatformFavListResponse.class);
    }

    public List<Song> getBiliSongList(List<Integer> id, User user) {
        return null;
    }
}
