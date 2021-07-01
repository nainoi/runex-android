package com.think.runex.feature.workout.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.think.runex.feature.workout.data.WorkingOutLocation

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: WorkingOutLocation): Long

    @Query("SELECT * FROM " + WorkingOutLocation.TABLE_NAME + " ORDER BY " + WorkingOutLocation.TIME)
    fun getLocations(): List<WorkingOutLocation>

    @Query("DELETE FROM " + WorkingOutLocation.TABLE_NAME)
    fun deleteAllLocation()

}