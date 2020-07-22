package com.amalcodes.wisescreen.presentation.screen

import android.graphics.Color
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.amalcodes.wisescreen.core.autoCleared
import com.amalcodes.wisescreen.databinding.FragmentHomeBinding
import com.amalcodes.wisescreen.presentation.UIState
import com.amalcodes.wisescreen.presentation.ui.HomeUIEvent
import com.amalcodes.wisescreen.presentation.ui.HomeUIState
import com.amalcodes.wisescreen.presentation.viewentity.StackedBarChartItemViewEntity
import com.amalcodes.wisescreen.presentation.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import java.util.concurrent.TimeUnit.MILLISECONDS

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding by autoCleared()

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentHomeBinding.inflate(inflater)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvScreenTimeLabel.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToScreenTimeFragment()
            )
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is UIState.Initial -> onInitialized()
                is HomeUIState.Content -> onContent(it)
            }
        }
    }

    private fun onContent(state: HomeUIState.Content) {
        val totalTimeInForeground = state.viewEntity.totalTimeInForeground
        var reservedPercentage = 0f
        binding.tvScreenTimeValue.text = DateUtils.formatElapsedTime(totalTimeInForeground / 1000)
        binding.chartStats.data = state.viewEntity.dailyUsage.mapIndexed { index, appUsageEntity ->
            val percentage = 100f * appUsageEntity.totalTimeInForeground / totalTimeInForeground
            reservedPercentage = 100f - percentage
            StackedBarChartItemViewEntity(
                percentage = percentage,
                color = when (index) {
                    0 -> Color.GREEN
                    1 -> Color.BLUE
                    else -> Color.YELLOW
                }
            )
        } + StackedBarChartItemViewEntity(reservedPercentage, Color.GRAY)
    }

    private fun onInitialized() {
        viewModel.dispatch(HomeUIEvent.Fetch)
    }
}