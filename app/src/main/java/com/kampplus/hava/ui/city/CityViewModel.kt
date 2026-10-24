package com.kampplus.hava.ui.city

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kampplus.hava.data.CityCatalog
import com.kampplus.hava.model.City
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Şehir listesi ekranının beyni.
 *
 * Görev dağılımı:
 * - Ekran (Composable) yalnızca çizer.
 * - ViewModel state'i tutar ve kararları verir (filtreleme burada).
 * - Repository veriye erişir.
 *
 * Neden ViewModel? Ekran döndüğünde Activity yok edilip yeniden kurulur, ama
 * ViewModel yaşamaya devam eder; arama metni ve sonuçlar kaybolmaz.
 *
 * @param cities Şehir kaynağı. Varsayılanı sabit katalog; Uygulama 4'te internetten gelecek.
 */
class CityViewModel(
    private val cities: List<City> = CityCatalog.cities
) : ViewModel() {

    // State ViewModel'de MutableStateFlow ile tutulur.
    // Değiştirilebilir alan private'tır; dışarıya yalnızca okunabilir hâli açılır.
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    /**
     * Arama metnine göre süzülmüş liste.
     *
     * `map` ile `_query`'den türetilir: metin her değiştiğinde yeniden hesaplanır.
     * `stateIn` akışı bir StateFlow'a çevirir ki ekran her an güncel değeri okuyabilsin.
     */
    val results: StateFlow<List<City>> = _query
        .map { text -> cities.filter { it.name.contains(text.trim(), ignoreCase = true) } }
        .stateIn(
            scope = viewModelScope,
            // Ekran arka plana geçtikten 5 saniye sonra hesaplama durur.
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
            initialValue = cities
        )

    /** Kullanıcı arama kutusuna her harf yazdığında çağrılır. */
    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

    private companion object {
        const val STOP_TIMEOUT_MS = 5_000L
    }
}
