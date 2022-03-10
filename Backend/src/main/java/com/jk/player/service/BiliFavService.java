package com.jk.player.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.jk.player.dao.UserCookieDAO;
import com.jk.player.model.Song;
import com.jk.player.model.User;
import com.jk.player.response.PlatformFavListResponse;
import com.jk.player.response.PlatformListDetailResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

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

    public JSONArray getBiliListDetailRequest(User user, Integer listId, Integer ps, Integer pn) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("media_id", listId.toString()));
        params.add(new BasicNameValuePair("ps", ps.toString()));
        if (pn != null) params.add(new BasicNameValuePair("pn", pn.toString()));

        String url = "http://api.bilibili.com/x/v3/fav/resource/list?" + URLEncodedUtils.format(params, "UTF-8");
        ResponseEntity<JSONObject> response = biliLoginService.biliRequestWithCookie(url, HttpMethod.GET, null, user);
        if (Objects.requireNonNull(response.getBody()).isNull("data"))
            return null;

        return response.getBody().getJSONObject("data").getJSONArray("medias");
    }

    public JSONObject getBiliListDetailRequest(User user, BigInteger aid) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("aid", aid.toString()));

        String url = "http://api.bilibili.com/x/web-interface/view?" + URLEncodedUtils.format(params, "UTF-8");
        ResponseEntity<JSONObject> response = biliLoginService.biliRequestWithCookie(url, HttpMethod.GET, null, user);
        if (Objects.requireNonNull(response.getBody()).isNull("data"))
            return null;

        return response.getBody().getJSONObject("data");
    }

    public List<PlatformListDetailResponse> getBiliListDetail(User user, Integer listId, Integer ps, Integer pn) {
        JSONArray mediaArray = getBiliListDetailRequest(user, listId, ps, pn);

        if (mediaArray == null)
            return null;

        mediaArray.replaceAll(item -> {
            JSONObject obj = (JSONObject) item;
            obj.set("name", obj.getStr("title"));
            obj.set("creator", obj.getJSONObject("upper").getStr("name"));
            obj.set("coverUrl", obj.getStr("cover"));
            return obj;
        });
        return mediaArray.toList(PlatformListDetailResponse.class);
    }

    public JSONArray getBiliSongList(List<BigInteger> id, User user) {
        JSONArray songArray = new JSONArray();
        for (BigInteger i : id) {
            JSONObject obj = getBiliListDetailRequest(user, i);
            if (obj == null) continue;
//                obj = new JSONObject().set("aid", i);
            songArray.add(obj);
        }
        return songArray;
    }
}
