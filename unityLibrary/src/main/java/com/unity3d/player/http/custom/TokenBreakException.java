package com.unity3d.player.http.custom;

import java.io.IOException;

/**
 * 登录失效、Token失效
 */
public class TokenBreakException extends IOException {

    private String msg;
    private int code;

    public TokenBreakException(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
