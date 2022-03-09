package com.jk.player.dao;

import com.jk.player.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongDAO extends JpaRepository<Song, Integer> {
    Song findByPlatformAndSerial(Integer platform, String serial);
}
