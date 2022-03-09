package com.jk.player.controller;

import com.jk.player.model.User;
import com.jk.player.response.PlatformFavListResponse;
import com.jk.player.result.BaseResult;
import com.jk.player.result.ResponseCode;
import com.jk.player.service.LoginService;
import com.jk.player.service.NeteaseFavService;
import com.jk.player.service.NeteaseLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class NeteaseFavController {

    @Autowired
    private NeteaseFavService neteaseFavService;

    @Autowired
    private NeteaseLoginService neteaseLoginService;

    @Autowired
    private LoginService loginService;

    @CrossOrigin
    @PostMapping(value = "/api/netease/fav-list")
    @ResponseBody
    public BaseResult<List<PlatformFavListResponse>> biliFavList(@CookieValue(value = "session", defaultValue = "NULL") String session, @CookieValue(value = "username") String username) {
        User user = loginService.getUser(username);
        if(!neteaseLoginService.isLogin(user))
            return new BaseResult<>(ResponseCode.PLATFORM_NOT_LOGIN);

        List<PlatformFavListResponse> list = neteaseFavService.getNeteaseFavList(user);
        if(list == null) new BaseResult<>(ResponseCode.SERVER_ERROR);

        return new BaseResult<>(ResponseCode.SUCCESS, list);
    }
}
