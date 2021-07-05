package com.think.runex.feature.workout.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.think.runex.feature.workout.data.WorkingOutLocation

@Database(
    entities = [WorkingOutLocation::class],
    version = 1,
    exportSchema = false
)
abstract class WorkoutDataBase : RoomDatabase() {

    abstract fun locationDao(): LocationDao

    companion object {

        @Volatile
        private var INSTANCE: WorkoutDataBase? = null

        fun getDatabase(context: Context): WorkoutDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorkoutDataBase::class.java,
                    "workout_location_database"
                ).fallbackToDestructiveMigration()
                    //.allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                return instance
            }
        }

        fun destroy() {
            INSTANCE?.close()
            INSTANCE = null
        }
    }
}