package com.kampplus.hava.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kampplus.hava.ui.theme.HavaTheme

/**
 * "Sonuç yok" durumunun görünümü.
 *
 * Boş ekran bırakmak kullanıcıyı yalnız bırakır: uygulama mı bozuldu, internet mi yok,
 * yoksa gerçekten sonuç mu yok? Bu bileşeni her zaman bir başlık ve mümkünse bir
 * yönlendirme mesajıyla gösteririz.
 */
@Composable
fun EmptyView(title: String, modifier: Modifier = Modifier, message: String? = null) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            // Dekoratif ikon; ekran okuyucu atlasın diye contentDescription = null.
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        // Yardımcı mesaj isteğe bağlıdır; `?.let` yalnızca doluysa çizer.
        message?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyViewPreview() {
    HavaTheme {
        EmptyView(
            title = "Şehir bulunamadı",
            message = "Yazımı kontrol et ya da başka bir şehir dene."
        )
    }
}
