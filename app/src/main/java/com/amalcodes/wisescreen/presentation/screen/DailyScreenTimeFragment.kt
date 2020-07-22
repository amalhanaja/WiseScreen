package com.amalcodes.wisescreen.presentation.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.core.autoCleared
import com.amalcodes.wisescreen.databinding.FragmentDailyScreenTimeBinding
import com.amalcodes.wisescreen.presentation.viewmodel.DailyScreenTimeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DailyScreenTimeFragment : Fragment() {

    private var binding: FragmentDailyScreenTimeBinding by autoCleared()

    private val viewModel: DailyScreenTimeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentDailyScreenTimeBinding.inflate(inflater)
        .also { binding = it }
        .root

}