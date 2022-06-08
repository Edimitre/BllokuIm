package com.edimitre.bllokuim.systemservices

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.edimitre.bllokuim.R
import com.edimitre.bllokuim.data.dao.DailyReportDao
import com.edimitre.bllokuim.data.dao.ExpenseDao
import com.edimitre.bllokuim.data.dao.MonthlyIncomeDao
import com.edimitre.bllokuim.data.model.DailyReport
import com.edimitre.bllokuim.data.utils.TimeUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DailyReportGenerator : Service() {

    @Inject
    lateinit var expenseDao: ExpenseDao

    @Inject
    lateinit var monthlyIncomeDao: MonthlyIncomeDao

    @Inject
    lateinit var dailyReportDao: DailyReportDao

    @Inject
    lateinit var systemService: SystemService

    override fun onBind(intent: Intent): IBinder? {

        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val thread = Thread {


            startServiceNotification()

            generateReport()



        }
        thread.start()
        thread.join()

        systemService.exportDatabase()

        stopSelf()
        return START_NOT_STICKY
    }

    private fun generateReport() {


        val nrOfDays = TimeUtils().getNrOfRemainingDaysOfActualMonth()


        val monthlyIncomeValue = monthlyIncomeDao.getValueOfIncomesByYearMonthOnThread(
            TimeUtils().getCurrentYear(),
            TimeUtils().getCurrentMonth()
        )


        val dailyLimit = monthlyIncomeValue?.div(nrOfDays)

        val spentValue = expenseDao.getValueOfExpenseListByYearMonthDateOnThread(
            TimeUtils().getCurrentYear(),
            TimeUtils().getCurrentMonth(),
            TimeUtils().getCurrentDate()
        )

        var isOk = true

        if (spentValue != null && spentValue > dailyLimit!!) {
            isOk = false
        }

        val dailyReport = DailyReport(
            0, TimeUtils().getCurrentYear(),
            TimeUtils().getCurrentMonth(), TimeUtils().getCurrentDate(),
            TimeUtils().getCurrentHour(), TimeUtils().getCurrentMinute(),
            dailyLimit, spentValue, isOk
        )

        dailyReportDao.saveReportOnThread(dailyReport)
        Log.e("BllokuIm => ", "daily report generated and saved : ${dailyReport.isOk}")

    }

    private fun startServiceNotification() {
        val serviceNotification: Notification = NotificationCompat.Builder(
            applicationContext, "BllokuImNotificationChannel"
        )
            .setContentTitle("BllokuIm")
            .setContentText("Po gjeneroj raportin ditor")
            .setSmallIcon(R.drawable.report)
            .build()
        startForeground(1, serviceNotification)
    }

}