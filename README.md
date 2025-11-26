# WeatherApp

WeatherApp adalah aplikasi cuaca sederhana berbasis Android (Jetpack Compose) yang menampilkan informasi cuaca saat ini dan prakiraan 3 hari.

Pembuat: Narendra

## Ringkasan fitur
- Menampilkan cuaca saat ini (temperature, kondisi cuaca, kelembapan, kecepatan angin, visibilitas, dsb).
- Prakiraan cuaca 3 hari.
- Deteksi lokasi saat ini (menggunakan Google Fused Location API bila permission diberikan), fallback ke kota default (Surakarta).
- Pencarian lokasi dengan saran otomatis (autocomplete) menggunakan endpoint search.
- Antarmuka yang menyesuaikan warna (ColorScheme) berdasarkan kondisi cuaca (mis. hujan, berawan, cerah) dan mendukung mode terang / gelap.
- Efek visual (rain, snow, cloud, sun, thunder) berdasarkan kondisi cuaca.
- Dukungan layout portrait & landscape.

## API
Aplikasi menggunakan WeatherAPI (https://www.weatherapi.com/) via REST dan Retrofit.

Base URL:
```
https://api.weatherapi.com/v1/
```

Endpoints yang digunakan (implementasi di `WeatherApi.kt`):
- `GET current.json` — fetch weather by query (`q`) atau koordinat (lat,lon)
- `GET forecast.json` — fetch current + forecast (dipanggil dengan `days=3`)
- `GET search.json` — autocomplete/pencarian lokasi

API key saat ini tersimpan di `app/src/main/java/com/example/weather_app/api/Constant.kt`.

Catatan keamanan: sebaiknya pindahkan API key ke mekanisme yang lebih aman (keystore, gradle properties, server proxy) sebelum merilis publik.

## Struktur penting kode
- `app/src/main/java/com/example/weather_app/WeatherPage.kt` — UI utama (Compose) dan berbagai komponen halaman.
- `app/src/main/java/com/example/weather_app/WeatherViewModel.kt` — ViewModel untuk load data dari API & lokasi.
- `app/src/main/java/com/example/weather_app/api/` — berisi `Retrofitlnstance.kt`, `WeatherApi.kt`, `Constant.kt`, dan model API.
- `app/src/main/java/com/example/weather_app/ui/theme/WeatherColors.kt` — pemilihan ColorScheme berdasarkan kondisi cuaca dan mode gelap/terang.

## Cara build dan jalankan (pengembangan)
1. Pastikan Android Studio terpasang (stable) dengan plugin Kotlin dan emulator atau perangkat fisik.
2. Buka project di Android Studio.
3. Sync Gradle.
4. Jalankan app di emulator / perangkat.

Command line (opsional):
```powershell
# dari root project
.\gradlew assembleDebug
.\gradlew installDebug
```

## Cara testing fitur warna & kondisi cuaca
- Untuk menguji skema warna, jalankan aplikasi dan cari atau set lokasi yang memiliki kondisi cuaca berbeda (rain, cloud, clear). UI akan mengubah ColorScheme sesuai kondisi.
- Untuk menguji dark mode: aktifkan dark mode di emulator atau pengaturan sistem.
- Untuk menguji landscape: putar emulator / perangkat.

## Catatan dan rekomendasi
- Pindahkan API key dari `Constant.kt` ke `local.properties` / `gradle.properties` atau server backend untuk menghindari ekspos kunci di repo publik.
- Tambahkan unit tests/instrumentation tests untuk model parsing & ViewModel.
- Tambahkan lebih banyak resolusi ikon (mdpi/hdpi/xhdpi) bila ingin dukungan perangkat lengkap.



