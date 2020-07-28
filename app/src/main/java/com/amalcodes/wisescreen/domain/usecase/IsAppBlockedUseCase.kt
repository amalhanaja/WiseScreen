package com.amalcodes.wisescreen.domain.usecase

import com.amalcodes.wisescreen.domain.entity.AppLimitType
import com.amalcodes.wisescreen.domain.entity.TimeRangeEnum
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

/**
 * @author: AMAL
 * Created On : 28/07/20
 */


class IsAppBlockedUseCase @Inject constructor(
    private val getApplicationList: GetApplicationList,
    private val getUsageStatsUseCase: GetUsageStatsUseCase,
    private val getScreenTimeConfigUseCase: GetScreenTimeConfigUseCase
) : UseCase<IsAppBlockedUseCase.Input, Boolean> {
    data class Input(val packageName: String, val currentDayOfWeek: Int)

    /*
    1. jika limit harian melebih config == blocked
    2. Jika limit app melebihi === blocked
    * */
    @ExperimentalCoroutinesApi
    override fun invoke(input: Input): Flow<Boolean> {
        val totalTimeInForeground = getUsageStatsUseCase(TimeRangeEnum.TODAY).map { list ->
            val totalTimeInForeground =
                list.fold(0L) { acc, appUsageEntity -> acc + appUsageEntity.totalTimeInForeground }
            val appUsage = list.firstOrNull { it.packageName == input.packageName }
            appUsage to totalTimeInForeground
        }
        val appLimit = getApplicationList(UseCase.None).map { list ->
            list.first { it.packageName == input.packageName }
        }
        val config = getScreenTimeConfigUseCase(UseCase.None)
        return totalTimeInForeground.zip(appLimit) { t1, t2 ->
            Pair(t1, t2)
        }.zip(config) { (t1, t2), t3 ->
            if (t2.type == AppLimitType.NEVER_ALLOW) {
                return@zip true
            }
            if (t2.type == AppLimitType.ALWAYS_ALLOW) {
                return@zip false
            }
            if (t2.type == AppLimitType.LIMIT_USE) {
                return@zip t1.first?.totalTimeInForeground ?: 0 >= t2.limitTimeInMillis
            }
            val dayLimit = if (t3.workingDays.contains(input.currentDayOfWeek)) {
                t3.workingDayDailyScreenTimeInMillis
            } else {
                t3.restDayDailyScreenTimeInMillis
            }
            val isOverDayLimit = t1.second >= dayLimit
            isOverDayLimit
        }
    }
}