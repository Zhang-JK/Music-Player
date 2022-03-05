package com.jk.player.model;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 256)
    private String password;

    @Column(name = "salt", nullable = false, length = 256)
    private String salt;

    @OneToMany(mappedBy = "user")
    private Set<UserCookie> userCookies = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<List> lists = new LinkedHashSet<>();

    public Set<List> getLists() {
        return lists;
    }

    public void setLists(Set<List> lists) {
        this.lists = lists;
    }

    public Set<UserCookie> getUserCookies() {
        return userCookies;
    }

    public void setUserCookies(Set<UserCookie> userCookies) {
        this.userCookies = userCookies;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}