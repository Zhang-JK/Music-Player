package com.jk.player.response;

import java.math.BigInteger;
import java.time.Instant;

public class PlayerLinkResponse {
    // 0: success, 1: not found, 2: not authorized, 3: unavailable, 4: invalid parameters
    Integer status;
    String message;
    String link;
    // mp3, flac, mp4
    String type;
    Instant expires;
    BigInteger srcId;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Instant getExpires() {
        return expires;
    }

    public void setExpires(Instant expires) {
        this.expires = expires;
    }

    public BigInteger getSrcId() {
        return srcId;
    }

    public void setSrcId(BigInteger srcId) {
        this.srcId = srcId;
    }
}
