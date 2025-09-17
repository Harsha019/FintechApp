package com.example.fintechtestapp.ui.viewmodel

import androidx.lifecycle.*
import com.example.fintechtestapp.data.local.UserEntity
import com.example.fintechtestapp.data.model.ModuleState
import com.example.fintechtestapp.data.model.Module
import com.example.fintechtestapp.data.model.User
import com.example.fintechtestapp.data.repository.AccessRepository
import com.example.fintechtestapp.utils.AccessManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: AccessRepository,
    private val accessManager: AccessManager
) : ViewModel() {

    private val _user = MutableLiveData<UserEntity?>()
    val user: LiveData<UserEntity?> get() = _user

    fun loadUser() {
        viewModelScope.launch {
            val userEntity = repository.getUser()
            _user.postValue(userEntity)
        }
    }

    fun isCoolingPeriod(): Boolean {
        return accessManager.isCoolingPeriod(_user.value)
    }

    fun getRemainingCoolingTime(): String {
        return accessManager.getRemainingCoolingTime(_user.value)
    }

    fun isModuleAccessible(moduleId: String): Boolean {
        return accessManager.isModuleAccessible(_user.value, moduleId)
    }
}
