package com.think.runex.common

import com.jozzee.android.core.utility.FragmentAnimations
import com.think.runex.R

fun fadeIn() = FragmentAnimations(enter = R.anim.fade_in, exit = R.anim.fade_out,
        popEnter = R.anim.fade_in, popExit = R.anim.fade_out)

fun fadeOut() = FragmentAnimations(enter = R.anim.fade_out, exit = R.anim.fade_in,
        popEnter = R.anim.fade_out, popExit = R.anim.fade_in)
