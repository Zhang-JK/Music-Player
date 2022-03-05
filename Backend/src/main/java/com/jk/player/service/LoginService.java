package com.jk.player.service;

import cn.hutool.core.util.IdUtil;
import com.jk.player.model.User;
import com.jk.player.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private UserDAO userDAO;

    public User getUser(String username) {
        return userDAO.findByUsername(username);
    }

    public String generateSession(int userId) {
        String session = IdUtil.simpleUUID();
        return session;
    }
}
