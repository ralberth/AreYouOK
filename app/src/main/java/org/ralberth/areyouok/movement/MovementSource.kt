package org.ralberth.areyouok.movement

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MovementSource @Inject constructor(
    @ApplicationContext context: Context
) : SensorEventListener {
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
//    val callbacks: ArrayList<(Float, Float, Float) -> Unit> = arrayListOf()
//    var isListeningForUpdates: Boolean = false
    var lastObservation = RotationPosition()


    fun start() {
        println("MovementSource.start()")
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }


    fun stop() {
        println("MovementSource.stop()")
        sensorManager.unregisterListener(this)
    }


    fun position(): RotationPosition {
        println("MovementSource.get(): ${lastObservation.dump()}")
        return lastObservation
    }

//    fun listenForUpdates(callback: (Float, Float, Float) -> Unit) {
//        if (sensor != null) {
//            callbacks.add(callback)
//            if (!isListeningForUpdates) {
//                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
//                isListeningForUpdates = true
//            }
//        }
//    }
//
//
//    fun stopListening(callback: (x: Float, y: Float, z: Float) -> Unit) {
//        callbacks.remove(callback)
//        if (callbacks.isEmpty()) {
//            sensorManager.unregisterListener(this)
//            isListeningForUpdates = false
//        }
//    }


    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            lastObservation = RotationPosition.from(event.values)
            println("MovementSource.onSensorChanged(): ${lastObservation.dump()}")
//            callbacks.forEach { it(x, y, z) }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        println("MovementSource.onAccuracyChanged(): accuracy=$accuracy")
    }
}