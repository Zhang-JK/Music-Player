package com.jk.player.controller;

import com.jk.player.model.Song;
import com.jk.player.model.User;
import com.jk.player.response.SongImportResponse;
import com.jk.player.response.BaseResult;
import com.jk.player.response.ResponseCode;
import com.jk.player.response.SongResponse;
import com.jk.player.service.LoginService;
import com.jk.player.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SongController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private SongService songService;

    @CrossOrigin
    @PostMapping(value = "/api/song/import")
    @ResponseBody
    public BaseResult<SongImportResponse> importSong(@CookieValue(value = "session") String session, @CookieValue(value = "username") String username, @RequestParam Integer platform, @RequestParam List<BigInteger> id) {
        User user = loginService.verifyLoginUser(session, username);
        if (user == null) return new BaseResult<>(ResponseCode.NOT_LOGIN);
        switch (platform) {
            // bili
            case 1:
                return songService.importBiliSongListById(id, user);
            // netease
            case 2:
                return songService.importNeteaseSongListById(id, user);
            default:
                return new BaseResult<>(ResponseCode.BAD_REQUEST, null);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/api/song/import-list")
    @ResponseBody
    public BaseResult<SongImportResponse> importSongList(@CookieValue(value = "session") String session, @CookieValue(value = "username") String username, @RequestParam Integer platform, @RequestParam Integer listId) {
        User user = loginService.verifyLoginUser(session, username);
        if (user == null) return new BaseResult<>(ResponseCode.NOT_LOGIN);
        switch (platform) {
            // bili
            case 1:
                return songService.importBiliSongList(listId, user);
            // netease
            case 2:
                return songService.importNeteaseSongList(listId, user);
            default:
                return new BaseResult<>(ResponseCode.BAD_REQUEST, null);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/api/song/search")
    @ResponseBody
    public BaseResult<List<SongResponse>> importSongList(@CookieValue(value = "session") String session,
                                                         @CookieValue(value = "username") String username,
                                                         @RequestParam(required = false) Integer platform,
                                                         @RequestParam(required = false) String serial,
                                                         @RequestParam(required = false) String name,
                                                         @RequestParam(required = false) String artist) {
        User user = loginService.verifyLoginUser(session, username);
        if (user == null) return new BaseResult<>(ResponseCode.NOT_LOGIN);

        List<Song> songList = songService.searchSong(platform, serial, name, artist);
        return new BaseResult<>(ResponseCode.SUCCESS, songList.stream().map(SongResponse::new).collect(Collectors.toList()));
    }
}
