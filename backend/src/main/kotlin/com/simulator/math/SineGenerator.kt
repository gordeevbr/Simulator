package com.simulator.math

import org.springframework.stereotype.Component

@Component
class SineGenerator(
        private val amplitude: Double = 1.0,
        private val frequency: Double = 1.0,
        private val phaseShift: Double = 0.0
): Generator {

    override fun valueAt(millisecond: Long): Double =
            amplitude * Math.sin(2 * Math.PI * millisecond * frequency + phaseShift)
}