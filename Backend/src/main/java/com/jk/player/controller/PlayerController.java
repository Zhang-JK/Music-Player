package com.jk.player.controller;

import com.jk.player.model.User;
import com.jk.player.response.PlayerLinkResponse;
import com.jk.player.response.BaseResult;
import com.jk.player.response.ResponseCode;
import com.jk.player.service.LoginService;
import com.jk.player.service.PlayerService;
import com.jk.player.utils.Platforms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PlayerController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private PlayerService playerService;

    @CrossOrigin
    @PostMapping(value = "/api/player/link")
    @ResponseBody
    public BaseResult<List<PlayerLinkResponse>> getPlayerLink(@CookieValue(value = "session") String session, @CookieValue(value = "username") String username, @RequestParam Integer platform, @RequestParam List<BigInteger> ids) {
        User user = loginService.verifyLoginUser(session, username);
        if(user == null) return new BaseResult<>(ResponseCode.NOT_LOGIN);

        List<PlayerLinkResponse> list = new ArrayList<>();
        for (BigInteger id : ids) {
            PlayerLinkResponse response = playerService.getPlayerLinkById(Platforms.getPlatform(platform), id, user);
            response.setSrcId(id);
            list.add(response);
        }

        return new BaseResult<>(ResponseCode.SUCCESS, list);
    }

}
