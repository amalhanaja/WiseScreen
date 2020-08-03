package com.amalcodes.wisescreen.presentation.screen

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amalcodes.wisescreen.core.Util
import com.amalcodes.wisescreen.core.autoCleared
import com.amalcodes.wisescreen.databinding.FragmentRequestEnableSettingBinding
import com.amalcodes.wisescreen.presentation.service.CurrentAppAccessibilityService

/**
 * @author: AMAL
 * Created On : 29/07/20
 */


class RequestEnableSettingsFragment : Fragment() {

    private var binding: FragmentRequestEnableSettingBinding by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentRequestEnableSettingBinding.inflate(inflater)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermissions()
        binding.btnExit.setOnClickListener { requireActivity().finishAndRemoveTask() }
    }

    private fun checkPermissions() {
        binding.switchUsageAccess.setOnCheckedChangeListener(null)
        binding.switchAccessibilityService.setOnCheckedChangeListener(null)
        val isAccessibilityServiceActivated = Util.isAccessibilityServiceGranted(
            requireContext(),
            CurrentAppAccessibilityService::class.java
        )
        val isUsageAccessGranted = Util.isAppUsageStatsGranted(requireContext())
        if (isAccessibilityServiceActivated && isUsageAccessGranted) {
            findNavController().navigate(RequestEnableSettingsFragmentDirections.actionRequestEnableSettingsDialogToHomeFragment())
        }
        binding.switchAccessibilityService.isChecked = isAccessibilityServiceActivated
        binding.switchUsageAccess.isChecked = isUsageAccessGranted
        binding.switchUsageAccess.setOnCheckedChangeListener { _, _ ->
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }
        binding.switchAccessibilityService.setOnCheckedChangeListener { _, _ ->
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }
    }

    override fun onResume() {
        super.onResume()
        checkPermissions()
    }
}