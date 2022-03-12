package com.jk.player.model;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "user_session")
public class UserSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "session", nullable = false, length = 256)
    private String session;

    @Column(name = "expire_stamp", nullable = false)
    private Instant expireStamp;

    public Instant getExpireStamp() {
        return expireStamp;
    }

    public void setExpireStamp(Instant expireStamp) {
        this.expireStamp = expireStamp;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}