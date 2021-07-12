package com.motoka64.barometer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.motoka64.barometer.database.BarometricData
import com.motoka64.barometer.logger.PressureSensorLogger
import com.motoka64.barometer.viewmodel.BarometerViewModel
import com.motoka64.barometer.viewmodel.BarometerViewModelFactory
import com.motoka64.barometer.viewmodel.PressureListAdapter

class MainActivity : AppCompatActivity() {
    private val barometerViewModel: BarometerViewModel by viewModels {
        BarometerViewModelFactory((application as BarometerApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        val adapter = PressureListAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        barometerViewModel.allData.observe(this) { list ->
            adapter.submitList(list)
        }
    }

    override fun onResume() {
        super.onResume()
        val pressureSensorLogger = PressureSensorLogger(this)
        pressureSensorLogger.getPressure { pressure ->
            barometerViewModel.insert(BarometricData.fromFloat(pressure))
        }
    }
}