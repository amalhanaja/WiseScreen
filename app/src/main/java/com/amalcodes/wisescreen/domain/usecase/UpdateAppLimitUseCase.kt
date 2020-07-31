package com.amalcodes.wisescreen.domain.usecase

import com.amalcodes.wisescreen.domain.AppLimitRepository
import com.amalcodes.wisescreen.domain.entity.AppLimitEntity
import com.amalcodes.wisescreen.domain.entity.AppLimitType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author: AMAL
 * Created On : 26/07/20
 */


class UpdateAppLimitUseCase @Inject constructor(
    private val appLimitRepository: AppLimitRepository
) : UseCase<AppLimitEntity, Unit> {
    override fun invoke(input: AppLimitEntity): Flow<Unit> = if (input.id != 0L) {
        if (input.type == AppLimitType.DEFAULT) {
            appLimitRepository.delete(input)
        } else {
            appLimitRepository.update(input)
        }
    } else {
        appLimitRepository.insert(input)
    }
}