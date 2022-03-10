package com.jk.player.controller;

import com.jk.player.model.Song;
import com.jk.player.model.User;
import com.jk.player.response.PlatformFavListResponse;
import com.jk.player.response.SongImportResponse;
import com.jk.player.result.BaseResult;
import com.jk.player.result.ResponseCode;
import com.jk.player.service.*;
import com.jk.player.utils.Platforms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        User user = loginService.getUser(username);
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
        User user = loginService.getUser(username);
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
}
