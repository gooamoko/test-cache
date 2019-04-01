package ru.gooamoko.testcache;

/**
 * Evist strategy for test task cache
 *
 * @author Voronin Leonid
 * @since 01.04.19
 **/
public enum EvictStrategy {
    LRU, LFU;

    long getMaxEvictKey() {
        if (this == EvictStrategy.LRU) {
            return System.currentTimeMillis();
        }
        return Long.MAX_VALUE;
    }

    long getMinEvictKey() {
        if (this == EvictStrategy.LRU) {
            return System.currentTimeMillis();
        }
        return 0;
    }

    long getNextKey(long oldKeyValue) {
        if (this == EvictStrategy.LRU) {
            return System.currentTimeMillis();
        }
        return oldKeyValue + 1;
    }
}
