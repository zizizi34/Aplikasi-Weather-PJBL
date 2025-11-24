package com.example.weather_app.api
// Di file WeatherModel.kt atau di file yang sama dengan model Anda

data class WeatherModel(
    val location: Location,
    val current: Current,
    val forecast: Forecast? = null  // Pastikan field ini ada
)

data class Forecast(
    val forecastday: List<ForecastDay>
)

data class ForecastDay(
    val date: String,
    val day: Day,
    val astro: Astro? = null
)

data class Day(
    val maxtemp_c: Double,
    val mintemp_c: Double,
    val avgtemp_c: Double,
    val maxwind_kph: Double,
    val totalprecip_mm: Double,
    val avghumidity: Int,
    val daily_chance_of_rain: Int,
    val condition: Condition
)

data class Astro(
    val sunrise: String,
    val sunset: String
)