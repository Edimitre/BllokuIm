package com.edimitre.bllokuim.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "description_table")
data class Description(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String


) {
    override fun toString(): String = this.name
}
