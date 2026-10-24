package com.kampplus.hava.data

import com.kampplus.hava.model.City
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Şehir verisine erişimin tek kapısı.
 *
 * ViewModel'in görevi state yönetmek, Repository'nin görevi veriyi getirmektir.
 * ViewModel verinin internetten mi yoksa sabit bir listeden mi geldiğini bilmez;
 * bu sayede veri kaynağı değişse de ViewModel'e dokunmak gerekmez.
 *
 * Arayüz olarak tanımlanmıştır ki testlerde sahte (fake) bir uygulama verilebilsin.
 */
interface CityRepository {
    /** İsme göre şehir arar. Sonuç yoksa boş liste döner. */
    suspend fun search(query: String): List<City>
}

/**
 * Gerçek uygulama: aramayı Open-Meteo geocoding servisine sorar.
 *
 * @param api Varsayılanı [ApiClient.cityApi]; testlerde sahte bir API verilebilir.
 * @param ioDispatcher Ağ işlerinin çalışacağı thread havuzu.
 */
class OpenMeteoCityRepository(
    private val api: CityApi = ApiClient.cityApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CityRepository {

    override suspend fun search(query: String): List<City> = withContext(ioDispatcher) {
        // `suspend` tek başına thread değiştirmez. İşi arka plana almak
        // için withContext ile IO dispatcher'ına geçiyoruz.
        val response = api.search(query.trim())
        // Sonuç bulunamazsa servis "results" alanını hiç göndermez; orEmpty() bunu
        // boş listeye çevirir ve ViewModel bunu "Empty" durumu olarak yorumlar.
        response.results.orEmpty()
    }
}
