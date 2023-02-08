package com.regpab.preferencesflow

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class PreferenceFlowTest {

    private lateinit var appContext: Context
    private lateinit var preferencesFlow: MutablePreferencesFlow

    @Before
    fun setup() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        preferencesFlow = appContext.getMutablePreferencesFlow()
    }

    @Test
    fun initPreferences() = runTest {
        val storedValue = preferencesFlow
            .getString("any", null)
            .stateIn(GlobalScope)
            .value

        assertEquals(null, storedValue)
    }

    @Test
    fun updateString() = runTest {
        val stringKey = "STRING"
        val stringValue = "VALUE"

        val storedValueFlow = mutableListOf<String?>()
        val anotherStoredValueFlow = mutableListOf<String?>()

        val job1 = launch(UnconfinedTestDispatcher()) {
            preferencesFlow
                .getString(stringKey, null)
                .stateIn(this)
                .toList(storedValueFlow)
        }

        val job2 = launch(UnconfinedTestDispatcher()) {
            preferencesFlow
                .getString(stringKey, null)
                .stateIn(this)
                .toList(anotherStoredValueFlow)
        }

        assertEquals(null, storedValueFlow[0])
        assertEquals(null, anotherStoredValueFlow[0])

        preferencesFlow.putString(stringKey, stringValue)

        assertEquals(stringValue, storedValueFlow[1])
        assertEquals(stringValue, anotherStoredValueFlow[1])

        job1.cancel()
        job2.cancel()
    }

    @Test
    fun updateStringSet() = runTest {
        val stringSetKey = "STRING_SET"
        val stringSetValue = setOf("VALUE", "VALUE2")

        val storedValueFlow = mutableListOf<Set<String?>?>()
        val anotherStoredValueFlow = mutableListOf<Set<String?>?>()

        val job1 = launch(UnconfinedTestDispatcher()) {
            preferencesFlow
                .getStringSet(stringSetKey, null)
                .stateIn(this)
                .toList(storedValueFlow)
        }

        val job2 = launch(UnconfinedTestDispatcher()) {
            preferencesFlow
                .getStringSet(stringSetKey, null)
                .stateIn(this)
                .toList(anotherStoredValueFlow)
        }

        assertEquals(null, storedValueFlow[0])
        assertEquals(null, anotherStoredValueFlow[0])

        preferencesFlow.putStringSet(stringSetKey, stringSetValue)

        assertEquals(stringSetValue, storedValueFlow[1])
        assertEquals(stringSetValue, anotherStoredValueFlow[1])

        job1.cancel()
        job2.cancel()
    }

    @Test
    fun updateInt() = runTest {
        val intKey = "INT"
        val intValue = 156
        val default = -1

        val storedValueFlow = mutableListOf<Int?>()
        val anotherStoredValueFlow = mutableListOf<Int?>()

        val job1 = launch(UnconfinedTestDispatcher()) {
            preferencesFlow
                .getInt(intKey, default)
                .stateIn(this)
                .toList(storedValueFlow)
        }

        val job2 = launch(UnconfinedTestDispatcher()) {
            preferencesFlow
                .getInt(intKey, default)
                .stateIn(this)
                .toList(anotherStoredValueFlow)
        }

        assertEquals(default, storedValueFlow[0])
        assertEquals(default, anotherStoredValueFlow[0])

        preferencesFlow.putInt(intKey, intValue)

        assertEquals(intValue, storedValueFlow[1])
        assertEquals(intValue, anotherStoredValueFlow[1])

        job1.cancel()
        job2.cancel()
    }

    @Test
    fun updateLong() = runTest {
        val longKey = "LONG"
        val longValue = 123L
        val default = -1L

        val storedValueFlow = mutableListOf<Long?>()
        val anotherStoredValueFlow = mutableListOf<Long?>()

        val job1 = launch(UnconfinedTestDispatcher()) {
            preferencesFlow
                .getLong(longKey, default)
                .stateIn(this)
                .toList(storedValueFlow)
        }

        val job2 = launch(UnconfinedTestDispatcher()) {
            preferencesFlow
                .getLong(longKey, default)
                .stateIn(this)
                .toList(anotherStoredValueFlow)
        }

        assertEquals(default, storedValueFlow[0])
        assertEquals(default, anotherStoredValueFlow[0])

        preferencesFlow.putLong(longKey, longValue)

        assertEquals(longValue, storedValueFlow[1])
        assertEquals(longValue, anotherStoredValueFlow[1])

        job1.cancel()
        job2.cancel()
    }

    @Test
    fun updateFloat() = runTest {
        val floatKey = "FLOAT"
        val floatValue = 12.3f
        val default = -1f

        val storedValueFlow = mutableListOf<Float?>()
        val anotherStoredValueFlow = mutableListOf<Float?>()

        val job1 = launch(UnconfinedTestDispatcher()) {
            preferencesFlow
                .getFloat(floatKey, default)
                .stateIn(this)
                .toList(storedValueFlow)
        }

        val job2 = launch(UnconfinedTestDispatcher()) {
            preferencesFlow
                .getFloat(floatKey, default)
                .stateIn(this)
                .toList(anotherStoredValueFlow)
        }

        assertEquals(default, storedValueFlow[0])
        assertEquals(default, anotherStoredValueFlow[0])

        preferencesFlow.putFloat(floatKey, floatValue)

        assertEquals(floatValue, storedValueFlow[1])
        assertEquals(floatValue, anotherStoredValueFlow[1])

        job1.cancel()
        job2.cancel()
    }

    @Test
    fun updateBoolean() = runTest {
        val booleanKey = "BOOLEAN"
        val booleanValue = true
        val default = false

        val storedValueFlow = mutableListOf<Boolean?>()
        val anotherStoredValueFlow = mutableListOf<Boolean?>()

        val job1 = launch(UnconfinedTestDispatcher()) {
            preferencesFlow
                .getBoolean(booleanKey, default)
                .stateIn(this)
                .toList(storedValueFlow)
        }

        val job2 = launch(UnconfinedTestDispatcher()) {
            preferencesFlow
                .getBoolean(booleanKey, default)
                .stateIn(this)
                .toList(anotherStoredValueFlow)
        }

        assertEquals(default, storedValueFlow[0])
        assertEquals(default, anotherStoredValueFlow[0])

        preferencesFlow.putBoolean(booleanKey, booleanValue)

        assertEquals(booleanValue, storedValueFlow[1])
        assertEquals(booleanValue, anotherStoredValueFlow[1])

        job1.cancel()
        job2.cancel()
    }

    @Test
    fun remove() = runTest {
        val stringKey = "STRING"
        val stringValue = "VALUE"

        val storedValueFlow = mutableListOf<String?>()
        val anotherStoredValueFlow = mutableListOf<String?>()

        preferencesFlow.clear()

        val job1 = launch(UnconfinedTestDispatcher()) {
            preferencesFlow
                .getString(stringKey, null)
                .stateIn(this)
                .toList(storedValueFlow)
        }

        val job2 = launch(UnconfinedTestDispatcher()) {
            preferencesFlow
                .getString(stringKey, null)
                .stateIn(this)
                .toList(anotherStoredValueFlow)
        }

        assertEquals(null, storedValueFlow[0])
        assertEquals(null, anotherStoredValueFlow[0])

        preferencesFlow.putString(stringKey, stringValue)

        assertEquals(stringValue, storedValueFlow[1])
        assertEquals(stringValue, anotherStoredValueFlow[1])

        preferencesFlow.remove(stringKey)

        assertEquals(null, storedValueFlow[0])
        assertEquals(null, anotherStoredValueFlow[0])

        job1.cancel()
        job2.cancel()
    }

    @Test
    fun clear() = runTest {
        preferencesFlow.clear()

        val stringKey = "STRING"
        val stringValue = "VALUE"

        val floatKey = "FLOAT"
        val floatValue = 12.3f
        val default = -1f

        val storedValueFlow = mutableListOf<String?>()
        val anotherStoredValueFlow = mutableListOf<Float?>()


        val job1 = launch(UnconfinedTestDispatcher()) {
            preferencesFlow
                .getString(stringKey, null)
                .stateIn(this)
                .toList(storedValueFlow)
        }

        val job2 = launch(UnconfinedTestDispatcher()) {
            preferencesFlow
                .getFloat(floatKey, default)
                .stateIn(this)
                .toList(anotherStoredValueFlow)
        }

        assertEquals(null, storedValueFlow[0])
        assertEquals(default, anotherStoredValueFlow[0])

        preferencesFlow.putString(stringKey, stringValue)
        preferencesFlow.putFloat(floatKey, floatValue)

        assertEquals(stringValue, storedValueFlow[1])
        assertEquals(floatValue, anotherStoredValueFlow[1])

        preferencesFlow.clear()

        assertEquals(null, storedValueFlow[2])
        assertEquals(default, anotherStoredValueFlow[2])

        job1.cancel()
        job2.cancel()
    }

    @After
    fun dispose() = runTest {
        preferencesFlow.clear()
    }
}