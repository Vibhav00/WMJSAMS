package com.vk.wmjsams

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.delay

class MyWorker(appContext: Context,workerParameters: WorkerParameters):CoroutineWorker(appContext,workerParameters) {

    override suspend fun doWork(): Result {
        delay(3000)
        val result = workDataOf(Pair("vibhav","this is vibhav kumar mishra "))
        return Result.success(result)
    }
}