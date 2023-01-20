package com.amalcodes.wisescreen.domain.usecase

import com.amalcodes.wisescreen.domain.AppLimitRepository
import com.amalcodes.wisescreen.domain.entity.AppLimitEntity
import com.amalcodes.wisescreen.domain.entity.AppLimitType
import javax.inject.Inject

/**
 * @author: AMAL
 * Created On : 26/07/20
 */


class UpdateAppLimitUseCase @Inject constructor(
    private val appLimitRepository: AppLimitRepository,
) : SuspendingUseCaseWithParam<AppLimitEntity> {

    override suspend fun invoke(param: AppLimitEntity) {
        when {
            param.id != 0L && param.type == AppLimitType.DEFAULT -> appLimitRepository.delete(param)
            param.id != 0L -> appLimitRepository.update(param)
            else -> appLimitRepository.insert(param)
        }
    }
}