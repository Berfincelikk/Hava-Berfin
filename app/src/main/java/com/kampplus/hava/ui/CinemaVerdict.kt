package com.kampplus.hava.ui

enum class CinemaVerdict(val emoji: String, val title: String) {
    GO("🎬", "Git"),
    MAYBE("🧥", "Belki"),
    SKIP("🌧️", "Gitme")
}

data class CinemaAdvice(
    val verdict: CinemaVerdict,
    val reason: String
)

/**
 * Akşam saatleri için açık hava sinemasına gidilip gidilmeyeceğine karar verir.
 * weatherCode: WMO kodu (Open-Meteo), temperature: °C, windKmh: km/s
 */
fun cinemaAdvice(weatherCode: Int, temperature: Double, windKmh: Double): CinemaAdvice =
    when {
        weatherCode in 95..99 ->
            CinemaAdvice(CinemaVerdict.SKIP, "Fırtına var, bu akşam evde izle.")
        weatherCode in 51..67 || weatherCode in 80..86 ->
            CinemaAdvice(CinemaVerdict.SKIP, "Yağmur bekleniyor.")
        weatherCode in 71..77 ->
            CinemaAdvice(CinemaVerdict.SKIP, "Kar var, perde donar.")
        temperature < 3 ->
            CinemaAdvice(CinemaVerdict.SKIP, "Çok soğuk (${temperature.toInt()}°C).")
        weatherCode == 45 || weatherCode == 48 ->
            CinemaAdvice(CinemaVerdict.MAYBE, "Sis var, görüntü net olmayabilir.")
        temperature < 12 ->
            CinemaAdvice(CinemaVerdict.MAYBE, "Serin (${temperature.toInt()}°C), battaniye al.")
        windKmh > 30 ->
            CinemaAdvice(CinemaVerdict.MAYBE, "Rüzgâr güçlü, perde sallanabilir.")
        else ->
            CinemaAdvice(CinemaVerdict.GO, "Hava uygun, ${temperature.toInt()}°C. İyi seanslar!")
    }
