package com.example.weather_app


import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.weather_app.api.LocationSuggestion
import com.example.weather_app.api.NetworkResponse
import com.example.weather_app.api.WeatherModel
import com.example.weather_app.ui.theme.StackSansText
import com.example.weather_app.ui.theme.Typography
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.ui.platform.LocalConfiguration
import android.content.res.Configuration
import kotlin.random.Random
import androidx.compose.foundation.interaction.MutableInteractionSource
import com.example.weather_app.api.ForecastDay
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun isLandscape(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}

private val BlueLightScheme = lightColorScheme(
    primary = Color(0xFF1E88E5),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE3F2FD),
    onPrimaryContainer = Color(0xFF0D47A1),
    background = Color(0xFFF5F9FF),
    onBackground = Color(0xFF1A1C1E),
    surface = Color.White,
    onSurface = Color(0xFF1A1C1E)
)

private val BlueDarkScheme = darkColorScheme(
    primary = Color(0xFF90CAF9),
    onPrimary = Color(0xFF003258),
    primaryContainer = Color(0xFF0D47A1),
    onPrimaryContainer = Color(0xFFD0E8FF),
    background = Color(0xFF0A1929),
    onBackground = Color(0xFFE3F2FD),
    surface = Color(0xFF0D1B2A),
    onSurface = Color(0xFFE3F2FD)
)

@Composable
fun HaloBlueTheme(content: @Composable () -> Unit) {
    val dark = isSystemInDarkTheme()
    MaterialTheme(
        colorScheme = if (dark) BlueDarkScheme else BlueLightScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun WeatherPage(viewModel: WeatherViewModel) {
    HaloBlueTheme {
        var showCurrentLocation by remember { mutableStateOf(true) }

        // Reset ke current location saat back dari search
        BackHandler(enabled = !showCurrentLocation) {
            showCurrentLocation = true
            viewModel.getData("Surakarta") // Reload lokasi saat ini
        }

        if (showCurrentLocation) {
            CurrentLocationWeatherCard(
                viewModel = viewModel,
                onNavigateToSearch = { showCurrentLocation = false }
            )
        } else {
            WeatherSearchPage(
                viewModel = viewModel,
                onBackToHome = { showCurrentLocation = true }
            )
        }
    }
}

@Composable
fun CurrentLocationWeatherCard(
    viewModel: WeatherViewModel,
    onNavigateToSearch: () -> Unit
) {
    val weatherResult = viewModel.weatherResult.observeAsState()
    val colors = MaterialTheme.colorScheme
    val isDark = isSystemInDarkTheme()

    // Animasi untuk card
    val infiniteTransition = rememberInfiniteTransition(label = "card_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "glow"
    )

    LaunchedEffect(Unit) {
        // Auto-detect lokasi saat ini (gunakan city default atau GPS)
        viewModel.getData("Surakarta") // Ganti dengan lokasi user atau GPS
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = if (isDark) {
                        listOf(
                            Color(0xFF0A1929),
                            Color(0xFF1565C0),
                            Color(0xFF1E88E5)
                        )
                    } else {
                        listOf(
                            Color(0xFF42A5F5),
                            Color(0xFF1E88E5),
                            Color(0xFF1565C0)
                        )
                    }
                )
            )
    ) {
        // Weather-based animation effect
        when (val result = weatherResult.value) {
            is NetworkResponse.Success -> {
                WeatherAmbience(result.data.current.condition.text)
            }
            else -> {
                // No animation when loading or error
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header: Logo di kiri, Nama App di kanan
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo di kiri dengan animasi
                Image(
                    painter = painterResource(id = R.drawable.halo_weather_icon),
                    contentDescription = "Halo Weather Logo",
                    modifier = Modifier
                        .size(60.dp)
                        .graphicsLayer {
                            scaleX = 1f + (glowAlpha * 0.1f)
                            scaleY = 1f + (glowAlpha * 0.1f)
                        }
                )

                // Nama aplikasi di kanan
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        "Halo Weather",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = StackSansText,
                        color = Color.White,
                        style = androidx.compose.ui.text.TextStyle(
                            shadow = Shadow(
                                color = Color.Black.copy(alpha = 0.3f),
                                offset = Offset(0f, 4f),
                                blurRadius = 8f
                            )
                        )
                    )
                    Text(
                        text = stringResource(id = R.string.cuaca_lokal_anda),
                        fontSize = 12.sp,
                        fontFamily = StackSansText,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Weather Card dengan glassmorphism effect
            when (val result = weatherResult.value) {
                is NetworkResponse.Success -> {
                    CurrentLocationCard(
                        data = result.data,
                        glowAlpha = glowAlpha,
                        onNavigateToSearch = onNavigateToSearch
                    )
                }
                NetworkResponse.Loading -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp),
                        shape = RoundedCornerShape(32.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.15f)
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    strokeWidth = 3.dp
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    stringResource(id = R.string.detect_location),
                                    color = Color.White,
                                    fontFamily = StackSansText,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
                is NetworkResponse.Error -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFEBEE).copy(alpha = 0.9f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                stringResource(id = R.string.failed_detect_location),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFC62828),
                                fontFamily = StackSansText
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                result.message,
                                fontSize = 13.sp,
                                color = Color(0xFFC62828),
                                fontFamily = StackSansText,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                else -> {
                    // Idle state
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Search Button dengan animasi
            Button(
                onClick = onNavigateToSearch,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.2f)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(id = R.string.cari_lokasi_lainnya),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = StackSansText,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CurrentLocationCard(
    data: WeatherModel,
    glowAlpha: Float,
    onNavigateToSearch: () -> Unit
) {
    val translatedCondition = remember(data.current.condition.text) {
        WeatherTranslations.translate(data.current.condition.text)
    }
    val isDark = isSystemInDarkTheme()
    val isLandscape = isLandscape()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(
                onClick = onNavigateToSearch,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) Color(0xFF1565C0) else Color(0xFF1E88E5)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(if (isLandscape) 16.dp else 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(if (isLandscape) 40.dp else 48.dp)
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(if (isLandscape) 10.dp else 12.dp)
                    )
                }

                Spacer(modifier = Modifier.width(if (isLandscape) 12.dp else 16.dp))

                Column {
                    Text(
                        text = data.location.name,
                        fontSize = if (isLandscape) 18.sp else 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = StackSansText,
                        color = Color.White
                    )
                    Text(
                        text = data.location.country,
                        fontSize = if (isLandscape) 12.sp else 14.sp,
                        fontFamily = StackSansText,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(if (isLandscape) 12.dp else 16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) Color.White.copy(alpha = 0.12f)
                else Color.White.copy(alpha = 0.95f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            if (isLandscape) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "${data.current.temp_c.toInt()}°",
                            fontSize = 56.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = StackSansText,
                            color = if (isDark) Color.White else Color(0xFF1565C0),
                            letterSpacing = (-2).sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = translatedCondition,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = stacksansText,
                            color = if (isDark) Color.White.copy(alpha = 0.85f)
                            else Color(0xFF1A1C1E).copy(alpha = 0.75f)
                        )
                    }

                    AsyncImage(
                        model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )

                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        val tempF = TemperatureUtils.celsiusToFahrenheit(data.current.temp_c)
                        val tempK = TemperatureUtils.celsiusToKelvin(data.current.temp_c)
                        val tempR = TemperatureUtils.celsiusToReamur(data.current.temp_c)

                        TempUnitCompact(TemperatureUtils.formatTemp(tempF), "°F", isDark)
                        Spacer(modifier = Modifier.height(4.dp))
                        TempUnitCompact(TemperatureUtils.formatTemp(tempK), "K", isDark)
                        Spacer(modifier = Modifier.height(4.dp))
                        TempUnitCompact(TemperatureUtils.formatTemp(tempR), "°R", isDark)
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = "${data.current.temp_c.toInt()}°",
                            fontSize = 72.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = StackSansText,
                            color = if (isDark) Color.White else Color(0xFF1565C0),
                            letterSpacing = (-3).sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        val tempF = TemperatureUtils.celsiusToFahrenheit(data.current.temp_c)
                        val tempK = TemperatureUtils.celsiusToKelvin(data.current.temp_c)
                        val tempR = TemperatureUtils.celsiusToReamur(data.current.temp_c)

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TempUnit(TemperatureUtils.formatTemp(tempF), "°F", isDark)
                            TempUnit(TemperatureUtils.formatTemp(tempK), "K", isDark)
                            TempUnit(TemperatureUtils.formatTemp(tempR), "°R", isDark)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            translatedCondition,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = StackSansText,
                            color = if (isDark) Color.White.copy(alpha = 0.85f)
                            else Color(0xFF1A1C1E).copy(alpha = 0.75f)
                        )
                    }

                    AsyncImage(
                        model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp)
                    )
                }
            }
        }

        data.forecast?.let { forecast ->
            Spacer(modifier = Modifier.height(if (isLandscape) 12.dp else 16.dp))

            ForecastCard(
                forecastDays = forecast.forecastday,
                isDark = isDark,
                isLandscape = isLandscape
            )
        }
    }
}

@Composable
fun ForecastCard(
    forecastDays: List<ForecastDay>,
    isDark: Boolean,
    isLandscape: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color.White.copy(alpha = 0.12f)
            else Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(if (isLandscape) 16.dp else 20.dp)
        ) {
            Text(
                text = stringResource(R.string.forecast_3_days),
                fontSize = if (isLandscape) 16.sp else 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = StackSansText,
                color = if (isDark) Color.White else Color(0xFF1565C0)
            )

            Spacer(modifier = Modifier.height(if (isLandscape) 12.dp else 16.dp))

            forecastDays
                ?.take(3)
                ?.withIndex()
                ?.forEach { (index, day) ->



                    ForecastDayItem(
                        forecastDay = day,
                        isDark = isDark,
                        isLandscape = isLandscape
                    )

                    if (index < 2) {
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(
                            color = if (isDark) Color.White.copy(alpha = 0.1f)
                            else Color(0xFF1A1C1E).copy(alpha = 0.1f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
        }
    }
}

@Composable
fun ForecastDayItem(
    forecastDay: ForecastDay,
    isDark: Boolean,
    isLandscape: Boolean
) {
    val translatedCondition = remember(forecastDay.day.condition.text) {
        WeatherTranslations.translate(forecastDay.day.condition.text)
    }

    val date = remember(forecastDay.date) {
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("EEE, dd MMM", Locale("id", "ID"))
            val parsed = inputFormat.parse(forecastDay.date)
            parsed?.let { outputFormat.format(it) } ?: forecastDay.date
        } catch (_: Exception) {
            forecastDay.date
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                date,
                fontSize = if (isLandscape) 13.sp else 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = StackSansText,
                color = if (isDark) Color.White else Color(0xFF1A1C1E)
            )
            Text(
                translatedCondition,
                fontSize = if (isLandscape) 11.sp else 12.sp,
                fontFamily = StackSansText,
                color = if (isDark) Color.White.copy(alpha = 0.7f)
                else Color(0xFF1A1C1E).copy(alpha = 0.7f)
            )
        }

        AsyncImage(
            model = "https:${forecastDay.day.condition.icon}",
            contentDescription = null,
            modifier = Modifier.size(if (isLandscape) 40.dp else 48.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "${forecastDay.day.maxtemp_c.toInt()}°",
                fontSize = if (isLandscape) 16.sp else 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = StackSansText,
                color = if (isDark) Color.White else Color(0xFF1565C0)
            )
            Text(
                "${forecastDay.day.mintemp_c.toInt()}°",
                fontSize = if (isLandscape) 14.sp else 16.sp,
                fontFamily = StackSansText,
                color = if (isDark) Color.White.copy(alpha = 0.5f)
                else Color(0xFF1A1C1E).copy(alpha = 0.5f)
            )
        }
    }
}


// Compact temperature unit untuk landscape
@Composable
fun TempUnitCompact(value: String, unit: String, isDark: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = StackSansText,
            color = if (isDark) Color.White.copy(alpha = 0.7f)
            else Color(0xFF1A1C1E).copy(alpha = 0.7f)
        )
        Text(
            unit,
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = StackSansText,
            color = if (isDark) Color.White.copy(alpha = 0.5f)
            else Color(0xFF1A1C1E).copy(alpha = 0.5f)
        )
    }
}

@Composable
fun TempUnit(value: String, unit: String, isDark: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = StackSansText,
            color = if (isDark) Color.White.copy(alpha = 0.7f) else Color(0xFF1A1C1E).copy(alpha = 0.7f)
        )
        Text(
            unit,
            fontSize = 11.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = StackSansText,
            color = if (isDark) Color.White.copy(alpha = 0.5f) else Color(0xFF1A1C1E).copy(alpha = 0.5f)
        )
    }
}

@Composable
fun QuickInfoItemSimple(
    icon: ImageVector,
    label: String,
    value: String,
    isDark: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = if (isDark) Color.White else Color(0xFF1565C0)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = StackSansText,
            color = if (isDark) Color.White else Color(0xFF1565C0)
        )

        Text(
            text = label,
            fontSize = 10.sp,
            fontFamily = StackSansText,
            color = if (isDark) Color.White.copy(alpha = 0.6f)
            else Color(0xFF1A1C1E).copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun QuickInfoItem(
    icon: ImageVector,
    label: String,
    value: String,
    isDark: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = if (isDark) Color.White else Color(0xFF1565C0)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = StackSansText,
            color = if (isDark) Color.White else Color(0xFF1565C0)
        )

        Text(
            text = label,
            fontSize = 10.sp,
            fontFamily = StackSansText,
            color = if (isDark) Color.White.copy(alpha = 0.6f)
            else Color(0xFF1A1C1E).copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun WeatherSearchPage(viewModel: WeatherViewModel, onBackToHome: () -> Unit) {
    var city by remember { mutableStateOf("") }
    val weatherResult = viewModel.weatherResult.observeAsState()
    val locationSuggestions = viewModel.locationSuggestions.observeAsState(emptyList())
    val keyboardController = LocalSoftwareKeyboardController.current
    var showSuggestions by remember { mutableStateOf(false) }
    val isDark = isSystemInDarkTheme()

    val hasSearched = weatherResult.value != null && weatherResult.value !is NetworkResponse.Idle
    val colors = MaterialTheme.colorScheme

    // Debounce untuk search suggestions
    LaunchedEffect(city) {
        if (city.length >= 2) {
            kotlinx.coroutines.delay(300)
            viewModel.searchLocationSuggestions(city)
        } else {
            viewModel.clearSuggestions()
        }
    }

    // Handle back press
    BackHandler(enabled = true) {
        if (hasSearched) {
            viewModel.resetWeatherResult()
            city = ""
        } else {
            onBackToHome()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = if (isDark) {
                        listOf(
                            Color(0xFF0A1929),
                            Color(0xFF1565C0),
                            Color(0xFF1E88E5)
                        )
                    } else {
                        listOf(
                            Color(0xFF42A5F5),
                            Color(0xFF1E88E5),
                            Color(0xFF1565C0)
                        )
                    }
                )
            )
    ) {
        // Weather-based animation effect
        when (val result = weatherResult.value) {
            is NetworkResponse.Success -> {
                WeatherAmbience(result.data.current.condition.text)
            }
            else -> {
                // No animation when loading or error
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (!hasSearched) Arrangement.Center else Arrangement.Top
        ) {

            if (!hasSearched) {
                // Header: Logo di kiri, Nama App di kanan
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.halo_weather_icon),
                        contentDescription = "Halo Weather Logo",
                        modifier = Modifier.size(60.dp)
                    )

                    Text(
                        stringResource(id = R.string.search_city),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = StackSansText,
                        color = Color.White
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(40.dp))
            }

            // Search Box dengan Suggestions sebagai Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                // Card Search Input
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .zIndex(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = city,
                            onValueChange = { newValue ->
                                city = newValue
                                showSuggestions = newValue.length >= 2
                            },
                            modifier = Modifier.weight(1f),
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.search_label),
                                    color = Color(0xFF1A1C1E).copy(alpha = 0.5f),
                                    fontFamily = StackSansText
                                )
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF1E88E5),
                                focusedLabelColor = Color(0xFF1E88E5),
                                unfocusedBorderColor = Color(0xFF1A1C1E).copy(alpha = 0.2f),
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            ),
                            singleLine = true,
                            textStyle = androidx.compose.ui.text.TextStyle(
                                fontFamily = StackSansText,
                                color = Color(0xFF1A1C1E)
                            )
                        )
                        IconButton(
                            onClick = {
                                if (city.isNotEmpty()) {
                                    viewModel.getData(city)
                                    showSuggestions = false
                                    viewModel.clearSuggestions()
                                }
                                keyboardController?.hide()
                            },
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .size(56.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFF1E88E5),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = Color.White,
                                    modifier = Modifier.padding(14.dp)
                                )
                            }
                        }
                    }
                }

                // Suggestions Dropdown - Positioned as Overlay
                if (showSuggestions && locationSuggestions.value.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 76.dp)
                            .heightIn(max = 250.dp)
                            .zIndex(2f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                        ) {
                            locationSuggestions.value.forEach { suggestion ->
                                SuggestionItem(
                                    suggestion = suggestion,
                                    onClick = {
                                        city = suggestion.name
                                        showSuggestions = false
                                        viewModel.getData(suggestion.name)
                                        viewModel.clearSuggestions()
                                        keyboardController?.hide()
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Crossfade(
                targetState = weatherResult.value,
                animationSpec = tween(500),
                label = "weather_crossfade"
            ) { result ->
                when (result) {

                    NetworkResponse.Idle -> {
                        // Idle state
                    }

                    NetworkResponse.Loading ->
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 3.dp
                        )

                    is NetworkResponse.Error ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFFEBEE)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    result.message,
                                    color = Color(0xFFC62828),
                                    fontFamily = StackSansText,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                    is NetworkResponse.Success -> {
                        WeatherDetailsModern(result.data)
                    }

                    null -> {
                        // Null state
                    }
                }
            }
        }
    }
}

@Composable
fun SuggestionItem(
    suggestion: LocationSuggestion,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color(0xFF1E88E5),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    suggestion.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = StackSansText,
                    color = Color(0xFF1A1C1E)
                )
                Text(
                    "${suggestion.region}${if (suggestion.region.isNotEmpty()) ", " else ""}${suggestion.country}",
                    fontSize = 12.sp,
                    fontFamily = StackSansText,
                    color = Color(0xFF1A1C1E).copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun WeatherDetailsModern(data: WeatherModel) {
    val translatedCondition = remember(data.current.condition.text) {
        WeatherTranslations.translate(data.current.condition.text)
    }
    val tempCelsius = data.current.temp_c
    val tempFahrenheit = TemperatureUtils.celsiusToFahrenheit(tempCelsius)
    val tempKelvin = TemperatureUtils.celsiusToKelvin(tempCelsius)
    val tempReamur = TemperatureUtils.celsiusToReamur(tempCelsius)
    val isDark = isSystemInDarkTheme()
    val isLandscape = isLandscape()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(vertical = if (isLandscape) 4.dp else 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header Card dengan Location
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(if (isLandscape) 20.dp else 24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) Color(0xFF1565C0) else Color(0xFF1E88E5)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(if (isLandscape) 16.dp else 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(if (isLandscape) 40.dp else 48.dp)
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(if (isLandscape) 10.dp else 12.dp)
                    )
                }
                Spacer(modifier = Modifier.width(if (isLandscape) 12.dp else 16.dp))
                Column {
                    Text(
                        data.location.name,
                        fontSize = if (isLandscape) 18.sp else 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = StackSansText,
                        color = Color.White
                    )
                    Text(
                        data.location.country,
                        fontSize = if (isLandscape) 12.sp else 14.sp,
                        fontFamily = StackSansText,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(if (isLandscape) 12.dp else 16.dp))

        // Main Weather Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(if (isLandscape) 20.dp else 24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) {
                    Color.White.copy(alpha = 0.12f)
                } else {
                    Color.White.copy(alpha = 0.95f)
                }
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            if (isLandscape) {
                // LANDSCAPE: Horizontal layout
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left: Temperature & Icon
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                "${tempCelsius.toInt()}°",
                                fontSize = 64.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Color.White else Color(0xFF1565C0),
                                fontFamily = StackSansText,
                                letterSpacing = (-3).sp
                            )
                            Text(
                                translatedCondition,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = StackSansText,
                                color = if (isDark) Color.White.copy(alpha = 0.9f)
                                else Color(0xFF1A1C1E).copy(alpha = 0.85f)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        AsyncImage(
                            model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
                            contentDescription = null,
                            modifier = Modifier.size(90.dp)
                        )
                    }

                    // Right: Quick Info
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        // Temperature Conversions
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TempUnit(TemperatureUtils.formatTemp(tempFahrenheit), "°F", isDark)
                            Text("•", color = if (isDark) Color.White.copy(alpha = 0.3f)
                            else Color(0xFF1A1C1E).copy(alpha = 0.3f), fontSize = 12.sp)
                            TempUnit(TemperatureUtils.formatTemp(tempKelvin), "K", isDark)
                            Text("•", color = if (isDark) Color.White.copy(alpha = 0.3f)
                            else Color(0xFF1A1C1E).copy(alpha = 0.3f), fontSize = 12.sp)
                            TempUnit(TemperatureUtils.formatTemp(tempReamur), "°R", isDark)
                        }

                        HorizontalDivider(
                            color = if (isDark) Color.White.copy(alpha = 0.2f)
                            else Color(0xFF1A1C1E).copy(alpha = 0.2f)
                        )

                        // Quick stats
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            QuickStat(stringResource(R.string.humidity), "${data.current.humidity}%", isDark)
                            QuickStat(stringResource(R.string.wind), "${data.current.wind_kph.toInt()} km/h", isDark)
                            QuickStat(stringResource(R.string.visibility), "${data.current.vis_km} km", isDark)
                        }
                    }
                }
            } else {
                // PORTRAIT: Original vertical layout
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "${tempCelsius.toInt()}°",
                        fontSize = 96.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color.White else Color(0xFF1565C0),
                        fontFamily = StackSansText,
                        letterSpacing = (-4).sp
                    )

                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TempUnit(TemperatureUtils.formatTemp(tempFahrenheit), "°F", isDark)
                        Text("•", color = if (isDark) Color.White.copy(alpha = 0.3f)
                        else Color(0xFF1A1C1E).copy(alpha = 0.3f), fontSize = 14.sp)
                        TempUnit(TemperatureUtils.formatTemp(tempKelvin), "K", isDark)
                        Text("•", color = if (isDark) Color.White.copy(alpha = 0.3f)
                        else Color(0xFF1A1C1E).copy(alpha = 0.3f), fontSize = 14.sp)
                        TempUnit(TemperatureUtils.formatTemp(tempReamur), "°R", isDark)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    AsyncImage(
                        model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
                        contentDescription = null,
                        modifier = Modifier.size(120.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        translatedCondition,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = StackSansText,
                        color = if (isDark) Color.White.copy(alpha = 0.9f)
                        else Color(0xFF1A1C1E).copy(alpha = 0.85f)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        QuickInfoItemSimple(
                            Icons.Default.WaterDrop,
                            stringResource(R.string.humidity),
                            "${data.current.humidity}%",
                            isDark
                        )
                        QuickInfoItemSimple(
                            Icons.Default.Air,
                            stringResource(R.string.wind),
                            "${data.current.wind_kph.toInt()} km/h",
                            isDark
                        )
                        QuickInfoItemSimple(
                            Icons.Default.Visibility,
                            stringResource(R.string.visibility),
                            "${data.current.vis_km} km",
                            isDark
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(if (isLandscape) 12.dp else 16.dp))

        // Additional Info Card - Compact untuk landscape
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(if (isLandscape) 20.dp else 24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) {
                    Color.White.copy(alpha = 0.12f)
                } else {
                    Color.White.copy(alpha = 0.95f)
                }
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(if (isLandscape) 20.dp else 24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WeatherKeyValModern(stringResource(R.string.uv_index), data.current.uv.toString(), isDark)
                    WeatherKeyValModern(stringResource(R.string.precipitation), "${data.current.precip_mm} mm", isDark)
                    WeatherKeyValModern(stringResource(R.string.pressure), "${data.current.pressure_mb} mb", isDark)
                }

                Spacer(modifier = Modifier.height(if (isLandscape) 16.dp else 20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WeatherKeyValModern(stringResource(R.string.feels_like), "${data.current.feelslike_c}°C", isDark)
                    WeatherKeyValModern(stringResource(R.string.time), data.location.localtime.split(" ")[1], isDark)
                    WeatherKeyValModern(stringResource(R.string.date), data.location.localtime.split(" ")[0], isDark)
                }
            }
        }

        Spacer(modifier = Modifier.height(if (isLandscape) 8.dp else 16.dp))
    }
}

@Composable
fun QuickStat(label: String, value: String, isDark: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = StackSansText,
            color = if (isDark) Color.White else Color(0xFF1565C0)
        )
        Text(
            label,
            fontSize = 10.sp,
            fontFamily = StackSansText,
            color = if (isDark) Color.White.copy(alpha = 0.6f)
            else Color(0xFF1A1C1E).copy(alpha = 0.6f)
        )
    }
}

@Composable
fun WeatherKeyValModern(label: String, value: String, isDark: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(6.dp)
    ) {
        Text(
            value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = StackSansText,
            color = if (isDark) Color.White else Color(0xFF1565C0)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            label,
            fontSize = 11.sp,
            fontFamily = StackSansText,
            color = if (isDark) Color.White.copy(alpha = 0.6f) else Color(0xFF1A1C1E).copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun WeatherAmbience(condition: String) {
    when {
        condition.contains("rain", true) || condition.contains("drizzle", true) || condition.contains("shower", true) -> RainEffect()
        condition.contains("snow", true) || condition.contains("sleet", true) || condition.contains("blizzard", true) -> SnowEffect()
        condition.contains("cloud", true) || condition.contains("overcast", true) || condition.contains("mist", true) || condition.contains("fog", true) -> CloudEffect()
        condition.contains("thunder", true) || condition.contains("storm", true) -> ThunderstormEffect()
        condition.contains("clear", true) || condition.contains("sunny", true) -> SunEffect()
        else -> {
            // No special effect for other conditions
        }
    }
}

@Composable
fun RainEffect() {
    val drops = remember {
        List(60) {
            Triple(
                Random.nextFloat(),
                Random.nextFloat(),
                3f + Random.nextFloat() * 2f
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "rain")
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "rain_progress"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        drops.forEach { (xPos, initialY, speed) ->
            val progress = (animationProgress + initialY) % 1f
            val yPos = progress * size.height

            drawLine(
                color = Color(0x6689CFF0),
                start = Offset(xPos * size.width, yPos),
                end = Offset(xPos * size.width, yPos + 25f),
                strokeWidth = 2.5f,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun SnowEffect() {
    val flakes = remember {
        List(40) {
            Triple(
                Random.nextFloat(),
                Random.nextFloat(),
                2f + Random.nextFloat() * 1.5f
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "snow")
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "snow_progress"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        flakes.forEach { (xPos, initialY, flakeSize) ->
            val progress = (animationProgress + initialY) % 1f
            val yPos = progress * size.height

            drawCircle(
                color = Color(0xCCE3F2FD),
                radius = flakeSize,
                center = Offset(xPos * size.width, yPos)
            )
        }
    }
}

@Composable
fun CloudEffect() {
    val clouds = remember {
        List(8) {
            Triple(
                Random.nextFloat(),
                Random.nextFloat() * 0.6f,
                40f + Random.nextFloat() * 60f
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "cloud")
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(30000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "cloud_progress"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        clouds.forEach { (initialX, yPos, cloudSize) ->
            val progress = (animationProgress + initialX) % 1f
            val xPos = progress * (size.width + cloudSize * 2) - cloudSize

            val alpha = when {
                progress < 0.1f -> progress * 10
                progress > 0.9f -> (1 - progress) * 10
                else -> 1f
            }

            drawCircle(
                color = Color.White.copy(alpha = 0.15f * alpha),
                radius = cloudSize,
                center = Offset(xPos, yPos * size.height)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.12f * alpha),
                radius = cloudSize * 0.8f,
                center = Offset(xPos + cloudSize * 0.6f, yPos * size.height)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.1f * alpha),
                radius = cloudSize * 0.7f,
                center = Offset(xPos - cloudSize * 0.4f, yPos * size.height)
            )
        }
    }
}

@Composable
fun SunEffect() {
    val infiniteTransition = rememberInfiniteTransition(label = "sun")
    val glow by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "sun_glow"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val centerX = size.width * 0.85f
        val centerY = size.height * 0.15f
        val maxRadius = minOf(size.width, size.height) / 4f

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0x33FFEB3B),
                    Color(0x22FFC107),
                    Color(0x11FFB300),
                    Color.Transparent
                ),
                center = Offset(centerX, centerY),
                radius = maxRadius * glow
            ),
            radius = maxRadius * glow,
            center = Offset(centerX, centerY)
        )
    }
}

@Composable
fun ThunderstormEffect() {
    val drops = remember {
        List(80) {
            Triple(
                Random.nextFloat(),
                Random.nextFloat(),
                4f + Random.nextFloat() * 2f
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "thunderstorm")
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "rain_progress"
    )

    val lightningAlpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                0f at 0
                0f at 1000
                1f at 1050
                0f at 1100
                0f at 2000
                0.8f at 2030
                0f at 2060
            },
            repeatMode = RepeatMode.Restart
        ), label = "lightning"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        // Rain effect
        drops.forEach { (xPos, initialY, speed) ->
            val progress = (animationProgress + initialY) % 1f
            val yPos = progress * size.height

            drawLine(
                color = Color(0x8889CFF0),
                start = Offset(xPos * size.width, yPos),
                end = Offset(xPos * size.width, yPos + 30f),
                strokeWidth = 2.5f,
                cap = StrokeCap.Round
            )
        }

        // Lightning effect
        if (lightningAlpha > 0.1f) {
            drawRect(
                color = Color.White.copy(alpha = 0.3f * lightningAlpha),
                size = size
            )
        }
    }
}

@Composable
fun WeatherDetails(data: WeatherModel) {
    val colors = MaterialTheme.colorScheme

    val translatedCondition = WeatherTranslations.translate(data.current.condition.text)

    val tempCelsius = data.current.temp_c
    val tempFahrenheit = TemperatureUtils.celsiusToFahrenheit(tempCelsius)
    val tempKelvin = TemperatureUtils.celsiusToKelvin(tempCelsius)
    val tempReamur = TemperatureUtils.celsiusToReamur(tempCelsius)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF1565C0),
                                Color(0xFF1976D2),
                                Color(0xFF1E88E5)
                            )
                        )
                    )
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.2f),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            data.location.name,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = StackSansText,
                            color = Color.White
                        )
                        Text(
                            data.location.country,
                            fontSize = 15.sp,
                            fontFamily = StackSansText,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "${tempCelsius.toInt()}°",
                fontSize = 88.sp,
                fontWeight = FontWeight.Bold,
                color = colors.primary,
                fontFamily = StackSansText,
                style = androidx.compose.ui.text.TextStyle(
                    shadow = Shadow(
                        color = colors.primary.copy(alpha = 0.3f),
                        offset = Offset(0f, 4f),
                        blurRadius = 8f
                    )
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colors.primaryContainer.copy(alpha = 0.3f)
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TemperatureConversion(
                        value = TemperatureUtils.formatTemp(tempFahrenheit),
                        unit = "°F",
                        colors = colors
                    )

                    Text(
                        "•",
                        color = colors.onSurface.copy(alpha = 0.3f),
                        fontSize = 14.sp
                    )

                    TemperatureConversion(
                        value = TemperatureUtils.formatTemp(tempKelvin),
                        unit = "K",
                        colors = colors
                    )

                    Text(
                        "•",
                        color = colors.onSurface.copy(alpha = 0.3f),
                        fontSize = 14.sp
                    )

                    TemperatureConversion(
                        value = TemperatureUtils.formatTemp(tempReamur),
                        unit = "°R",
                        colors = colors
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            shape = RoundedCornerShape(24.dp),
            color = colors.primaryContainer.copy(alpha = 0.3f),
            modifier = Modifier.size(140.dp)
        ) {
            AsyncImage(
                model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
                contentDescription = null,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            translatedCondition,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = StackSansText,
            color = colors.onBackground
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = colors.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WeatherKeyVal(R.string.humidity, "${data.current.humidity}%")
                    WeatherKeyVal(R.string.wind, "${data.current.wind_kph} km/h")
                    WeatherKeyVal(R.string.uv_index, data.current.uv.toString())
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = colors.onSurface.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WeatherKeyVal(R.string.precipitation, "${data.current.precip_mm} mm")
                    WeatherKeyVal(R.string.pressure, "${data.current.pressure_mb} mb")
                    WeatherKeyVal(R.string.feels_like, "${data.current.feelslike_c}°C")
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = colors.onSurface.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WeatherKeyVal(R.string.visibility, "${data.current.vis_km} km")
                    WeatherKeyVal(R.string.time, data.location.localtime.split(" ")[1])
                    WeatherKeyVal(R.string.date, data.location.localtime.split(" ")[0])
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun TemperatureConversion(
    value: String,
    unit: String,
    colors: ColorScheme
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            value,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = StackSansText,
            color = colors.onSurface.copy(alpha = 0.8f)
        )
        Text(
            unit,
            fontSize = 11.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = StackSansText,
            color = colors.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun WeatherKeyVal(key: Int, value: String) {
    val colors = MaterialTheme.colorScheme

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(6.dp)
    ) {
        Text(
            value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = StackSansText,
            color = colors.primary
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = stringResource(id = key),
            fontSize = 11.sp,
            fontFamily = StackSansText,
            color = colors.onSurface.copy(alpha = 0.6f)
        )
    }
}