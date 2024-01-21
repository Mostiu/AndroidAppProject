package com.example.androidappproject

import android.content.Context


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

val Context.myPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "myPreferences")

data class UserPreferences(
    val username: String,
    val description: String,
    )


@Singleton
class MyPreferencesDataStore @Inject constructor(
    @ApplicationContext context: Context
){
    private val myPreferencesDataStore = context.myPreferencesDataStore

    private object PreferencesKeys {
        val USERNAME = stringPreferencesKey("username")
        val DESCRIPTION = stringPreferencesKey("description")

    }

    val userFlow = myPreferencesDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val username = preferences[PreferencesKeys.USERNAME] ?: "Username"
            val description = preferences[PreferencesKeys.DESCRIPTION] ?: "Description"
            UserPreferences(username, description)
        }

    suspend fun updateUsername(username: String) {
        myPreferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.USERNAME] = username
        }
    }

    suspend fun updateDescription(description: String) {
        myPreferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.DESCRIPTION] = description
        }
    }
}
