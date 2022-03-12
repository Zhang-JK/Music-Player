package com.jk.player.controller;

import cn.hutool.json.JSONObject;
import com.jk.player.model.User;
import com.jk.player.response.PlatformLoginResponse;
import com.jk.player.result.BaseResult;
import com.jk.player.result.ResponseCode;
import com.jk.player.service.LoginService;
import com.jk.player.service.NeteaseLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class NeteaseLoginController {

    @Autowired
    private NeteaseLoginService neteaseLoginService;

    @Autowired
    private LoginService loginService;

    @CrossOrigin
    @PostMapping(value = "/api/netease/qrcode")
    @ResponseBody
    public BaseResult<PlatformLoginResponse> loginQrcode(@CookieValue(value = "session") String session, @CookieValue(value = "username") String username) {
        User user = loginService.verifyLoginUser(session, username);
        if(user == null) return new BaseResult<>(ResponseCode.NOT_LOGIN);
        JSONObject data = neteaseLoginService.getNeteaseLoginUrl();
        PlatformLoginResponse response = new PlatformLoginResponse();
        response.setUrl("https://music.163.com/login?codekey="+data.getJSONObject("data").getStr("unikey"));
        response.setOauthKey(data.getJSONObject("data").getStr("unikey"));
        return new BaseResult<>(ResponseCode.SUCCESS, response);
    }

    @CrossOrigin
    @PostMapping(value = "/api/netease/qr-check")
    @ResponseBody
    public BaseResult<String> loginQrcodeCheck(@CookieValue(value = "session") String session, @CookieValue(value = "username") String username, @RequestParam(value = "oauthKey") String oauthKey) {
        User user = loginService.verifyLoginUser(session, username);
        if(user == null) return new BaseResult<>(ResponseCode.NOT_LOGIN);
        return new BaseResult<>(neteaseLoginService.checkNeteaseLogin(oauthKey, loginService.getUser(username)));
    }

    @CrossOrigin
    @PostMapping(value = "/api/netease/login-check")
    @ResponseBody
    public BaseResult<String> loginCheck(@CookieValue(value = "session") String session, @CookieValue(value = "username") String username) {
        User user = loginService.verifyLoginUser(session, username);
        if(user == null) return new BaseResult<>(ResponseCode.NOT_LOGIN);
        return new BaseResult<>(neteaseLoginService.isLogin(loginService.getUser(username))?ResponseCode.SUCCESS:ResponseCode.PLATFORM_NOT_LOGIN);
    }
}
