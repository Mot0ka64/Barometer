package com.motoka64.barometer

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.motoka64.barometer.database.BarometricData
import com.motoka64.barometer.logger.BarometricLogJobService
import com.motoka64.barometer.logger.PressureSensorLogger
import com.motoka64.barometer.viewmodel.BarometerViewModel
import com.motoka64.barometer.viewmodel.BarometerViewModelFactory

class MainActivity : AppCompatActivity() {
    private val barometricJob by lazy {
        JobInfo.Builder(
            BAROMETRIC_JOB_ID,
            ComponentName(this, BarometricLogJobService::class.java)
        )
            .setPersisted(true)
            .setPeriodic(JobInfo.getMinPeriodMillis(), JobInfo.getMinFlexMillis())
            .build()
    }

    private val barometerViewModel: BarometerViewModel by viewModels {
        BarometerViewModelFactory((application as BarometerApplication).repository)
    }

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            if (jobScheduler.getPendingJob(BAROMETRIC_JOB_ID) == null) {
                jobScheduler.schedule(barometricJob)
            }

            val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
            if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
                AlertDialog.Builder(this)
                    .setTitle(R.string.alert_whitelist_title)
                    .setMessage(R.string.alert_whitelist_text)
                    .setPositiveButton(R.string.ok){ _, _ ->
                        val intent = Intent()
                        intent.action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
                        startActivity(intent)
                    }
                    .create()
                    .show()
            }

            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.main_nav_host) as NavHostFragment
            val navController = navHostFragment.navController
            appBarConfiguration = AppBarConfiguration(navController.graph)
            setupActionBarWithNavController(navController, appBarConfiguration)
            barometerViewModel.let { }
        }
    }

    override fun onResume() {
        super.onResume()
        val pressureSensorLogger = PressureSensorLogger(this)
        pressureSensorLogger.getPressure { pressure ->
            barometerViewModel.insert(BarometricData.fromFloat(pressure))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.main_nav_host)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    companion object {
        private const val BAROMETRIC_JOB_ID = 1
    }
}