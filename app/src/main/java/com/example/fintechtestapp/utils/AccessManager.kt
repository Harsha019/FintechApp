package com.example.fintechtestapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.fintechtestapp.data.local.UserEntity
import com.example.fintechtestapp.data.model.Module
import com.example.fintechtestapp.data.model.User
import com.google.gson.Gson
import java.time.Duration
import java.time.Instant
import kotlin.math.max

class AccessManager {

    @RequiresApi(Build.VERSION_CODES.O)
    fun isCoolingPeriod(user: User?): Boolean {
        if (user == null) return false
        val now = Instant.now()
        val end = Instant.parse(user.coolingEndTime)
        return now.isBefore(end)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRemainingCoolingTime(user: User?): String {
        if (user == null) return ""
        val now = Instant.now()
        val end = Instant.parse(user.coolingEndTime)
        val duration = Duration.between(now, end)
        val minutes = duration.toMinutes()
        val seconds = duration.seconds % 60
        return String.format("Cooling ends in %02d:%02d", minutes, seconds)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun canAccess(user: User?, module: Module): Boolean {
        if (user == null) return false
        if (isCoolingPeriod(user)) return false
        return user.accessibleModules.contains(module.id)
    }
}
