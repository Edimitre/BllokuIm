package com.edimitre.bllokuim.systemservices

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.edimitre.bllokuim.data.utils.TimeUtils


class MyNotificationWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    private val mySystemService: SystemService = SystemService(context)

    override fun doWork(): Result {

        val hour: Int = TimeUtils().getCurrentHour()

        if (hour > 9) {
            mySystemService.notify(
                "BllokuIm",
                "Ka ndonje shpenzim te harruar ?"
            )
        }

        return Result.success()
    }



}