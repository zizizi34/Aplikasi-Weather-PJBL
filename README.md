# Weather-App

<p align="center">
	<img src="app/src/main/res/drawable/weather_app_text.png" alt="Weather-App Logo" width="360" />
</p>



Weather-App adalah aplikasi cuaca untuk Android yang dirancang dengan fokus pada pengalaman pengguna (UX) yang bersih dan responsif. Aplikasi dibangun dengan Jetpack Compose dan menyediakan dukungan tema terang/gelap serta skema warna dinamis yang menyesuaikan dengan kondisi cuaca.

## Fitur

- Dukungan tema Light & Dark
- Skema warna dinamis berdasarkan kondisi cuaca (cerah, hujan, mendung, dll.)
- Tampilan cuaca saat ini (suhu, kondisi, ikon, lokasi)
- Prakiraan beberapa hari ke depan
- Pencarian lokasi dengan autocomplete
- Implementasi modern: Jetpack Compose, ViewModel, Retrofit

## Tampilan Utama

<p align="center">
	<img src="README/image%20copy.png" alt="Tampilan Aplikasi 1" width="420" style="margin-right:12px;" />
	<img src="README/image%20copy%206.png" alt="Tampilan Aplikasi 2" width="420" />
</p>

## Galeri
# Weather-App

<p align="center">
	<img src="app/src/main/res/drawable/weather_app_text.png" alt="Weather-App Logo" width="360" />
</p>

Weather-App adalah aplikasi cuaca untuk Android yang dirancang dengan fokus pada tampilan yang bersih, performa yang baik, dan pengalaman pengguna yang konsisten. Aplikasi dibangun menggunakan Jetpack Compose dan dilengkapi dukungan tema terang/gelap serta penyesuaian warna berdasarkan kondisi cuaca.

## Fitur Utama

- Dukungan Light & Dark theme
- Warna UI dinamis mengikuti kondisi cuaca (clear, rain, cloudy, dll.)
- Informasi cuaca saat ini: suhu, kondisi, ikon, lokasi
- Prakiraan beberapa hari ke depan
- Pencarian lokasi dengan fitur autocomplete
- Arsitektur modern: Jetpack Compose, ViewModel, Retrofit

## Preview Seluruh Tampilan Aplikasi

<p align="center">
	<img src="README/image%20copy.png" alt="Tampilan 1" width="300" style="margin:6px" />
	<img src="README/image%20copy%202.png" alt="Tampilan 2" width="300" style="margin:6px" />
	<img src="README/image%20copy%203.png" alt="Tampilan 3" width="300" style="margin:6px" />
	<img src="README/image%20copy%204.png" alt="Tampilan 4" width="300" style="margin:6px" />
	<img src="README/image%20copy%205.png" alt="Tampilan 5" width="300" style="margin:6px" />
	<img src="README/image%20copy%206.png" alt="Tampilan 6" width="300" style="margin:6px" />
</p>

## Arsitektur & Teknologi

- Jetpack Compose (UI)
- Android ViewModel / State untuk manajemen UI
- Retrofit + OkHttp untuk komunikasi jaringan
- Modular yang sederhana untuk kemudahan perawatan kode

## Persyaratan Sistem

- Android Studio (direkomendasikan versi terbaru)
- JDK 11 atau lebih baru
- Gradle wrapper (termasuk di repositori)

## Build & Jalankan (Windows PowerShell)

Jalankan dari direktori root project:

```powershell
cd <path-ke-project>
.\gradlew assembleDebug
.\gradlew installDebug
```

Atau buka project di Android Studio, sinkronkan Gradle, lalu jalankan konfigurasi `app` pada emulator atau perangkat fisik.

## Konfigurasi API

Aplikasi menggunakan **WeatherAPI.com** untuk mendapatkan data cuaca real-time. Layanan ini menyediakan informasi cuaca akurat dengan coverage global.

### WeatherAPI.com

**Dokumentasi:** https://www.weatherapi.com/docs/

**Base URL:**
```
https://api.weatherapi.com/v1/
```


## Struktur Proyek (singkat)

```
app/
	├─ src/main/java/com/example/weather_app/
	├─ ui/theme/        # definisi warna dan tema
	└─ api/             # model, Retrofit client, dan endpoint
```

## Kontribusi

Kontribusi diterima melalui fork → branch → pull request. Sertakan deskripsi perubahan, langkah untuk mereproduksi, dan contoh bila relevan.


## Kontak

Pemilik / Developer: Narendra


