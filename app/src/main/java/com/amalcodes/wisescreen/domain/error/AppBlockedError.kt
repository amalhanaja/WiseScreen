package com.amalcodes.wisescreen.domain.error

import com.amalcodes.wisescreen.domain.entity.AppLimitEntity

/**
 * @author: AMAL
 * Created On : 31/07/20
 */


sealed class AppBlockedError(val app: AppLimitEntity) : Error("$app: blocked") {
    class DailyTimeLimitReached(app: AppLimitEntity) : AppBlockedError(app)
    class NeverAllowed(app: AppLimitEntity) : AppBlockedError(app)
    class AppLimitUsageReached(app: AppLimitEntity) : AppBlockedError(app)
}