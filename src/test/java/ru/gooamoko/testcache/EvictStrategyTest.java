package ru.gooamoko.testcache;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Class for testing {@code EvictStrategy}
 *
 * @author Voronin Leonid
 * @since 01.04.19
 **/
public class EvictStrategyTest {

    @Test
    public void testLfuMinEvictValueIs0() {
        EvictStrategy strategy = EvictStrategy.LFU;
        assertEquals(0, strategy.getMinEvictKey());
    }

    @Test
    public void testLfuMaxEvictValueIsMAX_VAL() {
        EvictStrategy strategy = EvictStrategy.LFU;
        assertEquals(Long.MAX_VALUE, strategy.getMaxEvictKey());
    }
}
