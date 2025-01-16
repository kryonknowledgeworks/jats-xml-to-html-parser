package com.kryonknowledgeworks.jats2html.mapbuilder;

import java.util.HashMap;
import java.util.Map;

public class MetaDataBuilder {

    private Map<String, Object> dataMap = new HashMap<>();


    public MetaDataBuilder addValue(String key, Object value) {
        dataMap.put(key, value);
        return this;
    }

    public Map<String, Object> build() {
        return dataMap;
    }
}
