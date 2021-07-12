package com.motoka64.barometer.logger

import android.app.job.JobParameters
import android.app.job.JobService

class BarometricLogJobService: JobService() {
    override fun onStartJob(params: JobParameters?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        jobFinished(params,true)
        return true
    }
}