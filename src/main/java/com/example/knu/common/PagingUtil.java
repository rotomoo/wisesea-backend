package com.example.knu.common;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class PagingUtil {

    public static Map<String, Object> toMap(Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.convertValue(obj, Map.class);
    }
}
