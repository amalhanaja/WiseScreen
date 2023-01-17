package com.amalcodes.wisescreen.domain.usecase

import com.amalcodes.wisescreen.domain.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * @author: AMAL
 * Created On : 29/07/20
 */


class SetPinUseCase @Inject constructor(
    private val repository: Repository
) : UseCase<String, Unit> {
    override fun invoke(input: String): Flow<Unit> = flow {
        emit(repository.setPin(input))
    }
}