package com.amalcodes.wisescreen.presentation.screen

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.core.autoCleared
import com.amalcodes.wisescreen.core.formatTime
import com.amalcodes.wisescreen.core.setMs
import com.amalcodes.wisescreen.databinding.FragmentHomeBinding
import com.amalcodes.wisescreen.domain.entity.ScreenTimeConfigEntity
import com.amalcodes.wisescreen.presentation.MergeAdapter
import com.amalcodes.wisescreen.presentation.UIState
import com.amalcodes.wisescreen.presentation.ui.HomeUIEvent
import com.amalcodes.wisescreen.presentation.ui.HomeUIState
import com.amalcodes.wisescreen.presentation.viewentity.MenuItemViewEntity
import com.amalcodes.wisescreen.presentation.viewentity.StackedBarChartItemViewEntity
import com.amalcodes.wisescreen.presentation.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

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
                            R.drawable.ic_hourglass
                        )
                    )
                ),
                MenuItemViewEntity(
                    title = "App limits",
                    description = "App Limits description",
                    icon = requireNotNull(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_warning_o
                        )
                    )
                )
            )
        )
        binding.rvScreenManagementMenu.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
        binding.tvMore.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToScreenTimeFragment()
            )
        }
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is UIState.Initial -> onInitialized()
                is HomeUIState.Content -> onContent(it)
                is HomeUIState.ScreenTimeConfigUpdated -> onScreenTimeConfigUpdated(it.screenTimeConfigEntity)
            }
        }
    }

    private fun onScreenTimeConfigUpdated(newConfig: ScreenTimeConfigEntity) {
        setupScreenTimeConfig(newConfig)
    }

    private fun onContent(state: HomeUIState.Content) {
        val totalTimeInForeground = state.viewEntity.totalTimeInForeground
        var reservedPercentage = 100f
        val cal = Calendar.getInstance().apply { setMs(totalTimeInForeground.toInt()) }
        binding.tvScreenTimeValue.text = cal.formatTime(requireContext())
        binding.chartStats.data = state.viewEntity.dailyUsage.mapIndexed { index, appUsageEntity ->
            val percentage = 100f * appUsageEntity.totalTimeInForeground / totalTimeInForeground
            reservedPercentage -= percentage
            StackedBarChartItemViewEntity(
                percentage = percentage,
                color = when (index) {
                    0 -> ContextCompat.getColor(requireContext(), R.color.persianGreen)
                    1 -> ContextCompat.getColor(requireContext(), R.color.orangeYellowCrayola)
                    else -> ContextCompat.getColor(requireContext(), R.color.burntSienna)
                }
            )
        } + StackedBarChartItemViewEntity(
            reservedPercentage,
            Color.GRAY
        )
        setupScreenTimeConfig(state.viewEntity.screenTimeConfigEntity)
        binding.switchPin.isChecked = state.viewEntity.isPinSet
        binding.switchPin.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSetupPin())
            }
        }
    }

    private fun setupScreenTimeConfig(config: ScreenTimeConfigEntity) {
        binding.switchScreenTimeManagement.isChecked = config.isScreenTimeManageable
        binding.rvScreenManagementMenu.isVisible = config.isScreenTimeManageable
        binding.switchScreenTimeManagement.setOnCheckedChangeListener { _, isChecked ->
            viewModel.dispatch(
                HomeUIEvent.UpdateScreenTimeConfig(
                    screenTimeConfigEntity = config.copy(
                        isScreenTimeManageable = isChecked
                    )
                )
            )
            binding.rvScreenManagementMenu.isVisible = isChecked
        }
    }

    private fun onInitialized() {
//        if (!Util.isAppUsageStatsGranted(requireContext())) {
//            findNavController().navigate(
//                HomeFragmentDirections.actionGlobalRequestEnableSettingsDialog(s
//                    Settings.ACTION_USAGE_ACCESS_SETTINGS
//                )
//            )
//        }
//        else if (!Util.isAccessibilityServiceGranted(
//                requireContext(),
//                CurrentAppAccessibilityService::class.java
//            )
//        ) {
//            findNavController().navigate(
//                HomeFragmentDirections.actionGlobalRequestEnableSettingsDialog(
//                    Settings.ACTION_ACCESSIBILITY_SETTINGS
//                )
//            )
//        } else {
//            viewModel.dispatch(HomeUIEvent.Fetch)
//        }
        viewModel.dispatch(HomeUIEvent.Fetch)
    }
}