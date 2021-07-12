package com.motoka64.barometer.database

import androidx.annotation.WorkerThread

class BarometricRepository(private val barometricDataDao: BarometricDataDao) {
    val allData = barometricDataDao.queryAll()

    @WorkerThread
    suspend fun insert(data: BarometricData) {
        barometricDataDao.insert(data)
    }
}