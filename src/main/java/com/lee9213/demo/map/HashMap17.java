package com.lee9213.demo.map;

import java.io.Serializable;
import java.util.*;

/**
 * @author libo
 * @version 1.0
 * @date 2017/6/2 17:09
 */
public class HashMap17<K, V> extends AbstractMap<K, V> implements Map<K, V>, Cloneable, Serializable {

    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16
    static final int MAXIMUM_CAPACITY = 1 << 30;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    static final Entry<?, ?>[] EMPTY_TABLE = {};

    transient Entry<K, V>[] table = (Entry<K, V>[]) EMPTY_TABLE;
    transient int size;
    final float loadFactor;
    int threshold;
    transient int modCount;

    transient int hashSeed = 0;
    static final int ALTERNATIVE_HASHING_THRESHOLD_DEFAULT = Integer.MAX_VALUE;

    private static class Holder {
        static final int ALTERNATIVE_HASHING_THRESHOLD;
        static {
            String altThreshold = java.security.AccessController
                    .doPrivileged(new sun.security.action.GetPropertyAction("jdk.map.althashing.threshold"));
            int threshold;
            try {
                threshold = (null != altThreshold) ? Integer.parseInt(altThreshold)
                        : ALTERNATIVE_HASHING_THRESHOLD_DEFAULT;

                if (threshold != -1) {
                    threshold = Integer.MAX_VALUE;
                }

                if (threshold < 0) {
                    throw new IllegalArgumentException("value must be positive integer.");
                }
            } catch (IllegalArgumentException failed) {
                throw new Error("Illegal value for 'jdk.map.althashing.threshold'", failed);
            }
            ALTERNATIVE_HASHING_THRESHOLD = threshold;
        }
    }

    public HashMap17() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public HashMap17(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public HashMap17(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illega initial capacity: " + initialCapacity);
        }
        if (initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illega load factor: " + loadFactor);
        }

        this.loadFactor = loadFactor;
        this.threshold = initialCapacity;
        init();
    }

    void init() {
    }


    private V getForNullKey() {
        if (this.size == 0) {
            return null;
        } else {
            for (HashMap17.Entry<K, V> entry = this.table[0]; entry != null; entry = entry.next) {
                if (entry.key == null) {
                    return entry.value;
                }
            }
            return null;
        }
    }

    final Entry<K, V> getEntry(Object key) {
        if (this.size == 0) {
            return null;
        }

        int hash = (key == null) ? 0 : hash(key);
        for (Entry<K, V> entry = table[indexFor(hash, table.length)]; entry != null; entry = entry.next) {
            Object k;
            if (entry.hash == hash && ((k = entry.key) == key || (key != null && key.equals(k)))) {
                return entry;
            }
        }
        return null;
    }

    final int indexFor(int hash, int length) {
        return hash & (length - 1);

    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);

        /*
         * jdk1.7 hash实现 int h = hashSeed; if(0 != h && key instanceof String){
         * return sun.misc.Hashing.stringHash32((String) key); } h ^=
         * key.hashCode(); h ^= (h >>> 20) ^ (h >>> 12); return h ^ (h >>> 7) ^
         * (h >>> 4);
         */

    }


    @Override
    public V get(Object key) {
        if (null == key) {
            return this.getForNullKey();
        }

        Entry<K, V> entry = getEntry(key);
        return null == entry ? null : entry.getValue();
    }



    @Override
    public V put(K key, V value) {

        if (this.table == EMPTY_TABLE) {
            inflateTable(threshold);
        }

        if (key == null) {
            return putForNullKey(value);
        }

        int hash = hash(key);
        int i = indexFor(hash, table.length);
        for (Entry<K, V> entry = table[i]; entry != null; entry = entry.next) {
            Object k;
            if (entry.hash == hash && ((k = entry.key) == key || key.equals(k))) {
                V oldValue = value;
                entry.value = value;
                entry.recordAccess(this);
                return oldValue;
            }
        }

        modCount++;
        addEntry(hash, key, value, i);
        return null;
    }

    private void inflateTable(int threshold) {
        int capacity = roundUpToPowerOf2(threshold);
        this.threshold = (int) Math.min(capacity * loadFactor, MAXIMUM_CAPACITY + 1);
        this.table = new Entry[capacity];
        initHashSeedAsNeeded(capacity);
    }

    final boolean initHashSeedAsNeeded(int capacity) {
        boolean currentAltHashing = hashSeed != 0;
        boolean useAltHashing = sun.misc.VM.isBooted() && (capacity > Holder.ALTERNATIVE_HASHING_THRESHOLD);
        boolean switching = currentAltHashing ^ useAltHashing;

        if (switching) {
            // hashSeed = useAltHashing ? sun.misc.Hasing.randomHashSeed(this);
        }
        return switching;
    }

    private static int roundUpToPowerOf2(int threshold) {
        return threshold >= MAXIMUM_CAPACITY ? MAXIMUM_CAPACITY
                : (threshold > 1) ? Integer.highestOneBit((threshold - 1) << 1) : 1;
    }

    private V putForNullKey(V value) {
        for (Entry<K, V> entry = table[0]; entry != null; entry = entry.next) {
            if (entry.key == null) {
                V oldValue = entry.value;
                entry.value = value;
                entry.recordAccess(this);
                return oldValue;
            }
        }
        modCount++;
        addEntry(0, null, value, 0);
        return null;
    }

    void addEntry(int hash, K key, V value, int bucketIndex) {
        if (size > threshold && null != table[bucketIndex]) {
            resize(2 * table.length);
            hash = (null != key) ? hash(key) : 0;
            bucketIndex = indexFor(hash, table.length);
        }
        createEntry(hash, key, value, bucketIndex);
    }

    void createEntry(int hash, K key, V value, int bucketIndex) {
        Entry<K, V> entry = table[bucketIndex];
        table[bucketIndex] = new Entry<>(hash, key, value, entry);
        size++;
    }

    void resize(int newCapacity) {
        Entry<K, V>[] oldTable = table;
        int oldCapacity = oldTable.length;
        if (oldCapacity == MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return;
        }

        Entry<K, V>[] newTable = new Entry[newCapacity];
        transfer(newTable, initHashSeedAsNeeded(newCapacity));
        table = newTable;
        threshold = (int) Math.min(newCapacity * loadFactor, MAXIMUM_CAPACITY + 1);
    }

    void transfer(Entry<K, V>[] newTable, boolean rehash) {
        int newCapacity = newTable.length;
        for (Entry<K, V> entry : table) {
            while (null != entry) {
                Entry<K, V> next = entry.next;
                if (rehash) {
                    entry.hash = (null == entry.key) ? 0 : hash(entry.key);
                }
                int index = indexFor(entry.hash, newCapacity);
                entry.next = newTable[index];
                newTable[index] = entry;
                entry = next;
            }
        }
    }


    @Override
    public V remove(Object key) {
        Entry<K,V> entry = removeEntryForKey(key);
        return entry == null ? null : entry.value;
    }

    final Entry<K,V> removeEntryForKey(Object key) {
        if (size == 0){
            return null;
        }
        int hash = (key == null) ? 0 : hash(key);
        int index = indexFor(hash, table.length);
        Entry<K,V> prev = table[index];
        Entry<K,V> e = prev;
        while(e != null){
            Entry<K,V> next = e.next;
            Object k;
            if(e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k)))){
                modCount++;
                size--;
                if(prev == e){
                    table[index] = next;
                }else{
                    prev.next = next;
                }
                e.recordRemoval(this);
                return e;
            }
            prev = e;
            e = next;
        }
        return e;
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public Set<K> keySet() {
        return super.keySet();
    }

    @Override
    public Collection<V> values() {
        return super.values();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return null;
    }



    static class Entry<K, V> implements Map.Entry<K, V> {

        final K key;
        V value;
        Entry<K, V> next;
        int hash;

        public Entry(int hash, K key, V value, Entry<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public final K getKey() {
            return key;
        }

        @Override
        public final V getValue() {
            return value;
        }

        @Override
        public final V setValue(V value) {
            V oldValue = value;
            this.value = value;
            return oldValue;
        }

        void recordAccess(HashMap17<K, V> map) {
        }

        void recordRemoval(HashMap17<K, V> map) {
        }


        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e = (Map.Entry) o;
            Object k1 = getKey();
            Object k2 = e.getKey();

            if (k1 == k2 || (k1 != null && k1.equals(k2))) {
                Object v1 = getValue();
                Object v2 = e.getValue();
                if (v1 == v2 || (v1 != null && v1.equals(v2))) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(getKey()) ^ Objects.hashCode(getValue());
        }

        @Override
        public String toString() {
            return getKey() + "=" + getValue();
        }
    }

}
