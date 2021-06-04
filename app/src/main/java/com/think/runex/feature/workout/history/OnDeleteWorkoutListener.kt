package com.think.runex.feature.workout.history

import com.think.runex.feature.workout.data.WorkoutInfo

interface OnDeleteWorkoutListener {
    fun onDeleteWorkout(
        month: Int,
        year: Int,
        monthPosition: Int,
        dayPosition: Int,
        workoutInfo: WorkoutInfo
    )
}