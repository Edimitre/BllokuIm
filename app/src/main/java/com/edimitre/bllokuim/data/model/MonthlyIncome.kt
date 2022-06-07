package com.edimitre.bllokuim.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.edimitre.bllokuim.data.model.MonthlyIncomeType

@Entity(tableName = "monthly_income_table")
data class MonthlyIncome(

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val description: String,
    val value: Double,
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,

    @Embedded
    val monthlyIncomeType: MonthlyIncomeType

)
