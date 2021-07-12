package com.motoka64.barometer.view

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.motoka64.barometer.R
import com.motoka64.barometer.viewmodel.BarometerViewModel

class GraphFragment : Fragment() {
    private val barometerViewModel: BarometerViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.fragment_graph, container, false)

        val graph: LineChart = inflate.findViewById(R.id.line_chart)
        barometerViewModel.allData.observe(viewLifecycleOwner) { list ->
            val entryList = list.map { d -> Entry(d.unixTimestamp.toFloat(), d.pressure) }

            val lineDataSets = listOf(
                LineDataSet(entryList, "pressure").apply { color = Color.BLUE }
            )

            graph.apply{
                data = LineData(lineDataSets)
                invalidate()
            }
        }

        return inflate
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_graph, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_to_recycler -> {
                findNavController().navigate(R.id.recyclerFragment)
                return true
            }
        }
        return false
    }
}