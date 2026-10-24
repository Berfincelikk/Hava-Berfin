package com.kampplus.hava.model

/**
 * Bir şehir.
 *
 * Şimdilik yalnızca ad ve plaka kodu tutuyoruz; veri sabit bir listeden geliyor.
 * Uygulama 4'te veri internetten gelmeye başlayınca bu model değişecek.
 *
 * `data class`: Kotlin bu sınıf için `equals`, `hashCode`, `toString` ve `copy`
 * fonksiyonlarını kendisi yazar. Veri taşıyan sınıflarda tercih edilir.
 */
data class City(
    val name: String,
    val plate: Int
)
