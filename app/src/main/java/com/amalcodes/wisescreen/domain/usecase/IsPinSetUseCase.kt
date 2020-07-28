package com.amalcodes.wisescreen.domain.usecase

import com.amalcodes.wisescreen.domain.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * @author: AMAL
 * Created On : 29/07/20
 */
 
 
class IsPinSetUseCase @Inject constructor(
    private val repository: Repository
): UseCase<UseCase.None, Boolean> {
    override fun invoke(input: UseCase.None): Flow<Boolean> {
        return repository.getPin().map { it.isNotBlank() }
    }
}