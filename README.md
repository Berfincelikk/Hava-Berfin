# Hava — Hava Durumu ve Akşam Planı

Kotlin ve Jetpack Compose ile geliştirilmiş Android hava durumu uygulaması. Open-Meteo üzerinden şehir arar; anlık hava durumunu, saatlik ve 7 günlük tahmini gösterir. Hava durumuna göre açık hava sineması için akşam önerisi sunar.

GitHub deposu: [Berfincelikk/Hava-Berfin](https://github.com/Berfincelikk/Hava-Berfin)

Mimari ayrıntıları için [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) dosyasına bakın.

**Teknoloji:** Kotlin · Jetpack Compose · Material 3 · ViewModel · StateFlow · Coroutines · Retrofit · Gson · Navigation Compose

## Özellikler

- Türkçe şehir arama ve şehir seçimi
- Anlık sıcaklık, hissedilen sıcaklık, nem ve rüzgâr bilgisi
- Önümüzdeki 24 saat için saatlik; 7 gün için günlük tahmin
- Yükleniyor, veri, boş sonuç ve hata durumları
- Yeniden deneme ve ekranlar arası gezinme
- 20.00–23.00 yerel saat aralığına göre açık hava sineması önerisi: **Git**, **Belki** veya **Gitme**

### Açık hava sineması önerisi

Tahmin ekranının en üstündeki kart, seçilen şehirde o akşam açık hava sinemasına gidilip gidilemeyeceğini ve kararın nedenini gösterir. Hesaplama `ui/CinemaVerdict.kt` ve `ui/EveningAdvice.kt` dosyalarındadır.

Öneri, şehrin yerel saatine göre **20.00–23.00** arasındaki her saat için ayrı verilir. Saatlerden çıkan en olumsuz sonuç akşamın sonucunu belirler. Bu saatler için tahmin yoksa anlık hava koşulları kullanılır.

| Sonuç | Koşullar |
|---|---|
| **Gitme** 🌧️ | Fırtına, yağmur, kar veya 3°C altı sıcaklık |
| **Belki** 🧥 | Sis, 12°C altı sıcaklık veya 30 km/sa üzeri rüzgâr |
| **Git** 🎬 | Yukarıdaki olumsuz koşullar yoksa |

Saatlik yağış olasılığı da kararı etkiler: **%60 ve üzeri** “Gitme”, **%30–59** ise koşullar uygunsa “Belki” sonucunu verir. “Git” dışında bir sonuçta kart, şehirdeki kapalı sinemaları Google Haritalar’da aramak için Plan B düğmesi gösterir.

## Mimari

*UI görünümü çizer, ViewModel state'i yönetir, Repository veriye erişir.*

```text
UI (Composable) → ViewModel (StateFlow) → Repository (suspend) → Retrofit → Open-Meteo
```

```text
com.kampplus.hava
├── MainActivity.kt             Uygulamanın giriş noktası
├── model/                      Şehir ve tahmin modelleri
│   ├── City.kt
│   ├── Forecast.kt
│   └── WeatherCondition.kt     WMO kodu → emoji ve Türkçe açıklama
├── data/                       API, DTO ve repository katmanı
│   ├── ApiClient.kt
│   ├── CityApi.kt
│   ├── ForecastApi.kt
│   ├── ForecastResponse.kt     API sütunlarını model listelerine dönüştürür
│   ├── CityRepository.kt
│   └── WeatherRepository.kt
└── ui/
    ├── HavaNavHost.kt          Şehir ve tahmin ekranı rotaları
    ├── LoadState.kt            Loading / Success / Empty / Error
    ├── CinemaVerdict.kt        Sinema önerisinin sonuç türleri ve kuralları
    ├── EveningAdvice.kt        Akşam tahmininden öneri üretir
    ├── city/                   Şehir arama ekranı, ViewModel ve kart
    ├── forecast/               Tahmin ekranı ve ViewModel
    ├── component/              Shimmer, boş durum ve hata görünümleri
    └── theme/                  Renk, tema ve tipografi
```

## Veri kaynakları

Uygulama [Open-Meteo](https://open-meteo.com/) servislerini kullanır; API anahtarı gerekmez.

| Amaç | Servis ve parametreler |
|---|---|
| Şehir arama | `geocoding-api.open-meteo.com/v1/search` — `name`, `count`, `language=tr` |
| Hava tahmini | `api.open-meteo.com/v1/forecast` — enlem/boylam, anlık, saatlik ve günlük değişkenler; `forecast_days=7`, `timezone=auto` |

- Şehir bulunamazsa API yanıtında `results` alanı olmayabilir; uygulama bunu boş durum olarak gösterir.
- Tahmin API'si saatlik ve günlük verileri sütunlar hâlinde döndürür. `ForecastResponse` bunları tarihli model listelerine çevirir.
- `weather_code`, WMO hava durumu kodlarıdır ve `WeatherCondition.fromWeatherCode` ile kullanıcıya uygun etikete çevrilir.
- Sinema önerisi, seçilen şehrin yerel saatindeki 20.00–23.00 tahminini değerlendirir. Bu aralık için veri yoksa mevcut hava durumunu kullanır.

## Gereksinimler

- Android Studio ve Android SDK
- JDK 17
- Android 8.0 (API 26) veya üzeri cihaz/emülatör (`minSdk = 26`)

SDK yolunu `local.properties` içindeki `sdk.dir` ile veya `ANDROID_HOME` ortam değişkeniyle tanımlayın. Android Studio'da projeyi açıp Gradle eşitlemesini tamamladıktan sonra uygulamayı `app` yapılandırmasıyla çalıştırabilirsiniz.

## Derleme ve test

Linux/macOS:

```bash
./gradlew assembleDebug
./gradlew testDebugUnitTest
./gradlew spotlessApply     # Kotlin biçimlendirme
./gradlew spotlessCheck     # Biçim denetimi
./gradlew assembleRelease
```

Windows PowerShell:

```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat testDebugUnitTest
.\gradlew.bat spotlessApply
.\gradlew.bat spotlessCheck
.\gradlew.bat assembleRelease
```

Debug APK, `app/build/outputs/apk/debug/app-debug.apk` konumunda oluşturulur. Release derlemesinde R8 küçültme ve kaynak budama açıktır.

## Testler

| Test | Kapsam |
|---|---|
| `WeatherConditionTest` | WMO kodlarının koşul eşlemesi |
| `CityApiTest` | Şehir yanıtının Gson ile çözülmesi |
| `ForecastApiTest` | Tahmin yanıtının DTO'lardan modellere dönüştürülmesi |
| `CityViewModelTest` | Arama ve ekran durumları |
| `ForecastViewModelTest` | Tahmin yükleme, rota parametreleri ve ekran durumları |

## Ders checkpoint'leri

Ders bloklarının sonundaki örnek durumlar ayrı branch'lerde tutulur. `cpN`, **Uygulama N+1** anlamına gelir.

| Branch | Karşılığı | İçerik |
|---|---|---|
| `cp1-baslangic` | — | Boş Empty Activity projesi, “Merhaba Kamp!” |
| `cp1-bitis` / `cp2-baslangic` | Uygulama 2 | Sabit şehir listesi, `CityCard`, `LazyColumn`, tıklama olayı |
| `cp2-bitis` / `cp3-baslangic` | Uygulama 3 | Arama, filtre, boş durum ve `CityViewModel` |
| `cp3-bitis` / `cp4-baslangic` | Uygulama 4 | Retrofit, repository, yükleniyor/boş/hata durumları |
| `cp4-bitis` (`main`) | Uygulama 5 | Tahmin ekranı, navigasyon ve testler |

Katılımcılar yetiştiremedikleri çalışmayı `katilimci/<ad>/cpN` branch'ine kaydedip sonraki bloğun başlangıç branch'inden devam edebilir. `archive/v1-clean-arch` branch'i önceki Clean Architecture + Hilt + Room sürümünü saklar.

## Release imzası

İmzalı release için `keystore.properties.example` dosyasını `keystore.properties` olarak kopyalayıp değerleri doldurun. Gerçek keystore olmadan release derlemesi debug anahtarıyla imzalanır; mağaza dağıtımı için gerçek imza anahtarı gerekir. `keystore.properties` depoya eklenmemelidir.
