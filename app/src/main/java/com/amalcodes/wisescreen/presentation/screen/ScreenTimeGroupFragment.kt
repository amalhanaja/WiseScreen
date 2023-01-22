package com.amalcodes.wisescreen.presentation.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import com.amalcodes.wisescreen.features.appusage.AppUsagePage
import com.amalcodes.wisescreen.features.appusage.AppUsageUiState
import com.amalcodes.wisescreen.features.appusage.AppUsageViewModel
import com.amalcodes.wisescreen.presentation.foundation.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * @author: AMAL
 * Created On : 31/07/20
 */


@AndroidEntryPoint
class ScreenTimeGroupFragment : Fragment() {

    @ExperimentalCoroutinesApi
    @ExperimentalFoundationApi
    @ExperimentalMaterial3Api
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val appUsageViewModel: AppUsageViewModel = hiltViewModel()
                val appUsageUiState: AppUsageUiState by appUsageViewModel.appUsageUiState.collectAsState()

                AppTheme {
                    AppUsagePage(appUsageUiState = appUsageUiState)
                }
            }
        }
    }
}