package com.jk.player.result;

public class BaseResult {
    // result code
    private int code;
    private String message;

    public BaseResult(int code) {
        this.code = code;
    }

    public BaseResult(ResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
