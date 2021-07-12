package com.motoka64.barometer.logger

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class PressureSensorLogger(context: Context) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

    fun getPressure(callback: (Float) -> Unit) {
        sensorManager.registerListener(
            object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    sensorManager.unregisterListener(this)
                    event?.let { e -> callback(e.values[0]) }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                    // Do nothing here.
                }

            }, pressureSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }
}