package com.example.colze.utils;

import java.util.HashMap;

/**
 * Created by tong on 15/11/19.
 */
public class LoggerHashMap<K,V> extends HashMap<K,V> {
    private String tag;

    public LoggerHashMap(String tag) {
        this.tag = tag;
    }

    public LoggerHashMap() {

    }

    @Override
    public V put(K key, V value) {
        d("<<<< put key: " + key + " val: " + value);
        return super.put(key, value);
    }

    @Override
    public V get(Object key) {
        V value =  super.get(key);
        d("<<<< get key: " + key + " val: " + value);
        return value;
    }

    private void d(String content) {
        if (tag != null) {
            Logger.d(tag,content);
        } else {
            Logger.d("<< logmap " + content);
        }
    }
}
