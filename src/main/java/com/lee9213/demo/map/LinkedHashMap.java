package com.lee9213.demo.map;

import java.util.Map;

/**
 * @author libo
 * @version 1.0
 * @date 2017/6/8 11:34
 */
public class LinkedHashMap<K, V> extends HashMap18<K, V> implements Map<K, V> {

    static class Entry<K, V> extends HashMap18.Node<K, V> {
        LinkedHashMap.Entry<K, V> before, after;

        Entry(int hash, K key, V value, HashMap18.Node<K, V> next) {
            super(hash, key, value, next);
        }
    }
}
