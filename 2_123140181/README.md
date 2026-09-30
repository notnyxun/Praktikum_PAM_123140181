# News Feed Simulator - Praktikum PAM

Aplikasi **News Feed Simulator** dikembangkan menggunakan **Kotlin Multiplatform (KMP)**, **Kotlin Coroutines (Flow & StateFlow)**, dan **Jetpack Compose**. Aplikasi ini mensimulasikan pembaruan feed berita secara *real-time* dengan opsi filter kategori, pencatatan statistik pembaca, dan detail berita asynchronous.

---

## 📱 Tangkapan Layar Saat Aplikasi Berjalan

![Tampilan Aplikasi News Feed Simulator](./screenshot.png)

---

## 🚀 Fitur Utama
1. **Real-Time Data Stream**: Generating data berita secara otomatis setiap 2 detik menggunakan Kotlin `Flow`.
2. **Filter Kategori**: Filter data berita secara dinamis (Teknologi, Olahraga, Bisnis, Hiburan).
3. **Statistik Pembaca**: Menyimpan & memperbarui statistik total berita dan berita dibaca secara *real-time* menggunakan `StateFlow`.
4. **Detail Berita Asynchronous**: Pengambilan data detail berita secara asynchronous menggunakan Kotlin Coroutines.
5. **Kontrol Stream**: Fitur *Pause* dan *Resume* aliran berita.

---

## 🛠️ Cara Menjalankan Kode Kotlin / Aplikasi

### Prasyarat
- **Android Studio** (Ladybug / Jellyfish atau lebih baru).
- **JDK 17** atau versi di atasnya.
- **Android Emulator** (API level 24+) atau **Perangkat Android Fisik** dengan USB Debugging aktif.

---

### Cara 1: Menggunakan Android Studio (Rekomendasi)

1. Buka **Android Studio**.
2. Pilih **Open** lalu pilih folder `2_123140181`.
3. Tunggu hingga proses **Gradle Sync** selesai.
4. Pilih konfigurasi run `androidApp` pada toolbar toolbar atas.
5. Pilih target perangkat (**Emulator** / **Device Fisik**).
6. Klik tombol **Run** (▶) atau tekan `Shift + F10`.

---

### Cara 2: Menggunakan Terminal / Gradle Command Line

Jalankan perintah berikut dari direktori `2_123140181`:

#### 1. Build & Install ke Emulator/Device Connected
- **Windows (PowerShell):**
  ```powershell
  .\gradlew.bat :androidApp:installDebug
  ```
- **Linux / macOS:**
  ```bash
  ./gradlew :androidApp:installDebug
  ```

#### 2. Build APK Debug
- **Windows:**
  ```powershell
  .\gradlew.bat :androidApp:assembleDebug
  ```
- **Linux / macOS:**
  ```bash
  ./gradlew :androidApp:assembleDebug
  ```

#### 3. Menjalankan Unit Test Kode Kotlin (`shared` module)
- **Windows:**
  ```powershell
  .\gradlew.bat :shared:testAndroidHostTest
  ```
- **Linux / macOS:**
  ```bash
  ./gradlew :shared:testAndroidHostTest
  ```

---

## 📁 Struktur Kode Utama

- `androidApp/`: Entri poin aplikasi Android (`MainActivity.kt`).
- `shared/src/commonMain/kotlin/`:
  - `data/NewsRepository.kt`: Logika data stream `Flow` berita.
  - `viewmodel/NewsViewModel.kt`: Pengelolaan state UI dengan `StateFlow`.
  - `ui/NewsScreen.kt`: Tampilan UI Compose Multiplatform.
