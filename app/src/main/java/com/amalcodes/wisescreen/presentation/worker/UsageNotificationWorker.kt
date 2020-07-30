package com.amalcodes.wisescreen.presentation.worker

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.*
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.core.Util
import com.amalcodes.wisescreen.domain.entity.TimeRangeEnum
import com.amalcodes.wisescreen.domain.usecase.GetTotalTimeInForegroundUseCase
import com.amalcodes.wisescreen.presentation.NotificationHelper
import com.amalcodes.wisescreen.presentation.viewentity.NotificationEntity
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

/**
 * @author: AMAL
 * Created On : 27/07/20
 */


class UsageNotificationWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    @EntryPoint
    @InstallIn(ApplicationComponent::class)
    interface UsageNotificationWorkerEntryPoint {
        fun getTotalTimeInForegroundUseCase(): GetTotalTimeInForegroundUseCase
    }

    private val hiltEntryPoint = EntryPointAccessors
        .fromApplication(applicationContext, UsageNotificationWorkerEntryPoint::class.java)

    companion object {

        const val WORKER_NAME = "USAGE_NOTIFICATION"

        fun enqueue(context: Context) {
            val req: OneTimeWorkRequest = OneTimeWorkRequestBuilder<UsageNotificationWorker>()
                .build()
            WorkManager.getInstance(context)
                .enqueueUniqueWork(
                    WORKER_NAME,
                    ExistingWorkPolicy.KEEP,
                    req
                )
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun doWork(): Result {
        Timber.d("doWork")
        val getTotalTimeInForegroundUseCase = hiltEntryPoint.getTotalTimeInForegroundUseCase()
        getTotalTimeInForegroundUseCase(TimeRangeEnum.TODAY)
            .collectLatest {
                val pendingIntent = NavDeepLinkBuilder(applicationContext).apply {
                    setGraph(R.navigation.app_graph)
                    setDestination(R.id.screenTimeGroupFragment)
                }.createPendingIntent()
                val timeUsedString = Util.formatTimeInMillis(applicationContext, it)
                NotificationHelper.notify(
                    applicationContext, NotificationEntity(
                        tag = WORKER_NAME,
                        channelName = WORKER_NAME,
                        channelId = WORKER_NAME,
                        title = applicationContext.getString(
                            R.string.text_screen_used_notification,
                            timeUsedString
                        ),
                        pendingIntent = pendingIntent,
                        isAutoCancel = false,
                        silent = true,
                        ongoing = true,
                        visibility = NotificationCompat.VISIBILITY_SECRET,
                        priority = NotificationCompat.PRIORITY_LOW
                    )
                )
            }
        return Result.success()
    }
}