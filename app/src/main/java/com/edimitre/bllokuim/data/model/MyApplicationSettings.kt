package com.edimitre.bllokuim.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "settings_table")
data class MyApplicationSettings(

    @PrimaryKey(autoGenerate = true)
    var id: Int,

    var workerEnabled: Boolean,

    var dailyReportGeneratorEnabled: Boolean,

    var backDbEnabled: Boolean


) {


}