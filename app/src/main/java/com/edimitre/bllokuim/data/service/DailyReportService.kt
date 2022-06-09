package com.edimitre.bllokuim.data.service


import androidx.lifecycle.asLiveData
import com.edimitre.bllokuim.data.dao.DailyReportDao
import com.edimitre.bllokuim.data.model.DailyReport
import javax.inject.Inject

class DailyReportService @Inject constructor(private val dailyReportDao: DailyReportDao) {


    var allDailyReports = dailyReportDao.getAllDailyReports()!!.asLiveData()


    fun deleteDailyReport(dailyReport: DailyReport) {
        dailyReportDao.deleteDailyReport(dailyReport)
    }

}