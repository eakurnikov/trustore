package com.eakurnikov.trustore.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.eakurnikov.trustore.TrustoreApp
import com.eakurnikov.trustore.ui.theme.TrustoreTheme
import javax.inject.Inject

class TrustoreActivity : ComponentActivity() {

    @Inject
    lateinit var viewModel: TrustoreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TrustoreApp).appComponent.inject(this)

        enableEdgeToEdge()
        setContent {
            TrustoreTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TrustoreAppBar() }
                ) { innerPadding: PaddingValues ->
                    TrustoreScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
