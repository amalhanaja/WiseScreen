package com.amalcodes.wisescreen.presentation.screen

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.core.Util
import com.amalcodes.wisescreen.core.autoCleared
import com.amalcodes.wisescreen.databinding.DialogRequestEnableSettingBinding
import com.amalcodes.wisescreen.presentation.service.CurrentAppAccessibilityService
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * @author: AMAL
 * Created On : 29/07/20
 */


class RequestEnableSettingsDialog : BottomSheetDialogFragment() {

    private var binding: DialogRequestEnableSettingBinding by autoCleared()

    private val args: RequestEnableSettingsDialogArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = DialogRequestEnableSettingBinding.inflate(inflater)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isCancelable = false
        super.onViewCreated(view, savedInstanceState)
        binding.tvDescription.text = when (val intentAction = args.intentAction) {
            Settings.ACTION_ACCESSIBILITY_SETTINGS -> getString(R.string.text_Accessibility_Service)
            Settings.ACTION_USAGE_ACCESS_SETTINGS -> getString(R.string.text_Usage_Statistics)
            else -> throw IllegalStateException("Unsupported IntentAction: $intentAction")
        }
        binding.btnSetting.text = when (val intentAction = args.intentAction) {
            Settings.ACTION_ACCESSIBILITY_SETTINGS -> getString(R.string.text_Activate)
            Settings.ACTION_USAGE_ACCESS_SETTINGS -> getString(R.string.text_Grant_Access)
            else -> throw IllegalStateException("Unsupported IntentAction: $intentAction")
        }
        binding.btnSetting.setOnClickListener { goToUsageAccessSettings() }
        binding.btnExit.setOnClickListener {requireActivity().finishAndRemoveTask() }
    }

    override fun onResume() {
        super.onResume()
        val isGranted = when (args.intentAction) {
            Settings.ACTION_ACCESSIBILITY_SETTINGS -> Util.isAccessibilityServiceGranted(
                requireContext(),
                CurrentAppAccessibilityService::class.java
            )
            Settings.ACTION_USAGE_ACCESS_SETTINGS -> Util.isAppUsageStatsGranted(requireContext())
            else -> false
        }
        if (isGranted) findNavController().navigateUp()
    }


    private fun goToUsageAccessSettings() {
        startActivity(Intent(args.intentAction))
    }
}