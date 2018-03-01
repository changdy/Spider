package com.smzdm;

import com.alibaba.fastjson.JSONArray;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public enum HandlerFunction {


    DEFAULT(x -> x),
    PRICE(price -> {
        if (price != null && !price.equals("") && !price.equals("无")) {
            return Double.valueOf(price);
        } else {
            return 0.0;
        }
    }),
    DATE(string -> {
        LocalDateTime localDateTime;
        if (string.contains("-")) {
            if (string.contains(":")) {
                string = LocalDate.now().getYear() + "-" + string;
                localDateTime = LocalDateTime.parse(string, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            } else {
                localDateTime = LocalDate.parse(string).atTime(0, 0);
            }
        } else {
            LocalTime time = LocalTime.parse(string);
            LocalDate date = LocalDate.now();
            if (time.isAfter(LocalTime.now())) {
                date = date.plusDays(-1);
            }
            localDateTime = time.atDate(date);
        }
        return localDateTime;
    }),
    CATEGORY(x -> {
        JSONArray array = JSONArray.parseArray(x);
        int size = array.size();
        Integer[] ints = new Integer[size];
        for (int i = 0; i < size; i++) {
            ints[i] = array.getJSONObject(i).getInteger("ID");
        }
        return ints;
    });

    private Function<String, Object> function;

    HandlerFunction(Function<String, Object> function) {
        this.function = function;
    }

    public Function<String, Object> getFunction() {
        return function;
    }

    public void setFunction(Function<String, Object> function) {
        this.function = function;
    }
}