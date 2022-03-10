package com.jk.player.result;

public enum ResponseCode {
    // success
    SUCCESS(200, "Success"),

    // error
    BAD_REQUEST(400, "Request is invalid"),
    LIMIT_EXCEED_MAX(400, "Limit exceed max"),
    SERVER_ERROR(500, "Server error"),

    // login error
    NOT_LOGIN(1001, "Not login"),
    LOGIN_WRONG_PASSWORD(1002, "Wrong password"),
    LOGIN_USER_NOT_EXIST(1003, "User not exist"),

    // platform account error
    PLATFORM_NOT_LOGIN(1101, "Platform not login"),
    PLATFORM_LOGIN_NOT_SCAN(1202, "Platform qrcode not scan"),
    PLATFORM_LOGIN_NOT_CONFIRM(1303, "Platform qrcode not confirm"),
    PLATFORM_LOGIN_URL_EXPIRED(1404, "Platform qrcode url expired"),

    // import song error
    IMPORT_ERROR(1201, "Import song error");

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
