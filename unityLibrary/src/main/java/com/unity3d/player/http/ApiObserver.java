package com.unity3d.player.http;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonParseException;
import com.unity3d.player.http.custom.DataResultException;
import com.unity3d.player.http.custom.TokenBreakException;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import retrofit2.HttpException;

public abstract class ApiObserver<T> implements Observer<ApiResponse<T>> {

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
    public static final int NET_ERROR = -2;
    public static final int JSON_ERROR = -3;
    public static final int DATA_ERROR = -4;
    public static final int TOKEN_ERROR = -5;
    public static final int PERMISSION_ERROR = -6;

    @Override
    public void onSubscribe(@NotNull Disposable d) {
        subscribe(d);
    }

    @Override
    public void onNext(ApiResponse<T> response) {
        //在这边对 基础数据 进行统一处理  初步解析：
        if (response.getCode() == 200) {
            onSuccess(response.getData(), response.getMsg() == null ? "" : response.getMsg());
        } else {
            onFail(response.getCode(), response.getMsg() == null ? "" : response.getMsg());
        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable throwable) {
        String error;
        if (throwable instanceof SocketTimeoutException  //网络超时,网络连接异常
                || throwable instanceof ConnectException   //均视为网络异常
                || throwable instanceof UnknownHostException) {
            Log.e("TAG", "网络连接异常: " + throwable.getMessage());
            error = "网络连接异常: " + throwable.getMessage();
            onFail(NET_ERROR, error);
        } else if (throwable instanceof TokenBreakException) {
            onFail(((TokenBreakException) throwable).getCode(), "Token失效");
        } else if (throwable instanceof DataResultException) {  //服务器返回特殊定义
            Log.e("TAG", "特殊定义,code: " + ((DataResultException) throwable).getCode() +
                    ", msg: " + ((DataResultException) throwable).getMsg());
            error = TextUtils.isEmpty(((DataResultException) throwable).getMsg()) ?
                    "Token失效" : ((DataResultException) throwable).getMsg();
            onFail(((DataResultException) throwable).getCode(), error);
        } else if (throwable instanceof JsonParseException
                || throwable instanceof JSONException     //均视为解析错误
                || throwable instanceof java.text.ParseException) {
            Log.e("TAG", "数据解析异常: " + throwable.getMessage());
            error = "数据解析异常: " + throwable.getMessage();
            onFail(JSON_ERROR, error);
        } else if (throwable instanceof HttpException) {
            if (((HttpException) throwable).code() == 401) {
                onFail(TOKEN_ERROR, "Login error");
            } else if (((HttpException) throwable).code() == 403) {
                onFail(PERMISSION_ERROR, "No permission");
            } else if (((HttpException) throwable).code() == 500) {
                onFail(NET_ERROR, "网络连接异常");
            } else {
                error = throwable.getMessage();
                onFail(DATA_ERROR, error == null ? "" : error);
            }
//            onFail(DATA_ERROR, error);
        } else {
            error = throwable.getMessage();
            onFail(NET_ERROR, error == null ? "" : error);
        }
    }

    public abstract void onSuccess(T demo, String msg);

    public void subscribe(Disposable d) {

    }

    public void onFinish(ApiResponse<T> response) {
    }

    public abstract void onFail(int errorCode, String errorMsg);
}
