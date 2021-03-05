package com.think.runex.feature.workout.data

import androidx.annotation.IntDef

@IntDef(value = [WorkoutStatus.UNKNOWN, WorkoutStatus.READY, WorkoutStatus.WORKING_OUT,
    WorkoutStatus.PAUSE, WorkoutStatus.STOP])
annotation class WorkoutStatus {
    companion object {

        const val UNKNOWN = -1

        /**
         * First status when open workout screen it mean
         * ready to start working out
         */
        const val READY = 0

        /**
         * On working out such as running
         */
        const val WORKING_OUT = 1

        const val PAUSE = 2
        const val STOP = 3
        //const val FINISH = 4

        fun statusText(@WorkoutStatus status: Int): String = when (status) {
            0 -> "Ready"
            1 -> "Working Out"
            2 -> "Pause"
            3 -> "Stop"
            else -> "Unknown"
        }
    }
}
