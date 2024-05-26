package com.harleylizard.trouble.common;

import java.util.*;

public final class MultiMap<K, V> {
    private final Map<K, List<V>> map;

    private MultiMap(Map<K, List<V>> map) {
        this.map = map;
    }

    public void put(K k, V v) {
        map.computeIfAbsent(k, m -> new ArrayList<>()).add(v);
    }

    public List<V> get(K k) {
        var list = map.getOrDefault(k, List.of());
        return list.isEmpty() ? List.of() : Collections.unmodifiableList(list);
    }

    public void clear() {
        map.clear();
    }

    public static <K, V> MultiMap<K, V> mutableOf() {
        return new MultiMap<>(new HashMap<>());
    }
}
