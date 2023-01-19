package com.amalcodes.wisescreen.presentation.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amalcodes.wisescreen.core.Const
import com.amalcodes.wisescreen.features.picker.TimePickerDialog
import com.amalcodes.wisescreen.presentation.foundation.AppTheme
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * @author: AMAL
 * Created On : 24/07/20
 */


class TimePickerDialog : BottomSheetDialogFragment() {

    companion object {
        const val KEY_TIME_IN_MILLIS = "TIME_IN_MILLIS"
    }

    private val args: TimePickerDialogArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = ComposeView(requireContext()).apply {
        setContent {
            AppTheme {
                TimePickerDialog(
                    title = args.title,
                    millis = args.timeInMillis,
                    onSubmit = { timeInMillis ->
                        setFragmentResult(Const.REQUEST_RESULT_KEY, bundleOf(KEY_TIME_IN_MILLIS to timeInMillis))
                        findNavController().navigateUp()
                    },
                    onCancel = findNavController()::navigateUp
                )
            }
        }
    }
}