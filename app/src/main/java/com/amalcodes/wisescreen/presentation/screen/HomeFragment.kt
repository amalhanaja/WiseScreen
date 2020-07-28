package com.amalcodes.wisescreen.presentation.screen

import android.graphics.Color
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.core.autoCleared
import com.amalcodes.wisescreen.databinding.FragmentHomeBinding
import com.amalcodes.wisescreen.presentation.MergeAdapter
import com.amalcodes.wisescreen.presentation.UIState
import com.amalcodes.wisescreen.presentation.ui.HomeUIEvent
import com.amalcodes.wisescreen.presentation.ui.HomeUIState
import com.amalcodes.wisescreen.presentation.viewentity.MenuItemViewEntity
import com.amalcodes.wisescreen.presentation.viewentity.StackedBarChartItemViewEntity
import com.amalcodes.wisescreen.presentation.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding by autoCleared()

    private val viewModel: HomeViewModel by viewModels()

    private val adapter: MergeAdapter by lazy { MergeAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentHomeBinding.inflate(inflater)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvScreenManagementMenu.adapter = adapter
        adapter.setOnViewHolderClickListener { v, _ ->
            if (v.id == R.id.item_menu) {
                when (v.tag) {
                    0 -> findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToDailyScreenTimeFragment()
                    )
                    1 -> findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToAppLimitFragment()
                    )
                }
            }
        }
        adapter.submitList(
            listOf(
                MenuItemViewEntity(
                    title = "Screen Time",
                    description = "Screen Time Description",
                    icon = requireNotNull(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.mipmap.ic_launcher
                        )
                    )
                ),
                MenuItemViewEntity(
                    title = "App limits",
                    description = "App Limits description",
                    icon = requireNotNull(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.mipmap.ic_launcher
                        )
                    )
                )
            )
        )
        binding.tvScreenTimeLabel.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToScreenTimeFragment()
            )
        }
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
        binding.switchPin.isChecked = state.viewEntity.isPinSet
        binding.switchPin.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSetupPin())
            }
        }
    }

    private fun onInitialized() {
        viewModel.dispatch(HomeUIEvent.Fetch)
    }
}