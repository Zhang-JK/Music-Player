package com.jk.player.service;

import com.jk.player.dao.ListDAO;
import com.jk.player.dao.SongDAO;
import com.jk.player.model.List;
import com.jk.player.model.Song;
import com.jk.player.model.User;
import com.jk.player.response.ListResponse;
import com.jk.player.response.SongResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ListService {

    @Autowired
    private ListDAO listDAO;

    @Autowired
    private SongDAO songDAO;

    public Boolean verifyUser(User user, int listId) {
        return Objects.equals(listDAO.getById(listId).getUser().getId(), user.getId());
    }

    public String createList(String name, String cover, User user) {
        if (listDAO.findByUserAndName(user, name) != null) {
            return "List with this name already exists";
        }
        List list = new List();
        list.setName(name);
        list.setUser(user);
        list.setCover(cover);
        try {
            listDAO.save(list);
        }catch (Exception e){
            return e.getMessage();
        }
        return null;
    }

    public String deleteList(int id) {
        try {
            listDAO.deleteById(id);
        }catch (Exception e){
            return e.getMessage();
        }
        return null;
    }

    public java.util.List<ListResponse> getListByUser(User user) {
        java.util.List<List> lists = listDAO.findAllByUser(user);
        return lists.stream().map(item -> {
            ListResponse listResponse = new ListResponse();
            listResponse.setId(item.getId());
            listResponse.setName(item.getName());
            listResponse.setCover(item.getCover());
            listResponse.setOwner(item.getUser().getUsername());
            return listResponse;
        }).collect(Collectors.toList());
    }

    public Set<Song> getSongs(int id) {
        return listDAO.getById(id).getSongs();
    }

    public java.util.List<SongResponse> getSongsInList(int id) {
        Set<Song> songs = getSongs(id);
        return songs.stream().map(SongResponse::new).collect(Collectors.toList());
    }

    public String addSongToList(int listId, java.util.List<Long> songId) {
        Set<Song> songs = getSongs(listId);
        try {
            songs.addAll(songDAO.findAllById(songId));
        }catch (Exception e){
            return e.getMessage();
        }

        List list = listDAO.getById(listId);
        list.setSongs(songs);
        try {
            listDAO.saveAndFlush(list);
        }catch (Exception e){
            return e.getMessage();
        }
        return null;
    }

    public String deleteSongFromList(int listId, java.util.List<Long> songId) {
        Set<Song> songs = getSongs(listId);
        try {
            songDAO.findAllById(songId).forEach(songs::remove);
        }catch (Exception e){
            return e.getMessage();
        }

        List list = listDAO.getById(listId);
        list.setSongs(songs);
        try {
            listDAO.saveAndFlush(list);
        }catch (Exception e){
            return e.getMessage();
        }
        return null;
    }
}
