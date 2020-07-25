package com.amalcodes.wisescreen.domain.usecase

import com.amalcodes.wisescreen.data.DataRepository
import com.amalcodes.wisescreen.domain.Repository
import com.amalcodes.wisescreen.domain.entity.ScreenTimeConfigEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author: AMAL
 * Created On : 24/07/20
 */


class UpdateScreenTimeConfigUseCase @Inject constructor(
    private val repository: Repository
): UseCase<ScreenTimeConfigEntity, Unit> {
    override fun invoke(input: ScreenTimeConfigEntity): Flow<Unit> {
        return repository.saveScreenTimeConfig(config = input)
    }
}