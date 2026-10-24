# Hava

Bilişim Kampı Android eğitiminin referans projesi.

Bu branch (`cp3-bitis`) Uygulama 4'ün çıktısıdır: şehirler artık koda gömülü değil,
Open-Meteo geocoding servisinden geliyor. Her istek dört durumdan biriyle bitiyor —
Loading (Shimmer), Success (liste), Empty ("Şehir bulunamadı"), Error (mesaj +
"Tekrar dene").

**Mimari:** UI (Composable) → ViewModel (StateFlow) → Repository (suspend) → Retrofit

## API — Open-Meteo (anahtar gerekmez)

`https://geocoding-api.open-meteo.com/v1/search?name=Ankara&count=10&language=tr`

Sonuç yoksa yanıtta `results` alanı **hiç gelmez** → Boş (Empty) durumu.

**Teknoloji:** Kotlin · Jetpack Compose (Material 3) · ViewModel + StateFlow · Coroutines · Retrofit + Gson

## Çalıştırma

```bash
./gradlew assembleDebug        # debug APK
./gradlew testDebugUnitTest    # unit testler
./gradlew spotlessApply        # ktlint formatı
```

Android SDK yolunu `local.properties` içinde `sdk.dir=...` ile veya `ANDROID_HOME`
ortam değişkeniyle verin.

## Yol haritası

Her ders bloğunun sonunda projenin olması gereken hâli ayrı bir branch'tedir.
`cpN` = **Uygulama N+1**.

| Branch | Karşılığı | İçerik |
|---|---|---|
| `cp1-baslangic` | — | Boş proje, "Merhaba Kamp!" |
| `cp1-bitis` | Uygulama 2 | Sabit şehir listesi |
| `cp2-bitis` | Uygulama 3 | Arama, state, ViewModel |
| `cp3-bitis` | Uygulama 4 | API ve dört durum |
| `cp4-bitis` | Uygulama 5 | Tahmin ekranı ve cila |
