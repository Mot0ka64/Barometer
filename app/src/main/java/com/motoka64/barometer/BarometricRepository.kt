package com.motoka64.barometer

import androidx.annotation.WorkerThread
import com.motoka64.barometer.database.BarometricData
import com.motoka64.barometer.database.BarometricDataDao

class BarometricRepository(private val barometricDataDao: BarometricDataDao) {
    val allData = barometricDataDao.queryAll()

    @WorkerThread
    suspend fun insert(data: BarometricData) {
        barometricDataDao.insert(data)
    }
}