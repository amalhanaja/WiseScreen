package com.amalcodes.wisescreen.presentation.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.core.Const
import com.amalcodes.wisescreen.core.Util
import com.amalcodes.wisescreen.core.autoCleared
import com.amalcodes.wisescreen.databinding.DialogDayPickerBinding
import com.amalcodes.wisescreen.presentation.MergeAdapter
import com.amalcodes.wisescreen.presentation.screen.DailyScreenTimeFragment
import com.amalcodes.wisescreen.presentation.viewentity.DayItemViewEntity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import java.util.*

/**
 * @author: AMAL
 * Created On : 23/07/20
 */


class DayPickerDialog : BottomSheetDialogFragment() {

    companion object {
        const val KEY_SELECTED_DAYS = "SELECTED_DAYS"
    }

    private var binding: DialogDayPickerBinding by autoCleared()
    private val adapter: MergeAdapter by lazy { MergeAdapter() }

    private val args: DayPickerDialogArgs by navArgs()

    private val items: List<DayItemViewEntity> by lazy {
        Util.getDaysOfWeek().map {
            DayItemViewEntity(
                dayOfWeek = it,
                isSelected = args.selectedDayOfWeek.contains(it)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = DialogDayPickerBinding.inflate(inflater)
        .also { binding = it }
        .root

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvDays.adapter = adapter
        binding.tvTitle.text = args.title
        adapter.submitList(items)
        adapter.setOnViewHolderClickListener { v, item ->
            if (v.id == R.id.check_box) {
                require(item is DayItemViewEntity)
                items.first { it.dayOfWeek == item.dayOfWeek }.isSelected = item.isSelected
            }
        }
        binding.btnCancel.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnOk.setOnClickListener {
            setFragmentResult(
                DailyScreenTimeFragment.KEY_REQUEST_DAYS, bundleOf(
                    KEY_SELECTED_DAYS to items.filter { it.isSelected }
                        .map { it.dayOfWeek }.toIntArray()
                )
            )
            findNavController().navigateUp()
        }
    }
}