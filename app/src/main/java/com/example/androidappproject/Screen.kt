package com.example.androidappproject

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object TravelList : Screen("travelList")
    object TravelDetail : Screen("travelDetail")
    object Settings : Screen("settings")
}
