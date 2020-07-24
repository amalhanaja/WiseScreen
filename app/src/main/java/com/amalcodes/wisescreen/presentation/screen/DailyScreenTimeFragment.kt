package com.amalcodes.wisescreen.presentation.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.core.Const
import com.amalcodes.wisescreen.core.Util
import com.amalcodes.wisescreen.core.autoCleared
import com.amalcodes.wisescreen.core.clearTime
import com.amalcodes.wisescreen.databinding.FragmentDailyScreenTimeBinding
import com.amalcodes.wisescreen.domain.entity.ScreenTimeConfigEntity
import com.amalcodes.wisescreen.presentation.MergeAdapter
import com.amalcodes.wisescreen.presentation.UIState
import com.amalcodes.wisescreen.presentation.component.DayPickerDialog
import com.amalcodes.wisescreen.presentation.ui.DailyScreenTimeUIEvent
import com.amalcodes.wisescreen.presentation.ui.DailyScreenTimeUIState
import com.amalcodes.wisescreen.presentation.viewentity.KeyValueMenuItemViewEntity
import com.amalcodes.wisescreen.presentation.viewmodel.DailyScreenTimeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DailyScreenTimeFragment : Fragment() {

    private var binding: FragmentDailyScreenTimeBinding by autoCleared()

    private val viewModel: DailyScreenTimeViewModel by viewModels()

    private val workDaysAdapter: MergeAdapter by lazy { MergeAdapter() }
    private val restDaysAdapter: MergeAdapter by lazy { MergeAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentDailyScreenTimeBinding.inflate(inflater)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvRestDays.adapter = restDaysAdapter
        binding.rvWorkDays.adapter = workDaysAdapter
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is DailyScreenTimeUIState.Content -> onContent(it)
                is UIState.Initial -> onInitialized()
                is UIState.UIFailure -> onFailed(it)
            }
        }
    }

    private fun showDayPicker(initialData: ScreenTimeConfigEntity, isWorkingDay: Boolean) {
        findNavController().navigate(
            DailyScreenTimeFragmentDirections.actionGlobalDayPickerDialog(
                selectedDayOfWeek = if (isWorkingDay) {
                    initialData.workingDays.toIntArray()
                } else {
                    Util.getDaysOfWeek()
                        .filterNot { initialData.workingDays.toIntArray().contains(it) }
                        .toIntArray()
                }
            )
        )
        setFragmentResultListener(Const.REQUEST_RESULT_KEY) { _, bundle ->

            val selectedDay = bundle.getIntArray(DayPickerDialog.KEY_SELECTED_DAYS)
                ?.toList().orEmpty()
            Timber.d("OnFragmentResult $selectedDay")
            val data = if (isWorkingDay) {
                initialData.copy(workingDays = selectedDay)
            } else {
                initialData.copy(
                    workingDays = Util.getDaysOfWeek().filterNot { selectedDay.contains(it) }
                )
            }
            viewModel.dispatch(DailyScreenTimeUIEvent.UpdateScreenTimeConfig(data))
        }
    }

    private fun onContent(content: DailyScreenTimeUIState.Content) {
        val (data) = content
        val workingDays = data.workingDays
        val restDays = Util.getDaysOfWeek().filterNot { workingDays.contains(it) }
        restDaysAdapter.submitList(
            listOf(
                KeyValueMenuItemViewEntity(
                    key = "Repeat",
                    value = formatDaysOfWeek(restDays)
                ),
                KeyValueMenuItemViewEntity(
                    key = "Waktu Harian",
                    value = "6 jam"
                )
            )
        )
        workDaysAdapter.submitList(
            listOf(
                KeyValueMenuItemViewEntity(
                    key = "Repeat",
                    value = formatDaysOfWeek(workingDays)
                ),
                KeyValueMenuItemViewEntity(
                    key = "Waktu Harian",
                    value = "6 jam"
                )
            )
        )
        workDaysAdapter.setOnViewHolderClickListener { v, item ->
            if (v.id == R.id.item_key_value_menu) {
                require(item is KeyValueMenuItemViewEntity)
                when (v.tag) {
                    0 -> showDayPicker(data, true)
                    1 -> {
                        Timber.d("Show Time Picker")
                    }
                }
            }
        }
        restDaysAdapter.setOnViewHolderClickListener { v, item ->
            if (v.id == R.id.item_key_value_menu) {
                require(item is KeyValueMenuItemViewEntity)
                when (v.tag) {
                    0 -> showDayPicker(data, false)
                    1 -> {
                        Timber.d("Show Time Picker")
                    }
                }
            }
        }
    }

    private fun onFailed(failure: UIState.UIFailure) {
        Timber.e(failure.cause, "UIFailure")
    }

    private fun onInitialized() {
        viewModel.dispatch(DailyScreenTimeUIEvent.Fetch)
    }

    private fun formatDaysOfWeek(days: List<Int>): String {
        return if (days.count() == 7) {
            "Every Day"
        } else {
            days.joinToString {
                val cal = Calendar.getInstance().apply {
                    clearTime()
                    this[Calendar.DAY_OF_WEEK] = it
                }
                cal.getDisplayName(
                    Calendar.DAY_OF_WEEK,
                    Calendar.LONG,
                    Locale.getDefault()
                )!!
            }
        }
    }

}