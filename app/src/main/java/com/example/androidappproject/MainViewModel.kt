package com.example.androidappproject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val myPreferencesDataStore: MyPreferencesDataStore
): ViewModel(){
    val username = myPreferencesDataStore.userFlow.map{
        it.username
    }

    val description = myPreferencesDataStore.userFlow.map{
        it.description
    }

    val profilePicture = myPreferencesDataStore.userFlow.map{
        it.profilePicture
    }

    fun updateUsername(username: String) {
        viewModelScope.launch {
            myPreferencesDataStore.updateUsername(username)
        }
    }

    fun updateDescription(description: String) {
        viewModelScope.launch {
            myPreferencesDataStore.updateDescription(description)
        }
    }

    fun updateProfilePicture(profilePicture: String) {
        viewModelScope.launch {
            myPreferencesDataStore.updateProfilePicture(profilePicture)
        }
    }
}