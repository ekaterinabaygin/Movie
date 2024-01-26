package com.ekaterinabaygin.domain.common


suspend fun <P, R> UseCase<in P, R>.executeSafely(params: P) = runCatching { execute(params) }

interface UseCase<P, R> {
    suspend fun execute(params: P): R
}