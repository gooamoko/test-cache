package ru.gooamoko.testcache;

/**
 * Test task cache API interface.
 * <p>
 * K means data type for key
 * V means data type for value
 *
 * @author Voronin Leonid
 * @since 01.04.19
 **/
public interface TestCache<K, V> {

    /**
     * Get data from cache by the key value
     * if key doesn't contains in cache, then will be attempt to read data in cache.
     *
     * @param key key value. Must be not null
     * @return data from cache. Data may be null
     */
    V get(K key);

    /**
     * Set (or update) data in cache and writes it by the CacheDataWriter
     *
     * @param key   key value. Must be not null
     * @param value data value. May be null
     */
    void set(K key, V value);

    /**
     * Get current size if cache.
     *
     * @return current cache size
     */
    int size();

    /**
     * Check cache is full, i.e. cache size == capacity
     *
     * @return {@code true} if cache size == capacity and {@code false} otherwise
     */
    boolean isFull();

    /**
     * Get current cache capacity
     *
     * @return current capacity
     */
    int getCapacity();

    /**
     * Get current evict strategy
     *
     * @return cache evict stratery
     */
    EvictStrategy getEvictStrategy();

    /**
     * Check cache is contains data with specified kay value
     *
     * @param key key value
     * @return {@code true} if cache contains entry with this key and {@code false} otherwise
     */
    boolean containsKay(K key);

    /**
     * Evicts all cache entries
     */
    void evictAll();
}
