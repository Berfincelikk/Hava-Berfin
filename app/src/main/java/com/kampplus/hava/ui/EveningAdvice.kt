package com.kampplus.hava.ui

import com.kampplus.hava.model.Forecast
import java.time.LocalTime

private val EVENING_START = LocalTime.of(20, 0)
private val EVENING_END = LocalTime.of(23, 0)

/**
 * Bir şehrin "bu akşam" (20:00-23:00, şehrin yerel saati) açık hava sinemasına
 * uygun olup olmadığını hesaplar.
 *
 * Akşamın her saati için ayrı karar verilir, en kötü saat sonucu belirler:
 * 20:00 açık ama 22:00 yağmurluysa sonuç "Gitme" olur.
 */
fun eveningCinemaAdvice(forecast: Forecast): CinemaAdvice {
    val today = forecast.current.observedAt.toLocalDate()
    val wind = forecast.current.windSpeedKmh ?: 0.0

    val eveningHours = forecast.hourly.filter {
        it.time.toLocalDate() == today &&
            !it.time.toLocalTime().isBefore(EVENING_START) &&
            !it.time.toLocalTime().isAfter(EVENING_END)
    }

    // Akşam saatleri verisi yoksa (örneğin gece geç saatte açıldıysa) anlık havaya bak.
    if (eveningHours.isEmpty()) {
        return cinemaAdvice(
            weatherCode = forecast.current.weatherCode,
            temperature = forecast.current.temperatureC,
            windKmh = wind
        )
    }

    // Her saat için karar ver, en kötüsünü seç (GO < MAYBE < SKIP sırasıyla tanımlı).
    val worst = eveningHours
        .map { cinemaAdvice(it.weatherCode, it.temperatureC, wind) }
        .maxByOrNull { it.verdict.ordinal }!!

    // Yağış olasılığı yüksekse, hava kodu açık görünse bile kararı sertleştir.
    val maxRainChance = eveningHours.mapNotNull { it.precipitationProbability }.maxOrNull() ?: 0
    return when {
        maxRainChance >= 60 ->
            CinemaAdvice(CinemaVerdict.SKIP, "Akşam yağmur ihtimali %$maxRainChance.")
        maxRainChance >= 30 && worst.verdict == CinemaVerdict.GO ->
            CinemaAdvice(CinemaVerdict.MAYBE, "Akşam yağmur ihtimali %$maxRainChance, şemsiye al.")
        else -> worst
    }
}
