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
        if (user == null) return false
        val now = Instant.now()
        val end = Instant.parse(user.coolingEndTime)
        return now.isBefore(end)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getRemainingCoolingTime(user: UserEntity?): String {
        if (user == null) return ""
        val now = Instant.now()
        val end = Instant.parse(user.coolingEndTime)
        val duration = Duration.between(now, end)
        return if (duration.isNegative) "" else {
            val minutes = duration.toMinutes()
            val seconds = duration.seconds % 60
            "Cooling ends in %02d:%02d".format(minutes, seconds)
        }
    }

    fun isModuleAccessible(user: UserEntity?, moduleId: String): Boolean {
        if (user == null) return false
        val accessibleModules = Gson().fromJson(user.accessibleModules, Array<String>::class.java).toList()
        return accessibleModules.contains(moduleId)
    }


}
