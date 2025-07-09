package org.ralberth.areyouok.movement

data class RotationPosition(
    val x: Float = 0f,
    val y: Float = 0f,
    val z: Float = 0f
) {
    companion object {
        fun from(xyz: FloatArray): RotationPosition {
            return RotationPosition(xyz[0], xyz[1], xyz[2])
        }
    }

    fun dump(): String {
        return "x=$x, y=$y, z=$z"
    }
}
