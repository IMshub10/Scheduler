package com.summer.scheduler.utils

import android.content.Context
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.ceil

object AndroidUtils {

    private var density = 1f

    fun dp(value: Float, context: Context): Int {
        if (density == 1f) {
            checkDisplaySize(context)
        }
        return if (value == 0f) {
            0
        } else ceil((density * value).toDouble()).toInt()
    }

    fun getDisplayWidth(context: Context): Int {
        var width = 0
        try {
            width = context.resources.displayMetrics.widthPixels
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return width
    }

    private fun checkDisplaySize(context: Context) {
        try {
            density = context.resources.displayMetrics.density
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}