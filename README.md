# Hava — Hava Durumu ve Akşam Planı

Kotlin ve Jetpack Compose ile geliştirilmiş Android hava durumu uygulaması. Open-Meteo üzerinden şehir arar; anlık hava durumunu, saatlik ve 7 günlük tahmini gösterir. Hava durumuna göre sinema ve akşam planı önerileri sunar.

GitHub deposu: [Berfincelikk/Hava-Berfin](https://github.com/Berfincelikk/Hava-Berfin)

Mimari ayrıntıları için [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md).

**Teknoloji:** Kotlin · Jetpack Compose (Material 3) · ViewModel + StateFlow · Coroutines · Retrofit + Gson · Navigation Compose

## Mimari — üç katman

*UI görünümü çizer, ViewModel state'i yönetir, Repository veriye erişir.*

```
UI (Composable)  →  ViewModel (StateFlow)  →  Repository (suspend)  →  Retrofit → Open-Meteo
```

```
com.kampplus.hava
├── MainActivity.kt            Uygulamanın kapısı
├── model/                     Veri neye benzer?
│   ├── City.kt                City + CitySearchResponse
│   ├── Forecast.kt            Forecast + Current/Hourly/Daily
│   └── WeatherCondition.kt    WMO kodu → emoji + Türkçe etiket
├── data/                      Veri nereden gelir?
│   ├── ApiClient.kt           object + Retrofit kurulumu
│   ├── CityApi.kt             @GET v1/search
│   ├── ForecastApi.kt         @GET v1/forecast
│   ├── ForecastResponse.kt    DTO + sütun→satır çevrimi
│   ├── CityRepository.kt      suspend search()
│   └── WeatherRepository.kt   suspend getForecast()
└── ui/                        Ne çizilir, ne zaman değişir?
    ├── LoadState.kt           Loading / Success / Empty / Error
    ├── HavaNavHost.kt         iki ekran arası geçiş
    ├── city/                  CityScreen · CityViewModel · CityCard
    ├── forecast/              ForecastScreen · ForecastViewModel
    ├── component/             ShimmerList · EmptyView · ErrorView
    └── theme/                 Color · Theme · Type
```

## API — Open-Meteo (anahtar gerekmez)

| Amaç | Çağrı |
|---|---|
| Şehir arama | `https://geocoding-api.open-meteo.com/v1/search?name=Ankara&count=10&language=tr` |
| Tahmin | `https://api.open-meteo.com/v1/forecast?latitude=..&longitude=..&current=..&hourly=..&daily=..&forecast_days=7&timezone=auto` |

- Sonuç yoksa arama yanıtında `results` alanı **hiç gelmez** → Boş (Empty) durumu.
- Tahmin yanıtı **sütun** biçimindedir: `hourly.time[i]` ile `hourly.temperature_2m[i]` aynı saate aittir.
- `weather_code` WMO standardındadır (0 açık, 61 yağmur, 95 fırtına…); `WeatherCondition.fromWeatherCode` ile görünüme çevrilir.
- Ücretsiz kullanım ticari olmayan projeler içindir (~10.000 istek/gün).

## Çalıştırma

```bash
./gradlew assembleDebug        # debug APK
./gradlew testDebugUnitTest    # unit testler
./gradlew spotlessApply        # ktlint formatı (denetim için spotlessCheck)
./gradlew assembleRelease      # release APK (R8 açık)
```

Android SDK yolunu `local.properties` içinde `sdk.dir=...` ile veya `ANDROID_HOME` ortam değişkeniyle verin.

## Testler

| Test | Neyi korur |
|---|---|
| `WeatherConditionTest` | WMO kod → görünüm eşlemesi |
| `CityApiTest` | Gson `@SerializedName` eşlemesi (sessizce bozulur) |
| `ForecastApiTest` | Sütun → satır çevrimi (index kayması hata vermez) |
| `CityViewModelTest` | Dört durum + "Tekrar dene" |
| `ForecastViewModelTest` | Rota parametreleri + durumlar |

## Release imzası

`keystore.properties.example` dosyasını `keystore.properties` olarak kopyalayıp doldurun (repoya girmez). Dosya yoksa release APK debug anahtarıyla imzalanır.
