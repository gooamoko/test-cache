package ru.gooamoko.testcache;

import java.util.HashMap;
import java.util.Map;

/**
 * TestCache interface implementation
 *
 * @author Voronin Leonid
 * @since 01.04.19
 **/
public class TestCacheImpl<K, V> implements TestCache<K, V> {
    private static final int DEFAULT_CAPACITY = 5;
    private static final EvictStrategy DEFAULT_EVICT_STRATEGY = EvictStrategy.LFU;
    private static final String NULL_ERROR = "%s must be not null!";

    private HashMap<K, CacheEntry<V>> cacheMap = new HashMap<K, CacheEntry<V>>();
    private int capacity = DEFAULT_CAPACITY;
    private CacheDataReader<K, V> reader;
    private CacheDataWriter<K, V> writer;
    private EvictStrategy strategy = DEFAULT_EVICT_STRATEGY;


    public TestCacheImpl(CacheDataReader<K, V> reader, CacheDataWriter<K, V> writer, EvictStrategy strategy, int capacity) {
        checkNotNull(reader, "Reader");
        checkNotNull(writer, "Writer");
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0!");
        }
        this.writer = writer;
        this.capacity = capacity;
        this.reader = reader;
        this.strategy = strategy;
    }

    public TestCacheImpl(CacheDataReader<K, V> reader, CacheDataWriter<K, V> writer) {
        checkNotNull(reader, "Reader");
        checkNotNull(writer, "Writer");
        this.writer = writer;
        this.reader = reader;
    }

    public V get(K key) {
        checkNotNull(key, "Key");
        if (cacheMap.containsKey(key)) {
            CacheEntry<V> cacheEntry = cacheMap.get(key);
            cacheEntry.updateEvictKey(strategy);
            return cacheEntry.data;
        } else {
            V value = reader.read(key);
            if (isFull()) {
                evictOne();
            }
            CacheEntry<V> cacheEntry = new CacheEntry<V>(strategy.getMinEvictKey(), value);
            cacheMap.put(key, cacheEntry);
            return value;
        }
    }

    public void set(K key, V value) {
        checkNotNull(key, "Key");
        if (cacheMap.containsKey(key)) {
            CacheEntry<V> cacheEntry = cacheMap.get(key);
            cacheEntry.data = value;
            cacheEntry.updateEvictKey(strategy);
        } else {
            if (isFull()) {
                evictOne();
            }
            CacheEntry<V> cacheEntry = new CacheEntry<V>(strategy.getMinEvictKey(), value);
            cacheMap.put(key, cacheEntry);
        }
        writer.write(key, value);
    }

    public int size() {
        return cacheMap.size();
    }

    public boolean isFull() {
        return cacheMap.size() == capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public EvictStrategy getEvictStrategy() {
        return strategy;
    }

    public boolean containsKay(K key) {
        checkNotNull(key, "Key");
        return cacheMap.containsKey(key);
    }

    public void evictAll() {
        cacheMap.clear();
    }

    private void checkNotNull(Object o, String variableName) {
        if (o == null) {
            throw new IllegalArgumentException(String.format(NULL_ERROR, variableName));
        }
    }

    private void evictOne() {
        K key = getEvictKey();
        if (key != null) {
            cacheMap.remove(key);
        } else {
            throw new IllegalStateException("Wrong item key for evict!");
        }
    }

    private K getEvictKey() {
        long evictKey = strategy.getMaxEvictKey();
        K key = null;
        for (Map.Entry<K, CacheEntry<V>> mapEntry : cacheMap.entrySet()) {
            CacheEntry<V> cacheEntry = mapEntry.getValue();
            if (evictKey >= cacheEntry.evictKey) {
                evictKey = cacheEntry.evictKey;
                key = mapEntry.getKey();
                if (isLFU() && evictKey == strategy.getMinEvictKey()) {
                    return key;
                }
            }
        }
        return key;
    }

    private boolean isLFU() {
        return strategy.equals(EvictStrategy.LFU);
    }


    private class CacheEntry<V> {
        private long evictKey;
        private V data;

        CacheEntry(long evictKey, V data) {
            this.evictKey = evictKey;
            this.data = data;
        }

        void updateEvictKey(EvictStrategy strategy) {
            evictKey = strategy.getNextEvictKey(evictKey);
        }
    }
}
