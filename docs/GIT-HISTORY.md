# Git Stratejisi ve Commit Tarihçesi

## Branch yapısı

`main` üzerinde doğrusal bir geçmiş vardır. Checkpoint'ler bu geçmişteki belirli
commit'lere işaret eden salt-okunur branch'lerdir; ayrı bir geliştirme hattı değildirler.

```
main ──●──●──●──●──●──●──●──●──●──●──●──●──●──●──●──●──●──●──●──●──●──●──●──●──●──●──●
       │        │              │                 │                                  │
   cp1-baslangic│         cp2-bitis          cp3-bitis                         cp4-bitis
             cp1-bitis    cp3-baslangic      cp4-baslangic                        (= main)
             cp2-baslangic
```

`cpN` = **Uygulama N+1** (kamp programındaki uygulama oturumu). Bir bloğun bitiş
branch'i, bir sonraki bloğun başlangıç branch'iyle aynı commit'i gösterir.

| Branch | Karşılığı | Commit |
|---|---|---|
| `cp1-baslangic` | — | `docs: add readme with checkpoint roadmap` |
| `cp1-bitis` / `cp2-baslangic` | Uygulama 2 | `docs: describe the static city list checkpoint` |
| `cp2-bitis` / `cp3-baslangic` | Uygulama 3 | `docs: describe the search and viewmodel checkpoint` |
| `cp3-bitis` / `cp4-baslangic` | Uygulama 4 | `docs: describe the api and ui states checkpoint` |
| `cp4-bitis` / `main` | Uygulama 5 | `docs: document architecture and checkpoints` |

`archive/v1-clean-arch` branch'i, projenin önceki Clean Architecture + Hilt + Room
sürümünün tüm geçmişini (61 commit, Git Flow, feature branch'leri) saklar. Yeni geçmişle
ortak atası yoktur.

Katılımcılar kendi çalışmalarını `katilimci/<ad>/cpN` branch'lerine kaydeder.

## Commit biçimi

Conventional Commits: `<type>(<scope>): <özet>`

- **type:** `feat`, `fix`, `refactor`, `test`, `docs`, `build`, `ci`, `chore`
- **scope:** `city`, `forecast`, `ui`, `nav`, `release`
- Özet İngilizce, küçük harfle, emir kipinde yazılır.

## Sıralı tarihçe

### İskelet → `cp1-baslangic`

```
chore: initialize android project with compose template
docs: add readme with checkpoint roadmap
```

Empty Activity şablonu, Material 3 teması (Color/Theme/Type), uygulama simgesi.
Ekranda yalnızca "Merhaba Kamp!" yazar. `MainActivity` yaşam döngüsü geri çağrılarını
Logcat'e yazar.

### Uygulama 2 — sabit liste → `cp1-bitis` / `cp2-baslangic`

```
feat(city): add City model and static catalog of 81 cities
feat(city): add CityCard with plate badge
feat(city): render scrollable city list with LazyColumn
docs: describe the static city list checkpoint
```

`City(name, plate)` modeli ve koda gömülü 81 il. `CityCard` yapısı:
`Card` → `Row` → plaka rozeti → `Column(weight(1f))` → sağ ok. Liste
`LazyColumn` ile çizilir; karta dokununca Logcat'e `Tıklandı: Ankara` yazılır.

### Uygulama 3 — arama ve state → `cp2-bitis` / `cp3-baslangic`

```
feat(city): add search field and filter the list as the user types
feat(city): show empty view when no city matches
refactor(city): move search state into CityViewModel
docs: describe the search and viewmodel checkpoint
```

Önce arama metni ekranda `rememberSaveable` ile tutulur. Ardından
`refactor` commit'i state'i `CityViewModel`'e taşır: `MutableStateFlow` + `asStateFlow()`,
ekranda `collectAsStateWithLifecycle`. Aynı commit `CityRoute` / `CityScreen`
ayrımını getirir — ekran artık ViewModel tanımaz ve `@Preview`'da çalışır.

### Uygulama 4 — API ve dört durum → `cp3-bitis` / `cp4-baslangic`

```
build: add retrofit and gson converter
feat(city): declare internet permission
feat(city): replace static catalog with open-meteo geocoding api
feat(ui): add LoadState with loading, success, empty and error
feat(ui): add shimmer placeholder and error view
feat(city): map search result to the four ui states
test(city): cover gson mapping and the four ui states
docs: describe the api and ui states checkpoint
```

Dört adım sırasıyla: bağımlılık → izin → `CityApi` arayüzü → `ApiClient`.
`City` modeli geocoding yanıtına göre yeniden yazılır (`@SerializedName`), `CityCatalog`
silinir, yerine `CityRepository` gelir. `LoadState` dört durumu kapatır; `CityViewModel`
`try/catch` ile hatayı insan diline çevirir.

### Uygulama 5 — tahmin ekranı ve cila → `cp4-bitis` / `main`

```
build: add navigation compose
feat(forecast): add forecast models and wmo condition mapping
feat(forecast): add open-meteo forecast api and repository
feat(forecast): add forecast screen with hourly and daily sections
feat(nav): navigate from city list to forecast detail
test(forecast): cover column to row mapping and ui states
build(release): add signing config and r8 keep rules for gson
ci: add pull request pipeline and template
docs: document architecture and checkpoints
```

Şehre dokununca ikinci ekran açılır: anlık durum, 24 saatlik ve 7 günlük tahmin.
Tahmin yanıtı sütun biçimindedir; `ForecastResponse.toForecast()` satırlara çevirir.
Release imzalama ve R8 keep kuralları eklenir.

## Her checkpoint derlenir

Checkpoint branch'lerinin tamamı `./gradlew assembleDebug` ile doğrulanmıştır.
`cp2-bitis` ve sonrasında `testDebugUnitTest` de geçer.

```bash
for b in cp1-baslangic cp1-bitis cp2-bitis cp3-bitis cp4-bitis; do
  git checkout "$b" && ./gradlew assembleDebug || break
done
```
