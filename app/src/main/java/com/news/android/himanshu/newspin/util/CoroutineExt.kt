package com.news.android.himanshu.newspin.util

import com.news.android.himanshu.newspin.data.source.Result
import kotlinx.coroutines.experimental.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.coroutines.experimental.EmptyCoroutineContext

/**
 * Equivalent to [launch] but return [Unit] instead of [Job].
 *
 * Mainly for usage when you want to lift [launch] to return. Example:
 *
 * ```
 * override fun loadData() = launchSilent {
 *
 * }
 * ```
 */
fun launchSilent(
        context: CoroutineContext = DefaultDispatcher,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        parent: Job? = null,
        block: suspend CoroutineScope.() -> Unit
) {
    launch(context, start, parent, block)
}

suspend fun <T: Any> Call<T>.getResult(): Result<T> = suspendCancellableCoroutine { continuation ->
    this.enqueue(object : Callback<T> {

        override fun onFailure(call: Call<T>?, error: Throwable?) = continuation.resume(Result.Error(error))

        override fun onResponse(call: Call<T>?, response: Response<T>?) {
            response?.body()?.run { continuation.resume((Result.Success(this))) }
            response?.errorBody()?.run { continuation.resume(Result.Error(HttpException(response))) }
        }
    })
    continuation.invokeOnCompletion {
        if (continuation.isCancelled)
            try {
                cancel()
            } catch (e: Throwable) {
                //ignored
            }
    }
}