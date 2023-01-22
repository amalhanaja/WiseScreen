package com.amalcodes.wisescreen.presentation.screen

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.findNavController
import com.amalcodes.wisescreen.core.Const
import com.amalcodes.wisescreen.features.home.HomePage
import com.amalcodes.wisescreen.features.home.HomeViewModel
import com.amalcodes.wisescreen.presentation.foundation.AppTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment() {

    @ExperimentalPermissionsApi
    @ExperimentalMaterial3Api
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = ComposeView(requireContext()).apply {
        setContent {
            val homeViewModel: HomeViewModel = hiltViewModel()
            val screenTimeSummarySectionUiState by homeViewModel.sectionScreenTimeSummaryUiState.collectAsStateWithLifecycle()
            val sectionConfigUiState by homeViewModel.sectionConfigUiState.collectAsStateWithLifecycle()

            AppTheme {
                HomePage(
                    sectionScreenTimeSummaryUiState = screenTimeSummarySectionUiState,
                    sectionConfigUiState = sectionConfigUiState,
                    toggleScreenTimeManageable = homeViewModel::toggleScreenTimeManagement,
                    togglePin = homeViewModel::disablePin,
                    goToCreatePin = { findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSetupPin()) },
                    goToPinVerification = { key: String, onSuccess: (key: String) -> Unit ->
                        setFragmentResultListener(Const.REQUEST_RESULT_KEY) { _, _ -> onSuccess(key) }
                        findNavController().navigate(HomeFragmentDirections.actionGlobalPinDialog(key))
                    },
                    goToScreenTimeConfig = { findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDailyScreenTimeFragment()) },
                    goToAppLimitConfig = { findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAppLimitFragment()) },
                    goToDetailScreenTime = { findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToScreenTimeGroupFragment()) },
                    goToAppNotificationSetting = {
                        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}