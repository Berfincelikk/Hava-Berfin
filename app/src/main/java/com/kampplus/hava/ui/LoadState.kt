package com.kampplus.hava.ui

/**
 * Bir ağ isteğinin bitebileceği dört durum.
 *
 * Ekran her an bu dört durumdan **tam olarak birindedir**:
 * - [Loading] : Veri bekleniyor. Kullanıcı bir şeylerin olduğunu görmeli (Shimmer / gösterge).
 * - [Success] : Veri geldi, içinde kayıt var.
 * - [Empty]   : Cevap geldi ama içi boş. En çok unutulan durum budur.
 * - [Error]   : Bir şey ters gitti. Anlaşılır mesaj + "Tekrar dene".
 *
 * `sealed` = durum listesi kapalıdır. Ekranı çizerken `when` içinde bir dalı
 * unutursan Kotlin derleme hatası verir; durum atlamak imkânsız hâle gelir.
 *
 * Tip jeneriktir: `out T` sayesinde hem şehir listesi hem de tahmin ekranı aynı
 * yapıyı paylaşır; dört durumu her ekran için ayrı ayrı yazmak gerekmez.
 */
sealed interface LoadState<out T> {

    data object Loading : LoadState<Nothing>

    data class Success<T>(val data: T) : LoadState<T>

    data object Empty : LoadState<Nothing>

    /** [message] kullanıcıya gösterilecek, insan dilinde yazılmış metindir. */
    data class Error(val message: String) : LoadState<Nothing>
}
