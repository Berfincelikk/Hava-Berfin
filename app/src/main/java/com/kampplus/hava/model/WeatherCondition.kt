package com.kampplus.hava.model

/**
 * Hava durumunun ekrandaki karşılığı: bir emoji ve Türkçe bir etiket.
 *
 * Open-Meteo, sıcaklığın yanında bir de `weather_code` gönderir. Bu kod WMO 4677
 * standardındadır ve tek başına bir sayıdır (0, 61, 95...). Kullanıcıya sayı
 * gösteremeyiz; [fromWeatherCode] ile anlaşılır bir görünüme çeviririz.
 */
enum class WeatherCondition(val emoji: String, val label: String) {
    CLEAR("☀️", "Açık"),
    MAINLY_CLEAR("🌤️", "Az bulutlu"),
    PARTLY_CLOUDY("⛅", "Parçalı bulutlu"),
    OVERCAST("☁️", "Kapalı"),
    FOG("🌫️", "Sisli"),
    DRIZZLE("🌦️", "Çisenti"),
    RAIN("🌧️", "Yağmurlu"),
    SNOW("🌨️", "Karlı"),
    RAIN_SHOWERS("🌦️", "Sağanak yağış"),
    SNOW_SHOWERS("🌨️", "Kar sağanağı"),
    THUNDERSTORM("⛈️", "Gök gürültülü fırtına"),
    UNKNOWN("🌡️", "Bilinmiyor");

    companion object {
        /**
         * WMO 4677 kod tablosu. `when` ifadesi birden çok kodu tek bir görünüme eşler
         * (örneğin 61, 63 ve 65 hafif/orta/şiddetli yağmurdur; üçü de "Yağmurlu").
         *
         * Tanınmayan bir kod gelirse uygulama çökmez, [UNKNOWN] ile devam eder.
         */
        fun fromWeatherCode(code: Int): WeatherCondition = when (code) {
            0 -> CLEAR
            1 -> MAINLY_CLEAR
            2 -> PARTLY_CLOUDY
            3 -> OVERCAST
            45, 48 -> FOG
            51, 53, 55, 56, 57 -> DRIZZLE
            61, 63, 65, 66, 67 -> RAIN
            71, 73, 75, 77 -> SNOW
            80, 81, 82 -> RAIN_SHOWERS
            85, 86 -> SNOW_SHOWERS
            95, 96, 99 -> THUNDERSTORM
            else -> UNKNOWN
        }
    }
}
