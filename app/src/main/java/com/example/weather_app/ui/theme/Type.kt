package com.example.weather_app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.weather_app.R

// Define StackSansText font family backed by the bundled fonts in res/font
val StackSansText = FontFamily(
    Font(R.font.stacksanstext_regular, weight = FontWeight.Normal),
    Font(R.font.stacksanstext_medium, weight = FontWeight.Medium),
    Font(R.font.stacksanstext_semibold, weight = FontWeight.SemiBold),
    Font(R.font.stacksanstext_bold, weight = FontWeight.Bold),
    Font(R.font.stacksanstext_light, weight = FontWeight.Light),
    Font(R.font.stacksanstext_extralight, weight = FontWeight.ExtraLight)
)

// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = StackSansText,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp
    ),
    titleLarge = TextStyle(
        fontFamily = StackSansText,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = StackSansText,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    labelSmall = TextStyle(
        fontFamily = StackSansText,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    )
)