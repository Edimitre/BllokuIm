package com.edimitre.bllokuim.data.dao

import androidx.room.*
import com.edimitre.bllokuim.data.model.Expense
import kotlinx.coroutines.flow.Flow


@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)

    // FLOW
    @Query("SELECT * FROM expense_table WHERE year = :year and month = :month and date = :date")
    fun getAllExpensesByYearMonthAndDateFlow(year: Int, month: Int, date: Int): Flow<List<Expense?>?>?

    @Query("SELECT * FROM expense_table WHERE year = :year and month = :month")
    fun getAllExpensesByYearMonth(year: Int, month: Int): Flow<List<Expense?>?>?

    @Query("SELECT COUNT(*) FROM expense_table where year = :year and month = :month and date =:date")
    fun getSizeOfExpenseListByYearMonthDate(year: Int, month: Int,date: Int): Flow<Int?>



    @Query("select * from expense_table where name LIKE '%' || :name || '%'")
    fun getAllExpensesByName(name: String): Flow<List<Expense?>?>?

    @Query("SELECT SUM(spentValue) FROM expense_table WHERE name LIKE '%' || :name || '%'")
    fun getValueOfExpenseListByName(name: String?): Flow<Int?>

    @Query("SELECT COUNT(*) FROM expense_table where name = :name")
    fun getSizeOfExpenseListByName(name: String): Flow<Int?>

    @Query("SELECT SUM(spentValue) FROM expense_table WHERE year = :year and month = :month")
    fun getValueOfExpenseListByYearMonth(year: Int, month: Int): Flow<Int?>

    @Query("SELECT SUM(spentValue) FROM expense_table WHERE year = :year and month = :month and date = :date")
    fun getValueOfExpenseListByYearMonthDate(year: Int, month: Int, date: Int): Flow<Int?>

    @Query("SELECT SUM(spentValue) FROM expense_table WHERE year = :year and month = :month and date = :date")
    fun getValueOfExpenseListByYearMonthDateOnThread(year: Int, month: Int, date: Int): Int?

    @Query("SELECT SUM(spentValue) FROM expense_table WHERE year = :year")
    fun getValueOfExpenseListByYear(year: Int): Flow<Int?>

    @Query("SELECT COUNT(*) FROM expense_table where year = :year and month = :month")
    fun getSizeOfExpenseListByYearMonth(year: Int, month: Int): Flow<Int?>

    @Query("SELECT COUNT(*) FROM expense_table where year = :year")
    fun getSizeOfExpenseListByYear(year: Int): Flow<Int?>

    @Query("SELECT * FROM expense_table where year =:year ORDER BY spentValue DESC LIMIT 1")
    fun getBiggestExpenseByYear(year: Int): Flow<Expense?>

    @Query("SELECT * FROM expense_table where year =:year and month =:month ORDER BY spentValue DESC LIMIT 1")
    fun getBiggestExpenseByYearMonth(year: Int, month: Int): Flow<Expense?>

}