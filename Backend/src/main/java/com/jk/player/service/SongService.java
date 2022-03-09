package com.jk.player.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.jk.player.dao.SongDAO;
import com.jk.player.model.Song;
import com.jk.player.model.User;
import com.jk.player.response.SongImportResponse;
import com.jk.player.result.BaseResult;
import com.jk.player.utils.Platforms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jk.player.result.ResponseCode.*;

@Service
public class SongService {

    @Autowired
    private SongDAO songDAO;

    @Autowired
    private NeteaseFavService neteaseFavService;

    public String importSong(Song song) {
        if (songDAO.findByPlatformAndSerial(song.getPlatform(), song.getSerial()) == null) {
            try {
                songDAO.save(song);
            } catch (Exception e) {
                return e.getMessage();
            }
            return null;
        }
        return "Song already exists";
    }

    public BaseResult<SongImportResponse> importSongList(JSONArray songArray, User user, Platforms platform) {
        switch (platform) {
            case NETEASE:
                songArray.replaceAll(item -> {
                    JSONObject obj = (JSONObject) item;
                    obj.put("serial", obj.getInt("id"));
                    obj.put("platform", platform.getNumVal());
                    obj.put("artist", obj.getJSONArray("ar").getJSONObject(0).getStr("name"));
                    obj.put("avatar", obj.getJSONObject("al").getStr("picUrl"));
                    obj.remove("id");
                    return obj;
                });
                break;
            case BILI:
                break;
        }
        List<Song> songs = songArray.toList(Song.class);

        SongImportResponse response = new SongImportResponse();
        response.setTotalCount(songs.size());
        List<Object> successList = new ArrayList<>();
        Map<Object, String> failList = new HashMap<>();
        for (Song song : songs) {
            String msg = importSong(song);
            if(msg == null)
                successList.add(song.getSerial());
            else
                failList.put(song.getSerial(), msg);
        }
        response.setSuccessList(successList);
        response.setFailList(failList);

        return new BaseResult<>(SUCCESS, response);
    }

    public BaseResult<SongImportResponse> importBiliSongList(Integer listId, User user) {
        return new BaseResult<>(IMPORT_ERROR, null);
    }

    public BaseResult<SongImportResponse> importNeteaseSongList(Integer listId, User user) {
        JSONArray songArray = neteaseFavService.getNeteaseListDetailRequest(user, listId, null, null);
        if (songArray == null) {
            return new BaseResult<>(PLATFORM_NOT_LOGIN, null);
        }

        return importSongList(songArray, user, Platforms.NETEASE);
    }

    public BaseResult<SongImportResponse> importBiliSongListById(List<Integer> ids, User user) {
        return new BaseResult<>(IMPORT_ERROR, null);
    }

    public BaseResult<SongImportResponse> importNeteaseSongListById(List<Integer> ids, User user) {
        JSONArray songArray = neteaseFavService.getNeteaseSongList(ids, user);
        if (songArray == null) {
            return new BaseResult<>(PLATFORM_NOT_LOGIN, null);
        }

        return importSongList(songArray, user, Platforms.NETEASE);
    }
}
