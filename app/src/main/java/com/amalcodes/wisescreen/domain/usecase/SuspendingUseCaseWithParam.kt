package com.amalcodes.wisescreen.domain.usecase

interface SuspendingUseCaseWithParam<in P> {
    suspend operator fun invoke(param: P)
}