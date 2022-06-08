package com.edimitre.bllokuim.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder_table")
data class Reminder(

    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var alarmTimeInMillis: Long,
    var description: String,
    var isActive: Boolean

) {
    override fun toString(): String {
        return "Reminder{" +
                "id=" + id +
                ", alarmTimeInMillis=" + alarmTimeInMillis +
                ", description='" + description + '\'' +
                ", isActive=" + isActive +
                '}'
    }
}