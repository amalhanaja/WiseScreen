package com.amalcodes.wisescreen.presentation.screen

import android.app.AppOpsManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.content.PermissionChecker.checkCallingOrSelfPermission
import androidx.core.content.getSystemService
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.core.Const
import com.amalcodes.wisescreen.core.Util
import com.amalcodes.wisescreen.core.autoCleared
import com.amalcodes.wisescreen.core.setMs
import com.amalcodes.wisescreen.databinding.FragmentScreenTimeBinding
import com.amalcodes.wisescreen.domain.entity.TimeRangeEnum
import com.amalcodes.wisescreen.presentation.MergeAdapter
import com.amalcodes.wisescreen.presentation.UIState
import com.amalcodes.wisescreen.presentation.component.DividerItemDecoration
import com.amalcodes.wisescreen.presentation.ui.ScreenTimeUIEvent
import com.amalcodes.wisescreen.presentation.ui.ScreenTimeUIState
import com.amalcodes.wisescreen.presentation.viewmodel.ScreenTimeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import java.util.*


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ScreenTimeFragment : Fragment() {

    private var binding: FragmentScreenTimeBinding by autoCleared()

    private val viewModel: ScreenTimeViewModel by viewModels()

    private val adapter: MergeAdapter by lazy { MergeAdapter() }

    companion object {
        fun newInstance(timeRangeEnum: TimeRangeEnum): ScreenTimeFragment {
            return ScreenTimeFragment().apply {
                arguments = bundleOf(
                    "TIME_RANGE_ENUM" to timeRangeEnum.name
                )
            }
        }
    }

    private val timeRangeEnum: TimeRangeEnum by lazy {
        val arg = requireNotNull(arguments?.getString("TIME_RANGE_ENUM", ""))
        TimeRangeEnum.valueOf(arg)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentScreenTimeBinding.inflate(inflater)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvAppsUsage.adapter = adapter
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is UIState.Initial -> onInitialized()
                is UIState.UIFailure -> onFailed(it)
                is ScreenTimeUIState.Content -> onContent(it)
            }
        }
    }

    private fun onContent(content: ScreenTimeUIState.Content) {
        val usage = content.usageItems.sumBy { it.usageDuration }
        val cal = Calendar.getInstance().apply { setMs(usage) }
        binding.tvScreenTimeValue.text = Util.formatTimeInMillis(requireContext(), usage.toLong())
        binding.pbScreenTime.max = when (timeRangeEnum) {
            TimeRangeEnum.TODAY -> Const.ONE_DAY
            TimeRangeEnum.THIS_WEEK -> Const.ONE_WEEK
        }
        binding.tvStartScreenTime.text = when (timeRangeEnum) {
            TimeRangeEnum.TODAY -> getString(R.string.text_number_0)
            TimeRangeEnum.THIS_WEEK -> getString(R.string.Six_days_ago)
        }
        binding.tvEndScreenTime.text = when (timeRangeEnum) {
            TimeRangeEnum.TODAY -> getString(R.string.text_number_24)
            TimeRangeEnum.THIS_WEEK -> getString(R.string.text_Today)
        }
        binding.pbScreenTime.progress = usage
        binding.rvAppsUsage.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                dividerMarginLeft = resources.getDimension(R.dimen.space__usage_item_divider)
            )
        )
        adapter.submitList(content.usageItems)
    }

    private fun onFailed(failure: UIState.UIFailure) {
        Timber.e(failure.cause, "UI Failure")
    }

    private fun onInitialized() {
        if (isAppUsageStatsGranted()) {
            viewModel.dispatch(ScreenTimeUIEvent.Fetch(timeRangeEnum))
        } else {
            goToUsageAccessSettings()
        }
    }

    private fun isAppUsageStatsGranted(): Boolean {
        val appOpsManager: AppOpsManager? = requireContext().getSystemService()
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOpsManager?.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                requireContext().packageName
            )
        } else {
            @Suppress("DEPRECATION")
            appOpsManager?.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                requireContext().packageName
            )
        }
        if (mode == AppOpsManager.MODE_DEFAULT) {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkCallingOrSelfPermission(
                    requireContext(),
                    android.Manifest.permission.PACKAGE_USAGE_STATS
                ) == PERMISSION_GRANTED
            } else {
                true
            }
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }


    private fun goToUsageAccessSettings() {
        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
    }

}