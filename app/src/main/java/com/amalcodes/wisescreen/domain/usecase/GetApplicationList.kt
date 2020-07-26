package com.amalcodes.wisescreen.domain.usecase

import com.amalcodes.wisescreen.domain.AppLimitRepository
import com.amalcodes.wisescreen.domain.Repository
import com.amalcodes.wisescreen.domain.entity.AppLimitEntity
import com.amalcodes.wisescreen.domain.entity.AppLimitType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

/**
 * @author: AMAL
 * Created On : 25/07/20
 */


@ExperimentalCoroutinesApi
class GetApplicationList @Inject constructor(
    private val repository: Repository,
    private val appLimitRepository: AppLimitRepository
) : UseCase<UseCase.None, List<AppLimitEntity>> {
    override fun invoke(input: UseCase.None): Flow<List<AppLimitEntity>> {
        return appLimitRepository.getList()
            .zip(repository.getApplicationList()) { appsLimit, appsInfo ->
                appsInfo.map { appInfo ->
                    val appLimit: AppLimitEntity? = appsLimit.firstOrNull { it.packageName == appInfo.packageName }
                    appLimit ?: AppLimitEntity(
                        id = 0,
                        packageName = appInfo.packageName,
                        limitTimeInMillis = 0,
                        type = AppLimitType.UNLIMITED
                    )
                }
            }
    }
}