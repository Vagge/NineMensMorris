package com.example.ninemensmorris.DataConverter;

import androidx.room.TypeConverter;

import com.example.ninemensmorris.Model.GameItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class DataConverter {
    @TypeConverter
    public String fromParamList(int[] params) {
        if (params == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<int[]>() {}.getType();
        String json = gson.toJson(params, type);
        return json;
    }

    @TypeConverter
    public int[] toParamList(String params) {
        if (params == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<int[]>() {}.getType();
        int[] countryLangList = gson.fromJson(params, type);
        return countryLangList;
    }
}