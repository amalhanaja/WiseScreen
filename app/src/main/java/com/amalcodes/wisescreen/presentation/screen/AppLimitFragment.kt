package com.amalcodes.wisescreen.presentation.screen

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.core.Const
import com.amalcodes.wisescreen.core.autoCleared
import com.amalcodes.wisescreen.core.getApplicationName
import com.amalcodes.wisescreen.core.onCompoundDrawableClickListener
import com.amalcodes.wisescreen.databinding.FragmentAppLimitBinding
import com.amalcodes.wisescreen.presentation.MergeAdapter
import com.amalcodes.wisescreen.presentation.UIState
import com.amalcodes.wisescreen.presentation.component.DividerItemDecoration
import com.amalcodes.wisescreen.presentation.toAppLimitViewEntity
import com.amalcodes.wisescreen.presentation.ui.AppLimitUIEvent
import com.amalcodes.wisescreen.presentation.ui.AppLimitUIState
import com.amalcodes.wisescreen.presentation.viewentity.AppLimitViewEntity
import com.amalcodes.wisescreen.presentation.viewmodel.AppLimitViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

    private var searchQuery = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentAppLimitBinding.inflate(inflater)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvApps.adapter = adapter
        binding.etSearch.setCompoundDrawablesRelativeWithIntrinsicBounds(
            getSearchDrawable(),
            null,
            null,
            null
        )
        binding.etSearch.onCompoundDrawableClickListener(Const.DRAWABLE_RIGHT) {
            binding.etSearch.setText("")
        }
        binding.rvApps.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                dividerMarginLeft = resources.getDimension(R.dimen.space_app_limit_divider)
            )
        )
        setFragmentResultListener(Const.REQUEST_RESULT_KEY) { _, _ -> onInitialized() }
        adapter.setOnViewHolderClickListener { v, item ->
            if (v.id == R.id.item_app_limit) {
                require(item is AppLimitViewEntity)
                findNavController().navigate(
                    AppLimitFragmentDirections.actionAppLimitFragmentToAppLimitDialog(item)
                )
            }
        }
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
        val appsViewEntity: List<AppLimitViewEntity> = content.apps
            .map { it.toAppLimitViewEntity() }
        binding.etSearch.doAfterTextChanged { editable ->
            val text = editable?.toString()?.trim().orEmpty()
            binding.etSearch.setCompoundDrawablesRelativeWithIntrinsicBounds(
                getSearchDrawable(),
                null,
                if (text.isNotEmpty()) getCloseDrawable() else null,
                null
            )
            if (searchQuery == text) {
                return@doAfterTextChanged
            }
            searchQuery = text
            lifecycleScope.launch {
                delay(300)
                if (searchQuery != text) {
                    return@launch
                }
                if (text.isEmpty()) {
                    adapter.submitList(appsViewEntity)
                } else {
                    adapter.submitList(
                        appsViewEntity.filter {
                            val appName =
                                requireContext().packageManager.getApplicationName(it.packageName)
                            appName.contains(text, ignoreCase = true)
                        }
                    )
                }
            }
        }
        adapter.submitList(appsViewEntity)
    }

    private fun onInitialized() {
        viewModel.dispatch(AppLimitUIEvent.Fetch)
    }

    private fun onFailed(failure: UIState.UIFailure) {
        Timber.e(failure.cause, "UIFailure")
    }

    private fun getSearchDrawable(): Drawable? = ContextCompat.getDrawable(
        requireContext(), R.drawable.ic_search
    )?.apply {
        setTint(ContextCompat.getColor(requireContext(), R.color.black33))
    }

    private fun getCloseDrawable(): Drawable? = ContextCompat.getDrawable(
        requireContext(),
        R.drawable.ic_close
    )?.apply {
        setTint(ContextCompat.getColor(requireContext(), R.color.black87))
    }
}