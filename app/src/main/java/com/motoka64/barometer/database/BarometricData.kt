package com.motoka64.barometer.database

import androidx.room.*
import java.util.*

@Entity(tableName = "barometric_data")
@TypeConverters(BarometricData.FetchedMethodConverter::class)
data class BarometricData(
    @PrimaryKey @ColumnInfo(name = "unix_timestamp") val unixTimestamp: Int,
    val pressure: Float,
    @ColumnInfo(name = "methods") val fetchedMethod: FetchedMethod
) {
    fun getDate(): Date {
        return Date(unixTimestamp * 1000L)
    }

    companion object {
        fun fromFloat(
            pressure: Float,
            method: FetchedMethod = FetchedMethod.ActivityResume
        ): BarometricData {
            return BarometricData((System.currentTimeMillis() / 1000L).toInt(), pressure, method)
        }
    }

    enum class FetchedMethod(val value: Int) {
        ActivityResume(1), JobService(2)
    }

    class FetchedMethodConverter {
        @TypeConverter
        fun fromFetchedMethod(m: FetchedMethod): Int {
            return m.value
        }

        @TypeConverter
        fun toFetchedMethod(i: Int): FetchedMethod {
            val methods = FetchedMethod.values().filter { m -> m.value == i }
            if (methods.isNotEmpty()) return methods[0]
            throw IllegalArgumentException("There are no methods whose value is ${i}!")
        }
    }
}
