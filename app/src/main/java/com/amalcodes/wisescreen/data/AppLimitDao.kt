package com.amalcodes.wisescreen.data

import androidx.room.*
import com.amalcodes.wisescreen.data.entity.AppLimitDataEntity
import kotlinx.coroutines.flow.Flow

/**
 * @author: AMAL
 * Created On : 25/07/20
 */

@Dao
interface AppLimitDao {

    @Query("SELECT * FROM APPS_LIMIT")
    fun getList(): Flow<List<AppLimitDataEntity>>

    @Insert
    suspend fun insert(data: AppLimitDataEntity)

    @Delete
    suspend fun delete(data: AppLimitDataEntity)

    @Update
    suspend fun update(data: AppLimitDataEntity)
}