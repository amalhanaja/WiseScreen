package com.amalcodes.wisescreen.domain.usecase

import com.amalcodes.wisescreen.core.Const
import com.amalcodes.wisescreen.core.clearTime
import com.amalcodes.wisescreen.domain.entity.TimeRangeEntity
import com.amalcodes.wisescreen.domain.entity.TimeRangeEnum
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * @author: AMAL
 * Created On : 18/07/20
 */


class GetTimeRangeUseCase @Inject constructor() : UseCase<TimeRangeEnum, TimeRangeEntity> {
    override fun invoke(input: TimeRangeEnum): Flow<TimeRangeEntity> {
        val timeRange = when (input) {
            TimeRangeEnum.TODAY -> getTodayRange()
            TimeRangeEnum.THIS_WEEK -> getThisWeekRange()
        }
        return flowOf(timeRange)
    }

    private fun getThisWeekRange(): TimeRangeEntity {
        val currentTimeInMillis = System.currentTimeMillis()
        val cal = Calendar.getInstance().apply {
            clearTime()
            add(Calendar.DAY_OF_WEEK, -6)
        }
        val start = cal.timeInMillis
        return TimeRangeEntity(
            start = start,
            end = if (start + Const.ONE_WEEK > currentTimeInMillis) currentTimeInMillis else start + Const.ONE_WEEK
        )
    }

    private fun getTodayRange(): TimeRangeEntity {
        val currentTimeInMillis = System.currentTimeMillis()
        val cal = Calendar.getInstance().apply {
            clearTime()
        }
        val start = cal.timeInMillis
        return TimeRangeEntity(
            start = start,
            end = if (start + Const.ONE_DAY > currentTimeInMillis) currentTimeInMillis else start + Const.ONE_DAY
        )
    }
}