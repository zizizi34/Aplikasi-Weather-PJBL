package com.example.weather_app.api

import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("country")
    val country: String = "",

    @SerializedName("lat")
    val lat: Double = 0.0,

    @SerializedName("localtime")
    val localtime: String = "",

    @SerializedName("localtime_epoch")
    val localtime_epoch: Long = 0,  // Ubah dari Int ke Long untuk safety

    @SerializedName("lon")
    val lon: Double = 0.0,

    @SerializedName("name")
    val name: String = "",

    @SerializedName("region")
    val region: String = "",

    @SerializedName("tz_id")
    val tz_id: String = ""
)