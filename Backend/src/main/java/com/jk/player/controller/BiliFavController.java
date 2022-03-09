package com.jk.player.controller;

import com.jk.player.model.User;
import com.jk.player.response.BiliFavListResponse;
import com.jk.player.result.BaseResult;
import com.jk.player.result.ResponseCode;
import com.jk.player.service.BiliFavService;
import com.jk.player.service.BiliLoginService;
import com.jk.player.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public BaseResult<List<BiliFavListResponse>> biliFavList(@CookieValue(value = "session", defaultValue = "NULL") String session, @CookieValue(value = "username") String username) {
        User user = loginService.getUser(username);
        if(!biliLoginService.isLogin(user))
            return new BaseResult<>(ResponseCode.PLATFORM_NOT_LOGIN);

        List<BiliFavListResponse> list = biliFavService.getBiliFavList(user);
        if(list == null) new BaseResult<>(ResponseCode.SERVER_ERROR);

        return new BaseResult<>(ResponseCode.SUCCESS, list);
    }
}
