package com.amalcodes.wisescreen.presentation.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.core.autoCleared
import com.amalcodes.wisescreen.databinding.FragmentSetupPinBinding
import com.amalcodes.wisescreen.presentation.UIState
import com.amalcodes.wisescreen.presentation.ui.PinSetupUIEvent
import com.amalcodes.wisescreen.presentation.ui.PinSetupUIFailure
import com.amalcodes.wisescreen.presentation.ui.PinSetupUIState
import com.amalcodes.wisescreen.presentation.viewmodel.PinSetupViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

/**
 * @author: AMAL
 * Created On : 29/07/20
 */


@AndroidEntryPoint
class VerifyPinFragment: Fragment() {

    private var binding: FragmentSetupPinBinding by autoCleared()

    @ExperimentalCoroutinesApi
    private val viewModel: PinSetupViewModel by viewModels(
        ownerProducer = {
            findNavController().getBackStackEntry(R.id.setup_pin)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentSetupPinBinding.inflate(inflater)
        .also { binding = it }
        .root

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.uiState.observe(viewLifecycleOwner) {
                when (it) {
                    is PinSetupUIState.PinVerified -> onPinVerified()
                    is UIState.UIFailure -> onFailed(it)
                    else -> {}
                }
            }
        }
        binding.tvTitle.text = getString(R.string.text_Confirm_PIN)
        binding.etPin.doAfterTextChanged {
            if ((it?.length ?: 0) == 6) {
                viewModel.dispatch(PinSetupUIEvent.VerifyPin(it.toString()))
            }
        }
    }

    private fun onPinVerified() {
        findNavController().navigate(VerifyPinFragmentDirections.actionGlobalHomeFragment())
    }

    private fun onFailed(failure: UIState.UIFailure) {
        Timber.e(failure.cause, "UIFailure")
        when (failure) {
            is PinSetupUIFailure.PinMismatch -> onPinMismatch()
            else -> {}
        }
    }

    private fun onPinMismatch() {
        val shake: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.shake)
        binding.tvError.isVisible = true
        binding.tvError.text = getString(R.string.text_PIN_Mismatch)
        binding.etPin.setText("")
        binding.etPin.startAnimation(shake)
        binding.tvError.startAnimation(shake)
    }
}