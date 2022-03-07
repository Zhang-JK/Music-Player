package com.jk.player.controller;

import cn.hutool.json.JSONObject;
import com.jk.player.response.BiliFavListResponse;
import com.jk.player.response.BiliLoginResponse;
import com.jk.player.result.BaseResult;
import com.jk.player.result.ResponseCode;
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
    LoginService loginService;

    @CrossOrigin
    @PostMapping(value = "/api/bili/fav-list")
    @ResponseBody
    public BaseResult<List<BiliFavListResponse>> biliFavList(@CookieValue(value = "session", defaultValue = "NULL") String session, @CookieValue(value = "username") String username) {
        if(!biliLoginService.isLogin(loginService.getUser(username)))
            return new BaseResult<>(ResponseCode.BILI_NOT_LOGIN);

        return new BaseResult<>(ResponseCode.SUCCESS, null);
    }
}
