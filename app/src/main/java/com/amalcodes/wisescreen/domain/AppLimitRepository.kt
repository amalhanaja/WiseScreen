package com.amalcodes.wisescreen.domain

import com.amalcodes.wisescreen.domain.entity.AppLimitEntity
import kotlinx.coroutines.flow.Flow

/**
 * @author: AMAL
 * Created On : 25/07/20
 */


interface AppLimitRepository {
    fun getList(): Flow<List<AppLimitEntity>>

    fun insert(data: AppLimitEntity): Flow<Unit>

    fun update(data: AppLimitEntity): Flow<Unit>

    fun delete(data: AppLimitEntity): Flow<Unit>
}