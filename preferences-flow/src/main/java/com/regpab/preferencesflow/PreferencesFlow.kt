package com.regpab.preferencesflow

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow

/**
 * Readonly access to the [SharedPreferences] [Flow]'s
 */
interface PreferencesFlow {
    fun getString(key: String, default: String?): Flow<String?>

    fun getStringSet(key: String, default: Set<String?>?): Flow<Set<String?>?>

    fun getInt(key: String, default: Int): Flow<Int>

    fun getLong(key: String, default: Long): Flow<Long>

    fun getFloat(key: String, default: Float): Flow<Float>

    fun getBoolean(key: String, default: Boolean): Flow<Boolean>
}

/**
 * Full access to the [SharedPreferences] [Flow]'s
 */
interface MutablePreferencesFlow : PreferencesFlow {
    suspend fun putString(key: String, value: String?)

    suspend fun putStringSet(key: String, value: Set<String?>?)

    suspend fun putInt(key: String, value: Int)

    suspend fun putLong(key: String, value: Long)

    suspend fun putFloat(key: String, value: Float)

    suspend fun putBoolean(key: String, value: Boolean)

    /**
     * Removes key from the [SharedPreferences]
     * emits default value to the flow
     */
    suspend fun remove(key: String)

    /**
     * Clears [SharedPreferences]
     * emits default value for each flow
     */
    suspend fun clear()
}