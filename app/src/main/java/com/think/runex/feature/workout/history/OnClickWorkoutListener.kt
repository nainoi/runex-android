package com.think.runex.feature.workout.history

import com.think.runex.feature.workout.data.WorkoutInfo

interface OnClickWorkoutListener {
    fun onClickWorkout(workoutInfo: WorkoutInfo)
}