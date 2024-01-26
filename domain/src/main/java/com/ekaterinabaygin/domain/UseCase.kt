package com.ekaterinabaygin.domain

abstract class UseCase<in P, R> {

    suspend fun execute(params: P): R = doWork(params)

    protected abstract suspend fun doWork(params: P): R
}

