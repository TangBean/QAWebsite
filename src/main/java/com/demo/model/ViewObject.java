package com.demo.model;

import java.util.HashMap;
import java.util.Map;

public class ViewObject {
    private Map<String, Object> objectMap = new HashMap<>();

    public void set(String key, Object value) {
        objectMap.put(key, value);
    }

    public Object get(String key) {
        return objectMap.get(key);
    }
}
