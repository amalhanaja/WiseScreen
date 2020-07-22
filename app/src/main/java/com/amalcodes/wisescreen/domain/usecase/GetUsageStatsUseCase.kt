package com.amalcodes.wisescreen.domain.usecase

import com.amalcodes.wisescreen.domain.Repository
import com.amalcodes.wisescreen.domain.entity.AppUsageEntity
import com.amalcodes.wisescreen.domain.entity.TimeRangeEnum
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * @author: AMAL
 * Created On : 19/07/20
 */


class GetUsageStatsUseCase @Inject constructor(
    private val getTimeRangeUseCase: GetTimeRangeUseCase,
    private val repository: Repository
) : UseCase<TimeRangeEnum, List<AppUsageEntity>> {
    @ExperimentalCoroutinesApi
    override fun invoke(input: TimeRangeEnum): Flow<List<AppUsageEntity>> {
        return getTimeRangeUseCase(input)
            .flatMapLatest { repository.getUsageStats(it) }
            .map { list -> list.filter { it.isOpenable } }
    }
}