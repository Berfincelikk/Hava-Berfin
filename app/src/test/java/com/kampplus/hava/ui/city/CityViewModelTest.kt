package com.kampplus.hava.ui.city

import com.kampplus.hava.model.City
import com.kampplus.hava.testing.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * Arama ve filtreleme mantığının ViewModel'de doğru çalıştığını doğrular.
 *
 * Filtreleme ViewModel'e taşındığı için ekranı açmadan, saf Kotlin ile test edilebilir.
 * Bu, mantığı Composable içinde bırakmanın aksine mümkün olan en hızlı testtir.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CityViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val cities = listOf(
        City("Ankara", 6),
        City("Antalya", 7),
        City("İstanbul", 34),
        City("Kahramanmaraş", 46)
    )

    /**
     * ViewModel'i oluşturur **ve** `results` akışına abone olur.
     *
     * Neden abone olmak gerekiyor? `results`, `SharingStarted.WhileSubscribed` ile
     * paylaşılır: kimse dinlemiyorken hesaplama yapılmaz ve akış `initialValue`
     * değerinde kalır. Gerçek uygulamada aboneyi ekran olur
     * (`collectAsStateWithLifecycle`); testte bunu elle yapmamız gerekir.
     *
     * `backgroundScope`: test bitince bu coroutine otomatik iptal edilir.
     */
    private fun TestScope.createViewModel() = CityViewModel(cities).also { viewModel ->
        backgroundScope.launch { viewModel.results.collect { } }
    }

    @Test
    fun `baslangicta tum sehirler gosterilir`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(4, viewModel.results.value.size)
        assertEquals("", viewModel.query.value)
    }

    @Test
    fun `yazdikca liste suzulur`() = runTest {
        val viewModel = createViewModel()

        viewModel.onQueryChange("Ank")
        advanceUntilIdle()

        assertEquals(listOf("Ankara"), viewModel.results.value.map { it.name })
    }

    @Test
    fun `arama kelimenin ortasinda da eslesir`() = runTest {
        val viewModel = createViewModel()

        // `contains` baştan değil, herhangi bir yerden eşleşir:
        // "An" hem Ankara hem de istANbul ve kahramANmaraş ile eşleşir.
        viewModel.onQueryChange("An")
        advanceUntilIdle()

        assertEquals(
            listOf("Ankara", "Antalya", "İstanbul", "Kahramanmaraş"),
            viewModel.results.value.map { it.name }
        )
    }

    @Test
    fun `arama buyuk kucuk harf ayrimi yapmaz`() = runTest {
        val viewModel = createViewModel()

        viewModel.onQueryChange("ankara")
        advanceUntilIdle()

        assertEquals(1, viewModel.results.value.size)
    }

    @Test
    fun `bastaki ve sondaki bosluklar yok sayilir`() = runTest {
        val viewModel = createViewModel()

        viewModel.onQueryChange("  Antalya  ")
        advanceUntilIdle()

        assertEquals(1, viewModel.results.value.size)
    }

    @Test
    fun `eslesme yoksa liste bos doner`() = runTest {
        val viewModel = createViewModel()

        viewModel.onQueryChange("xyz")
        advanceUntilIdle()

        // Ekran bu durumda EmptyView gösterir.
        assertTrue(viewModel.results.value.isEmpty())
    }

    @Test
    fun `arama temizlenince tum liste geri gelir`() = runTest {
        val viewModel = createViewModel()
        viewModel.onQueryChange("Ankara")
        advanceUntilIdle()

        viewModel.onQueryChange("")
        advanceUntilIdle()

        assertEquals(4, viewModel.results.value.size)
    }
}
