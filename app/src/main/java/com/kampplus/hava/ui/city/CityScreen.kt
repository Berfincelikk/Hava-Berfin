package com.kampplus.hava.ui.city

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kampplus.hava.R
import com.kampplus.hava.data.CityCatalog
import com.kampplus.hava.model.City
import com.kampplus.hava.ui.theme.HavaTheme

private const val TAG = "CityScreen"

/**
 * Şehir listesi ekranı (Uygulama 2).
 *
 * Şimdilik veriyi doğrudan [CityCatalog]'dan okuyor. Uygulama 3'te bu sorumluluk
 * bir ViewModel'e taşınacak.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityScreen(modifier: Modifier = Modifier, cities: List<City> = CityCatalog.cities) {
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) }
    ) { innerPadding ->
        CityList(
            cities = cities,
            onCityClick = { city ->
                // Logcat'te `CityScreen` etiketiyle filtreleyip görebilirsin.
                Log.d(TAG, "Tıklandı: ${city.name}")
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

/**
 * Kaydırılabilir şehir listesi.
 *
 * `LazyColumn` tembeldir: 81 şehir olsa da yalnızca ekranda görünen satırlar
 * çizilir, kaybolanlar geri dönüştürülür. Normal bir `Column` olsaydı 81 kartın
 * tamamı baştan çizilirdi.
 *
 * `key = { it.plate }`: liste değiştiğinde Compose hangi satırın hangisi olduğunu
 * bilir; gereksiz yeniden çizim yapmaz.
 */
@Composable
private fun CityList(cities: List<City>, onCityClick: (City) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = cities, key = { it.plate }) { city ->
            CityCard(
                name = city.name,
                plate = city.plate,
                onClick = { onCityClick(city) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CityScreenPreview() {
    HavaTheme {
        CityScreen(cities = CityCatalog.cities.take(5))
    }
}
