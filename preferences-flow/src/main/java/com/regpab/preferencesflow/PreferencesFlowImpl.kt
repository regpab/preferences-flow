package com.regpab.preferencesflow

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 * Init [PreferencesFlow] for readonly state listening
 * @param name refers to [Context.getSharedPreferences] unique name of the storage
 * @param mode refers to [Context.getSharedPreferences] mode constant
 */
fun Context.getPreferencesFlow(
    name: String = "app",
    mode: Int = MODE_PRIVATE
): PreferencesFlow = getPreferenceFlow(name, mode)

/**
 * Init [MutablePreferencesFlow] for data writing
 * @param name refers to [Context.getSharedPreferences] unique name of the storage
 * @param mode refers to [Context.getSharedPreferences] mode constant
 */
fun Context.getMutablePreferencesFlow(
    name: String = "app",
    mode: Int = MODE_PRIVATE
): MutablePreferencesFlow = getPreferenceFlow(name, mode)

private val preferencesFlows: HashMap<String, PreferencesFlowImpl> = HashMap()

private fun Context.getPreferenceFlow(name: String, mode: Int): PreferencesFlowImpl {
    return if (preferencesFlows.containsKey(name)) {
        preferencesFlows[name] as PreferencesFlowImpl
    } else {
        val newPreferences = PreferencesFlowImpl(this, name, mode)
        preferencesFlows[name] = newPreferences
        newPreferences
    }
}

private class PreferencesFlowImpl(context: Context, name: String, mode: Int) :
    MutablePreferencesFlow {
    private val storedValues = HashMap<String, MutableStateFlow<SupportedPreferencesTypes>>()
    private val sharedPreferences = context.getSharedPreferences(name, mode)

    override fun getString(key: String, default: String?): Flow<String?> {
        return getValue(key, default = {
            val sharedPreferencesValue = sharedPreferences.getString(key, default)
            SupportedPreferencesTypes.StringWrapper(sharedPreferencesValue)
        }, mapper = {
            (it as? SupportedPreferencesTypes.StringWrapper?)?.value
        })
    }

    override fun getStringSet(key: String, default: Set<String?>?): Flow<Set<String?>?> {
        return getValue(key, default = {
            val sharedPreferencesValue = sharedPreferences.getStringSet(key, default)
            SupportedPreferencesTypes.StringSetWrapper(sharedPreferencesValue)
        }, mapper = {
            (it as? SupportedPreferencesTypes.StringSetWrapper?)?.value ?: default
        })
    }

    override fun getInt(key: String, default: Int): Flow<Int> {
        return getValue(key, default = {
            val sharedPreferencesValue = sharedPreferences.getInt(key, default)
            SupportedPreferencesTypes.IntWrapper(sharedPreferencesValue)
        }, mapper = {
            (it as? SupportedPreferencesTypes.IntWrapper?)?.value ?: default
        })
    }

    override fun getLong(key: String, default: Long): Flow<Long> {
        return getValue(key, default = {
            val sharedPreferencesValue = sharedPreferences.getLong(key, default)
            SupportedPreferencesTypes.LongWrapper(sharedPreferencesValue)
        }, mapper = {
            (it as? SupportedPreferencesTypes.LongWrapper?)?.value ?: default
        })
    }

    override fun getFloat(key: String, default: Float): Flow<Float> {
        return getValue(key, default = {
            val sharedPreferencesValue = sharedPreferences.getFloat(key, default)
            SupportedPreferencesTypes.FloatWrapper(sharedPreferencesValue)
        }, mapper = {
            (it as? SupportedPreferencesTypes.FloatWrapper?)?.value ?: default
        })
    }

    override fun getBoolean(key: String, default: Boolean): Flow<Boolean> {
        return getValue(key, default = {
            val sharedPreferencesValue = sharedPreferences.getBoolean(key, default)
            SupportedPreferencesTypes.BooleanWrapper(sharedPreferencesValue)
        }, mapper = {
            (it as? SupportedPreferencesTypes.BooleanWrapper?)?.value ?: default
        })
    }

    private fun <T> getValue(
        key: String,
        default: () -> SupportedPreferencesTypes,
        mapper: (SupportedPreferencesTypes) -> T
    ): Flow<T> {
        val storedValueFlow = storedValues.getOrElse(key) {
            val newValue: MutableStateFlow<SupportedPreferencesTypes> = MutableStateFlow(default())
            storedValues[key] = newValue
            return newValue.map(mapper)
        }
        return storedValueFlow.map(mapper)
    }

    override suspend fun putString(key: String, value: String?) {
        putValue(key, SupportedPreferencesTypes.StringWrapper(value)) {
            putString(key, value)
        }
    }

    override suspend fun putStringSet(key: String, value: Set<String?>?) {
        putValue(key, SupportedPreferencesTypes.StringSetWrapper(value)) {
            putStringSet(key, value)
        }
    }

    override suspend fun putInt(key: String, value: Int) {
        putValue(key, SupportedPreferencesTypes.IntWrapper(value)) {
            putInt(key, value)
        }
    }

    override suspend fun putLong(key: String, value: Long) {
        putValue(key, SupportedPreferencesTypes.LongWrapper(value)) {
            putLong(key, value)
        }
    }

    override suspend fun putFloat(key: String, value: Float) {
        putValue(key, SupportedPreferencesTypes.FloatWrapper(value)) {
            putFloat(key, value)
        }
    }

    override suspend fun putBoolean(key: String, value: Boolean) {
        putValue(key, SupportedPreferencesTypes.BooleanWrapper(value)) {
            putBoolean(key, value)
        }
    }

    private suspend fun putValue(
        key: String,
        wrapper: SupportedPreferencesTypes,
        sharedPreferencesAdder: SharedPreferences.Editor.() -> Unit
    ) {
        if (storedValues.containsKey(key)) {
            storedValues[key]?.emit(wrapper)
        } else {
            storedValues[key] = MutableStateFlow(wrapper)
        }
        sharedPreferences.edit {
            sharedPreferencesAdder()
        }
    }

    override suspend fun remove(key: String) {
        if (storedValues.containsKey(key))
            storedValues[key]?.emit(SupportedPreferencesTypes.NothingWrapper)
        sharedPreferences.edit {
            remove(key)
        }
    }

    override suspend fun clear() {
        storedValues.values.forEach { it.emit(SupportedPreferencesTypes.NothingWrapper) }
        sharedPreferences.edit {
            clear()
        }
    }

    private sealed interface SupportedPreferencesTypes {
        data class StringWrapper(val value: String?) : SupportedPreferencesTypes
        data class StringSetWrapper(val value: Set<String?>?) : SupportedPreferencesTypes
        data class IntWrapper(val value: Int?) : SupportedPreferencesTypes
        data class LongWrapper(val value: Long?) : SupportedPreferencesTypes
        data class FloatWrapper(val value: Float?) : SupportedPreferencesTypes
        data class BooleanWrapper(val value: Boolean?) : SupportedPreferencesTypes

        object NothingWrapper : SupportedPreferencesTypes
    }
}