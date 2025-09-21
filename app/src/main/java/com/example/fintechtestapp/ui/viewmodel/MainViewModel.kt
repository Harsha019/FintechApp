package com.example.fintechtestapp.ui.viewmodel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.VisibleForTesting
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
import java.time.Duration
import java.time.Instant

class MainViewModel(
    private val repository: AccessRepository,
    private val accessManager: AccessManager
) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _modules = MutableLiveData<List<ModuleState>>()
    val modules: LiveData<List<ModuleState>> = _modules

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadUserAndModules() {
        viewModelScope.launch {
            val data = repository.loadAppData()
            _user.value = data.user
            _modules.value = data.modules.map { module ->
                ModuleState(module, accessManager.canAccess(data.user, module))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRemainingMillis(): Long {
        val user = user.value ?: return 0
        val now = Instant.now()
        val end = Instant.parse(user.coolingEndTime)
        return maxOf(0, Duration.between(now, end).toMillis())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isCoolingPeriod(): Boolean = accessManager.isCoolingPeriod(_user.value)

    @VisibleForTesting
    fun setUserForTest(user: User) {
        _user.value = user
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRemainingCoolingTime(): String = accessManager.getRemainingCoolingTime(_user.value)
}
