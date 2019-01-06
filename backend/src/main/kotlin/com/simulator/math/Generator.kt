package com.simulator.math

interface Generator {
    fun valueAt(millisecond: Long): Double
}