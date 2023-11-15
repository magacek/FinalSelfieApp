package com.example.finalselfieapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.finalselfieapp.ShakeDetector
/**
 * MainActivity serves as the primary activity of the application, managing fragment transitions
 * and the sensor for shake detection. It initializes the app's user interface and orchestrates
 * the navigation between different fragments based on user interaction and sensor events.
 *
 * @see AppCompatActivity for the basic application support.
 * @see SensorManager and ShakeDetector for handling shake gestures.
 * @see Fragment for managing various screens within the app.
 *
 * @author Matt Gacek
 */


class MainActivity : AppCompatActivity() {

    private lateinit var sensorManager: SensorManager
    private var shakeDetector: ShakeDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        shakeDetector = ShakeDetector {
            // Open Camera Fragment
            replaceFragment(CameraFragment())
        }
        if (savedInstanceState == null) {
            replaceFragment(LoginRegisterFragment())
        }
    }
    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            shakeDetector,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_UI
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(shakeDetector)
    }
}
