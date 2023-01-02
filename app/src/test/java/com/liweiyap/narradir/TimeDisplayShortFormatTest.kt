package com.liweiyap.narradir

import com.liweiyap.narradir.util.TimeDisplay

import org.junit.Assert
import org.junit.Test

class TimeDisplayShortFormatTest {
    @Test
    fun runTest() {
        Assert.assertEquals(TimeDisplay.shortFormat(msec = 0), "00:00")
        Assert.assertEquals(TimeDisplay.shortFormat(msec = 1000), "00:01")
        Assert.assertEquals(TimeDisplay.shortFormat(msec = 2000), "00:02")
        Assert.assertEquals(TimeDisplay.shortFormat(msec = 3000), "00:03")
        Assert.assertEquals(TimeDisplay.shortFormat(msec = 4000), "00:04")
        Assert.assertEquals(TimeDisplay.shortFormat(msec = 5000), "00:05")
        Assert.assertEquals(TimeDisplay.shortFormat(msec = 6000), "00:06")
        Assert.assertEquals(TimeDisplay.shortFormat(msec = 7000), "00:07")
        Assert.assertEquals(TimeDisplay.shortFormat(msec = 8000), "00:08")
        Assert.assertEquals(TimeDisplay.shortFormat(msec = 9000), "00:09")
        Assert.assertEquals(TimeDisplay.shortFormat(msec = 10000), "00:10")
    }
}