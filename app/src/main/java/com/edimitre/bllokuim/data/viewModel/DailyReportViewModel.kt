package com.edimitre.bllokuim.data.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edimitre.bllokuim.data.model.DailyReport
import com.edimitre.bllokuim.data.service.DailyReportService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.concurrent.thread

@HiltViewModel
class DailyReportViewModel @Inject constructor(private val dailyReportService: DailyReportService) :
    ViewModel() {

    var allDailyReports = dailyReportService.allDailyReports


    fun saveDailyReport(dailyReport: DailyReport) {

        val thread = thread{
            dailyReportService.saveDailyReport(dailyReport)

        }
        thread.start()
        thread.join()

    }

    fun deleteDailyReport(dailyReport: DailyReport) {

        val thread = thread{
            dailyReportService.deleteDailyReport(dailyReport)

        }
        thread.start()
        thread.join()

    }


}