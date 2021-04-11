package com.luan.common.interactor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.KoinComponent
import kotlin.coroutines.CoroutineContext

abstract class UseCase<in T, out O> : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    abstract suspend fun execute(params: T) : O

    suspend operator fun invoke(params: T) : O {
        return execute(params)
    }

}