package com.unity3d.player.http.custom;

import com.google.gson.Gson;
import com.unity3d.player.http.ApiResponse;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class MyResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Gson gson;
    private final Type type;

    MyResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String json = value.string();
        try {
            ApiResponse<T> apiResponse = gson.fromJson(json, ApiResponse.class);
            if (apiResponse.getCode() != 200)
                throw new DataResultException(apiResponse.getMsg(), apiResponse.getCode());
//            switch (apiResponse.getCode()) {
//                case 501:
//                case 508:
//                    throw new TokenBreakException(apiResponse.getMsg(), apiResponse.getCode());
//                case 500:
//                case 502:
//                case 503:
//                case 504:
//                case 506:
//                case 507:
//                    throw new DataResultException(apiResponse.getMsg(), apiResponse.getCode());
//            }
            return gson.fromJson(json, type);
        } finally {
            value.close();
        }
    }
}
