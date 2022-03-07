package com.jk.player.controller;

import cn.hutool.json.JSONObject;
import com.jk.player.response.BiliLoginResponse;
import com.jk.player.result.BaseResult;
import com.jk.player.result.ResponseCode;
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
    public BaseResult<BiliLoginResponse> loginQrcode(@CookieValue(value = "session", defaultValue = "NULL") String session) {
        if (session.equals("NULL"))
            return new BaseResult<>(ResponseCode.NOT_LOGIN);
        JSONObject data = biliLoginService.getBiliLoginUrl();
        BiliLoginResponse response = new BiliLoginResponse();
        response.setUrl(data.getJSONObject("data").getStr("url"));
        response.setOauthKey(data.getJSONObject("data").getStr("oauthKey"));
        return new BaseResult<>(ResponseCode.SUCCESS, response);
    }

    @CrossOrigin
    @PostMapping(value = "/api/bili/qr-check")
    @ResponseBody
    public BaseResult<String> loginQrcodecheck(@CookieValue(value = "session") String session, @CookieValue(value = "username") String username, @RequestParam(value = "oauthKey") String oauthKey) {
        if (session.isEmpty() || username.isEmpty())
            return new BaseResult<>(ResponseCode.NOT_LOGIN);
        return new BaseResult<>(biliLoginService.checkBiliLogin(oauthKey, loginService.getUser(username)));
    }
}
