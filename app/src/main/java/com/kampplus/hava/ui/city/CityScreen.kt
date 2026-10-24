package com.kampplus.hava.ui.city

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kampplus.hava.R
import com.kampplus.hava.data.CityCatalog
import com.kampplus.hava.model.City
import com.kampplus.hava.ui.component.EmptyView
import com.kampplus.hava.ui.theme.HavaTheme

private const val TAG = "CityScreen"

/**
 * Şehir listesi ekranı, arama kutusuyla (Uygulama 3 · slayt 55).
 *
 * Arama metni burada tutulur; hem [SearchBar] hem de liste onu kullanır.
 * Slayt 56: state'i, onu kullanan bileşenlerin **ortak üst bileşenine** taşı.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityScreen(modifier: Modifier = Modifier, cities: List<City> = CityCatalog.cities) {
    // `remember` yeniden çizimler arasında hatırlar ama ekran dönünce unutur.
    // `rememberSaveable` değeri Bundle'a yazar; Activity yeniden kurulsa da geri gelir
    //. Deneme: yaz, telefonu yan çevir, metin yerinde mi?
    var query by rememberSaveable { mutableStateOf("") }

    // Her tuş vuruşunda yeniden hesaplanır. `query` değişince Compose bu satırı
    // tekrar çalıştırır ve liste süzülür (recomposition).
    val results = cities.filter { it.name.contains(query.trim(), ignoreCase = true) }

    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SearchBar(
                query = query,
                onQueryChange = { newQuery -> query = newQuery },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                // Arama hiçbir şehirle eşleşmediyse boş liste yerine açıklama gösterilir.
                if (results.isEmpty()) {
                    EmptyView(
                        title = "Şehir bulunamadı",
                        message = "Yazımı kontrol et ya da başka bir şehir dene."
                    )
                } else {
                    CityList(
                        cities = results,
                        onCityClick = { city -> Log.d(TAG, "Tıklandı: ${city.name}") }
                    )
                }
            }
        }
    }
}

/**
 * Arama kutusu.
 *
 * State tutmaz: değeri yukarıdan alır, her değişikliği yukarıya bildirir
 * ("değer aşağı, olay yukarı" — state hoisting). Bu sayede her yerde yeniden
 * kullanılabilir ve @Preview'da kolayca denenir.
 */
@Composable
private fun SearchBar(query: String, onQueryChange: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.search_hint)) },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        trailingIcon = {
            // Temizle düğmesi yalnızca kutu doluyken görünür.
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Filled.Clear, contentDescription = stringResource(R.string.action_clear))
                }
            }
        },
        singleLine = true
    )
}

/**
 * Kaydırılabilir şehir listesi.
 *
 * `LazyColumn` tembeldir: 81 şehir olsa da yalnızca ekranda görünen satırlar
 * çizilir, kaybolanlar geri dönüştürülür.
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
