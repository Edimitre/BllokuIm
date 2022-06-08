package com.edimitre.bllokuim.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_report_table")
data class DailyReport(

    @PrimaryKey(autoGenerate = true)
    var id: Long,

    var year: Int?,

    val month: Int?,

    val date: Int?,

    val hour: Int?,

    val minute: Int?,

    val valueLimit: Int?,

    val valueSpent: Int?,

    val isOk: Boolean = false

)
