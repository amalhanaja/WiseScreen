package com.amalcodes.wisescreen.presentation.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.core.*
import com.amalcodes.wisescreen.databinding.DialogPinBinding
import com.amalcodes.wisescreen.presentation.UIState
import com.amalcodes.wisescreen.presentation.ui.PinDialogUIEvent
import com.amalcodes.wisescreen.presentation.ui.PinDialogUIState
import com.amalcodes.wisescreen.presentation.viewmodel.PinDialogViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber


/**
 * @author: AMAL
 * Created On : 01/08/20
 */


@AndroidEntryPoint
class PinDialog : BottomSheetDialogFragment() {

    companion object {
        const val KEY_IS_PIN_CORRECT = "IS_PIN_CORRECT"
        const val KEY_ACTION_TAG = "ACTION_TAG"
    }

    private var binding: DialogPinBinding by autoCleared()

    @ExperimentalCoroutinesApi
    private val viewModel: PinDialogViewModel by viewModels()

    private val args: PinDialogArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = DialogPinBinding.inflate(inflater)
        .also { binding = it }
        .root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogWithKeyboard)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expanded()
        isCancelable = false
        binding.etPin.showKeyboard(requireContext())
        binding.etPin.doAfterTextChanged {
            val text = it?.trim()?.toString().orEmpty()
            if (text.count() == 6) {
                viewModel.dispatch(PinDialogUIEvent.SubmitPin(text))
            }
        }
        binding.btnCancel.setOnClickListener { findNavController().navigateUp() }
        lifecycleScope.launchWhenCreated { observeUIState() }
    }

    @ExperimentalCoroutinesApi
    private fun observeUIState() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is PinDialogUIState.IncorrectPin -> onPinIncorrect()
                is PinDialogUIState.PinCorrect -> onPinCorrect()
                is UIState.UIFailure -> onFailure(it)
                else -> {}
            }
        }
    }

    private fun onPinCorrect() {
        binding.tvError.isVisible = false
        findNavController().navigateUp()
        setFragmentResult(
            Const.REQUEST_RESULT_KEY, bundleOf(
                KEY_IS_PIN_CORRECT to true,
                KEY_ACTION_TAG to args.actionTag
            )
        )
    }

    private fun onPinIncorrect() {
        val shake: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.shake)
        binding.tvError.isVisible = true
        binding.tvError.text = getString(R.string.text_Incorrect_PIN)
        binding.etPin.setText("")
        binding.etPin.startAnimation(shake)
        binding.tvError.startAnimation(shake)
    }

    private fun onFailure(failure: UIState.UIFailure) {
        Timber.e(failure.cause, "UIFailure")
    }
}