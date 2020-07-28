package com.amalcodes.wisescreen.domain.usecase

import com.amalcodes.wisescreen.domain.entity.TimeRangeEnum
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * @author: AMAL
 * Created On : 28/07/20
 */


class GetTotalTimeInForegroundUseCase @Inject constructor(
    private val getUsageStatsUseCase: GetUsageStatsUseCase
) : UseCase<TimeRangeEnum, Long> {

    @ExperimentalCoroutinesApi
    override fun invoke(input: TimeRangeEnum): Flow<Long> {
        return getUsageStatsUseCase(input).map { list ->
            list.fold(0L) { acc, appUsageEntity ->
                acc + appUsageEntity.totalTimeInForeground
            }
        }
    }
}