package com.motoka64.barometer.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.motoka64.barometer.R
import com.motoka64.barometer.database.BarometricData

class PressureListAdapter :
    ListAdapter<BarometricData, PressureListAdapter.PressureViewHolder>(DIFF) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PressureViewHolder {
        return PressureViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: PressureViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    class PressureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val timestamp: TextView = itemView.findViewById(R.id.pressure_timestamp)
        private val pressure: TextView = itemView.findViewById(R.id.pressure_value)
        private val methods: TextView = itemView.findViewById(R.id.pressure_methods)

        fun bind(data: BarometricData) {
            timestamp.text = data.getDate().toString()
            pressure.text =
                itemView.context.getString(R.string.pressure_format_string).format(data.pressure)
            methods.text = data.fetchedMethod.toString()
        }

        companion object {
            fun create(parent: ViewGroup): PressureViewHolder {
                return PressureViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.recycler_pressure_item, parent, false)
                )
            }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<BarometricData>() {
            override fun areItemsTheSame(
                oldItem: BarometricData,
                newItem: BarometricData
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: BarometricData,
                newItem: BarometricData
            ): Boolean {
                return oldItem.unixTimestamp == newItem.unixTimestamp
            }

        }
    }
}