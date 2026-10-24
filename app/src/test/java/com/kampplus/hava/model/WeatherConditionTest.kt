package com.kampplus.hava.model

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * WMO hava durumu kodlarının doğru görünüme eşlendiğini doğrular.
 *
 * Bu eşleme saf Kotlin'dir: Android, ağ veya veritabanı gerekmez. Bu yüzden
 * testi milisaniyeler içinde çalışır.
 */
class WeatherConditionTest {

    @Test
    fun `acik hava kodu CLEAR dondurur`() {
        assertEquals(WeatherCondition.CLEAR, WeatherCondition.fromWeatherCode(0))
    }

    @Test
    fun `ayni gorunume dusen kodlar gruplanir`() {
        // 61, 63 ve 65 hafif/orta/şiddetli yağmurdur; üçü de "Yağmurlu" gösterilir.
        assertEquals(WeatherCondition.RAIN, WeatherCondition.fromWeatherCode(61))
        assertEquals(WeatherCondition.RAIN, WeatherCondition.fromWeatherCode(63))
        assertEquals(WeatherCondition.RAIN, WeatherCondition.fromWeatherCode(65))
    }

    @Test
    fun `firtina kodlari THUNDERSTORM dondurur`() {
        assertEquals(WeatherCondition.THUNDERSTORM, WeatherCondition.fromWeatherCode(95))
        assertEquals(WeatherCondition.THUNDERSTORM, WeatherCondition.fromWeatherCode(99))
    }

    @Test
    fun `taninmayan kod UNKNOWN dondurur ve uygulama cokmez`() {
        assertEquals(WeatherCondition.UNKNOWN, WeatherCondition.fromWeatherCode(12345))
        assertEquals(WeatherCondition.UNKNOWN, WeatherCondition.fromWeatherCode(-1))
    }

    @Test
    fun `her durumun bir emojisi ve etiketi vardir`() {
        // Boş bir etiket ekranda boşluk olarak görünürdü; hepsinin dolu olduğunu garanti ediyoruz.
        WeatherCondition.entries.forEach { condition ->
            assert(condition.emoji.isNotBlank()) { "${condition.name} için emoji boş" }
            assert(condition.label.isNotBlank()) { "${condition.name} için etiket boş" }
        }
    }
}
