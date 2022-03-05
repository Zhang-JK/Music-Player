package com.jk.player.result;

public enum ResponseCode {
    // success
    SUCCESS(200, "Success"),

    // login error
    NOT_LOGIN(301, "Not login"),
    LOGIN_WRONG_PASSWORD(302, "Wrong password"),
    LOGIN_USER_NOT_EXIST(303, "User not exist");


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
