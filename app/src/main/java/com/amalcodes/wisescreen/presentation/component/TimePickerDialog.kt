package com.amalcodes.wisescreen.presentation.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amalcodes.wisescreen.core.Const
import com.amalcodes.wisescreen.core.autoCleared
import com.amalcodes.wisescreen.core.millis
import com.amalcodes.wisescreen.databinding.DialogTimePickerBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * @author: AMAL
 * Created On : 24/07/20
 */


class TimePickerDialog : BottomSheetDialogFragment() {

    companion object {
        const val KEY_TIME_IN_MILLIS = "TIME_IN_MILLIS"
    }

    private var binding: DialogTimePickerBinding by autoCleared()

    private val args: TimePickerDialogArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = DialogTimePickerBinding.inflate(inflater)
        .also { binding = it }
        .root

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvTitle.text = args.title
        binding.timePicker.setIs24HourView(true)
        binding.timePicker.millis = args.timeInMillis
        binding.btnOk.setOnClickListener {
            val timeInMillis = binding.timePicker.millis
            setFragmentResult(
                Const.REQUEST_RESULT_KEY,
                bundleOf(KEY_TIME_IN_MILLIS to timeInMillis)
            )
            findNavController().navigateUp()
        }
        binding.btnCancel.setOnClickListener { findNavController().navigateUp() }
    }

}