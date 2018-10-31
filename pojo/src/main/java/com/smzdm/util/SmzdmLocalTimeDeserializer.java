package com.smzdm.util;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class SmzdmLocalTimeDeserializer implements ObjectDeserializer {

    public LocalDateTime deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONLexer lexer = parser.getLexer();
        Instant instant = Instant.ofEpochMilli(Long.valueOf(lexer.stringVal()) * 1000L);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}