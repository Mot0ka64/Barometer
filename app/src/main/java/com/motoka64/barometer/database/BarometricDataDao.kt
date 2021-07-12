package com.motoka64.barometer.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BarometricDataDao {
    @Query("SELECT * FROM barometric_data ORDER BY unix_timestamp ASC")
    fun queryAll(): Flow<List<BarometricData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: BarometricData)
}