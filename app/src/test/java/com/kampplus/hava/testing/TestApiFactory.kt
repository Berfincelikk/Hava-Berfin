package com.kampplus.hava.testing

import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Testlerde gerçek Open-Meteo yerine yerel bir sahte sunucuya bağlanan Retrofit kurar.
 *
 * Böylece testler internetsiz çalışır, her zaman aynı sonucu verir ve
 * Gson eşlemesini gerçek JSON üzerinden doğrular.
 */
internal inline fun <reified T> MockWebServer.createApi(): T = Retrofit.Builder()
    .baseUrl(url("/"))
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(T::class.java)

/** `src/test/resources` altındaki bir JSON dosyasını metin olarak okur. */
internal fun readResource(name: String): String = checkNotNull(object {}.javaClass.classLoader?.getResourceAsStream(name)) {
    "Test kaynağı bulunamadı: $name"
}.bufferedReader().use { it.readText() }
