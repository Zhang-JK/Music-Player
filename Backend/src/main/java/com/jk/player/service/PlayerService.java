package com.jk.player.service;

import com.jk.player.dao.SongDAO;
import com.jk.player.model.Song;
import com.jk.player.model.User;
import com.jk.player.response.PlayerLinkResponse;
import com.jk.player.utils.Platforms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Objects;

@Service
public class PlayerService {

    @Autowired
    private SongDAO songDAO;

    @Autowired
    private NeteaseFavService neteaseFavService;

    @Autowired
    private BiliFavService biliFavService;

    public PlayerLinkResponse getPlayerLinkById(Platforms platform, BigInteger id, User user) {
        if (platform == Platforms.LOCAL) {
            Song song;
            try {
                song = songDAO.getById(id.longValue());
            } catch (Exception e) {
                PlayerLinkResponse response = new PlayerLinkResponse();
                response.setMessage("ID not found");
                response.setStatus(1);
                return response;
            }
            platform = Platforms.getPlatform(song.getPlatform());
            BigInteger serial = new BigInteger(song.getSerial());
            id = BigInteger.valueOf(serial.longValue());
        }

        switch (Objects.requireNonNull(platform)) {
            case BILI:
                return biliFavService.getBiliSongLink(user, id);
            case NETEASE:
                return neteaseFavService.getNeteaseSongLink(user, id);
        }
        PlayerLinkResponse response = new PlayerLinkResponse();
        response.setMessage("Platform not supported");
        response.setStatus(4);
        return response;
    }
}
