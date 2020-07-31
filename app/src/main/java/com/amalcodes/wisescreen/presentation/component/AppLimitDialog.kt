package com.amalcodes.wisescreen.presentation.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.core.Const
import com.amalcodes.wisescreen.core.autoCleared
import com.amalcodes.wisescreen.core.getApplicationName
import com.amalcodes.wisescreen.core.millis
import com.amalcodes.wisescreen.databinding.DialogAppLimitBinding
import com.amalcodes.wisescreen.domain.entity.AppLimitType
import com.amalcodes.wisescreen.presentation.UIState
import com.amalcodes.wisescreen.presentation.ui.AppLimitDialogUIEvent
import com.amalcodes.wisescreen.presentation.ui.AppLimitDialogUIState
import com.amalcodes.wisescreen.presentation.viewmodel.AppLimitDialogViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

/**
 * @author: AMAL
 * Created On : 25/07/20
 */


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AppLimitDialog : BottomSheetDialogFragment() {

    private var binding: DialogAppLimitBinding by autoCleared()

    private val args: AppLimitDialogArgs by navArgs()

    private val viewModel: AppLimitDialogViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = DialogAppLimitBinding.inflate(inflater)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.uiState.observe(viewLifecycleOwner) {
                when (it) {
                    is AppLimitDialogUIState.Updated -> onUpdated()
                    is UIState.UIFailure -> onFailed(it)
                }
            }
        }
        binding.rgAppLimitType.check(args.viewEntity.type.checkedRadioButtonId())
        binding.tvTitle.text = requireContext().packageManager.getApplicationName(args.viewEntity.packageName)
        binding.timePicker.setIs24HourView(true)
        binding.timePicker.millis = args.viewEntity.limitTimeInMillis
        binding.timePicker.isVisible = binding.rbLimitUse.isChecked
        binding.rbLimitUse.setOnCheckedChangeListener { _, isChecked ->
            binding.timePicker.isVisible = isChecked
        }
        binding.btnCancel.setOnClickListener { findNavController().navigateUp() }
        binding.btnOk.setOnClickListener {
            val type = binding.rgAppLimitType.appLimitType()
            viewModel.dispatch(
                AppLimitDialogUIEvent.Update(
                    args.viewEntity.copy(
                        type = type,
                        limitTimeInMillis = binding.timePicker.millis
                    )
                )
            )
        }
    }

    private fun onUpdated() {
        setFragmentResult(Const.REQUEST_RESULT_KEY, Bundle.EMPTY)
        findNavController().navigateUp()
    }

    private fun onFailed(failure: UIState.UIFailure) {
        Timber.e(failure.cause, "UIFailure")
    }

    private fun AppLimitType.checkedRadioButtonId(): Int = when (this) {
        AppLimitType.NEVER_ALLOW -> R.id.rb_never_allowed
        AppLimitType.ALWAYS_ALLOW -> R.id.rb_alwas_allowed
        AppLimitType.LIMIT_USE -> R.id.rb_limit_use
        AppLimitType.DEFAULT -> R.id.rb_default
    }

    private fun RadioGroup.appLimitType(): AppLimitType = when (checkedRadioButtonId) {
        R.id.rb_limit_use -> AppLimitType.LIMIT_USE
        R.id.rb_alwas_allowed -> AppLimitType.ALWAYS_ALLOW
        R.id.rb_never_allowed -> AppLimitType.NEVER_ALLOW
        R.id.rb_default -> AppLimitType.DEFAULT
        else -> throw IllegalStateException("Unknown Checked Radio Button ID")
    }

}