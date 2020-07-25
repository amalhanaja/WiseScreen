package com.amalcodes.wisescreen.domain.usecase

import com.amalcodes.wisescreen.domain.Repository
import com.amalcodes.wisescreen.domain.entity.AppInfoEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author: AMAL
 * Created On : 25/07/20
 */


class GetApplicationList @Inject constructor(
    private val repository: Repository
) : UseCase<UseCase.None, List<AppInfoEntity>> {
    override fun invoke(input: UseCase.None): Flow<List<AppInfoEntity>> {
        return repository.getApplicationList()
    }
}