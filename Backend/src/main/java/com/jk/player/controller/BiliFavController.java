package com.jk.player.controller;

import com.jk.player.model.User;
import com.jk.player.response.BaseResult;
import com.jk.player.response.PlatformFavListResponse;
import com.jk.player.response.PlatformListDetailResponse;
import com.jk.player.response.ResponseCode;
import com.jk.player.service.BiliFavService;
import com.jk.player.service.BiliLoginService;
import com.jk.player.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BiliFavController {

    @Autowired
    BiliLoginService biliLoginService;

    @Autowired
    BiliFavService biliFavService;

    @Autowired
    LoginService loginService;

    @CrossOrigin
    @PostMapping(value = "/api/bili/fav-list")
    @ResponseBody
    public BaseResult<List<PlatformFavListResponse>> biliFavList(@CookieValue(value = "session") String session, @CookieValue(value = "username") String username) {
        User user = loginService.verifyLoginUser(session, username);
        if (user == null) return new BaseResult<>(ResponseCode.NOT_LOGIN);
        if (!biliLoginService.isLogin(user))
            return new BaseResult<>(ResponseCode.PLATFORM_NOT_LOGIN);

        List<PlatformFavListResponse> list = biliFavService.getBiliFavList(user);
        if (list == null) new BaseResult<>(ResponseCode.SERVER_ERROR);

        return new BaseResult<>(ResponseCode.SUCCESS, list);
    }

    @CrossOrigin
    @PostMapping(value = "/api/bili/list-detail")
    @ResponseBody
    public BaseResult<List<PlatformListDetailResponse>> biliListDetail(@CookieValue(value = "session") String session, @CookieValue(value = "username") String username, @RequestParam Integer id, @RequestParam Integer limit, @RequestParam(required = false) Integer offset) {
        User user = loginService.verifyLoginUser(session, username);
        if (user == null) return new BaseResult<>(ResponseCode.NOT_LOGIN);
        if (limit > 20) {
            return new BaseResult<>(ResponseCode.LIMIT_EXCEED_MAX);
        }

        if (!biliLoginService.isLogin(user)) return new BaseResult<>(ResponseCode.PLATFORM_NOT_LOGIN);

        List<PlatformListDetailResponse> list = biliFavService.getBiliListDetail(user, id, limit, offset);
        if (list == null)
            return new BaseResult<>(ResponseCode.SERVER_ERROR);

        return new BaseResult<>(ResponseCode.SUCCESS, list);
    }
}
