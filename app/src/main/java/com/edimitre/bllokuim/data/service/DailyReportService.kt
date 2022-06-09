package com.edimitre.bllokuim.data.service


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.edimitre.bllokuim.data.dao.DailyReportDao
import com.edimitre.bllokuim.data.dao.DescriptionDao
import com.edimitre.bllokuim.data.model.DailyReport
import com.edimitre.bllokuim.data.model.Description
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DailyReportService @Inject constructor(private val dailyReportDao: DailyReportDao) {

    val TAG = "BllokuIm => "

    var allDailyReports = dailyReportDao.getAllDailyReports()!!.asLiveData()

    fun saveDailyReport(dailyReport: DailyReport) {


        dailyReportDao.saveReportOnThread(dailyReport)

        Log.e(TAG, "description => " + dailyReport.isOk + " u ruajt me sukses")
    }

    fun deleteDailyReport(dailyReport: DailyReport){
        dailyReportDao.deleteDailyReport(dailyReport)
    }

}