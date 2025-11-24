package com.example.weather_app.api

import com.google.gson.annotations.SerializedName

data class LocationSuggestion(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("name")
    val name: String = "",

    @SerializedName("region")
    val region: String = "",

    @SerializedName("country")
    val country: String = "",

    @SerializedName("lat")
    val lat: Double = 0.0,

    @SerializedName("lon")
    val lon: Double = 0.0,

    @SerializedName("url")
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