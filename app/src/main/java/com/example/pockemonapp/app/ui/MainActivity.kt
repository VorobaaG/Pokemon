package com.example.pockemonapp.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pockemonapp.app.ui.screen.HomeBody
import com.example.pockemonapp.app.ui.screen.HomeScreen
import com.example.pockemonapp.app.ui.theme.PockemonAppTheme
import com.example.pockemonapp.app.ui.viewModel.HomeBodyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val homeBodyViewModel: HomeBodyViewModel by viewModels()
        enableEdgeToEdge()
        setContent {
            PockemonAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(
                        homeModifier = Modifier.padding(innerPadding),
                        vm = homeBodyViewModel)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PockemonAppTheme {
        Greeting("Android")
    }
}