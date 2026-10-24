# Hava

Bilişim Kampı Android eğitiminin referans projesi.

Bu branch (`cp2-bitis`) Uygulama 3'ün çıktısıdır: listenin üstünde bir arama kutusu
vardır, yazdıkça liste süzülür, sonuç yoksa "Şehir bulunamadı" görünür. Arama metni ve
filtreleme `CityViewModel` içinde `StateFlow` ile tutulur; ekran döndüğünde kaybolmaz.

**Mimari:** UI (Composable) → ViewModel (StateFlow) → veri kaynağı

**Teknoloji:** Kotlin · Jetpack Compose (Material 3) · ViewModel + StateFlow · Coroutines

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
