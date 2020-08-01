package com.amalcodes.wisescreen.domain.usecase

import com.amalcodes.wisescreen.domain.Repository
import com.amalcodes.wisescreen.domain.entity.ScreenTimeConfigEntity
import com.amalcodes.wisescreen.domain.error.PinRequiredError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

/**
 * @author: AMAL
 * Created On : 24/07/20
 */


class UpdateScreenTimeConfigUseCase @Inject constructor(
    private val repository: Repository,
    private val isPinSetUseCase: IsPinSetUseCase
) : UseCase<ScreenTimeConfigEntity, Unit> {

    @ExperimentalCoroutinesApi
    override fun invoke(input: ScreenTimeConfigEntity): Flow<Unit> = isPinSetUseCase(UseCase.None)
        .flatMapLatest {
            if (it) {
                updateScreenTimeConfig(input)
            } else {
                throw PinRequiredError()
            }
        }

    private fun updateScreenTimeConfig(input: ScreenTimeConfigEntity): Flow<Unit> {
        return repository.saveScreenTimeConfig(config = input)
    }
}