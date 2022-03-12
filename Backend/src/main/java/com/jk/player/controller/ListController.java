package com.jk.player.controller;

import com.jk.player.model.User;
import com.jk.player.response.BaseResult;
import com.jk.player.response.ListResponse;
import com.jk.player.response.ResponseCode;
import com.jk.player.response.SongResponse;
import com.jk.player.service.ListService;
import com.jk.player.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ListController {

    @Autowired
    LoginService loginService;

    @Autowired
    ListService listService;

    @CrossOrigin
    @PostMapping(value = "/api/list/create")
    @ResponseBody
    public BaseResult<String> createList(@CookieValue(value = "session") String session, @CookieValue(value = "username") String username, @RequestParam String name, @RequestParam(required = false) String cover) {
        User user = loginService.verifyLoginUser(session, username);
        if (user == null)
            return new BaseResult<>(ResponseCode.NOT_LOGIN, "Not login");

        String res = listService.createList(name, cover, user);
        return new BaseResult<>(res == null ? ResponseCode.SUCCESS : ResponseCode.LIST_DATABASE_FAIL, res);
    }

    @CrossOrigin
    @PostMapping(value = "/api/list/delete")
    @ResponseBody
    public BaseResult<String> deleteList(@CookieValue(value = "session") String session, @CookieValue(value = "username") String username, @RequestParam Integer id) {
        User user = loginService.verifyLoginUser(session, username);
        if (user == null)
            return new BaseResult<>(ResponseCode.NOT_LOGIN, "Not login");
        if (!listService.verifyUser(user, id))
            return new BaseResult<>(ResponseCode.LIST_NOT_ACCESSIBLE, "Not your list");

        String res = listService.deleteList(id);
        return new BaseResult<>(res == null ? ResponseCode.SUCCESS : ResponseCode.LIST_DATABASE_FAIL, res);
    }

    @CrossOrigin
    @PostMapping(value = "/api/list/user-lists")
    @ResponseBody
    public BaseResult<List<ListResponse>> getAllLists(@CookieValue(value = "session") String session, @CookieValue(value = "username") String username) {
        User user = loginService.verifyLoginUser(session, username);
        if (user == null)
            return new BaseResult<>(ResponseCode.NOT_LOGIN);

        List<ListResponse> lists = listService.getListByUser(user);
        return new BaseResult<>(ResponseCode.SUCCESS, lists);
    }

    @CrossOrigin
    @PostMapping(value = "/api/list/get-songs")
    @ResponseBody
    public BaseResult<List<SongResponse>> getSongsFromList(@CookieValue(value = "session") String session, @CookieValue(value = "username") String username, @RequestParam Integer id) {
        User user = loginService.verifyLoginUser(session, username);
        if (user == null)
            return new BaseResult<>(ResponseCode.NOT_LOGIN);
        if (!listService.verifyUser(user, id))
            return new BaseResult<>(ResponseCode.LIST_NOT_ACCESSIBLE);

        List<SongResponse> songs = listService.getSongsInList(id);
        return new BaseResult<>(songs == null ? ResponseCode.LIST_DATABASE_FAIL : ResponseCode.SUCCESS, songs);
    }

    @CrossOrigin
    @PostMapping(value = "/api/list/add-songs")
    @ResponseBody
    public BaseResult<String> addSongs(@CookieValue(value = "session") String session, @CookieValue(value = "username") String username, @RequestParam Integer id, @RequestParam List<Long> songIds) {
        User user = loginService.verifyLoginUser(session, username);
        if (user == null)
            return new BaseResult<>(ResponseCode.NOT_LOGIN, "Not login");
        if (!listService.verifyUser(user, id))
            return new BaseResult<>(ResponseCode.LIST_NOT_ACCESSIBLE, "Not your list");

        String res = listService.addSongToList(id, songIds);
        return new BaseResult<>(res == null ? ResponseCode.SUCCESS : ResponseCode.LIST_DATABASE_FAIL, res);
    }

    @CrossOrigin
    @PostMapping(value = "/api/list/delete-songs")
    @ResponseBody
    public BaseResult<String> deleteSongs(@CookieValue(value = "session") String session, @CookieValue(value = "username") String username, @RequestParam Integer id, @RequestParam List<Long> songIds) {
        User user = loginService.verifyLoginUser(session, username);
        if (user == null)
            return new BaseResult<>(ResponseCode.NOT_LOGIN, "Not login");
        if (!listService.verifyUser(user, id))
            return new BaseResult<>(ResponseCode.LIST_NOT_ACCESSIBLE, "Not your list");

        String res = listService.deleteSongFromList(id, songIds);
        return new BaseResult<>(res == null ? ResponseCode.SUCCESS : ResponseCode.LIST_DATABASE_FAIL, res);
    }
}
