package com.amalcodes.wisescreen.domain.usecase

import com.amalcodes.wisescreen.domain.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * @author: AMAL
 * Created On : 29/07/20
 */
 
 
class DisablePinUseCase @Inject constructor(
    private val repository: Repository
): UseCase<UseCase.None, Unit> {
    override fun invoke(input: UseCase.None): Flow<Unit> {
        return flow { emit(repository.setPin("")) }
    }
}