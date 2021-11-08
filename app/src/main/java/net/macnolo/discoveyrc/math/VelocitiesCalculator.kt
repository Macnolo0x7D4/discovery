package net.macnolo.discoveyrc.math

import androidx.core.math.MathUtils.clamp
import kotlin.math.PI
import kotlin.math.sin
import kotlin.math.sqrt

object VelocitiesCalculator {
    private fun calcA(angle:Double):Double {
        return sqrt(2.0) * sin(angle + PI/4)
    }

    private fun calcB(angle:Double):Double {
        return sqrt(2.0) * sin(angle - PI/4)
    }

    fun calculateVelocities(angle:Double, strength:Double):IntArray {
        val velocities = IntArray(2)
        velocities[0] = (clamp(calcA(angle), -1.0, 1.0) * strength * 255).toInt()
        velocities[1] = (clamp(calcB(angle), -1.0, 1.0) * strength * 255).toInt()
        return velocities
    }
}