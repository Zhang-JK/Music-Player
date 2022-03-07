package com.jk.player.controller;

import cn.hutool.crypto.SecureUtil;
import com.jk.player.model.User;
import com.jk.player.result.ResponseCode;
import com.jk.player.result.BaseResult;
import com.jk.player.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @CrossOrigin
    @PostMapping(value = "/api/login")
    @ResponseBody
    public BaseResult<String> login(@RequestBody User requestUser, HttpServletResponse response) {
        User user = loginService.getUser(requestUser.getUsername());
        if (user == null) return new BaseResult<>(ResponseCode.LOGIN_USER_NOT_EXIST);

        // database password: md5(md5(raw) + salt)
        // request password: md5(raw)
        if (!user.getPassword().equalsIgnoreCase(SecureUtil.md5(requestUser.getPassword() + user.getSalt())))
            return new BaseResult<>(ResponseCode.LOGIN_WRONG_PASSWORD);

        String session = loginService.generateSession(user.getId());
        Cookie sessionCookie = new Cookie("session", session);
        sessionCookie.setMaxAge(60 * 60 * 24 * 30);
        sessionCookie.setPath("/");
        Cookie usernameCookie = new Cookie("username", user.getUsername());
        usernameCookie.setMaxAge(60 * 60 * 24 * 30);
        usernameCookie.setPath("/");
        response.addCookie(sessionCookie);
        response.addCookie(usernameCookie);

        return new BaseResult<>(ResponseCode.SUCCESS);
    }
}
