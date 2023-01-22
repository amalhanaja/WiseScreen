package com.amalcodes.wisescreen.presentation.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.ComponentName
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.amalcodes.wisescreen.core.clearTime
import com.amalcodes.wisescreen.core.isActivity
import com.amalcodes.wisescreen.core.isOpenable
import com.amalcodes.wisescreen.domain.entity.AppBlockedType
import com.amalcodes.wisescreen.domain.error.AppBlockedError
import com.amalcodes.wisescreen.domain.usecase.VerifyAppNotBlocked
import com.amalcodes.wisescreen.presentation.screen.AppBlockedActivity
import com.amalcodes.wisescreen.presentation.screen.AppBlockedActivityArgs
import com.amalcodes.wisescreen.presentation.worker.UsageNotificationWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.util.Calendar
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * @author: AMAL
 * Created On : 28/07/20
 */

@AndroidEntryPoint
class CurrentAppAccessibilityService : AccessibilityService(), CoroutineScope {

    private var coroutineJob: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = coroutineJob + Dispatchers.Main.immediate

    @Inject
    lateinit var isAppBlockedUseCase: VerifyAppNotBlocked

    override fun onInterrupt() {
    }

    override fun onDestroy() {
        coroutineJob.cancel()
        super.onDestroy()
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        serviceInfo = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS
        }
    }

    @ExperimentalCoroutinesApi
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.packageName?.toString()?.let { eventPackageName ->
            event.className?.toString()?.let { className ->
                val componentName = ComponentName(eventPackageName, className)
                val isOpenable = packageManager.isOpenable(eventPackageName)
                val isNotInAppPackageName = eventPackageName != packageName
                if (isOpenable && packageManager.isActivity(componentName) && isNotInAppPackageName) {
                    UsageNotificationWorker.enqueue(applicationContext)
                    val cal = Calendar.getInstance().apply { clearTime() }
                    isAppBlockedUseCase(
                        VerifyAppNotBlocked.Input(eventPackageName, cal[Calendar.DAY_OF_WEEK])
                    ).catch { err ->
                        when (err) {
                            is AppBlockedError -> {
                                val appBlockedType: AppBlockedType = when (err) {
                                    is AppBlockedError.DailyTimeLimitReached -> AppBlockedType.DAILY_TIME_LIMIT
                                    is AppBlockedError.AppLimitUsageReached -> AppBlockedType.APP_LIMIT
                                    is AppBlockedError.NeverAllowed -> AppBlockedType.NEVER_ALLOWED
                                }
                                val intent = AppBlockedActivity.getIntent(
                                    applicationContext, AppBlockedActivityArgs(
                                        packageName = eventPackageName,
                                        appBlockedType = appBlockedType
                                    )
                                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                startActivity(intent)
                            }
                            else -> Timber.e(err, "Unhandled Error")
                        }
                    }.onEach { Timber.d("Allowed") }.launchIn(this)
                }
            }
        }
    }

}