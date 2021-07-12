package com.motoka64.barometer

import android.app.Application
import com.motoka64.barometer.database.BarometricDatabase
import com.motoka64.barometer.database.BarometricRepository

class BarometerApplication : Application() {
    private val barometricDatabase by lazy { BarometricDatabase.getDatabase(applicationContext) }
    val repository by lazy { BarometricRepository(barometricDatabase.barometricDataDao()) }
}