package com.example.fintechtestapp.ui.viewmodel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.fintechtestapp.data.local.UserEntity
import com.example.fintechtestapp.data.model.ModuleState
import com.example.fintechtestapp.data.model.Module
import com.example.fintechtestapp.data.model.User
import com.example.fintechtestapp.data.repository.AccessRepository
import com.example.fintechtestapp.utils.AccessManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainViewModel(
    private val repository: AccessRepository,
    private val accessManager: AccessManager
) : ViewModel() {

    private val _user = MutableLiveData<UserEntity?>()
    val user: LiveData<UserEntity?> get() = _user

    private val _modules = MutableLiveData<List<ModuleState>>()
    val modules: LiveData<List<ModuleState>> get() = _modules

    private var timer: Job? = null

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadUserAndModules() {
        viewModelScope.launch {
            repository.loadUserFromJson()
            val userEntity = repository.getUser()
            _user.postValue(userEntity)

            val modules = listOf(
                Module(
                    id = "payments",
                    title = "Payments",
                    requiresConsent = true
                ),
                Module(
                    id = "account_info",
                    title = "Account Info",
                    requiresConsent = false
                ),
                Module(
                    id = "consent_center",
                    title = "Consent Center",
                    requiresConsent = true
                )
            )

            val moduleStates = modules.map {
                ModuleState(
                    module = it,
                    isAccessible = isModuleAccessible(it.id)
                )
            }
            _modules.postValue(moduleStates)

            if (isCoolingPeriod()) startCoolingTimer()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isCoolingPeriod(): Boolean {
        return _user.value?.let { accessManager.isCoolingPeriod(it) } ?: false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRemainingCoolingTime(): String {
        return _user.value?.let { accessManager.getRemainingCoolingTime(it) } ?: "Cooling ends in 00:00"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isModuleAccessible(moduleId: String): Boolean {
        return _user.value?.let { accessManager.isModuleAccessible(it, moduleId) } ?: false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startCoolingTimer() {
        timer?.cancel()
        timer = viewModelScope.launch {
            while (isActive && isCoolingPeriod()) {
                _user.postValue(_user.value) // Trigger UI update
                delay(1000)
            }
        }
    }

    override fun onCleared() {
        timer?.cancel()
        super.onCleared()
    }
}