package com.kampplus.hava.testing

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * ViewModel testleri için ana thread'i taklit eden JUnit kuralı.
 *
 * `viewModelScope` varsayılan olarak `Dispatchers.Main` üzerinde çalışır; ama JVM
 * testlerinde Android'in main looper'ı yoktur. Bu kural, test süresince Main'i
 * denetlenebilir bir test dispatcher'ı ile değiştirir ve test bitince geri alır.
 *
 * Kullanımı: `@get:Rule val mainDispatcherRule = MainDispatcherRule()`
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
