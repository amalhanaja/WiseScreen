package com.amalcodes.wisescreen.presentation.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.core.Const
import com.amalcodes.wisescreen.core.expanded
import com.amalcodes.wisescreen.features.pin.verification.PinVerificationDialog
import com.amalcodes.wisescreen.features.pin.verification.PinVerificationViewModel
import com.amalcodes.wisescreen.presentation.foundation.AppTheme
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


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

    private val args: PinDialogArgs by navArgs()

    @ExperimentalComposeUiApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = ComposeView(requireContext()).apply {
        setContent {
            val pinVerificationViewModel: PinVerificationViewModel = hiltViewModel()
            val pinVerificationUiState by pinVerificationViewModel.pinVerificationUiState.collectAsStateWithLifecycle()

            AppTheme {
                PinVerificationDialog(
                    modifier = Modifier,
                    onCancel = { findNavController().navigateUp() },
                    onVerify = pinVerificationViewModel::verifyPin,
                    pinVerificationUiState = pinVerificationUiState,
                    onVerified = {
                        findNavController().navigateUp()
                        setFragmentResult(
                            Const.REQUEST_RESULT_KEY, bundleOf(
                                KEY_IS_PIN_CORRECT to true,
                                KEY_ACTION_TAG to args.actionTag
                            )
                        )
                    },
                    onType = pinVerificationViewModel::onType,
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogWithKeyboard)
        isCancelable = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expanded()
    }
}