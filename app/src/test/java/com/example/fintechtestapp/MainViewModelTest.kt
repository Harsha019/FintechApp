package com.example.fintechtestapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.fintechtestapp.data.model.AppData
import com.example.fintechtestapp.data.model.Module
import com.example.fintechtestapp.data.model.User
import com.example.fintechtestapp.data.repository.AccessRepository
import com.example.fintechtestapp.ui.viewmodel.MainViewModel
import com.example.fintechtestapp.utils.AccessManager
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val taskExecutor = InstantTaskExecutorRule()

    @MockK
    lateinit var repository: AccessRepository
    @MockK
    lateinit var accessManager: AccessManager

    private lateinit var viewModel: MainViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        try {
            Dispatchers.resetMain()
        }catch (e: Exception){}
        unmockkAll()
    }

    private fun <T> LiveData<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): T {

        var data: T? = null
        val latch = CountDownLatch(1)

        val observer = object : Observer<T> {

            override fun onChanged(value: T) {
                data = value
                latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
            }
        }

        this.observeForever(observer)

        if (!latch.await(time, timeUnit)) {
            this.removeObserver(observer)
            throw java.lang.IllegalStateException("LiveData value was not set within $time $timeUnit.")
        }

        @Suppress("UNCHECKED_CAST")
        return data as T

    }

    @Test
    fun loadUserAndModules_populatesLive_Data_correctly() = runTest {
        val testUser = User(
            userType = "admin",
            coolingStartTime = Instant.now().toString(),
            coolingEndTime = Instant.now().plusSeconds(60).toString(),
            accessibleModules = listOf("m1")
        )
        val moduleA = Module(id = "m1", title = "Payments", requiresConsent = false)
        val moduleB = Module(id = "m2", title = "Account Info", requiresConsent = true)

        val appData = AppData(user = testUser, modules = listOf(moduleA, moduleB))

        coEvery { repository.loadAppData() } returns appData

        every { accessManager.canAccess(testUser, moduleA) } returns true
        every { accessManager.canAccess(testUser, moduleB) } returns false

        viewModel = MainViewModel(repository, accessManager)
        viewModel.loadUserAndModules()

        val observedUser = viewModel.user.getOrAwaitValue()
        assertNotNull(observedUser)
        assertEquals(testUser, observedUser)

        val observedModules = viewModel.modules.getOrAwaitValue()
        assertEquals(2, observedModules.size)
        assertEquals(moduleA, observedModules[0].module)
        assertTrue(observedModules[0].isAccessible)
        assertEquals(moduleB, observedModules[1].module)
        assertFalse(observedModules[1].isAccessible)
        verify { accessManager.canAccess(testUser, moduleA) }
        verify { accessManager.canAccess(testUser, moduleB) }
    }


    // ---------- TEST 1: loadUserAndModules populates LiveData ----------
    @Test
    fun loadUserAndModules_populatesLiveData_correctly() = runTest {
        val testUser = User(
            userType = "admin",
            coolingStartTime = Instant.now().toString(),
            coolingEndTime = Instant.now().plusSeconds(60).toString(),
            accessibleModules = listOf("m1")
        )
        val moduleA = Module(id = "m1", title = "Payments", requiresConsent = false)
        val moduleB = Module(id = "m2", title = "Account Info", requiresConsent = true)

        val appData = AppData(user = testUser, modules = listOf(moduleA, moduleB))

        coEvery { repository.loadAppData() } returns appData

        every { accessManager.canAccess(testUser, moduleA) } returns true
        every { accessManager.canAccess(testUser, moduleB) } returns false

        viewModel = MainViewModel(repository, accessManager)
        viewModel.loadUserAndModules()

        val observedUser = viewModel.user.getOrAwaitValue()
        assertNotNull(observedUser)
        assertEquals(testUser, observedUser)

        val observedModules = viewModel.modules.getOrAwaitValue()
        assertEquals(2, observedModules.size)
        assertEquals(moduleA, observedModules[0].module)
        assertTrue(observedModules[0].isAccessible)
        assertEquals(moduleB, observedModules[1].module)
        assertFalse(observedModules[1].isAccessible)

        verify { accessManager.canAccess(testUser, moduleA) }
        verify { accessManager.canAccess(testUser, moduleB) }
    }

    // ---------- TEST 2: getRemainingMillis (future -> positive) ----------
    @Test
    fun getRemainingMillis_future_returnsPositive() = runTest {
        val futureUser = User(
            userType = "admin",
            coolingStartTime = Instant.now().toString(),
            coolingEndTime = Instant.now().plusSeconds(120).toString(),
            accessibleModules = emptyList()
        )
        coEvery { repository.loadAppData() } returns AppData(user = futureUser, modules = emptyList())
        viewModel = MainViewModel(repository, accessManager)
        viewModel.loadUserAndModules()

        val remaining = viewModel.getRemainingMillis()
        assertTrue(remaining > 0, "Expected remaining millis > 0 for future coolingEndTime")
    }

    // ---------- TEST 3: getRemainingMillis (past -> zero) ----------
    @Test
    fun getRemainingMillis_past_returnsZero() = runTest {
        val pastUser = User(
            userType = "admin",
            coolingStartTime = Instant.now().minusSeconds(120).toString(),
            coolingEndTime = Instant.now().minusSeconds(60).toString(),
            accessibleModules = emptyList()
        )
        coEvery { repository.loadAppData() } returns AppData(user = pastUser, modules = emptyList())
        viewModel = MainViewModel(repository, accessManager)
        viewModel.loadUserAndModules()

        val remaining = viewModel.getRemainingMillis()
        assertEquals(0L, remaining)
    }

    // ---------- TEST 4: isCoolingPeriod delegates to AccessManager ----------
    @Test
    fun isCoolingPeriod_delegatesToAccessManager() = runTest {
        val u = User(
            userType = "admin",
            coolingStartTime = Instant.now().toString(),
            coolingEndTime = Instant.now().plusSeconds(60).toString(),
            accessibleModules = listOf("m1")
        )
        coEvery { repository.loadAppData() } returns AppData(user = u, modules = emptyList())
        every { accessManager.isCoolingPeriod(u) } returns true

        viewModel = MainViewModel(repository, accessManager)
        viewModel.loadUserAndModules()

        assertTrue(viewModel.isCoolingPeriod())
        verify { accessManager.isCoolingPeriod(u) }
    }

    // ---------- TEST 5: getRemainingCoolingTime delegates ----------
    @Test
    fun getRemainingCoolingTime_delegatesToAccessManager() = runTest {
        val u = User(
            userType = "admin",
            coolingStartTime = Instant.now().toString(),
            coolingEndTime = Instant.now().plusSeconds(30).toString(),
            accessibleModules = emptyList()
        )
        coEvery { repository.loadAppData() } returns AppData(user = u, modules = emptyList())
        every { accessManager.getRemainingCoolingTime(u) } returns "00:30"

        viewModel = MainViewModel(repository, accessManager)
        viewModel.loadUserAndModules()

        assertEquals("00:30", viewModel.getRemainingCoolingTime())
        verify { accessManager.getRemainingCoolingTime(u) }
    }


}