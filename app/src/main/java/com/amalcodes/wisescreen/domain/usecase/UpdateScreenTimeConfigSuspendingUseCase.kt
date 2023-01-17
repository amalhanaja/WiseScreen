package com.amalcodes.wisescreen.domain.usecase

import com.amalcodes.wisescreen.domain.Repository
import com.amalcodes.wisescreen.domain.entity.ScreenTimeConfigEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * @author: AMAL
 * Created On : 24/07/20
 */


class UpdateScreenTimeConfigSuspendingUseCase @Inject constructor(
    private val repository: Repository,
) : SuspendingUseCaseWithParam<ScreenTimeConfigEntity> {

    override suspend fun invoke(param: ScreenTimeConfigEntity) {
        repository.saveScreenTimeConfig(config = param)
    }
}