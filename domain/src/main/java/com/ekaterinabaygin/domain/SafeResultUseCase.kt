package com.ekaterinabaygin.domain

abstract class SafeResultUseCase<in P, R> {

    suspend fun execute(params: P): Result<R> = runCatching { doWork(params) }

    protected abstract suspend fun doWork(params: P): R
}