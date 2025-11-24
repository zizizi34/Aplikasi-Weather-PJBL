package com.example.weather_app.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val baseURL = "https://api.weatherapi.com/v1/"

    private val gson = GsonBuilder()
        .setLenient()  // Lebih toleran terhadap JSON yang tidak perfect
        .create()

    private fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val weatherApi: WeatherApi = getInstance().create(WeatherApi::class.java)
}