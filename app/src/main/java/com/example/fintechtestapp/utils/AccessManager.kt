package com.example.fintechtestapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.fintechtestapp.data.local.UserEntity
import com.example.fintechtestapp.data.model.User
import com.google.gson.Gson
import java.time.Duration
import java.time.Instant
import kotlin.math.max

class AccessManager {
    @RequiresApi(Build.VERSION_CODES.O)
    fun isCoolingPeriod(user: UserEntity?): Boolean {
        user ?: return false
        return try {
            val now = Instant.now()
            val endTime = Instant.parse(user.coolingEndTime)
            now.isBefore(endTime)
        } catch (e: Exception) {
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRemainingCoolingTime(user: UserEntity?): String {
        user ?: return "Cooling ends in 00:00"
        return try {
            val now = Instant.now()
            val endTime = Instant.parse(user.coolingEndTime)
            val duration = Duration.between(now, endTime)
            if (duration.isNegative) return "Cooling ends in 00:00"
            val minutes = duration.toMinutes()
            val seconds = duration.seconds % 60
            String.format("Cooling ends in %02d:%02d", minutes, seconds)
        } catch (e: Exception) {
            "Cooling ends in 00:00"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isModuleAccessible(user: UserEntity?, moduleId: String): Boolean {
        user ?: return false
        if (isCoolingPeriod(user)) return false
        val accessibleModules = user.accessibleModules.split(",")
        return accessibleModules.contains(moduleId)
    }
}