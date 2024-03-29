package com.jk.player.dao;

import com.jk.player.model.User;
import com.jk.player.model.UserCookie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCookieDAO extends JpaRepository<UserCookie, Integer> {
    UserCookie findByUserAndPlatform(User user, Integer platform);
}
