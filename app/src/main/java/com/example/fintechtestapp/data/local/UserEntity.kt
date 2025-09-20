package com.example.fintechtestapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.fintechtestapp.data.model.User

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val id: Int = 0,
    val userType: String,
    val coolingStartTime: String,
    val coolingEndTime: String,
    val accessibleModules: String
)

fun UserEntity.toUser(): User {
    return User(
        userType = userType,
        coolingStartTime = coolingStartTime,
        coolingEndTime = coolingEndTime,
        accessibleModules = accessibleModules.split(",")
    )
}
