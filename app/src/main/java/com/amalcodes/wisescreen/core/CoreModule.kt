package com.amalcodes.wisescreen.core

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.core.content.getSystemService
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.amalcodes.wisescreen.data.AppDatabase
import com.amalcodes.wisescreen.data.AppLimitDao
import com.amalcodes.wisescreen.data.AppLimitDataRepository
import com.amalcodes.wisescreen.data.DataRepository
import com.amalcodes.wisescreen.domain.AppLimitRepository
import com.amalcodes.wisescreen.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author: AMAL
 * Created On : 18/07/20
 */

private val Context.preferencesDataStore by preferencesDataStore(name = "com.amalcodes.wisescreen_prefs")

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreModule {
    @Binds
    abstract fun bindDataRepository(
        dataRepository: DataRepository,
    ): Repository

    @Binds
    abstract fun bindAppLimitDataRepository(
        dataRepository: AppLimitDataRepository,
    ): AppLimitRepository
}

@Module
@InstallIn(SingletonComponent::class)
object CoreModuleProviders {

    @Provides
    fun providePackageManager(
        @ApplicationContext context: Context,
    ): PackageManager = context.packageManager

    @Provides
    fun provideUsageStatsManager(
        @ApplicationContext context: Context,
    ): UsageStatsManager {
        return requireNotNull(context.getSystemService()) {
            "failed provide usage stats manager"
        }
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context,
    ): SharedPreferences = context.getSharedPreferences(
        "${context.packageName}_prefs",
        Context.MODE_PRIVATE
    )

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return context.preferencesDataStore
    }


    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase = AppDatabase.getInstance(context)

    @Provides
    fun provideAppLimitDao(db: AppDatabase): AppLimitDao = db.appLimitDao()
}