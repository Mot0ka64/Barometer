package com.motoka64.barometer.database

import android.content.Context
import androidx.room.*

@Database(entities = [BarometricData::class], version = 1)
abstract class BarometricDatabase : RoomDatabase() {
    abstract fun barometricDataDao(): BarometricDataDao

    companion object {
        @Volatile
        private var INSTANCE: BarometricDatabase? = null

        fun getDatabase(context: Context): BarometricDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BarometricDatabase::class.java,
                    "barometric_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}