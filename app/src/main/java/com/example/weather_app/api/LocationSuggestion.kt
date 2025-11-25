package com.example.weather_app.api

data class LocationSuggestion(
    val id: Int = 0,
    val name: String = "",
    val region: String = "",
    val country: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val url: String = ""
) {
    // Helper untuk display text
    fun getDisplayText(): String {
        return if (region.isNotEmpty()) {
            "$name, $region, $country"
        } else {
            "$name, $country"
        }
    }
}