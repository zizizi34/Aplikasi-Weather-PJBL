package com.example.weather_app.api

import com.google.gson.annotations.SerializedName

data class Current(
    @SerializedName("cloud")
    val cloud: Int = 0,

    @SerializedName("condition")
    val condition: Condition = Condition(),

    @SerializedName("dewpoint_c")
    val dewpoint_c: Double = 0.0,

    @SerializedName("dewpoint_f")
    val dewpoint_f: Double = 0.0,

    // PROBLEM FIELDS - UBAH KE NULLABLE ATAU DOUBLE
    @SerializedName("diff_rad")
    val diff_rad: Double? = null,  // Ubah dari Int ke Double nullable

    @SerializedName("dni")
    val dni: Double? = null,  // Ubah dari Int ke Double nullable

    @SerializedName("feelslike_c")
    val feelslike_c: Double = 0.0,

    @SerializedName("feelslike_f")
    val feelslike_f: Double = 0.0,

    @SerializedName("gti")
    val gti: Double? = null,  // Ubah dari Int ke Double nullable

    @SerializedName("gust_kph")
    val gust_kph: Double = 0.0,

    @SerializedName("gust_mph")
    val gust_mph: Double = 0.0,

    @SerializedName("heatindex_c")
    val heatindex_c: Double = 0.0,

    @SerializedName("heatindex_f")
    val heatindex_f: Double = 0.0,

    @SerializedName("humidity")
    val humidity: Int = 0,

    @SerializedName("is_day")
    val is_day: Int = 1,

    @SerializedName("last_updated")
    val last_updated: String = "",

    @SerializedName("last_updated_epoch")
    val last_updated_epoch: Int = 0,

    @SerializedName("precip_in")
    val precip_in: Double = 0.0,

    @SerializedName("precip_mm")
    val precip_mm: Double = 0.0,

    @SerializedName("pressure_in")
    val pressure_in: Double = 0.0,

    @SerializedName("pressure_mb")
    val pressure_mb: Double = 0.0,

    // PROBLEM FIELD - UBAH KE NULLABLE ATAU DOUBLE
    @SerializedName("short_rad")
    val short_rad: Double? = null,  // Ubah dari Int ke Double nullable

    @SerializedName("temp_c")
    val temp_c: Double = 0.0,

    @SerializedName("temp_f")
    val temp_f: Double = 0.0,

    @SerializedName("uv")
    val uv: Double = 0.0,

    @SerializedName("vis_km")
    val vis_km: Double = 0.0,

    @SerializedName("vis_miles")
    val vis_miles: Double = 0.0,

    @SerializedName("wind_degree")
    val wind_degree: Int = 0,

    @SerializedName("wind_dir")
    val wind_dir: String = "",

    @SerializedName("wind_kph")
    val wind_kph: Double = 0.0,

    @SerializedName("wind_mph")
    val wind_mph: Double = 0.0,

    @SerializedName("windchill_c")
    val windchill_c: Double = 0.0,

    @SerializedName("windchill_f")
    val windchill_f: Double = 0.0
)