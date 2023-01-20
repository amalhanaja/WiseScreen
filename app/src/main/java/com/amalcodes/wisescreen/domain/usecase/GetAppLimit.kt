package com.amalcodes.wisescreen.domain.usecase

import com.amalcodes.wisescreen.domain.AppLimitRepository
import com.amalcodes.wisescreen.domain.entity.AppLimitEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetAppLimit @Inject constructor(
    private val appLimitRepository: AppLimitRepository,
) : UseCase<UseCase.None, List<AppLimitEntity>> {

    override fun invoke(input: UseCase.None): Flow<List<AppLimitEntity>> {
        return appLimitRepository.getList()
    }
}