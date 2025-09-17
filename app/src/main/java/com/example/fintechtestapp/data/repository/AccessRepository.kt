package com.example.fintechtestapp.data.repository

import com.example.fintechtestapp.data.local.UserDao
import com.example.fintechtestapp.data.local.UserEntity

class AccessRepository(private val userDao: UserDao) {

    suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    suspend fun getUser(): UserEntity? {
        return userDao.getUser()
    }
}
