package com.edimitre.bllokuim.data.dao

import androidx.room.*
import com.edimitre.bllokuim.data.model.DailyReport
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyReportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveReportOnThread(dailyReport: DailyReport?)


    @Query("SELECT * FROM daily_report_table")
    fun getAllDailyReports(): Flow<List<DailyReport?>?>?

    @Delete
    suspend fun deleteDailyReport(dailyReport: DailyReport?)

}