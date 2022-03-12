package com.jk.player.dao;

import com.jk.player.model.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSessionDAO extends JpaRepository<UserSession, Long> {
    UserSession findBySession(String session);
}
