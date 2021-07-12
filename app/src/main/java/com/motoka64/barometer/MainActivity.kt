package com.motoka64.barometer

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.motoka64.barometer.database.BarometricData
import com.motoka64.barometer.logger.BarometricLogJobService
import com.motoka64.barometer.logger.PressureSensorLogger
import com.motoka64.barometer.viewmodel.BarometerViewModel
import com.motoka64.barometer.viewmodel.BarometerViewModelFactory
import com.motoka64.barometer.viewmodel.PressureListAdapter

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

        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        if (jobScheduler.getPendingJob(BAROMETRIC_JOB_ID) == null) {
            jobScheduler.schedule(barometricJob)
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        barometerViewModel.let {  }
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