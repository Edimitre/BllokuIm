package com.edimitre.bllokuim.data.dao

import androidx.room.*
import com.edimitre.bllokuim.data.model.Reminder
import kotlinx.coroutines.flow.Flow


@Dao
interface ReminderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveReminderOnThread(reminder: Reminder?)

    @Query("SELECT * FROM reminder_table where isActive like 1 ORDER BY alarmTimeInMillis ASC LIMIT 1")
    fun getFirstReminderOnThread(): Reminder?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveReminder(reminder: Reminder?)

    @Query("SELECT * FROM reminder_table")
    fun getAllReminders(): Flow<List<Reminder?>?>?

    @Delete
    suspend fun deleteReminder(reminder: Reminder?)

    @Query("SELECT * FROM reminder_table where isActive like 1 ORDER BY alarmTimeInMillis ASC LIMIT 1")
    fun getFirstReminder(): Flow<Reminder?>
}