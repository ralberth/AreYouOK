package org.ralberth.areyouok.movement

import kotlin.math.sqrt

data class RotationPosition(
    val x: Float = 0f,
    val y: Float = 0f,
    val z: Float = 0f
) {
    companion object {
        fun from(xyz: FloatArray): RotationPosition {
            return RotationPosition(
                xyz[0].coerceIn(-10f, 10f),
                xyz[1].coerceIn(-10f, 10f),
                xyz[2].coerceIn(-10f, 10f)
            )
        }
    }

    fun magnitude(): Float {
        return sqrt(x*x + y*y + z*z)
    }


    fun dump(): String {
        return "x=$x, y=$y, z=$z"
    }
}
