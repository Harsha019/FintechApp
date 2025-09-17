package com.example.fintechtestapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val id: Int = 0,
    val userType: String,
    val coolingStartTime: String,
    val coolingEndTime: String,
    val accessibleModules: String
)