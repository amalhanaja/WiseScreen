package com.amalcodes.wisescreen.presentation.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import com.amalcodes.wisescreen.core.autoCleared
import com.amalcodes.wisescreen.databinding.FragmentHomeBinding
import com.amalcodes.wisescreen.features.home.HomePage
import com.amalcodes.wisescreen.features.home.HomeViewModel
import com.amalcodes.wisescreen.presentation.foundation.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment() {

    companion object {
        private const val ACTION_TAG_CHANGE_IS_SCREEN_MANAGEABLE = "CHANGE_IS_SCREEN_MANAGEABLE"
        private const val ACTION_TAG_MANAGE_SCREEN_TIME = "MANAGE_SCREEN_TIME"
        private const val ACTION_TAG_MANAGE_APP_LIMITS = "MANAGE_APP_LIMITS"
        private const val ACTION_TAG_ENABLE_DISABLE_PIN = "DISABLE_PIN"
    }

    private var binding: FragmentHomeBinding by autoCleared()

//    private val viewModel: HomeViewModel by viewModels()

//    private val adapter: MergeAdapter by lazy { MergeAdapter() }

    private var lastTimePinInputted = 0L

    @ExperimentalMaterial3Api
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = ComposeView(requireContext()).apply {
        setContent {
            val homeViewModel: HomeViewModel = hiltViewModel()
            val screenTimeSummarySectionUiState = homeViewModel.sectionScreenTimeSummaryUiState.collectAsState()
            AppTheme {
                HomePage(sectionScreenTimeSummaryUiState = screenTimeSummarySectionUiState.value)
            }
        }
    }
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding.rvScreenManagementMenu.adapter = adapter
//        binding.rvScreenManagementMenu.addItemDecoration(
//            DividerItemDecoration(
//                requireContext(),
//                dividerMarginLeft = resources.getDimension(R.dimen.space__screen_time_management_items_divider)
//            )
//        )
//        binding.tvMore.setOnClickListener {
//            findNavController().navigate(
//                HomeFragmentDirections.actionHomeFragmentToScreenTimeGroupFragment()
//            )
//        }
//        viewModel.uiState.observe(viewLifecycleOwner) {
//            when (it) {
//                is HomeUIState.Content -> onContent(it)
//                else -> {}
//            }
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        viewModel.dispatch(HomeUIEvent.Fetch)
//    }
//
//    private fun onContent(state: HomeUIState.Content) {
//        val config = state.viewEntity.screenTimeConfigEntity
//        val isPinSet = state.viewEntity.isPinSet
//        val canEditConfig = if (isPinSet) {
//            lastTimePinInputted + TimeUnit.MINUTES.toMillis(3) >= System.currentTimeMillis()
//        } else {
//            true
//        }
//        binding.switchPin.isChecked = state.viewEntity.isPinSet
//        binding.switchScreenTimeManagement.isChecked = config.isScreenTimeManageable
//        binding.rvScreenManagementMenu.isVisible = config.isScreenTimeManageable
//        showScreenTimeStatistics(
//            state.viewEntity.totalTimeInForeground,
//            state.viewEntity.dailyUsage
//        )
//        adapter.submitList(
//            listOf(
//                MenuItemViewEntity(
//                    title = getString(R.string.text_Screen_Time),
//                    description = getString(R.string.text_Screen_time_menu_description),
//                    icon = requireNotNull(
//                        ContextCompat.getDrawable(
//                            requireContext(),
//                            R.drawable.ic_hourglass
//                        )
//                    )
//                ),
//                MenuItemViewEntity(
//                    title = getString(R.string.text_App_limits),
//                    description = getString(R.string.text_App_limits_description),
//                    icon = requireNotNull(
//                        ContextCompat.getDrawable(
//                            requireContext(),
//                            R.drawable.ic_warning_o
//                        )
//                    )
//                )
//            )
//        )
//        adapter.setOnViewHolderClickListener { v, _ ->
//            if (v.id == R.id.item_menu) {
//                if (!canEditConfig) {
//                    when (v.tag) {
//                        0 -> openPinDialog(ACTION_TAG_MANAGE_SCREEN_TIME)
//                        1 -> openPinDialog(ACTION_TAG_MANAGE_APP_LIMITS)
//                    }
//                } else {
//                    when (v.tag) {
//                        0 -> findNavController().navigate(
//                            HomeFragmentDirections.actionHomeFragmentToDailyScreenTimeFragment()
//                        )
//                        1 -> findNavController().navigate(
//                            HomeFragmentDirections.actionHomeFragmentToAppLimitFragment()
//                        )
//                    }
//                }
//            }
//        }
//        if (canEditConfig) {
//            showConfig(config)
//        } else {
//            showConfigWithRequirePin(config)
//        }
//    }
//
//    private fun showConfig(config: ScreenTimeConfigEntity) {
//        binding.switchScreenTimeManagement.setOnCheckedChangeListener { _, isChecked ->
//            viewModel.dispatch(
//                HomeUIEvent.UpdateScreenTimeConfig(
//                    screenTimeConfigEntity = config.copy(
//                        isScreenTimeManageable = isChecked
//                    )
//                )
//            )
//            binding.rvScreenManagementMenu.isVisible = isChecked
//        }
//        binding.switchPin.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSetupPin())
//            } else {
//                viewModel.dispatch(HomeUIEvent.DisablePIN)
//            }
//        }
//    }
//
//    private fun showConfigWithRequirePin(config: ScreenTimeConfigEntity) {
//        setFragmentResultListener(Const.REQUEST_RESULT_KEY) { _, bundle ->
//            val isPinCorrect = bundle.getBoolean(PinDialog.KEY_IS_PIN_CORRECT)
//            val actionTag = bundle.getString(PinDialog.KEY_ACTION_TAG)
//            if (isPinCorrect) {
//                lastTimePinInputted = System.currentTimeMillis()
//                showConfig(config)
//                when (actionTag) {
//                    ACTION_TAG_CHANGE_IS_SCREEN_MANAGEABLE -> {
//                        binding.switchScreenTimeManagement.isChecked =
//                            !binding.switchScreenTimeManagement.isChecked
//                    }
//                    ACTION_TAG_MANAGE_SCREEN_TIME -> findNavController().navigate(
//                        HomeFragmentDirections.actionHomeFragmentToDailyScreenTimeFragment()
//                    )
//                    ACTION_TAG_MANAGE_APP_LIMITS -> findNavController().navigate(
//                        HomeFragmentDirections.actionHomeFragmentToAppLimitFragment()
//                    )
//                    ACTION_TAG_ENABLE_DISABLE_PIN -> {
//                        binding.switchPin.isChecked = !binding.switchPin.isChecked
//                    }
//                }
//            }
//        }
//        binding.switchScreenTimeManagement.setOnCheckedChangeListener { _, isChecked ->
//            binding.switchScreenTimeManagement.isChecked = !isChecked
//            openPinDialog(ACTION_TAG_CHANGE_IS_SCREEN_MANAGEABLE)
//        }
//        binding.switchPin.setOnCheckedChangeListener { _, isChecked ->
//            binding.switchPin.isChecked = !isChecked
//            openPinDialog(ACTION_TAG_ENABLE_DISABLE_PIN)
//        }
//    }
//
//    private fun openPinDialog(actionTag: String) {
//        findNavController().navigate(HomeFragmentDirections.actionGlobalPinDialog(actionTag))
//    }
//
//
//    private fun showScreenTimeStatistics(
//        totalTimeInForeground: Long,
//        dailyUsage: List<AppUsageEntity>
//    ) {
//        var reservedPercentage = 100f
//        var othersTimeInForeground1 = totalTimeInForeground
//        binding.tvScreenTimeValue.text =
//            Util.formatTimeInMillis(requireContext(), totalTimeInForeground)
//        binding.chartStats.data = dailyUsage.mapIndexed { index, appUsageEntity ->
//            val percentage = 100f * appUsageEntity.totalTimeInForeground / totalTimeInForeground
//            reservedPercentage -= percentage
//            StackedBarChartItemViewEntity(
//                percentage = percentage,
//                color = when (index) {
//                    0 -> ContextCompat.getColor(requireContext(), R.color.persianGreen)
//                    1 -> ContextCompat.getColor(requireContext(), R.color.orangeYellowCrayola)
//                    else -> ContextCompat.getColor(requireContext(), R.color.burntSienna)
//                }
//            )
//        } + StackedBarChartItemViewEntity(
//            reservedPercentage,
//            Color.GRAY
//        )
//        binding.chartLegend.data = dailyUsage.mapIndexed { index, appUsageEntity ->
//            othersTimeInForeground1 -= appUsageEntity.totalTimeInForeground
//            StackedBarChartLegendItemViewEntity(
//                color = when (index) {
//                    0 -> ContextCompat.getColor(requireContext(), R.color.persianGreen)
//                    1 -> ContextCompat.getColor(requireContext(), R.color.orangeYellowCrayola)
//                    else -> ContextCompat.getColor(requireContext(), R.color.burntSienna)
//                },
//                name = appUsageEntity.appName,
//                duration = appUsageEntity.totalTimeInForeground.toInt()
//            )
//        } + StackedBarChartLegendItemViewEntity(
//            color = Color.GRAY,
//            name = getString(R.string.text_Others),
//            duration = othersTimeInForeground1.toInt()
//        )
//    }
}