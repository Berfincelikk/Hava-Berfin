package com.kampplus.hava.model

import com.google.gson.annotations.SerializedName

/**
 * Open-Meteo geocoding servisinden dönen şehir arama yanıtı.
 *
 * Gson, JSON alanlarını bu data class'ın aynı isimli alanlarına kendisi doldurur;
 * bize düşen yalnızca kalıbı doğru çizmek.
 *
 * Arama sonucu bulunamazsa servis `results` alanını **hiç göndermez**. Bu yüzden
 * alan nullable'dır ve "Boş" (Empty) durumunu temsil eder.
 */
data class CitySearchResponse(
    val results: List<City>?
)

/**
 * Bir şehir. Hem ağ yanıtının modeli hem de ekranların kullandığı modeldir
 * (tek model; ayrı bir DTO katmanı yoktur).
 *
 * @param id Geocoding servisinin şehir kimliği. Navigasyonda şehri taşımak için kullanılır.
 * @param latitude Enlem. Tahmin (forecast) isteği bu koordinatla yapılır.
 * @param longitude Boylam.
 * @param countryCode Ülke kodu, örneğin "TR". Kart üzerindeki rozette gösterilir.
 * @param admin1 Şehrin bağlı olduğu il/bölge. Kartta alt yazı olarak geçer.
 */
data class City(
    val id: Long,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    // JSON'da alt çizgili (country_code), Kotlin'de camelCase (countryCode).
    // @SerializedName iki ismi birbirine bağlar.
    @SerializedName("country_code")
    val countryCode: String?,
    val country: String?,
    // Bazı kayıtlarda bölge bilgisi gelmez; soru işareti uygulamayı çökmekten korur.
    val admin1: String?
) {
    /** Kartta ve tahmin ekranında gösterilen alt yazı: "Ankara, Türkiye". */
    val subtitle: String
        get() = listOfNotNull(admin1, country).distinct().joinToString(", ")
}
