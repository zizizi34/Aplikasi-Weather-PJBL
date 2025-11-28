🌤️ WeatherApp — Modern Android Weather Experience

Cuaca Lokal Anda • Dynamic UI • Light & Dark Mode • Jetpack Compose

<p align="center"> <img src="https://i.ibb.co/ysT8RLH/light.jpg" width="280"> <img src="https://i.ibb.co/6vHbD5d/dark.jpg" width="280"> </p>
<p align="center"> <b>WeatherApp</b> adalah aplikasi cuaca modern berbasis Android Jetpack Compose dengan UI adaptif mengikuti kondisi cuaca dan mode terang/gelap. <br>Dirancang oleh <b>Narendra</b>. </p>
📸 Preview Lengkap Aplikasi

Letakkan file screenshot kamu nanti di folder /screenshots/ pada repo.
Saya sudah menyiapkan layout markdown premium yang tinggal kamu isi.

🏠 Home Screen — Current Weather
<p align="center"> <img src="/screenshots/home_light.jpg" width="280"> <img src="/screenshots/home_dark.jpg" width="280"> </p>
🔍 Search Lokasi
<p align="center"> <img src="/screenshots/search_light.jpg" width="280"> <img src="/screenshots/search_dark.jpg" width="280"> </p>
🌤️ Detail Cuaca — Informasi Lengkap
<p align="center"> <img src="/screenshots/detail_light.jpg" width="280"> <img src="/screenshots/detail_dark.jpg" width="280"> </p>
📅 Prakiraan 3 Hari (Forecast)
<p align="center"> <img src="/screenshots/forecast_light.jpg" width="280"> <img src="/screenshots/forecast_dark.jpg" width="280"> </p>
🎨 Mode Terang & Mode Gelap Otomatis
<p align="center"> <img src="/screenshots/all_light.jpg" width="280"> <img src="/screenshots/all_dark.jpg" width="280"> </p>
✨ Fitur Utama
🎯 1. Current Weather

Suhu (C/F/K/R)

Kondisi cuaca

Ikon animasi

Lokasi & negara

UI yang berubah berdasarkan kondisi (rain, clear, cloudy)

🔮 2. Forecast 3 Hari

Suhu max/min

Status cuaca

Ikon kondisi

📍 3. Deteksi Lokasi Otomatis

Fused Location API

Fallback otomatis ke Surakarta bila izin ditolak

🔍 4. Pencarian Lokasi (Autocomplete)

Real-time suggestions

Menggunakan WeatherAPI search endpoint

🌙 5. Dynamic Theme

Light & dark mode

Color scheme menyesuaikan cuaca

Efek bokeh / ambience background

🧩 Arsitektur & Struktur Project
app/
 ├── api/
 │    ├── WeatherApi.kt
 │    ├── RetrofitInstance.kt
 │    └── Constant.kt
 ├── ui/theme/
 │    ├── WeatherColors.kt
 │    └── Color.kt
 ├── WeatherViewModel.kt
 └── WeatherPage.kt

🔗 API

Menggunakan WeatherAPI.com
Base URL:

https://api.weatherapi.com/v1/


Endpoints:

current.json

forecast.json?days=3

search.json

⚠️ Tips keamanan:
Pindahkan API Key dari kode ke local.properties atau backend server.

▶️ Cara Build & Jalankan
Android Studio

Clone repo

Buka project

Sync Gradle

Klik ▶ Run

CLI
./gradlew assembleDebug
./gradlew installDebug

🧪 Testing & Simulasi Cuaca
Tes	Cara
🌞 Cerah	Cari kota beriklim panas
🌧️ Hujan	Cari kota hujan seperti Bogor
☁️ Berawan	Cari kota seperti London
🌙 Mode Gelap	Aktifkan dark mode sistem
📱 Landscape	Putar perangkat
🚀 Rencana Pengembangan

⏱ Hourly forecast

🗺 Map-based weather

☁ Animasi awan / hujan realtime

🧊 Offline cache (Room)

🧩 Widget Android

❤️ Kontributor

👤 Narendra — Developer & UI/UX
