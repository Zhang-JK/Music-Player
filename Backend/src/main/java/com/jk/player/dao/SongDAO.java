package com.jk.player.dao;

import com.jk.player.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface SongDAO extends JpaRepository<Song, Integer> {
    Song findById(Long id);
    Song findByPlatformAndSerial(Integer platform, String serial);
}
