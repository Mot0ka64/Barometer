package com.motoka64.barometer.database

import androidx.room.*
import java.util.*

@Entity(tableName = "barometric_data")
data class BarometricData(
    @PrimaryKey @ColumnInfo(name = "unix_timestamp") val unixTimestamp: Int,
    val pressure: Float
) {
    fun getDate(): Date {
        return Date(unixTimestamp * 1000L)
    }

    companion object {
        fun fromFloat(pressure: Float): BarometricData {
            return BarometricData((System.currentTimeMillis() / 1000L).toInt(), pressure)
        }
    }
}
