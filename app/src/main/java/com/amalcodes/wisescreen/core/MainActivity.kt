package com.amalcodes.wisescreen.core

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding by autoCleared()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        findNavController(R.id.nav_host_fragment_container).addOnDestinationChangedListener { controller, destination, arguments ->
            val isHomeButtonEnabled: Boolean = destination.id != R.id.homeFragment
            val navIcon: Drawable? = if (isHomeButtonEnabled) getDrawable(R.drawable.ic_arrow_back) else null
            if (!destination.isDialog) {
                binding.toolbar.title = destination.label ?: getString(R.string.app_name)
                binding.toolbar.navigationIcon = navIcon
                binding.toolbar.setNavigationOnClickListener {controller.navigateUp() }
            }
        }
    }
}