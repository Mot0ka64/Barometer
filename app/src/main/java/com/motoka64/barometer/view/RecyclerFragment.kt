package com.motoka64.barometer.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.motoka64.barometer.R
import com.motoka64.barometer.viewmodel.BarometerViewModel
import com.motoka64.barometer.viewmodel.PressureListAdapter


class RecyclerFragment : Fragment() {
    private val barometerViewModel: BarometerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.fragment_recycler, container, false)
        val recyclerView: RecyclerView = inflate.findViewById(R.id.recycler_view)
        val adapter = PressureListAdapter()
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))

        barometerViewModel.allData.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

        return inflate
    }
}