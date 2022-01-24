package com.unity3d.player.http;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unity3d.player.http.custom.MyConverterFactory;
import com.unity3d.player.http.custom.MyTypeAdapterFactory;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;

public class RetrofitFactory {

    //app基础服务
    public static String NEW_URL;  //域名

    public static final String CLIENT = "2";//1后台用户 2app用户

    public static int DEFAULT_TIME = 30;
    public static RetrofitFactory httpUtils;

    private final OkHttpClient okHttpClient;

    private RetrofitFactory() {
        okHttpClient = initOkHttp();
    }

    public static RetrofitFactory getInstance() {
        if (httpUtils == null) {
            httpUtils = new RetrofitFactory();
        }
        return httpUtils;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    /**
     * 初始化app基础服务(新)
     *
     * @return
     */
    public Retrofit initNewRetrofit() {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(NEW_URL)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create(customGson()))
                .addConverterFactory(MyConverterFactory.create(customGson()))
                .build();
    }

    /**
     * 初始化okhttp
     * 上线前屏蔽掉 sslSocketFactory 和 hostnameVerifier
     *
     * @return
     */
    private OkHttpClient initOkHttp() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient().newBuilder()
//                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
//                        SSLSocketClient.getX509Trust() : SSLSocketClient.getX509Extended())//忽略证书校验
//                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())//忽略证书校验
                .readTimeout(DEFAULT_TIME, TimeUnit.SECONDS)//设置读取超时时间
                .connectTimeout(DEFAULT_TIME, TimeUnit.SECONDS)//设置请求超时时间
                .writeTimeout(DEFAULT_TIME, TimeUnit.SECONDS)//设置写入超时时间
                .addInterceptor(new LogInterceptor())//添加打印拦截器
                .hostnameVerifier((hostname, session) -> true)
                .addNetworkInterceptor(httpLoggingInterceptor)
                .retryOnConnectionFailure(true)//设置出现错误进行重新连接。
                .build();
    }

    public static class LogInterceptor implements Interceptor {

        @NotNull
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            okhttp3.Response response = chain.proceed(request);
            okhttp3.MediaType mediaType = Objects.requireNonNull(response.body()).contentType();
            String content = Objects.requireNonNull(response.body()).string();
            Log.w("result", "入参:" + request.toString() + "\n出参:" + content);

            return response.newBuilder()
                    .body(okhttp3.ResponseBody.create(content, mediaType))
                    .build();
        }
    }

    public static Gson customGson() {
        GsonBuilder builder = new GsonBuilder();
        return builder.registerTypeAdapterFactory(new MyTypeAdapterFactory())
                .create();
    }
}
