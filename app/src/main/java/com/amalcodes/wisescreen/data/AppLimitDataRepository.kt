package com.amalcodes.wisescreen.data

import com.amalcodes.wisescreen.data.entity.AppLimitDataEntity
import com.amalcodes.wisescreen.domain.AppLimitRepository
import com.amalcodes.wisescreen.domain.entity.AppLimitEntity
import com.amalcodes.wisescreen.domain.entity.AppLimitType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * @author: AMAL
 * Created On : 25/07/20
 */


class AppLimitDataRepository @Inject constructor(
    private val appLimitDao: AppLimitDao,
) : AppLimitRepository {

    override fun getList(): Flow<List<AppLimitEntity>> = appLimitDao.getList().map { list ->
        list.map(AppLimitDataEntity::toAppLimitEntity)
    }

    override fun getByPackageName(packageName: String): Flow<AppLimitEntity> {
        return appLimitDao.getByPackageName(packageName).map {
            it?.toAppLimitEntity() ?: AppLimitEntity(
                packageName = packageName,
                type = AppLimitType.DEFAULT,
                limitTimeInMillis = 0
            )
        }
    }

    override suspend fun insert(data: AppLimitEntity) {
        appLimitDao.insert(data.toAppLimitDataEntity())
    }

    override suspend fun update(data: AppLimitEntity) {
        appLimitDao.update(data.toAppLimitDataEntity())
    }

    override suspend fun delete(data: AppLimitEntity) {
        appLimitDao.delete(data.toAppLimitDataEntity())
    }
}