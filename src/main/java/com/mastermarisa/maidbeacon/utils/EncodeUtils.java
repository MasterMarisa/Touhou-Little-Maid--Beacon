package com.mastermarisa.maidbeacon.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EncodeUtils {
    private static final Gson GSON;

    public static <T> String toJson(T obj){
        return GSON.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<? extends T> type){
        return GSON.fromJson(json, type);
    }

    static {
        GSON = new GsonBuilder().setPrettyPrinting().create();
    }
}
