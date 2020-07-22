package com.amalcodes.wisescreen.domain.usecase

import kotlinx.coroutines.flow.Flow

/**
 * @author: AMAL
 * Created On : 18/07/20
 */


interface UseCase<in Input, out Output> {
    operator fun invoke(input: Input): Flow<Output>

    object None
}