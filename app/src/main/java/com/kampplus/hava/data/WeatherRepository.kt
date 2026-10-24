package com.kampplus.hava.data

import com.kampplus.hava.model.Forecast
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Hava tahmini verisine erişimin tek kapısı.
 *
 * [CityRepository] ile aynı desen: arayüz + gerçek uygulama. Tahmin ekranı yalnızca
 * bu arayüzü tanır; Retrofit'i veya JSON'u bilmez.
 */
interface WeatherRepository {
    /** Verilen koordinat için anlık durum + saatlik + günlük tahmini getirir. */
    suspend fun getForecast(latitude: Double, longitude: Double): Forecast
}

/** Gerçek uygulama: tahmini Open-Meteo forecast servisinden alır. */
class OpenMeteoWeatherRepository(
    private val api: ForecastApi = ApiClient.forecastApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : WeatherRepository {

    override suspend fun getForecast(latitude: Double, longitude: Double): Forecast = withContext(ioDispatcher) {
        // Ağ yanıtı sütun biçimindedir; toForecast() onu ekranın beklediği
        // satır biçimine çevirir (bkz. ForecastResponse.kt).
        api.getForecast(latitude = latitude, longitude = longitude).toForecast()
    }
}
