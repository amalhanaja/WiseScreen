package com.amalcodes.wisescreen.presentation.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.findNavController
import com.amalcodes.wisescreen.core.Const
import com.amalcodes.wisescreen.features.config.ScreenTimeConfigPage
import com.amalcodes.wisescreen.features.config.ScreenTimeConfigUiState
import com.amalcodes.wisescreen.features.config.ScreenTimeConfigViewModel
import com.amalcodes.wisescreen.presentation.component.DayPickerDialog
import com.amalcodes.wisescreen.presentation.component.TimePickerDialog
import com.amalcodes.wisescreen.presentation.foundation.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DailyScreenTimeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = ComposeView(requireContext()).apply {
        setContent {
            val screenTimeConfigViewModel: ScreenTimeConfigViewModel = hiltViewModel()
            val screenTimeConfigUiState: ScreenTimeConfigUiState by screenTimeConfigViewModel.screenTimeConfigUiState.collectAsStateWithLifecycle()
            AppTheme {
                ScreenTimeConfigPage(
                    screenTimeConfigUiState = screenTimeConfigUiState,
                    openDayPicker = { title, key, activeDays, onSubmit ->
                        setFragmentResultListener(Const.REQUEST_RESULT_KEY) { _, bundle ->
                            val selectedDay = bundle.getIntArray(DayPickerDialog.KEY_SELECTED_DAYS)?.toList().orEmpty()
                            onSubmit(key, selectedDay)
                        }
                        findNavController().navigate(
                            DailyScreenTimeFragmentDirections.actionGlobalDayPickerDialog(
                                selectedDayOfWeek = activeDays.toIntArray(),
                                title = title,
                            )
                        )
                    },
                    openTimePicker = { title, key, timeInMillis, onSubmit ->
                        setFragmentResultListener(Const.REQUEST_RESULT_KEY) { _, bundle ->
                            val submittedTimeInMillis = bundle.getInt(TimePickerDialog.KEY_TIME_IN_MILLIS)
                            onSubmit(key, submittedTimeInMillis)
                        }
                        findNavController().navigate(
                            DailyScreenTimeFragmentDirections.actionGlobalTimePickerDialog(
                                title = title,
                                timeInMillis = timeInMillis,
                            )
                        )
                    },
                    updateWorkDays = screenTimeConfigViewModel::updateWorkDays,
                    updateWorkDayScreenTime = screenTimeConfigViewModel::updateWorkDayScreenTime,
                    updateRestDays = screenTimeConfigViewModel::updateRestDays,
                    updateRestDayScreenTime = screenTimeConfigViewModel::updateRestDayScreenTime,
                )
            }
        }
    }

}