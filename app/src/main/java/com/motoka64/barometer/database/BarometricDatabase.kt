package com.motoka64.barometer.database

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [BarometricData::class], version = 2)
abstract class BarometricDatabase : RoomDatabase() {
    abstract fun barometricDataDao(): BarometricDataDao

    companion object {
        @Volatile
        private var INSTANCE: BarometricDatabase? = null

        private val MIGRATE1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE new_barometric_data (
                        unix_timestamp INTEGER NOT NULL,
                        pressure REAL NOT NULL,
                        methods Integer NOT NULL,
                        PRIMARY KEY(`unix_timestamp`)
                    )
                """.trimIndent())
                database.execSQL("""
                    INSERT INTO new_barometric_data (unix_timestamp, pressure, methods)
                    SELECT *, 1 as methods FROM barometric_data
                """.trimIndent())
                database.execSQL("DROP TABLE barometric_data")
                database.execSQL("ALTER TABLE new_barometric_data RENAME TO barometric_data")
            }

        }

        fun getDatabase(context: Context): BarometricDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BarometricDatabase::class.java,
                    "barometric_database"
                ).addMigrations(MIGRATE1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}