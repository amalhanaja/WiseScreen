package com.amalcodes.wisescreen.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.databinding.ActivityMainBinding
import com.amalcodes.wisescreen.presentation.foundation.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding by autoCleared()

    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.appBarComposeView.setContent {
            AppTheme {
                val navController = findNavController(R.id.nav_host_fragment_container)
                val (appBarTitle, setAppBarTitle) = remember { mutableStateOf<CharSequence>("") }
                val (hasBackButton, setHasBackButton) = remember { mutableStateOf(false) }
                DisposableEffect(key1 = navController) {
                    val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
                        if (destination.isDialog) return@OnDestinationChangedListener
                        setHasBackButton(
                            when (destination.id) {
                                R.id.homeFragment, R.id.requestEnableSettingsFragment -> false
                                else -> true
                            }
                        )
                        setAppBarTitle(destination.label ?: getString(R.string.app_name))
                    }
                    navController.addOnDestinationChangedListener(listener)
                    onDispose {
                        navController.removeOnDestinationChangedListener(listener)
                    }
                }
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = appBarTitle.toString())
                    },
                    navigationIcon = navigationIcon@{
                        if (!hasBackButton) return@navigationIcon
                        IconButton(
                            onClick = { navController.navigateUp() }
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_arrow_back),
                                contentDescription = "Back",
                                colorFilter = ColorFilter.tint(color = LocalContentColor.current)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                )
            }
        }
    }
}