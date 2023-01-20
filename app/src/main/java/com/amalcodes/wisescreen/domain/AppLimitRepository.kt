package com.amalcodes.wisescreen.domain

import com.amalcodes.wisescreen.domain.entity.AppLimitEntity
import kotlinx.coroutines.flow.Flow

/**
 * @author: AMAL
 * Created On : 25/07/20
 */


interface AppLimitRepository {
    fun getList(): Flow<List<AppLimitEntity>>

    fun getByPackageName(packageName: String): Flow<AppLimitEntity>

    suspend fun insert(data: AppLimitEntity)

    suspend fun update(data: AppLimitEntity)

    suspend fun delete(data: AppLimitEntity)
}