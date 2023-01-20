package com.amalcodes.wisescreen.presentation.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.platform.ComposeView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amalcodes.wisescreen.domain.entity.AppLimitEntity
import com.amalcodes.wisescreen.features.screentime.applimit.AppLimitOptionsDialog
import com.amalcodes.wisescreen.features.screentime.applimit.options.AppLimitOptionsViewModel
import com.amalcodes.wisescreen.presentation.foundation.AppTheme
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * @author: AMAL
 * Created On : 25/07/20
 */


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AppLimitDialog : BottomSheetDialogFragment() {

    private val args: AppLimitDialogArgs by navArgs()


    @ExperimentalMaterial3Api
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = ComposeView(requireContext()).apply {
        setContent {
            val appLimitOptionsViewModel: AppLimitOptionsViewModel = hiltViewModel()
            AppTheme {
                AppLimitOptionsDialog(
                    appLimitEntity = AppLimitEntity(
                        id = args.id,
                        packageName = args.packageName,
                        type = args.type,
                        limitTimeInMillis = args.limitTimeInMillis,
                    ),
                    onSubmit = {
                        appLimitOptionsViewModel.updateAppLimit(it)
                        findNavController().navigateUp()
                    },
                    onCancel = findNavController()::navigateUp
                )
            }
        }
    }
}