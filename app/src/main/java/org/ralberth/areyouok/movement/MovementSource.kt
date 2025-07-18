package org.ralberth.areyouok.movement

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.DecimalFormat
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAmount
import java.util.LinkedList
import java.util.stream.Collectors
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.sqrt


/*
 * This provides the last 5 seconds-worth of movement data about the phone in 1/4 second blocks.
 * I.e., 20 observations, each spaced 250 milliseconds apart.  Within each 1/2 second span of time,
 * this picks the largest movement to record as the "high water mark" for that 1/4 second.
 *
 * In an effort to be as simple as possible for consumers, this doesn't provide any start() or
 * stop() methods to start and stop observing the accelerometer sensor.  It's too easy to forget to
 * call stop(), or accidentally let something go out of scope that should have called stop().  This
 * class uses a separate thread to pay attention to the last time anything wanted to get a value.
 * Once other classes stop calling methods on this class for 2 seconds, it stops listening for
 * sensor changes.  This keeps battery life conserved.  Anytime an outside part of the application
 * calls this, it turns the observations back on.
 *
 * The public methods on this class are thread-safe, since there is a background thread that
 * listens for sensor events and updates the array of data provide to callers of this class.
 * The entire class is a Monitor: an object (@Singleton) that self-protects all method calls with
 * a Critical Section guarded by Java-native synchronization object.  Feel free to call the methods
 * on this class from anywhere and from any other thread.
 *
 * ----------------------------------------------------------------------------------------------
 *
 * Internal Algorithm:
 *    1. All methods are synchronized on "monitor" that might touch object-local state.
 *    2. The local thread does all the listening, logic, and any other work.  Nothing is
 *       done on the calling thread.
 *    3. Event Listener:
 *       The entire object is a SensorStateListener.  When listening, the sensors call the callbacks
 *       on ourself with new values for the sensor.  We update the high-water-mark local variable.
 *       Every 250ms we store the high-water mark variable into the array of values and reset the
 *       HWM back to zero.
 *    4. Thread Start:
 *       The thread is started anytime getHistory() is called and the thread isn't currently
 *       running.  When started, this object starts listening for sensor updates.
 *    5. Thread Stop:
 *       The thread sleeps for 250ms, does work, then returns to sleeping (in a loop).  Every 250ms
 *       it checks to see the last time someone called getHistory().  If it was more than 3 seconds
 *       ago, the thread stops listening for sensor events and exits.
 */
@Singleton
class MovementSource @Inject constructor(
    @ApplicationContext context: Context
) : SensorEventListener, Runnable {

    companion object {
        const val SLICE_MS = 250L
        const val CUTOFF = 6L  // seconds
        val df = DecimalFormat("0.0")
    }


    private val monitor = Any()      // MONITOR object that makes everything thread-safe

    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

    private var lastObserved = Instant.now()         // just a seed value
    private val history: MutableList<Float> = LinkedList()  // last 5 seconds of observations
    private var worker: Thread? = null               // listens for sensor updates, maintains state

    private var observationHWM: Float = 0f           // highest value within this 250ms block of time
    private var observationMonitor = Any()           // separate monitor, HWM updated frequently


    fun getHistory(): List<Float> {
        if (sensor == null)
            return ArrayList()     // Edge case where we don't have a sensor

        synchronized(monitor) {
            lastObserved = Instant.now()       // so thread knows when to quit
            if (worker == null || ! worker!!.isAlive) {
                println("MovementSource.getHistory() => [], and start the background thread")
                history.clear()
                worker = Thread(this)
                worker!!.start()   // synchronized above protects us from having worker change after line above
                return ArrayList()
            } else {
                val listStr = history.stream()
                    .map { df.format(it) }
                    .collect(Collectors.toList())
                    .joinToString()
                println("MovementSource.getHistory() => $listStr")
                return ArrayList<Float>(history)  // make a copy of the CS-protected actual array
            }
        }
    }


    override fun run() = try {
        println("MovementSource.run(): starting the background thread")
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        var shouldKeepRunning = true
        while (shouldKeepRunning) {
            Thread.sleep(SLICE_MS)

            var lastObs = 0f
            synchronized(observationMonitor) {
                lastObs = observationHWM
                observationHWM = 0f
            }

            synchronized(monitor) {
                history.add(lastObs)
                if (history.size > 15)
                    history.removeAt(0)  // FIXME: remove size - 15 entries
                shouldKeepRunning = lastObservedWithinCutoff()
            }
        }
        println("MovementSource.run(): no getHistory() in ${CUTOFF}s, stopping background thread.")
    }
    finally {
        println("MovementSource.run(): stop listening for sensor updates")
        sensorManager.unregisterListener(this)
    }


    /*
     * NON-THREAD-SAFE helper method to keep the WHILE loop above simpler.
     * Should we CONTINUE the while loop in the Thread run() above?
     */
    private fun lastObservedWithinCutoff(): Boolean {
        val now = Instant.now()
        val r: TemporalAmount
        val threshold = lastObserved.plus(CUTOFF, ChronoUnit.SECONDS)
        return now.isBefore(threshold)
    }


    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            val (x, y, z) = event.values
            val magnitude = sqrt(x*x + y*y + z*z)
            synchronized(observationMonitor) {
                if (magnitude > observationHWM)
                    observationHWM = magnitude
            }
        }
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
//        println("MovementSource.onAccuracyChanged()")
    }
}
