**Weather-App**

<p align="center">
	<img src="app/src/main/res/drawable/weather_app_text.png" alt="Weather-App Logo" width="360" />
</p>

**Deskripsi singkat**

Weather-App adalah aplikasi cuaca modern untuk Android, dibangun dengan Jetpack Compose. Aplikasi mendukung tema terang dan gelap, serta skema warna dinamis yang menyesuaikan tampilan berdasarkan kondisi cuaca saat ini.

**Screenshot Utama (Tampilan Aplikasi)**

<p align="center">
	<img src="README/image%20copy.png" alt="Tampilan 1" width="360" style="margin-right:10px;" />
	<img src="README/image%20copy%206.png" alt="Tampilan 2" width="360" />
</p>

**Fitur Utama**
- **Tema**: Dukungan Light & Dark mode otomatis.
- **Dynamic Colors**: Warna UI berubah mengikuti kondisi cuaca (cerah, hujan, mendung, dll.).
- **Current Weather**: Tampilan suhu, kondisi, lokasi, dan ikon cuaca.
- **Forecast**: Prakiraan beberapa hari ke depan.
- **Pencarian Lokasi**: Autocomplete untuk mencari lokasi.
- **Arsitektur**: Jetpack Compose + ViewModel + Retrofit.

**Preview Seluruh Tampilan Aplikasi**

Berikut koleksi tampilan aplikasi (semua screenshot tersedia di folder `README/`):

<p align="center">
	<img src="README/image%20copy%202.png" alt="Screen 2" width="300" style="margin:6px" />
	<img src="README/image%20copy%203.png" alt="Screen 3" width="300" style="margin:6px" />
	<img src="README/image%20copy%204.png" alt="Screen 4" width="300" style="margin:6px" />
	<img src="README/image%20copy%205.png" alt="Screen 5" width="300" style="margin:6px" />
	<img src="README/image%20copy%206.png" alt="Screen 6" width="300" style="margin:6px" />
</p>

**Instalasi & Build (Singkat)**
- Buka project di Android Studio, sinkronkan Gradle, lalu jalankan di emulator atau perangkat.
- Untuk build via CLI (Windows PowerShell):

```powershell
.\gradlew assembleDebug
.\gradlew installDebug
```

Catatan: jika memakai PowerShell di Windows gunakan `./gradlew` atau `./gradlew.bat`.

**Petunjuk Penggunaan Singkat**
- Aplikasi otomatis menyesuaikan tema sesuai pengaturan sistem.
- Warna latar dan aksen akan berubah mengikuti kondisi cuaca untuk pengalaman visual yang konsisten.

**Kontribusi**
- Ingin menambahkan fitur atau memperbaiki bug? Fork repo, buat branch baru, lalu buka pull request.

**Catatan Teknis**
- Sumber ikon/logo: `app/src/main/res/drawable/weather_app_text.png`.
- Screenshot & preview disimpan di `README/`.
- Simpan API key di `local.properties` atau server, jangan di-commit.

---

Terima kasih sudah melihat project ini — kalau mau, saya bisa:
- Menyusun badge build/coverage
- Menambahkan folder `screenshots/` dan memindahkan preview
- Menulis panduan deploy Play Store
