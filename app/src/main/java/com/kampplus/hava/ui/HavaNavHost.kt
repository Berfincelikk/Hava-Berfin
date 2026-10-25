package com.kampplus.hava.ui

import android.net.Uri
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kampplus.hava.model.CinemaVenue
import com.kampplus.hava.model.CinemaVenues
import com.kampplus.hava.model.City
import com.kampplus.hava.ui.cinema.CinemaDetailScreen
import com.kampplus.hava.ui.cinema.CinemaListScreen
import com.kampplus.hava.ui.city.CityRoute
import com.kampplus.hava.ui.forecast.ForecastRoute
import com.kampplus.hava.ui.forecast.ForecastViewModel

/**
 * Uygulamanın ekranları ve aralarındaki geçiş.
 *
 * Akış: Sinema listesi → Sinema detayı → Hava durumu.
 * Eski şehir arama ekranı rota olarak duruyor ama başlangıç ekranı değil.
 *
 * Parametreleri yol parçası yerine sorgu parametresi olarak taşıyoruz
 * (`forecast?name=...`); çünkü değerler boş olabilir ve boş bir yol
 * parçası rotayı eşleşmez hâle getirir.
 */
@Composable
fun HavaNavHost(modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = CINEMA_LIST_ROUTE,
        modifier = modifier
    ) {
        // Ana ekran: açık hava sinemaları listesi
        composable(CINEMA_LIST_ROUTE) {
            CinemaListScreen(
                onVenueClick = { venue -> navController.navigate("cinema/${Uri.encode(venue.id)}") }
            )
        }

        // 2) Sinema detayı
        composable(
            route = CINEMA_DETAIL_ROUTE,
            arguments = listOf(
                navArgument(ARG_VENUE_ID) { type = NavType.StringType }
            )
        ) { entry ->
            val venue = entry.arguments?.getString(ARG_VENUE_ID)?.let { CinemaVenues.byId(it) }
            if (venue == null) {
                Text("Mekan bulunamadı.")
            } else {
                CinemaDetailScreen(
                    venue = venue,
                    onBack = { navController.popBackStack() },
                    onWeatherClick = { navController.navigate(forecastRouteFor(venue)) }
                )
            }
        }

        // 3) Hava durumu (mevcut ekran, aynen)
        composable(
            route = FORECAST_ROUTE,
            arguments = listOf(
                // Hepsi String: koordinatlar ViewModel içinde Double'a çevrilir.
                navArgument(ForecastViewModel.ARG_NAME) {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument(ForecastViewModel.ARG_SUBTITLE) {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument(ForecastViewModel.ARG_LATITUDE) {
                    type = NavType.StringType
                    defaultValue = "0"
                },
                navArgument(ForecastViewModel.ARG_LONGITUDE) {
                    type = NavType.StringType
                    defaultValue = "0"
                }
            )
        ) {
            ForecastRoute(onBack = { navController.popBackStack() })
        }

        // Eski şehir arama ekranı: şimdilik bağlı değil ama silinmedi.
        composable(CITY_ROUTE) {
            CityRoute(
                onCityClick = { city -> navController.navigate(forecastRoute(city)) }
            )
        }
    }
}

private const val CINEMA_LIST_ROUTE = "cinema_list"
private const val ARG_VENUE_ID = "id"
private const val CINEMA_DETAIL_ROUTE = "cinema/{$ARG_VENUE_ID}"

/** Eski şehir arama ekranının adresi. */
private const val CITY_ROUTE = "city"

/** Tahmin ekranının adres şablonu; süslü parantezler çalışma anında doldurulur. */
private const val FORECAST_ROUTE =
    "forecast?name={name}&subtitle={subtitle}&latitude={latitude}&longitude={longitude}"

/** Demo için şehir merkezi koordinatları (yaklaşık). Mekanın kendi koordinatı yok. */
private fun cityCenter(cityName: String): Pair<Double, Double> = when (cityName) {
    "Ankara" -> 39.9334 to 32.8597
    "Denizli" -> 37.7765 to 29.0864
    "Sakarya" -> 40.7569 to 30.3781
    "Kocaeli" -> 40.7654 to 29.9408
    else -> 41.0082 to 28.9784 // İstanbul
}
/** Bir sinema mekanı için hava durumu adresi: şehrin tahminini gösterir. */
private fun forecastRouteFor(venue: CinemaVenue): String {
    val (lat, lon) = cityCenter(venue.city)
    return buildString {
        append("forecast")
        append("?name=").append(Uri.encode(venue.city))
        append("&subtitle=").append(Uri.encode("${venue.district}, ${venue.city}"))
        append("&latitude=").append(lat)
        append("&longitude=").append(lon)
    }
}

/**
 * Bir şehir için tahmin ekranının gerçek adresini üretir.
 *
 * `Uri.encode`: şehir adındaki boşluk ve Türkçe karakterler adreste sorun
 * çıkarmasın diye kaçışlanır ("Afyonkarahisar, Türkiye" → "Afyonkarahisar%2C%20T%C3%BCrkiye").
 */
private fun forecastRoute(city: City): String = buildString {
    append("forecast")
    append("?name=").append(Uri.encode(city.name))
    append("&subtitle=").append(Uri.encode(city.subtitle))
    append("&latitude=").append(city.latitude)
    append("&longitude=").append(city.longitude)
}
