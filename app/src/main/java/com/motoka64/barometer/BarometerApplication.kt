package com.motoka64.barometer

import android.app.Application
import com.motoka64.barometer.database.BarometricDatabase
import com.motoka64.barometer.database.BarometricRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class BarometerApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    private val barometricDatabase by lazy { BarometricDatabase.getDatabase(applicationContext) }
    val repository by lazy { BarometricRepository(barometricDatabase.barometricDataDao()) }
}