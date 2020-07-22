package com.amalcodes.wisescreen.presentation.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.core.autoCleared
import com.amalcodes.wisescreen.databinding.FragmentDailyScreenTimeBinding
import com.amalcodes.wisescreen.presentation.MergeAdapter
import com.amalcodes.wisescreen.presentation.viewentity.KeyValueMenuItemViewEntity
import com.amalcodes.wisescreen.presentation.viewmodel.DailyScreenTimeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

@ExperimentalCoroutinesApi
class DailyScreenTimeFragment : Fragment() {

    private var binding: FragmentDailyScreenTimeBinding by autoCleared()

    private val viewModel: DailyScreenTimeViewModel by viewModels()

    private val workDaysAdapter: MergeAdapter by lazy { MergeAdapter() }
    private val restDaysAdapter: MergeAdapter by lazy { MergeAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentDailyScreenTimeBinding.inflate(inflater)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvRestDays.adapter = restDaysAdapter
        binding.rvWorkDays.adapter = workDaysAdapter
        workDaysAdapter.setOnViewHolderClickListener { v, item ->
            if (v.id == R.id.item_key_value_menu) {
                require(item is KeyValueMenuItemViewEntity)
                when (v.tag) {
                    0 -> {
                        Timber.d("Show Date Picker")
                    }
                    1 -> {
                        Timber.d("Show Time Picker")
                    }
                }
            }
        }
        restDaysAdapter.setOnViewHolderClickListener { v, item ->
            if (v.id == R.id.item_key_value_menu) {
                require(item is KeyValueMenuItemViewEntity)
                when (v.tag) {
                    0 -> {
                        Timber.d("Show Date Picker")
                    }
                    1 -> {
                        Timber.d("Show Time Picker")
                    }
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        restDaysAdapter.submitList(
            listOf(
                KeyValueMenuItemViewEntity(
                    key = "Repeat",
                    value = "Senin, Selasa, Rabu"
                ),
                KeyValueMenuItemViewEntity(
                    key = "Waktu Harian",
                    value = "6 jam"
                )
            )
        )
        workDaysAdapter.submitList(
            listOf(
                KeyValueMenuItemViewEntity(
                    key = "Repeat",
                    value = "Senin, Selasa, Rabu"
                ),
                KeyValueMenuItemViewEntity(
                    key = "Waktu Harian",
                    value = "6 jam"
                )
            )
        )
    }

}