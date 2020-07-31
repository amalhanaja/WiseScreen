package com.amalcodes.wisescreen.presentation.viewentity

import androidx.annotation.IntDef
import com.amalcodes.wisescreen.core.Const

/**
 * @author: AMAL
 * Created On : 31/07/20
 */


@Retention(AnnotationRetention.SOURCE)
@IntDef(
    Const.APP_BLOCKED_APP_LIMIT,
    Const.APP_BLOCKED_DAILY_TIME_LIMIT,
    Const.APP_BLOCKED_NEVER_ALLOWED
)
annotation class AppBlockedType