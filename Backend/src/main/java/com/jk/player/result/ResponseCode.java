package com.jk.player.result;

public enum ResponseCode {
    // success
    SUCCESS(200, "Success"),

    // error
    BAD_REQUEST(400, "Request is invalid"),
    SERVER_ERROR(500, "Server error"),

    // login error
    NOT_LOGIN(1001, "Not login"),
    LOGIN_WRONG_PASSWORD(1002, "Wrong password"),
    LOGIN_USER_NOT_EXIST(1003, "User not exist"),

    // bili account error
    BILI_NOT_LOGIN(1101, "Bili not login"),
    BILI_LOGIN_NOT_SCAN(1202, "Bili qrcode not scan"),
    BILI_LOGIN_NOT_CONFIRM(1303, "Bili qrcode not confirm"),
    BILI_LOGIN_URL_EXPIRED(1404, "Bili qrcode url expired");

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
