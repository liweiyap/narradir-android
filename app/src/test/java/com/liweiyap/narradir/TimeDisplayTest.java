package com.liweiyap.narradir;

import org.junit.Test;

import static org.junit.Assert.*;

import com.liweiyap.narradir.util.TimeDisplay;

public class TimeDisplayTest
{
    @Test
    public void runTest()
    {
        assertEquals(TimeDisplay.fromMilliseconds(0), "00:00");
        assertEquals(TimeDisplay.fromMilliseconds(1000), "00:01");
        assertEquals(TimeDisplay.fromMilliseconds(2000), "00:02");
        assertEquals(TimeDisplay.fromMilliseconds(3000), "00:03");
        assertEquals(TimeDisplay.fromMilliseconds(4000), "00:04");
        assertEquals(TimeDisplay.fromMilliseconds(5000), "00:05");
        assertEquals(TimeDisplay.fromMilliseconds(6000), "00:06");
        assertEquals(TimeDisplay.fromMilliseconds(7000), "00:07");
        assertEquals(TimeDisplay.fromMilliseconds(8000), "00:08");
        assertEquals(TimeDisplay.fromMilliseconds(9000), "00:09");
        assertEquals(TimeDisplay.fromMilliseconds(10000), "00:10");
    }
}