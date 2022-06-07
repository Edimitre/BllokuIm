package com.edimitre.bllokuim.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.edimitre.bllokuim.data.model.MonthlyIncome
import com.edimitre.bllokuim.data.model.MonthlyIncomeType
import kotlinx.coroutines.flow.Flow

@Dao
interface MonthlyIncomeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMonthlyIncomeType(monthlyIncomeType: MonthlyIncomeType)

    @Delete
    suspend fun deleteMonthlyIncomeType(monthlyIncomeType: MonthlyIncomeType)

    @Query("SELECT * FROM monthly_income_type_table")
    fun getAllMonthlyIncomeTypes(): Flow<List<MonthlyIncomeType?>?>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMonthlyIncome(monthlyIncome: MonthlyIncome?)

    @Query("SELECT * FROM monthly_income_table WHERE id = :id")
    fun getMonthlyIncomeById(id: Int): Flow<MonthlyIncome?>

    // shfaq shpenzimet sipas muajit dhe dates te caktuar
    @Query("SELECT * FROM monthly_income_table WHERE year = :year and month = :month")
    fun getAllMonthlyIncomesByYearAndMonthLiveData(
        year: Int,
        month: Int
    ): Flow<List<MonthlyIncome?>?>?

    @Query("SELECT * FROM monthly_income_table WHERE year = :year and month = :month and day = :date")
    fun getAllMonthlyIncomesByYearMonthDate(year: Int, month: Int, date: Int): Flow<List<MonthlyIncome?>?>?

    @Query("select * from monthly_income_table where name LIKE '%' || :name || '%'")
    fun getAllMonthlyIncomeByName(name: String?): Flow<List<MonthlyIncome?>?>?

    @Query("SELECT SUM(value) FROM monthly_income_table WHERE name LIKE '%' || :name || '%'")
    fun getValueOfIncomesByName(name: String?): Flow<Int?>

    // mblidh shpenzimet e nje muaji te caktuar
    @Query("SELECT SUM(value) FROM monthly_income_table WHERE year = :year and month = :month")
    fun getValueOfIncomesByMonth(year: Int, month: Int): Flow<Int?>

    // mblidh shpenzimet e nje muaji te caktuar
    @Query("SELECT SUM(value) FROM monthly_income_table WHERE year = :year and month = :month")
    fun getValueOfIncomesByYearMonth(year: Int, month: Int): Flow<Int?>

    @Query("SELECT COUNT(*) FROM monthly_income_table where year = :year and month = :month")
    fun getNrOfIncomesByMonth(year: Int, month: Int): Flow<Int?>

    // mblidh shpenzimet e nje muaji te caktuar
    @Query("SELECT SUM(value) FROM monthly_income_table WHERE year = :year")
    fun getValueOfIncomesByYear(year: Int): Flow<Int?>

    @Query("SELECT COUNT(*) FROM monthly_income_table where year = :year")
    fun getNrOfIncomesByYear(year: Int): Flow<Int?>

    @Query("SELECT SUM(value) FROM monthly_income_table WHERE year = :year and month = :month and day = :date")
    fun getValueOfIncomesByYearMonthDate(year: Int, month: Int, date: Int): Flow<Int?>

    @Query("SELECT * FROM monthly_income_table WHERE year =:year and month =:month ORDER BY value DESC LIMIT 1")
    fun getBiggestMonthlyIncomeByYearMonth(year: Int,month: Int): Flow<MonthlyIncome?>

    @Query("SELECT * FROM monthly_income_table WHERE year =:year ORDER BY value DESC LIMIT 1")
    fun getBiggestMonthlyIncomeByYear(year: Int): Flow<MonthlyIncome?>

    @Delete
    suspend fun deleteMonthlyIncome(monthlyIncome: MonthlyIncome)

}