package com.tdclighthouse.prototype.services;

import java.util.HashMap;
import java.util.Map;

public class InMemoryReferenceRegistry implements ReferenceRegistry {
    
    private Map<String, String> map = new HashMap<>();

    @Override
    public void register(String reference, String uuid) {
        map.put(reference, uuid);
    }

    @Override
    public String lookup(String reference) {
        return map.get(reference);
    }

}
