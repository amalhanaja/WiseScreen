package com.amalcodes.wisescreen.domain.usecase

import com.amalcodes.wisescreen.core.zip3
import com.amalcodes.wisescreen.data.AppLimitDataRepository
import com.amalcodes.wisescreen.domain.entity.AppLimitType
import com.amalcodes.wisescreen.domain.entity.TimeRangeEnum
import com.amalcodes.wisescreen.domain.error.AppBlockedError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * @author: AMAL
 * Created On : 28/07/20
 */


class VerifyAppNotBlocked @Inject constructor(
    private val appLimitDataRepository: AppLimitDataRepository,
    private val getUsageStatsUseCase: GetUsageStatsUseCase,
    private val getScreenTimeConfigUseCase: GetScreenTimeConfigUseCase
) : UseCase<VerifyAppNotBlocked.Input, Unit> {
    data class Input(val packageName: String, val currentDayOfWeek: Int)

    /*
    1. jika limit harian melebih config == blocked
    2. Jika limit app melebihi === blocked
    * */
    @ExperimentalCoroutinesApi
    override fun invoke(input: Input): Flow<Unit> {
        val totalTimeInForeground = getUsageStatsUseCase(TimeRangeEnum.TODAY).map { list ->
            val totalTimeInForeground =
                list.fold(0L) { acc, appUsageEntity -> acc + appUsageEntity.totalTimeInForeground }
            val appUsage = list.firstOrNull { it.packageName == input.packageName }
            appUsage to totalTimeInForeground
        }
        val appLimit = appLimitDataRepository.getByPackageName(input.packageName)
        val config = getScreenTimeConfigUseCase(UseCase.None)
        return totalTimeInForeground.zip3(appLimit, config) { t1, t2, t3 ->
            val isAppLimitReached = t1.first?.totalTimeInForeground ?: 0 >= t2.limitTimeInMillis
            if (!t3.isScreenTimeManageable) {
                return@zip3
            }
            if (t2.type == AppLimitType.ALWAYS_ALLOW) {
                return@zip3
            }
            if (t2.type == AppLimitType.NEVER_ALLOW) {
                throw AppBlockedError.NeverAllowed(t2)
            }
            if (t2.type == AppLimitType.LIMIT_USE && isAppLimitReached) {
                throw AppBlockedError.AppLimitUsageReached(t2)
            }
            val dayLimit = if (t3.workingDays.contains(input.currentDayOfWeek)) {
                t3.workingDayDailyScreenTimeInMillis
            } else {
                t3.restDayDailyScreenTimeInMillis
            }
            val isOverDayLimit = t1.second >= dayLimit
            if (isOverDayLimit) {
                throw AppBlockedError.DailyTimeLimitReached(t2)
            }
//            isOverDayLimit
        }
    }
}