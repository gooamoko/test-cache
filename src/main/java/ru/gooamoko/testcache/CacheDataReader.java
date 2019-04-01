package ru.gooamoko.testcache;

/**
 * Interface for reading cache data callback
 *
 * @author Voronin Leonid
 * @since 01.04.19
 **/
public interface CacheDataReader<K, V> {

    /**
     * Reads data value by key value
     *
     * @param key key value
     * @return data value
     */
    V read(K key);
}
