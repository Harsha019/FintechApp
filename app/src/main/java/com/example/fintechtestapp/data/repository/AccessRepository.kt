package com.example.fintechtestapp.data.repository

import android.content.Context
import com.example.fintechtestapp.R
import com.example.fintechtestapp.data.local.UserDao
import com.example.fintechtestapp.data.local.UserEntity
import com.example.fintechtestapp.data.local.toUser
import com.example.fintechtestapp.data.model.AppData
import com.example.fintechtestapp.data.model.Module
import com.google.gson.Gson

class AccessRepository(
    private val context: Context,
    private val userDao: UserDao
) {

    suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    suspend fun getUser(): UserEntity? {
        return userDao.getUser()
    }

    suspend fun loadUserFromJson(): AppData {
        val jsonString = context.resources.openRawResource(R.raw.mock_data)
            .bufferedReader().use { it.readText() }
        val data = Gson().fromJson(jsonString, AppData::class.java)

        val userEntity = UserEntity(
            id = 0,
            userType = data.user.userType,
            coolingStartTime = data.user.coolingStartTime,
            coolingEndTime = data.user.coolingEndTime,
            accessibleModules = data.user.accessibleModules.joinToString(",")
        )
        insertUser(userEntity)

        return data
    }

    suspend fun loadAppData(): AppData {
        val data = loadUserFromJson()
        return data
    }

}
