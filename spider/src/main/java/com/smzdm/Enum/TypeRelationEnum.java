package com.smzdm.Enum;

import com.smzdm.pojo.Article;

import java.util.function.Function;

/**
 * Created by Changdy on 2018/3/9.
 */
public enum TypeRelationEnum {

    MALL("enum:article_mall", Article::getMall),
    CHANNEL("enum:article_channel", Article::getChannel),
    TYPE("enum:article_type", Article::getType),
    YH_TYPE("enum:yh_type", Article::getYhType);

    private String key;
    private Function<Article, String> function;

    TypeRelationEnum(String key, Function<Article, String> function) {
        this.key = key;
        this.function = function;
    }

    public String getKey() {
        return key;
    }

    public Function<Article, String> getFunction() {
        return function;
    }
}
