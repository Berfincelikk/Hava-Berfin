# Mimari

Tek `:app` modülü, üç katman: `model/` · `data/` · `ui/`.

```
UI (Composable)  →  ViewModel (StateFlow)  →  Repository (suspend)  →  Retrofit → Open-Meteo
```

Kurallar:

- **UI internete gitmez.** Composable yalnızca aldığı state'i çizer ve olayları yukarı bildirir.
- **ViewModel çizim yapmaz.** Renk veya Composable bilmez; yalnızca state üretir.
- **Repository ekranı bilmez.** Veriyi getirir; nasıl gösterileceği onu ilgilendirmez.

## Neden bu kadar sade?

Proje bilerek küçük tutuldu. Şu yapılar **yoktur**: bağımlılık enjeksiyonu (Hilt),
UseCase katmanı, `Result` sarmalayıcısı + hata eşleyici zinciri, `DataSource` ara katmanı,
domain/DTO/UI üçlü model ayrımı, favoriler ve Room.

Bunların hepsi gerçek projelerde yerinde tercihlerdir; ama üç katmanı anlamadan
eklendiklerinde kodu takip etmeyi zorlaştırırlar. Önce akışı öğren, katmanları
ihtiyaç doğdukça ekle.

`LoadState<out T>` jenerik tutuldu: iki ekran aynı dört durumu paylaştığı için tip
bir kez yazıldı.

## Veri akışı

```
Retrofit → Repository (suspend, withContext(IO)) → ViewModel (try/catch → LoadState)
         → Route (collectAsStateWithLifecycle) → Screen (stateless) → Composable
```

1. Kullanıcı arama kutusuna yazar → `onQueryChange` → `_query.value` güncellenir.
2. Enter'a basar → `search()` → `viewModelScope.launch` içinde coroutine başlar.
3. `_uiState.value = LoadState.Loading` → ekranda Shimmer görünür.
4. `repository.search(query)` askıya alınır; ana thread serbest kalır.
5. Cevap gelir → liste boşsa `Empty`, doluysa `Success`; hata olursa `catch` → `Error`.
6. `collectAsStateWithLifecycle` yeni değeri okur, Compose ekranı yeniden çizer.

## Hata yönetimi

Hata **ViewModel'de** yakalanır ve orada insan diline çevrilir.
Repository hatayı sarmalamaz, olduğu gibi fırlatır.

| Ne oldu | Kotlin'de | Kullanıcıya |
|---|---|---|
| İnternet yok, zaman aşımı | `IOException` | "İnternet bağlantını kontrol et." |
| Sunucu 4xx/5xx döndü | `retrofit2.HttpException` | "Sunucu şu an cevap vermiyor." |
| Beklenmeyen JSON | `JsonSyntaxException` | "Bir şeyler ters gitti. Tekrar dene." |
| Eksik `current` bloğu | `IllegalStateException` | "Bir şeyler ters gitti. Tekrar dene." |

Teknik ayrıntı her zaman `Log.e(TAG, ..., e)` ile Logcat'e yazılır.

## Dört durum

`LoadState` `sealed` olduğu için `when` dallarından biri unutulursa derleme hatası alınır.
Her ekran dört dalın tamamını karşılar:

| Durum | Liste ekranı | Tahmin ekranı |
|---|---|---|
| `Loading` | `ShimmerList` | `CircularProgressIndicator` |
| `Success` | `LazyColumn` + `CityCard` | Başlık + saatlik + günlük |
| `Empty` | "Şehir bulunamadı" + öneri | "Bu şehir için tahmin bulunamadı." |
| `Error` | Mesaj + "Tekrar dene" | Mesaj + "Tekrar dene" |

## Route / Screen ayrımı

Her ekran iki parçadır:

- **`XRoute`** — ViewModel'i edinir, `collectAsStateWithLifecycle` ile state'i toplar.
- **`XScreen`** — ViewModel tanımaz; aldığı değerleri çizer, olayları yukarı bildirir.

Bu ayrım sayesinde ekranlar `@Preview` içinde ve testte elle state verilerek çalıştırılabilir
(state hoisting — "değer aşağı, olay yukarı").

## Navigasyon

İki rota: `city` (başlangıç) ve `forecast?name=..&subtitle=..&latitude=..&longitude=..`.

Parametreler yol parçası yerine sorgu parametresidir; çünkü `subtitle` boş olabilir ve boş
bir yol parçası rotayı eşleşmez hâle getirir. Değerler `Uri.encode` ile kaçışlanır.
`ForecastViewModel` parametreleri `SavedStateHandle` üzerinden okur.

## Genişletme noktaları

| Senaryo | Eklenir | Değişir | Dokunulmaz |
|---|---|---|---|
| Yeni hava durumu görünümü | `WeatherCondition`'a yeni sabit | `fromWeatherCode` içindeki `when` | Ekranlar, ViewModel'ler |
| Yeni hava değişkeni (ör. UV) | `CurrentResponse` + `CurrentWeather` alanı (varsayılanlı) | `ForecastApi.CURRENT_FIELDS`, ekran | Repository, ViewModel |
| Farklı veri kaynağı | Yeni `CityRepository` implementasyonu | ViewModel'in varsayılan parametresi | Ekranlar, model |
| Sabit şehir listesine dönüş | `CityCatalog` sınıfı | `CityRepository` implementasyonu | Her şey |
