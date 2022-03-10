package com.jk.player.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.jk.player.model.User;
import com.jk.player.response.PlatformFavListResponse;
import com.jk.player.response.PlatformListDetailResponse;
import com.jk.player.response.PlayerLinkResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// Todo: merge all platform service into interface
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
        if (Objects.requireNonNull(response.getBody()).isNull("playlist"))
            return null;

        response.getBody().getJSONArray("playlist").replaceAll(item -> {
            JSONObject obj = (JSONObject) item;
            obj.append("title", obj.getStr("name"));
            obj.append("mediaCount", obj.getStr("trackCount"));
            return obj;
        });
        return response.getBody().getJSONArray("playlist").toList(PlatformFavListResponse.class);
    }

    public JSONArray getNeteaseListDetailRequest(User user, Integer listId, Integer limit, Integer offset) {
        Map<String, Object> param = new HashMap<>();
        String paramList = "?timestamp={t}&id={id}";
        param.put("t", Instant.now().getEpochSecond());
        param.put("id", listId);
        if (limit != null) {
            param.put("limit", limit);
            paramList += "&limit={limit}";
        }
        if (offset != null) {
            param.put("offset", offset);
            paramList += "&offset={offset}";
        }
        ResponseEntity<JSONObject> response = neteaseLoginService.neteaseRequestWithCookie("http://localhost:3000/playlist/track/all", paramList, param, user);
        if (Objects.requireNonNull(response.getBody()).isNull("songs"))
            return null;

        return response.getBody().getJSONArray("songs");
    }

    public JSONArray getNeteaseSongList(List<BigInteger> id, User user) {
        String ids = id.toString().replace("[", "").replace("]", "");
        Map<String, Object> param = new HashMap<>();
        param.put("ids", ids);
        ResponseEntity<JSONObject> response = neteaseLoginService.neteaseRequestWithCookie("http://localhost:3000/song/detail", "?ids={ids}", param, user);

        if (Objects.requireNonNull(response.getBody()).isNull("songs"))
            return null;

        return response.getBody().getJSONArray("songs");
    }

    public List<PlatformListDetailResponse> getNeteaseListDetail(User user, Integer listId, Integer limit, Integer offset) {
        JSONArray songArray = getNeteaseListDetailRequest(user, listId, limit, offset);
        if (songArray == null)
            return null;

        songArray.replaceAll(item -> {
            JSONObject obj = (JSONObject) item;
            obj.set("creator", obj.getJSONArray("ar").getJSONObject(0).getStr("name"));
            obj.set("coverUrl", obj.getJSONObject("al").getStr("picUrl"));
            return obj;
        });
        return songArray.toList(PlatformListDetailResponse.class);
    }

    public PlayerLinkResponse getNeteaseSongLink(User user, BigInteger id) {
        Map<String, Object> param = new HashMap<>();
        String paramList = "?timestamp={t}&id={id}";
        param.put("t", Instant.now().getEpochSecond());
        param.put("id", id);
        ResponseEntity<JSONObject> response = neteaseLoginService.neteaseRequestWithCookie("http://localhost:3000/song/url", paramList, param, user);
        if (Objects.requireNonNull(response.getBody()).isNull("data"))
            return null;

        JSONObject obj = response.getBody().getJSONArray("data").getJSONObject(0);
        PlayerLinkResponse res = new PlayerLinkResponse();

        if(obj.isNull("url")) {
            res.setStatus(2);
            res.setMessage("Failed");
            return res;
        }
        res.setLink(obj.getStr("url"));
        res.setType(obj.getStr("type"));
        res.setStatus(0);
        return res;
    }
}
