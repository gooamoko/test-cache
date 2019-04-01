package ru.gooamoko.testcache;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

/**
 * Class for test {@code TestCacheImpl}
 *
 * @author Voronin Leonid
 * @since 01.04.19
 **/
public class TestCacheImplTest {
    private CacheDataReader mockedReader;
    private CacheDataWriter mockedWriter;
    private CacheDataReader<Integer, String> defaultReader;

    private TestCache<Integer, String> cache;

    @Before
    public void setup() {
        mockedReader = mock(CacheDataReader.class);
        mockedWriter = mock(CacheDataWriter.class);
        defaultReader = new CacheDataReader<Integer, String>() {
            public String read(Integer key) {
                return String.valueOf(key);
            }
        };
    }

    @Test
    public void testDefaultStrategyIsLFU() {
        cache = new TestCacheImpl<Integer, String>(mockedReader, mockedWriter);
        assertEquals(EvictStrategy.LFU, cache.getEvictStrategy());
    }

    @Test
    public void testDefaultCapacityIs5() {
        cache = new TestCacheImpl<Integer, String>(mockedReader, mockedWriter);
        assertEquals(5, cache.getCapacity());
    }

    @Test
    public void testLruMapWillEvictEntryWithKey1() throws InterruptedException {
        int capacity = 5;
        cache = new TestCacheImpl<Integer, String>(defaultReader, mockedWriter, EvictStrategy.LRU, capacity-1);
        for (int i = 1; i <= capacity; i++) {
            cache.get(i);
            Thread.sleep(100);
        }
        assertFalse(cache.containsKay(1));
    }

    @Test
    public void testLfuMapWillEvictEntryWithKey1() {
        int capacity = 5;
        cache = new TestCacheImpl<Integer, String>(defaultReader, mockedWriter, EvictStrategy.LFU, capacity-1);
        // initial cache fill
        for (int i = 1; i < capacity; i++) {
            cache.get(i);
        }
        // access alements with keys more than 1 to make key 1 less used
        for (int i = 2; i <= capacity; i++) {
            cache.get(i);
        }
        assertFalse(cache.containsKay(1));
    }

    @Test
    public void testReaderCallsOnlyObceAndWriterIsNotCalled() {
        int capacity = 5;
        int key = 42;
        cache = new TestCacheImpl<Integer, String>(mockedReader, mockedWriter, EvictStrategy.LFU, capacity);
        for (int i = 0; i < capacity; i++) {
            cache.get(key);
        }
        verify(mockedReader, times(1)).read(key);
        verify(mockedWriter, times(0)).write(anyInt(), anyString());
    }

    @Test
    public void testWriterCallsEveryTimeWhenSetIsCalled() {
        int capacity = 5;
        int key = 42;
        String value = "test value";
        cache = new TestCacheImpl<Integer, String>(mockedReader, mockedWriter, EvictStrategy.LFU, capacity);
        for (int i = 0; i < capacity; i++) {
            cache.set(key, value);
        }
        verify(mockedWriter, times(capacity)).write(key, value);
    }
}