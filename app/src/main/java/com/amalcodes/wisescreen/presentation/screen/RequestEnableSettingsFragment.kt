package com.amalcodes.wisescreen.presentation.screen

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.amalcodes.wisescreen.features.requiredpermssion.RequiredPermissionPage
import com.amalcodes.wisescreen.presentation.foundation.AppTheme

/**
 * @author: AMAL
 * Created On : 29/07/20
 */


@ExperimentalMaterial3Api
class RequestEnableSettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = ComposeView(requireContext()).apply {
        setContent {
            AppTheme {
                RequiredPermissionPage(
                    onExit = { requireActivity().finishAndRemoveTask() },
                    goToUsageAccessSetting = { startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)) },
                    goToAccessibilitySetting = { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) },
                    onGranted = {
                        findNavController().navigate(RequestEnableSettingsFragmentDirections.actionRequestEnableSettingsDialogToHomeFragment())
                    }
                )
            }
        }
    }
}