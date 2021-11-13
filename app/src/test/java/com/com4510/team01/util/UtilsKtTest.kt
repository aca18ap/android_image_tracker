package com.com4510.team01.util

import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

class UtilsKtTest : TestCase() {
    var a : Int = 0

    @Before
    fun setup()
    {
        a = 5
        print("Before all tests: a")
    }

    @Test
    fun testConvertToImageDataWithoutId() {

        a = a + 1
        assertEquals(6,a)

    }

    @Test
    fun testAppend() {

        a = a + 2
        assertEquals(7,a)
    }
}