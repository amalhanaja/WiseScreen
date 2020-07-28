package com.amalcodes.wisescreen.presentation.worker

import android.content.Context
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.*
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.core.clearTime
import com.amalcodes.wisescreen.core.formatTime
import com.amalcodes.wisescreen.domain.entity.TimeRangeEnum
import com.amalcodes.wisescreen.domain.usecase.GetTotalTimeInForegroundUseCase
import com.amalcodes.wisescreen.domain.usecase.GetUsageStatsUseCase
import com.amalcodes.wisescreen.presentation.NotificationHelper
import com.amalcodes.wisescreen.presentation.viewentity.NotificationEntity
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.util.*

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

        fun run(context: Context) {
            val req: OneTimeWorkRequest = OneTimeWorkRequestBuilder<UsageNotificationWorker>()
                .build()
            WorkManager.getInstance(context)
                .enqueueUniqueWork(
                    WORKER_NAME,
                    ExistingWorkPolicy.REPLACE,
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
                    setDestination(R.id.screenTimeFragment)
                }.createPendingIntent()
                val cal = Calendar.getInstance().apply {
                    clearTime()
                    add(Calendar.MILLISECOND, it.toInt())
                }
                NotificationHelper.notify(
                    applicationContext, NotificationEntity(
                        tag = WORKER_NAME,
                        channelName = WORKER_NAME,
                        channelId = WORKER_NAME,
                        title = "${cal.formatTime(applicationContext)} of screen time used",
                        pendingIntent = pendingIntent,
                        isAutoCancel = false,
                        silent = true,
                        ongoing = true
                    )
                )
            }
        return Result.success()
    }
}