package com.motoka64.barometer.database

import androidx.room.*

@Entity(tableName = "barometric_data")
data class BarometricData(
    @PrimaryKey @ColumnInfo(name = "unix_timestamp") val unixTimestamp: Int,
    val pressure: Float
)
