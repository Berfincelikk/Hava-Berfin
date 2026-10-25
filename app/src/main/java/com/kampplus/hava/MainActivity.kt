package com.kampplus.hava

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kampplus.hava.ui.HavaNavHost
import com.kampplus.hava.ui.theme.HavaTheme

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "Lifecycle"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate çalıştı")

        // Uygulama her zaman koyu: durum ve gezinme çubuğu simgeleri açık renkli olsun.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )

        setContent {
            HavaTheme {
                HavaNavHost()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart çalıştı")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume çalıştı")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause çalıştı")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop çalıştı")
    }
}
