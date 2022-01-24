package com.unity3d.player.http.custom;

import java.io.IOException;

/**
 * 其他后台定义的特殊场景
 */
public class DataResultException extends IOException {

    /**
     * code的值：
     * 200接口请求成功，正常处理数据
     * 500接口请求成功，返回提示语
     * 501接口请求成功，TOKEN失效
     * 502接口请求成功，设置支付密码
     * 503接口请求成功，完成实名认证
     * 504接口请求成功，余额不足
     * 506接口请求成功，不作任何处理和提示
     * 507接口请求成功，支付密码错误
     * 508接口请求成功，缺TOKEN，需要登录
     **/
    private String msg;
    private int code;

    public DataResultException(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public DataResultException(String message, String msg, int code) {
        super(message);
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
