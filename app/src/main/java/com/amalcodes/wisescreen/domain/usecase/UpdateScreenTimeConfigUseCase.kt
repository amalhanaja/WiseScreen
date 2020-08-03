package com.amalcodes.wisescreen.domain.usecase

import com.amalcodes.wisescreen.domain.Repository
import com.amalcodes.wisescreen.domain.entity.ScreenTimeConfigEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author: AMAL
 * Created On : 24/07/20
 */


class UpdateScreenTimeConfigUseCase @Inject constructor(
    private val repository: Repository
) : UseCase<ScreenTimeConfigEntity, Unit> {

    @ExperimentalCoroutinesApi
    override fun invoke(input: ScreenTimeConfigEntity): Flow<Unit> {
        return updateScreenTimeConfig(input)
    }

    private fun updateScreenTimeConfig(input: ScreenTimeConfigEntity): Flow<Unit> {
        return repository.saveScreenTimeConfig(config = input)
    }
}