package com.smzdm.service;

import com.alibaba.fastjson.JSONObject;
import com.smzdm.Handler;
import com.smzdm.HandlerFunction;
import com.smzdm.pojo.Article;
import com.smzdm.pojo.ArticleInfo;
import com.smzdm.pojo.Category;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

@Service
public class JsonConvertService {
    private <T> T convert(JSONObject json, Class<T> test) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Field[] fields = test.getDeclaredFields();
        T instance = test.newInstance();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Handler.class)) {
                field.setAccessible(true);
                Object result;
                Handler handler = field.getDeclaredAnnotation(Handler.class);
                String value = getFiledValue(json, handler.value().split("\\|"));
                if (value == null || "".equals(value.trim())) {
                    continue;
                }
                HandlerFunction handlerFunction = handler.function();
                if (handlerFunction != HandlerFunction.DEFAULT) {
                    result = handlerFunction.getFunction().apply(value);
                } else {
                    Class<?> type = field.getType();
                    String reg = handler.regPattern();
                    if (!reg.equals("")) {
                        value = value.replaceAll(reg, "");
                    }
                    if (type.equals(String.class)) {
                        result = value;
                    } else {
                        Method valueOf;
                        valueOf = type.getMethod("valueOf", String.class);
                        result = valueOf.invoke(null, value);
                    }
                }
                field.set(instance, result);
            }
        }
        return instance;
    }

    private String getFiledValue(JSONObject json, String[] split) {
        for (String s : split) {
            if (s.contains(".")) {
                String[] layer = s.split("\\.");
                Optional<JSONObject> optional = Optional.of(json);
                for (int i = 0; i < layer.length - 1; i++) {
                    String temp = layer[i];
                    optional = optional.map(x -> x.getJSONObject(temp));
                }
                String temp = layer[layer.length - 1];
                Optional<String> string = optional.map(x -> x.getString(temp));
                if (string.isPresent()) {
                    return string.get();
                }
            } else {
                if (json.containsKey(s)) {
                    return json.getString(s);
                }
            }
        }
        return null;
    }

    private <T> T handlerConvertException(JSONObject json, Class<T> test) {
        try {
            return convert(json, test);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public Article convertToArticle(JSONObject json) {
        return handlerConvertException(json, Article.class);
    }

    public ArticleInfo convertToInfo(JSONObject json) {
        return handlerConvertException(json, ArticleInfo.class);
    }
}