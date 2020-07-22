package com.amalcodes.wisescreen.core

import android.app.Application
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.getSystemService
import com.amalcodes.wisescreen.data.DataRepository
import com.amalcodes.wisescreen.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

/**
 * @author: AMAL
 * Created On : 18/07/20
 */

@Module
@InstallIn(ApplicationComponent::class)
abstract class CoreModule {
    @Binds
    abstract fun bindDataRepository(
        dataRepository: DataRepository
    ): Repository
}

@Module
@InstallIn(ApplicationComponent::class)
object CoreModuleProviders {

    @Provides
    fun providePackageManager(
        @ApplicationContext context: Context
    ): PackageManager = context.packageManager

    @Provides
    fun provideUsageStatsManager(
        @ApplicationContext context: Context
    ): UsageStatsManager {
        return requireNotNull(context.getSystemService()) {
            "failed provide usage stats manager"
        }
    }
}