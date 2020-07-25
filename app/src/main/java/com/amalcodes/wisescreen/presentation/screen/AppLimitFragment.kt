package com.amalcodes.wisescreen.presentation.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.amalcodes.wisescreen.core.autoCleared
import com.amalcodes.wisescreen.core.getApplicationName
import com.amalcodes.wisescreen.core.getNullableApplicationIcon
import com.amalcodes.wisescreen.databinding.FragmentAppLimitBinding
import com.amalcodes.wisescreen.presentation.MergeAdapter
import com.amalcodes.wisescreen.presentation.UIState
import com.amalcodes.wisescreen.presentation.ui.AppLimitUIEvent
import com.amalcodes.wisescreen.presentation.ui.AppLimitUIState
import com.amalcodes.wisescreen.presentation.viewentity.AppItemViewEntity
import com.amalcodes.wisescreen.presentation.viewmodel.AppLimitViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

/**
 * @author: AMAL
 * Created On : 25/07/20
 */


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AppLimitFragment : Fragment() {
    private var binding: FragmentAppLimitBinding by autoCleared()

    private val viewModel: AppLimitViewModel by viewModels()

    private val adapter: MergeAdapter by lazy { MergeAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentAppLimitBinding.inflate(inflater)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.uiState.observe(viewLifecycleOwner) {
                when (it) {
                    is AppLimitUIState.Content -> onContent(it)
                    is UIState.Initial -> onInitialized()
                    is UIState.UIFailure -> onFailed(it)
                }
            }
        }
    }

    private fun onContent(content: AppLimitUIState.Content) {
        binding.rvApps.adapter = adapter
        val pm = requireContext().packageManager
        val appsViewEntity = content.apps.map { (packageName) ->
            AppItemViewEntity(
                packageName = packageName,
                appIcon = pm.getNullableApplicationIcon(packageName),
                appName = pm.getApplicationName(packageName)
            )
        }
        adapter.submitList(appsViewEntity)
    }

    private fun onInitialized() {
        viewModel.dispatch(AppLimitUIEvent.Fetch)
    }

    private fun onFailed(failure: UIState.UIFailure) {
        Timber.e(failure.cause, "UIFailure")
    }
}