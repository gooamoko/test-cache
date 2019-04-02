package ru.gooamoko.testcache;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Class for testing {@code EvictStrategy}
 *
 * @author Voronin Leonid
 * @since 01.04.19
 **/
public class EvictStrategyTest {
    private static final int SYSTEM_TIME_DELTA = 3; // Delta for System.currentTimeMillis(). Just in case
    private static final int LFU_MIN_VALUE = 0; // Delta for System.currentTimeMillis(). Just in case

    @Test
    public void testLfuMinEvictValueIs0() {
        EvictStrategy strategy = EvictStrategy.LFU;
        assertEquals(LFU_MIN_VALUE, strategy.getMinEvictKey());
    }

    @Test
    public void testLfuMaxEvictValueIsMAX_VAL() {
        EvictStrategy strategy = EvictStrategy.LFU;
        assertEquals(Long.MAX_VALUE, strategy.getMaxEvictKey());
    }

    @Test
    public void testLfuNextEvictValueDoesntChangeIfItsMAX_VAL() {
        EvictStrategy strategy = EvictStrategy.LFU;
        assertEquals(Long.MAX_VALUE, strategy.getNextEvictKey(Long.MAX_VALUE));
    }

    @Test
    public void testLfuNextEvictValueIncrementsIfIsLessThanMAX_VAL() {
        EvictStrategy strategy = EvictStrategy.LFU;
        assertEquals(Long.MAX_VALUE, strategy.getNextEvictKey(Long.MAX_VALUE - 1));
        assertEquals(LFU_MIN_VALUE + 1, strategy.getNextEvictKey(LFU_MIN_VALUE));
    }

    @Test
    public void testLruMaxEvictValueIsMAX_VAL() {
        EvictStrategy strategy = EvictStrategy.LRU;
        assertEquals(Long.MAX_VALUE, strategy.getMaxEvictKey());
    }

    @Test
    public void testLruMinEvictValueIsAboutCurrentMillis() {
        EvictStrategy strategy = EvictStrategy.LRU;
        assertTrue(Math.abs(strategy.getMinEvictKey() - System.currentTimeMillis()) < SYSTEM_TIME_DELTA);
    }

    @Test
    public void testLruNextEvictValueIsAboutCurrentMillis() {
        EvictStrategy strategy = EvictStrategy.LRU;
        assertTrue(Math.abs(strategy.getMinEvictKey() - System.currentTimeMillis()) < SYSTEM_TIME_DELTA);
    }
}
