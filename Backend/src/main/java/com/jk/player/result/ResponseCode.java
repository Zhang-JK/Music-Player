package com.jk.player.result;

public enum ResponseCode {
    // success
    SUCCESS(200, "Success"),

    // login error
    NOT_LOGIN(301, "Not login"),
    LOGIN_WRONG_PASSWORD(302, "Wrong password"),
    LOGIN_USER_NOT_EXIST(303, "User not exist"),

    // bili account error
    BILI_NOT_LOGIN(401, "Bili not login"),
    BILI_LOGIN_NOT_SCAN(402, "Bili qrcode not scan"),
    BILI_LOGIN_NOT_CONFIRM(403, "Bili qrcode not confirm"),
    BILI_LOGIN_URL_EXPIRED(404, "Bili qrcode url expired");

    private final int code;
    private final String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
