package com.jk.player.controller;

import cn.hutool.json.JSONObject;
import com.jk.player.model.User;
import com.jk.player.response.PlatformLoginResponse;
import com.jk.player.response.BaseResult;
import com.jk.player.response.ResponseCode;
import com.jk.player.service.BiliLoginService;
import com.jk.player.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class BiliLoginController {

    @Autowired
    private BiliLoginService biliLoginService;

    @Autowired
    private LoginService loginService;

    @CrossOrigin
    @PostMapping(value = "/api/bili/qrcode")
    @ResponseBody
    public BaseResult<PlatformLoginResponse> loginQrcode(@CookieValue(value = "session") String session, @CookieValue(value = "username") String username) {
        User user = loginService.verifyLoginUser(session, username);
        if(user == null) return new BaseResult<>(ResponseCode.NOT_LOGIN);
        JSONObject data = biliLoginService.getBiliLoginUrl();
        PlatformLoginResponse response = new PlatformLoginResponse();
        response.setUrl(data.getJSONObject("data").getStr("url"));
        response.setOauthKey(data.getJSONObject("data").getStr("oauthKey"));
        return new BaseResult<>(ResponseCode.SUCCESS, response);
    }

    @CrossOrigin
    @PostMapping(value = "/api/bili/qr-check")
    @ResponseBody
    public BaseResult<String> loginQrcodeCheck(@CookieValue(value = "session") String session, @CookieValue(value = "username") String username, @RequestParam(value = "oauthKey") String oauthKey) {
        User user = loginService.verifyLoginUser(session, username);
        if(user == null) return new BaseResult<>(ResponseCode.NOT_LOGIN);
        return new BaseResult<>(biliLoginService.checkBiliLogin(oauthKey, loginService.getUser(username)));
    }

    @CrossOrigin
    @PostMapping(value = "/api/bili/login-check")
    @ResponseBody
    public BaseResult<String> loginCheck(@CookieValue(value = "session") String session, @CookieValue(value = "username") String username) {
        User user = loginService.verifyLoginUser(session, username);
        if(user == null) return new BaseResult<>(ResponseCode.NOT_LOGIN);
        return new BaseResult<>(biliLoginService.isLogin(loginService.getUser(username))?ResponseCode.SUCCESS:ResponseCode.PLATFORM_NOT_LOGIN);
    }
}
