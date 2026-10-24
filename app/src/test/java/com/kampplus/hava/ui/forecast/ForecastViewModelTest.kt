package com.kampplus.hava.ui.forecast

import androidx.lifecycle.SavedStateHandle
import com.kampplus.hava.testing.FakeWeatherRepository
import com.kampplus.hava.testing.MainDispatcherRule
import com.kampplus.hava.ui.LoadState
import java.io.IOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * Tahmin ekranının durumlarını ve rota parametrelerini okuyuşunu doğrular.
 *
 * `SavedStateHandle` doğrudan oluşturulabildiği için navigasyonu ayağa kaldırmadan
 * test edebiliyoruz.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ForecastViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeWeatherRepository()

    private fun createViewModel(
        name: String = "Ankara",
        subtitle: String = "Ankara, Türkiye",
        latitude: String = "39.91987",
        longitude: String = "32.85427"
    ) = ForecastViewModel(
        savedStateHandle = SavedStateHandle(
            mapOf(
                ForecastViewModel.ARG_NAME to name,
                ForecastViewModel.ARG_SUBTITLE to subtitle,
                ForecastViewModel.ARG_LATITUDE to latitude,
                ForecastViewModel.ARG_LONGITUDE to longitude
            )
        ),
        repository = repository
    )

    @Test
    fun `rota parametreleri okunur`() {
        val viewModel = createViewModel()

        assertEquals("Ankara", viewModel.cityName)
        assertEquals("Ankara, Türkiye", viewModel.subtitle)
    }

    @Test
    fun `baslangicta Loading gosterilir`() = runTest {
        val viewModel = createViewModel()

        assertEquals(LoadState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `tahmin gelince Success durumuna gecilir`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("Success bekleniyordu, gelen: $state", state is LoadState.Success)
        assertEquals(21.0, (state as LoadState.Success).data.current.temperatureC, 0.01)
    }

    @Test
    fun `baglanti hatasi Error durumuna cevrilir`() = runTest {
        repository.error = IOException("no network")

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(
            "İnternet bağlantını kontrol et.",
            (viewModel.uiState.value as LoadState.Error).message
        )
    }

    @Test
    fun `eksik current blogu Error durumuna cevrilir`() = runTest {
        // ForecastResponse.toForecast() bozuk yanıtta IllegalStateException fırlatır.
        repository.error = IllegalStateException("Forecast response has no current block")

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is LoadState.Error)
    }

    @Test
    fun `tekrar dene yeni istek baslatir`() = runTest {
        repository.error = IOException("no network")
        val viewModel = createViewModel()
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value is LoadState.Error)

        repository.error = null
        viewModel.onRetry()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is LoadState.Success)
        assertEquals(2, repository.callCount)
    }

    @Test
    fun `gecersiz koordinat uygulamayi cokertmez`() = runTest {
        val viewModel = createViewModel(latitude = "abc", longitude = "")
        advanceUntilIdle()

        // toDoubleOrNull() null dönünce 0.0 kullanılır; istek yine de yapılır.
        assertTrue(viewModel.uiState.value is LoadState.Success)
    }
}
