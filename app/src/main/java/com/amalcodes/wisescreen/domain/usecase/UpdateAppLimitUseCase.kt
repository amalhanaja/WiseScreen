package com.amalcodes.wisescreen.domain.usecase

import com.amalcodes.wisescreen.domain.AppLimitRepository
import com.amalcodes.wisescreen.domain.entity.AppLimitEntity
import com.amalcodes.wisescreen.domain.entity.AppLimitType
import com.amalcodes.wisescreen.domain.error.PinRequiredError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

/**
 * @author: AMAL
 * Created On : 26/07/20
 */


class UpdateAppLimitUseCase @Inject constructor(
    private val appLimitRepository: AppLimitRepository,
    private val isPinSetUseCase: IsPinSetUseCase
) : UseCase<AppLimitEntity, Unit> {

    @ExperimentalCoroutinesApi
    override fun invoke(input: AppLimitEntity): Flow<Unit> = isPinSetUseCase(UseCase.None)
        .flatMapLatest {
            if (it) {
                updateAppLimit(input)
            } else {
                throw PinRequiredError()
            }
        }

    private fun updateAppLimit(input: AppLimitEntity) = if (input.id != 0L) {
        if (input.type == AppLimitType.DEFAULT) {
            appLimitRepository.delete(input)
        } else {
            appLimitRepository.update(input)
        }
    } else {
        appLimitRepository.insert(input)
    }
}