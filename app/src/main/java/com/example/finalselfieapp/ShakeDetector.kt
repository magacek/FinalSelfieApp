package com.example.finalselfieapp

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt
/**
 * ShakeDetector implements the SensorEventListener interface to detect shake motion
 * of the device. It is used in MainActivity to trigger actions, such as opening
 * the camera, upon detecting a shake. It calculates the acceleration changes to
 * determine if a shake gesture has occurred.
 *
 * @see SensorEventListener for receiving notifications from the SensorManager.
 * @see SensorManager for accessing the device's sensors.
 * @see MainActivity for invoking actions based on shake detection.
 *
 * @author Matt Gacek
 */

class ShakeDetector(private val onShake: () -> Unit) : SensorEventListener {

    private var acceleration = 0f
    private var currentAcceleration = SensorManager.GRAVITY_EARTH
    private var lastAcceleration = SensorManager.GRAVITY_EARTH

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Not used
    }

    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        lastAcceleration = currentAcceleration
        currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
        val delta = currentAcceleration - lastAcceleration
        acceleration = acceleration * 0.9f + delta

        if (acceleration > 12) { // Shake threshold
            onShake()
        }
    }
}
