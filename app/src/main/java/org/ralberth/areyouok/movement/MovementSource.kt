package org.ralberth.areyouok.movement

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.DecimalFormat
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.sqrt


val df = DecimalFormat("###0.0")

@Singleton
class MovementSource @Inject constructor(
    @ApplicationContext context: Context
) : SensorEventListener {
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)  // TYPE_GYROSCOPE)
//    val callbacks: ArrayList<(Float, Float, Float) -> Unit> = arrayListOf()
//    var isListeningForUpdates: Boolean = false
    var nextObservation: Float = 0f


    fun start() {
        println("MovementSource.start()")
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
    }


    fun stop() {
        println("MovementSource.stop()")
        sensorManager.unregisterListener(this)
    }


    fun position(): Float {
        println("MovementSource.get(): ${df.format(nextObservation)}")
        val ret = nextObservation
        nextObservation = 0f
        return ret
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
            val (x, y, z) = event.values
            val magnitude = sqrt(x*x + y*y + z*z)
            if (magnitude > nextObservation)
                nextObservation = magnitude
//            println(
//                "MovementSource.onSensorChanged(): x=${df.format(x)}, y=${df.format(y)}, " +
//                "z=${df.format(z)}, mag=${df.format(magnitude)}, " +
//                "nextObservation=${df.format(nextObservation)}"
//            )
//            callbacks.forEach { it(x, y, z) }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        println("MovementSource.onAccuracyChanged(): accuracy=$accuracy")
    }
}