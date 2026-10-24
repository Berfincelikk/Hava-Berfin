package com.kampplus.hava.ui.city

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kampplus.hava.ui.theme.HavaTheme

/**
 * Listedeki tek bir şehir satırı.
 *
 * Yapısı: `Card` → `Row` → rozet + `Column(weight(1f))` + sağ ok.
 *
 * `Modifier.weight(1f)`: ortadaki sütun kalan boşluğun tamamını kaplar, böylece
 * sağ oku sağ kenara iter. Uzun şehir adları (Afyonkarahisar) rozeti ezmez.
 *
 * @param onClick Karta dokunulduğunda çağrılır. Bileşen ne olacağını bilmez,
 *   yalnızca haber verir.
 */
@Composable
fun CityCard(name: String, plate: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(onClick = onClick, modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PlateBadge(plate = plate)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    // Tek satıra sığmayan ad "..." ile kısaltılır, satır kaymaz.
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Plaka $plate",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                // Ok yalnızca görseldir; "dokunulabilir" bilgisini Card zaten verir.
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Plaka rozeti: iki haneli, sıfırla doldurulmuş kod ("06", "34").
 *
 * `padStart(2, '0')`: 6 → "06". Rozetteki yazı hep aynı genişlikte görünür.
 */
@Composable
private fun PlateBadge(plate: Int, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.size(44.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = plate.toString().padStart(2, '0'),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CityCardPreview() {
    HavaTheme {
        CityCard(name = "Ankara", plate = 6, onClick = {})
    }
}
