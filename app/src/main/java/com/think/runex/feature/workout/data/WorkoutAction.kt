package com.think.runex.feature.workout.data

import androidx.annotation.IntDef

@IntDef(value = [WorkoutAction.START, WorkoutAction.PAUSE,
    WorkoutAction.RESUME, WorkoutAction.STOP, WorkoutAction.CLEAR])
annotation class WorkoutAction {
    companion object {
        const val START = 1
        const val PAUSE = 2
        const val RESUME = 3
        const val STOP = 4
        const val CLEAR = 5
    }
}