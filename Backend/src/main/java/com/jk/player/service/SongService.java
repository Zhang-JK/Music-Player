package com.jk.player.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.jk.player.dao.SongDAO;
import com.jk.player.model.Song;
import com.jk.player.model.User;
import com.jk.player.response.BaseResult;
import com.jk.player.response.SongImportResponse;
import com.jk.player.utils.Platforms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jk.player.response.ResponseCode.*;

@Service
public class SongService {

    @Autowired
    private SongDAO songDAO;

    @Autowired
    private NeteaseFavService neteaseFavService;

    @Autowired
    private BiliFavService biliFavService;

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

    // Todo: increase database efficiency
    public BaseResult<SongImportResponse> importSongList(JSONArray songArray, User user, Platforms platform) {
        switch (platform) {
            case NETEASE:
                songArray.replaceAll(item -> {
                    JSONObject obj = (JSONObject) item;
                    obj.set("serial", obj.getInt("id"));
                    obj.set("platform", platform.getNumVal());
                    obj.set("artist", obj.getJSONArray("ar").getJSONObject(0).getStr("name"));
                    obj.set("avatar", obj.getJSONObject("al").getStr("picUrl"));
                    obj.remove("id");
                    return obj;
                });
                break;
            case BILI:
                songArray.replaceAll(item -> {
                    JSONObject obj = (JSONObject) item;
                    try {
                        if (obj.getInt("aid") != null) obj.set("serial", obj.getInt("aid"));
                        else obj.set("serial", obj.getInt("id"));
                        obj.set("platform", platform.getNumVal());
                        obj.set("name", obj.getStr("title"));
                        if (obj.getJSONObject("owner") != null)
                            obj.set("artist", obj.getJSONObject("owner").getStr("name"));
                        else obj.set("artist", obj.getJSONObject("upper").getStr("name"));
                        if (obj.getStr("pic") != null) obj.set("avatar", obj.getStr("pic"));
                        else obj.set("avatar", obj.getStr("cover"));
                        obj.remove("id");
                    } catch (Exception e) {
                        if (obj.getInt("aid") != null) obj.set("serial", obj.getInt("aid"));
                        else obj.set("serial", obj.getInt("id"));
                    }
                    return obj;
                });
                break;
        }
        List<Song> songs = songArray.toList(Song.class);

        SongImportResponse response = new SongImportResponse();
        response.setTotalCount(songs.size());
        List<Object> successList = new ArrayList<>();
        Map<Object, String> failList = new HashMap<>();
        for (Song song : songs) {
            if (song.getName() == null || song.getArtist() == null || song.getAvatar() == null) {
                failList.put(song.getSerial(), "Song do NOT exist");
                continue;
            }

            if (song.getName().equals("已失效视频")) {
                failList.put(song.getSerial(), "Song is deleted");
                continue;
            }

            String msg = importSong(song);
            if (msg == null) successList.add(song.getSerial());
            else failList.put(song.getSerial(), msg);
        }
        response.setSuccessList(successList);
        response.setFailList(failList);

        return new BaseResult<>(SUCCESS, response);
    }

    public BaseResult<SongImportResponse> importBiliSongList(Integer listId, User user) {
        JSONArray songArray = new JSONArray();
        int pages = 0;
        while (true) {
            // page start from 1
            JSONArray temp = biliFavService.getBiliListDetailRequest(user, listId, 20, ++pages);
            if (temp != null) songArray.addAll(temp);
            if (temp == null || temp.size() < 20) break;
        }

        return importSongList(songArray, user, Platforms.BILI);
    }

    public BaseResult<SongImportResponse> importNeteaseSongList(Integer listId, User user) {
        JSONArray songArray = neteaseFavService.getNeteaseListDetailRequest(user, listId, null, null);
        if (songArray == null) {
            return new BaseResult<>(PLATFORM_NOT_LOGIN, null);
        }

        return importSongList(songArray, user, Platforms.NETEASE);
    }

    public BaseResult<SongImportResponse> importBiliSongListById(List<BigInteger> ids, User user) {
        JSONArray songArray = biliFavService.getBiliSongList(ids, user);
        if (songArray == null) {
            return new BaseResult<>(IMPORT_ERROR, null);
        }

        return importSongList(songArray, user, Platforms.BILI);
    }

    public BaseResult<SongImportResponse> importNeteaseSongListById(List<BigInteger> ids, User user) {
        JSONArray songArray = neteaseFavService.getNeteaseSongList(ids, user);
        if (songArray == null) {
            return new BaseResult<>(PLATFORM_NOT_LOGIN, null);
        }

        return importSongList(songArray, user, Platforms.NETEASE);
    }

    public List<Song> searchSong(Integer platform, String serial, String name, String artist) {
        return songDAO.searchForSongs(platform, serial, name, artist);
    }
}
