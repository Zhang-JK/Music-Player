package com.jk.player.service;

import cn.hutool.core.util.IdUtil;
import com.jk.player.dao.UserSessionDAO;
import com.jk.player.model.User;
import com.jk.player.dao.UserDAO;
import com.jk.player.model.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class LoginService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserSessionDAO userSessionDAO;

    public User getUser(String username) {
        return userDAO.findByUsername(username);
    }

    public String generateSession(User user) {
        String session = IdUtil.simpleUUID();
        UserSession userSession = new UserSession();
        userSession.setUser(user);
        userSession.setSession(session);
        userSession.setExpireStamp(Instant.now().plusSeconds(60 * 60 * 24 * 30));
        userSessionDAO.save(userSession);
        return session;
    }
}
