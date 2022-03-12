package com.jk.player.dao;

import com.jk.player.model.List;
import com.jk.player.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListDAO extends JpaRepository<List, Integer> {
    java.util.List<List> findAllByUser(User user);

    List findByUserAndName(User user, String name);
}
