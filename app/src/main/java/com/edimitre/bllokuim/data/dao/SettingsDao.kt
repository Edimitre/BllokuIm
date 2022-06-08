package com.edimitre.bllokuim.data.dao

import androidx.room.*
import com.edimitre.bllokuim.data.model.MyApplicationSettings

@Dao
interface SettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveSettings(myApplicationSettings: MyApplicationSettings)

    @Query("SELECT * FROM settings_table WHERE id = 1")
    fun getSettings(): MyApplicationSettings?


    @Delete
    fun deleteAppSettings(applicationSettings: MyApplicationSettings)

}