package com.kampplus.hava.testing

import com.kampplus.hava.data.CityRepository
import com.kampplus.hava.model.City

/**
 * Testlerde gerçek repository yerine kullanılan sahteler.
 *
 * Repository'yi arayüz olarak tanımlamamızın sebebi tam olarak budur: ViewModel
 * testleri internete çıkmadan, istenen her sonucu (başarı / boş / hata)
 * kolayca üretebilir.
 *
 * @param result Her çağrıda dönecek liste.
 * @param error Doluysa [search] bu hatayı fırlatır.
 */
class FakeCityRepository(
    var result: List<City> = emptyList(),
    var error: Throwable? = null
) : CityRepository {

    /** Son aranan metin; ViewModel'in doğru sorguyu ilettiğini doğrulamak için. */
    var lastQuery: String? = null
        private set

    /** Kaç kez arama yapıldığı; "Tekrar dene" davranışını doğrulamak için. */
    var callCount: Int = 0
        private set

    override suspend fun search(query: String): List<City> {
        lastQuery = query
        callCount++
        error?.let { throw it }
        return result
    }
}

/** Testlerde tekrar tekrar yazmamak için hazır bir şehir. */
fun sampleCity(id: Long = 323786L, name: String = "Ankara") = City(
    id = id,
    name = name,
    latitude = 39.91987,
    longitude = 32.85427,
    countryCode = "TR",
    country = "Türkiye",
    admin1 = "Ankara"
)
