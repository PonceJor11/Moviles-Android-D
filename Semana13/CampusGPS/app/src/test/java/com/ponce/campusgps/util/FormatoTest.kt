package com.ponce.campusgps.util

import org.junit.Assert.assertEquals
import org.junit.Test

class FormatoTest {

    @Test
    fun `menos de mil metros se muestra en metros enteros`() {
        assertEquals("350 m", formatearDistancia(350f))
        assertEquals("88 m", formatearDistancia(88.4f))
        assertEquals("0 m", formatearDistancia(0f))
    }

    @Test
    fun `desde mil metros se muestra en kilometros con un decimal`() {
        assertEquals("1.0 km", formatearDistancia(1000f))
        assertEquals("1.5 km", formatearDistancia(1500f))
        assertEquals("12.3 km", formatearDistancia(12340f))
    }

    @Test
    fun `el redondeo no produce 1000 m`() {
        assertEquals("999 m", formatearDistancia(999.4f))
        assertEquals("1.0 km", formatearDistancia(999.6f))
    }
}
