package com.unity3d.player.http.custom;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class MyTypeAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<T> rawType = (Class<T>) type.getRawType();
        if (rawType == String.class)
            return (TypeAdapter<T>) new StringAdapter();
        if (rawType == Integer.class)
            return (TypeAdapter<T>) new IntegerAdapter();
        if (rawType == Double.class)
            return (TypeAdapter<T>) new DoubleAdapter();
        return null;
    }

    public static class StringAdapter extends TypeAdapter<String> {

        @Override
        public void write(JsonWriter out, String value) {

        }

        @Override
        public String read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return "";
            }
            if (in.peek() == JsonToken.BOOLEAN) {
                in.nextBoolean();
                return "";
            }
            return in.nextString();
        }
    }

    public static class IntegerAdapter extends TypeAdapter<Integer> {

        @Override
        public void write(JsonWriter out, Integer value) {

        }

        @Override
        public Integer read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return 0;
            }
            if (in.peek() == JsonToken.STRING) {
                in.nextString();
                return 0;
            }
            if (in.peek() == JsonToken.BOOLEAN) {
                in.nextBoolean();
                return 0;
            }
            return in.nextInt();
        }
    }

    public static class DoubleAdapter extends TypeAdapter<Double> {

        @Override
        public void write(JsonWriter out, Double value) {

        }

        @Override
        public Double read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return 0.0;
            }
            if (in.peek() == JsonToken.STRING) {
                in.nextString();
                return 0.0;
            }
            if (in.peek() == JsonToken.BOOLEAN) {
                in.nextBoolean();
                return 0.0;
            }
            return in.nextDouble();
        }
    }
}
