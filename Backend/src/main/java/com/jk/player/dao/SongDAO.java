package com.jk.player.dao;

import com.jk.player.model.Song;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SongDAO extends JpaRepository<Song, Long> {
    Song findByPlatformAndSerial(Integer platform, String serial);

    @Query (value = "select s from Song s where (s.platform = :platform or :platform is null) and (s.serial = :serial or :serial is null) and (s.name like CONCAT('%',:name,'%') or :name is null) and (s.artist like CONCAT('%',:artist,'%') or :artist is null)")
    List<Song> searchForSongs(@Param("platform") Integer platform, @Param("serial") String serial, @Param("name") String name, @Param("artist") String artist);
}
