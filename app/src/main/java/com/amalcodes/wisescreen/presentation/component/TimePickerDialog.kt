package com.amalcodes.wisescreen.presentation.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amalcodes.wisescreen.core.autoCleared
import com.amalcodes.wisescreen.core.setMs
import com.amalcodes.wisescreen.databinding.DialogTimePickerBinding
import com.amalcodes.wisescreen.presentation.screen.DailyScreenTimeFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

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
        savedInstanceState: Bundle?
    ): View? = DialogTimePickerBinding.inflate(inflater)
        .also { binding = it }
        .root

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNumberPicker()
        binding.btnOk.setOnClickListener {
            val hour = binding.npHours.value * 3_600_000
            val minute = binding.npMinutes.value * 60 * 1000
            val timeInMillis = hour + minute
            setFragmentResult(
                DailyScreenTimeFragment.KEY_REQUEST_TIME,
                bundleOf(KEY_TIME_IN_MILLIS to timeInMillis)
            )
            findNavController().navigateUp()
        }
        binding.btnCancel.setOnClickListener { findNavController().navigateUp() }
    }

    private fun setupNumberPicker() {
        val formatter: (Int) -> String = { if (it < 10) "0$it" else "$it" }
        val cal = Calendar.getInstance().apply {
            setMs(args.timeInMillis)
        }
        binding.npHours.minValue = 0
        binding.npHours.maxValue = 23
        binding.npHours.setFormatter(formatter)
        binding.npMinutes.minValue = 0
        binding.npMinutes.maxValue = 59
        binding.npMinutes.setFormatter(formatter)
        binding.npHours.value = cal[Calendar.HOUR_OF_DAY]
        binding.npMinutes.value = cal[Calendar.MINUTE]
    }
}