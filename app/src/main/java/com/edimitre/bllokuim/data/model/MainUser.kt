package com.edimitre.bllokuim.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "main_user_table")
data class MainUser(

    @PrimaryKey(autoGenerate = true)
    var id: Int,

    var name: String,

    var totalAmountOfMoney: Double

) {

    fun spentMoney(value: Double) {

        totalAmountOfMoney -= value
    }

    fun addMoney(value: Double) {

        totalAmountOfMoney += value

    }

}
