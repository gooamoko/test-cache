package ru.gooamoko.testcache;

/**
 * Interface for Writing cache entry data callback
 *
 * @author Voronin Leonid
 * @since 01.04.19
 **/
public interface CacheDataWriter<K, V> {

    /**
     * Writes entry with specified key and value
     *
     * @param key   key value
     * @param value data value
     */
    void write(K key, V value);
}
