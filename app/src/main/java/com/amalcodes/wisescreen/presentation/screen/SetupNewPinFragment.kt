package com.amalcodes.wisescreen.presentation.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.amalcodes.wisescreen.features.pin.setup.PinSetupPage
import com.amalcodes.wisescreen.features.pin.setup.PinSetupUiState
import com.amalcodes.wisescreen.features.pin.setup.PinSetupViewModel
import com.amalcodes.wisescreen.presentation.foundation.AppTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author: AMAL
 * Created On : 28/07/20
 */


@AndroidEntryPoint
class SetupNewPinFragment : Fragment() {

    @ExperimentalComposeUiApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = ComposeView(requireContext()).apply {
        setContent {
            val pinSetupViewModel: PinSetupViewModel = hiltViewModel()
            val pinSetupUiState: PinSetupUiState by pinSetupViewModel.pinSetupUiState.collectAsStateWithLifecycle()
            AppTheme {
                PinSetupPage(
                    pinSetupUiState = pinSetupUiState,
                    onSuccess = { findNavController().navigate(SetupNewPinFragmentDirections.actionGlobalHomeFragment()) },
                    savePin = pinSetupViewModel::savePin,
                    confirmPin = pinSetupViewModel::confirmPin,
                    clearErrorState = pinSetupViewModel::clearErrorState,
                    resetPin = pinSetupViewModel::resetPin,
                )
            }
        }
    }
}