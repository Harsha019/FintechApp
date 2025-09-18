package com.example.fintechtestapp.data.repository

import android.content.Context
import com.example.fintechtestapp.data.local.UserDao
import com.example.fintechtestapp.data.local.UserEntity
import com.example.fintechtestapp.data.model.Module
import com.example.fintechtestapp.data.model.User
import org.json.JSONObject

class AccessRepository(private val userDao: UserDao) {

    suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    suspend fun getUser(): UserEntity? {
        return userDao.getUser()
    }

    suspend fun loadUserFromJson() {
        val userEntity = UserEntity(
            id = 0,
            userType = "active",
            coolingStartTime = "2025-09-19T01:00:00Z",
            coolingEndTime = "2025-09-19T01:05:00Z",
            accessibleModules = "payments,account_info"
        )
        insertUser(userEntity)
    }
}