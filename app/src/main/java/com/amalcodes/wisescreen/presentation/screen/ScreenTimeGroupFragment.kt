package com.amalcodes.wisescreen.presentation.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.core.autoCleared
import com.amalcodes.wisescreen.databinding.FragmentScreenTimeGroupBinding
import com.amalcodes.wisescreen.domain.entity.TimeRangeEnum
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * @author: AMAL
 * Created On : 31/07/20
 */


class ScreenTimeGroupFragment : Fragment() {

    private var binding: FragmentScreenTimeGroupBinding by autoCleared()

    private val adapter: ScreenTimePagerAdapter by lazy { ScreenTimePagerAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentScreenTimeGroupBinding.inflate(inflater)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.text_Today)
                else -> getString(R.string.text_Last_7_days)
            }
        }.attach()
    }

}

class ScreenTimePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2
    }

    @ExperimentalCoroutinesApi
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ScreenTimeFragment.newInstance(TimeRangeEnum.TODAY)
            else -> ScreenTimeFragment.newInstance(TimeRangeEnum.THIS_WEEK)
        }
    }
}