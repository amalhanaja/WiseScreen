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
import com.amalcodes.wisescreen.features.picker.DayPickerDialog
import com.amalcodes.wisescreen.presentation.foundation.AppTheme
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * @author: AMAL
 * Created On : 23/07/20
 */


class DayPickerDialog : BottomSheetDialogFragment() {

    companion object {
        const val KEY_SELECTED_DAYS = "SELECTED_DAYS"
    }
//
//    private var binding: DialogDayPickerBinding by autoCleared()
//    private val adapter: MergeAdapter by lazy { MergeAdapter() }

    private val args: DayPickerDialogArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = ComposeView(requireContext()).apply {
        setContent {
            AppTheme {
                DayPickerDialog(
                    title = args.title,
                    selected = args.selectedDayOfWeek.toList(),
                    onSubmit = { list ->
                        setFragmentResult(Const.REQUEST_RESULT_KEY, bundleOf(KEY_SELECTED_DAYS to list.toIntArray()))
                        findNavController().navigateUp()
                    },
                    onCancel = findNavController()::navigateUp
                )
            }
        }
    }
}