package com.edimitre.bllokuim.systemservices

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.edimitre.bllokuim.data.dao.SettingsDao
import com.edimitre.bllokuim.data.model.MyApplicationSettings
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var settingsDao: SettingsDao

    @Inject
    lateinit var systemService: SystemService

    private var myApplicationSettings: MyApplicationSettings? = null

    override fun onReceive(context: Context, intent: Intent) {

        val thread = Thread(Runnable {
            myApplicationSettings = settingsDao.getSettings()
        })

        thread.start()
        thread.join()


        loadSettings()


    }

    private fun loadSettings() {

        when (myApplicationSettings != null) {
            myApplicationSettings!!.dailyReportGeneratorEnabled -> {
                systemService.scheduleDailyReportAlarm()
            }
            myApplicationSettings!!.workerEnabled -> {
                systemService.startNotificationWorker()
            }
            myApplicationSettings!!.backDbEnabled -> {
                systemService.startDbBackupWorker()
            }
            else -> {
                Log.e("BllokuIm => ", "settings null on boot receiver")
            }
        }

        Log.e("BllokuIm => ", "loaded settings from boot receiver")

    }

}