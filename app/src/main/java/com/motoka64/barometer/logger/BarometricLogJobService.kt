package com.motoka64.barometer.logger

import android.app.job.JobParameters
import android.app.job.JobService
import com.motoka64.barometer.BarometerApplication
import com.motoka64.barometer.database.BarometricData
import kotlinx.coroutines.launch

class BarometricLogJobService : JobService() {
    override fun onStartJob(params: JobParameters?): Boolean {
        val barometerApplication = application as BarometerApplication
        PressureSensorLogger(applicationContext).getPressure { p ->
            barometerApplication.applicationScope.launch {
                val repository = barometerApplication.repository
                repository.insert(
                    BarometricData.fromFloat(
                        p,
                        BarometricData.FetchedMethod.JobService
                    )
                )
                jobFinished(params, true)
            }
        }

        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        jobFinished(params, true)
        return true
    }
}