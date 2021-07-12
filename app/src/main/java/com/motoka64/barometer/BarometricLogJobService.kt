package com.motoka64.barometer

import android.app.job.JobParameters
import android.app.job.JobService

class BarometricLogJobService: JobService() {
    override fun onStartJob(params: JobParameters?): Boolean {
        jobFinished(params,true)
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {

        TODO("Not yet implemented")
    }
}