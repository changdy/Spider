package com.smzdm.util;

import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;

/**
 * Created by Changdy on 2018/6/28.
 */
public class WxMsgConvertUtil {

    public static JSONObject objectConvert(Object o) {
        Class objectClass = o.getClass();
        Field[] declaredFields = objectClass.getDeclaredFields();
        JSONObject jsonObject = new JSONObject(declaredFields.length);
        for (Field declaredField : declaredFields) {
            JSONObject tempObject = new JSONObject(2);
            declaredField.setAccessible(true);
            try {
                tempObject.put("value", declaredField.get(o) == null ? "" : declaredField.get(o));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            jsonObject.put(declaredField.getName(), tempObject);
        }
        return jsonObject;
    }
}