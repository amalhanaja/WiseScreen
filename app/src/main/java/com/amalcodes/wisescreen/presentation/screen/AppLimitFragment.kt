package com.amalcodes.wisescreen.presentation.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amalcodes.wisescreen.features.screentime.applimit.AppLimitPage
import com.amalcodes.wisescreen.features.screentime.applimit.AppLimitUiState
import com.amalcodes.wisescreen.features.screentime.applimit.AppLimitViewModel
import com.amalcodes.wisescreen.presentation.foundation.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * @author: AMAL
 * Created On : 25/07/20
 */


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AppLimitFragment : Fragment() {

    @ExperimentalMaterial3Api
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = ComposeView(requireContext()).apply {
        setContent {
            val appLimitViewModel: AppLimitViewModel = hiltViewModel()
            val appLimitUiState: AppLimitUiState by appLimitViewModel.appLimitUiState.collectAsStateWithLifecycle()

            AppTheme {
                AppLimitPage(
                    appLimitUiState = appLimitUiState,
                    query = appLimitViewModel.query,
                    setQuery = appLimitViewModel::updateQuery,
                )
            }
        }
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding.rvApps.adapter = adapter
//        binding.etSearch.setCompoundDrawablesRelativeWithIntrinsicBounds(
//            getSearchDrawable(),
//            null,
//            null,
//            null
//        )
//        binding.etSearch.onCompoundDrawableClickListener(Const.DRAWABLE_RIGHT) {
//            binding.etSearch.setText("")
//        }
//        binding.rvApps.addItemDecoration(
//            DividerItemDecoration(
//                requireContext(),
//                dividerMarginLeft = resources.getDimension(R.dimen.space_app_limit_divider)
//            )
//        )
//        setFragmentResultListener(Const.REQUEST_RESULT_KEY) { _, _ -> onInitialized() }
//        adapter.setOnViewHolderClickListener { v, item ->
//            if (v.id == R.id.item_app_limit) {
//                require(item is AppLimitViewEntity)
//                findNavController().navigate(
//                    AppLimitFragmentDirections.actionAppLimitFragmentToAppLimitDialog(item)
//                )
//            }
//        }
//        lifecycleScope.launchWhenCreated {
//            viewModel.uiState.observe(viewLifecycleOwner) {
//                when (it) {
//                    is AppLimitUIState.Content -> onContent(it)
//                    is UIState.Initial -> onInitialized()
//                    is UIState.UIFailure -> onFailed(it)
//                    else -> {}
//                }
//            }
//        }
//    }
//
//    private fun onContent(content: AppLimitUIState.Content) {
//        val appsViewEntity: List<AppLimitViewEntity> = content.apps
//            .map { it.toAppLimitViewEntity() }
//        binding.etSearch.doAfterTextChanged { editable ->
//            val text = editable?.toString()?.trim().orEmpty()
//            binding.etSearch.setCompoundDrawablesRelativeWithIntrinsicBounds(
//                getSearchDrawable(),
//                null,
//                if (text.isNotEmpty()) getCloseDrawable() else null,
//                null
//            )
//            if (searchQuery == text) {
//                return@doAfterTextChanged
//            }
//            searchQuery = text
//            lifecycleScope.launch {
//                delay(300)
//                if (searchQuery != text) {
//                    return@launch
//                }
//                if (text.isEmpty()) {
//                    adapter.submitList(appsViewEntity)
//                } else {
//                    adapter.submitList(
//                        appsViewEntity.filter {
//                            val appName =
//                                requireContext().packageManager.getApplicationName(it.packageName)
//                            appName.contains(text, ignoreCase = true)
//                        }
//                    )
//                }
//            }
//        }
//        adapter.submitList(appsViewEntity)
//    }
//
//    private fun onInitialized() {
//        viewModel.dispatch(AppLimitUIEvent.Fetch)
//    }
//
//    private fun onFailed(failure: UIState.UIFailure) {
//        Timber.e(failure.cause, "UIFailure")
//    }
//
//    private fun getSearchDrawable(): Drawable? = ContextCompat.getDrawable(
//        requireContext(), R.drawable.ic_search
//    )?.apply {
//        setTint(ContextCompat.getColor(requireContext(), R.color.black33))
//    }
//
//    private fun getCloseDrawable(): Drawable? = ContextCompat.getDrawable(
//        requireContext(),
//        R.drawable.ic_close
//    )?.apply {
//        setTint(ContextCompat.getColor(requireContext(), R.color.black87))
//    }
}