package com.gs.coderepository.bean;

/**
 * Created by 13203 on 2019-01-11.
 */

public class HttpResult {
    private String message;
    private int code;

    public HttpResult(String message, int code) {
        this.message = message;
        this.code = code;
    }
}
