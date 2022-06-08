package com.edimitre.bllokuim.systemservices

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters


class MyDbBackupWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    private val systemService: SystemService = SystemService(context)

    override fun doWork(): Result {

        systemService.exportDatabase()

        return Result.success()
    }


}