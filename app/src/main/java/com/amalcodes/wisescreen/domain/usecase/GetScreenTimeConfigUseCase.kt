package com.amalcodes.wisescreen.domain.usecase

import com.amalcodes.wisescreen.data.DataRepository
import com.amalcodes.wisescreen.domain.entity.ScreenTimeConfigEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author: AMAL
 * Created On : 23/07/20
 */


class GetScreenTimeConfigUseCase @Inject constructor(
    private val dataRepository: DataRepository
) : UseCase<UseCase.None, ScreenTimeConfigEntity> {
    override fun invoke(input: UseCase.None): Flow<ScreenTimeConfigEntity> {
        return dataRepository.getScreenTimeConfig()
    }
}