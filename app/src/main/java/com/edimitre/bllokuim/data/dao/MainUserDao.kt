package com.edimitre.bllokuim.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.edimitre.bllokuim.data.model.MainUser
import kotlinx.coroutines.flow.Flow


@Dao
interface MainUserDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(mainUser: MainUser)

    @Delete
    suspend fun delete(mainUser: MainUser)

    @Query("SELECT * FROM main_user_table WHERE id = 1")
    fun getMainUser(): LiveData<MainUser?>

    @Query("SELECT * FROM main_user_table WHERE id = 1")
    suspend fun getMainUserByCoroutine(): MainUser?
}