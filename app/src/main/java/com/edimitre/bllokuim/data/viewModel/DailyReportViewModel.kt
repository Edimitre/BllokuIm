package com.edimitre.bllokuim.data.viewModel

import androidx.lifecycle.ViewModel
import com.edimitre.bllokuim.data.model.DailyReport
import com.edimitre.bllokuim.data.service.DailyReportService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.concurrent.thread

@HiltViewModel
class DailyReportViewModel @Inject constructor(private val dailyReportService: DailyReportService) :
    ViewModel() {

    var allDailyReports = dailyReportService.allDailyReports



    fun deleteDailyReport(dailyReport: DailyReport) {

        val thread = thread {
            dailyReportService.deleteDailyReport(dailyReport)

        }
        thread.start()
        thread.join()

    }


}