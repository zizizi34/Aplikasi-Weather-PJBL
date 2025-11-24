package com.example.weather_app


/**
 * Temperature conversion utilities
 */
object TemperatureUtils {

    /**
     * Convert Celsius to Fahrenheit
     */
    fun celsiusToFahrenheit(celsius: Double): Double {
        return (celsius * 9/5) + 32
    }

    /**
     * Convert Celsius to Kelvin
     */
    fun celsiusToKelvin(celsius: Double): Double {
        return celsius + 273.15
    }

    /**
     * Convert Celsius to Reamur
     */
    fun celsiusToReamur(celsius: Double): Double {
        return celsius * 4/5
    }

    /**
     * Format temperature with unit
     */
    fun formatTemp(value: Double, decimals: Int = 1): String {
        return String.format("%.${decimals}f", value)
    }
}