package com.winhxd.b2c.common.domain.pay.vo;

import java.util.HashMap;
import java.util.Map;

public class VerifyResultVO {

    private Map<String, Object> result = new HashMap<>();

    public Object get(Object key) {
        return result.get(key);
    }

    public Object put(String key, Object value) {
        return result.put(key, value);
    }

    public Map<String, Object> getMap() {
        return result;
    }
}
