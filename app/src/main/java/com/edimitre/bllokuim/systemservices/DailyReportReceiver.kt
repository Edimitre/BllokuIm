package com.edimitre.bllokuim.systemservices

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DailyReportReceiver : BroadcastReceiver() {

    @Inject
    lateinit var systemService: SystemService

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {

        systemService.startDailyReportService()
        systemService.scheduleDailyReportAlarm()

    }

}