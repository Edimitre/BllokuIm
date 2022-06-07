package com.edimitre.bllokuim.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "monthly_income_type_table")
data class MonthlyIncomeType(

    @PrimaryKey(autoGenerate = true)
    val income_type_id: Int,

    val name: String

) {

    override fun toString(): String {
        return name
    }

}
