package com.edimitre.bllokuim.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.edimitre.bllokuim.data.model.Description
import kotlinx.coroutines.flow.Flow


@Dao
interface DescriptionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(description: Description)

    @Delete
    suspend fun delete(description: Description)

    @Query("SELECT * FROM description_table WHERE id =:id")
    fun getById(id: Int): LiveData<Description>

    @Query("SELECT * FROM description_table WHERE name LIKE '%' || :name || '%'")
    fun getByName(name: String): LiveData<List<Description>>

    @Query("SELECT * FROM description_table")
    fun getAll(): Flow<List<Description>>

    @Query("SELECT * FROM description_table")
    suspend fun getAllList(): List<Description>

}