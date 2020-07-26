package com.amalcodes.wisescreen.data

import com.amalcodes.wisescreen.data.entity.AppLimitDataEntity
import com.amalcodes.wisescreen.domain.AppLimitRepository
import com.amalcodes.wisescreen.domain.entity.AppLimitEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * @author: AMAL
 * Created On : 25/07/20
 */


class AppLimitDataRepository @Inject constructor(
    private val appLimitDao: AppLimitDao
) : AppLimitRepository {

    override fun getList(): Flow<List<AppLimitEntity>> = appLimitDao.getList().map { list ->
        list.map(AppLimitDataEntity::toAppLimitEntity)
    }

    override fun insert(data: AppLimitEntity): Flow<Unit> {
        return flow { emit(appLimitDao.insert(data.toAppLimitDataEntity())) }
    }

    override fun update(data: AppLimitEntity): Flow<Unit> {
        return flow { emit(appLimitDao.update(data.toAppLimitDataEntity())) }
    }

    override fun delete(data: AppLimitEntity): Flow<Unit> {
        return flow { emit(appLimitDao.delete(data.toAppLimitDataEntity())) }
    }
}