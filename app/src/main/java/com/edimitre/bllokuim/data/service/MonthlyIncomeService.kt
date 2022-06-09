package com.edimitre.bllokuim.data.service


import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.edimitre.bllokuim.data.dao.MonthlyIncomeDao
import com.edimitre.bllokuim.data.model.MonthlyIncome
import com.edimitre.bllokuim.data.model.MonthlyIncomeType
import com.edimitre.bllokuim.data.utils.TimeUtils
import javax.inject.Inject

class MonthlyIncomeService @Inject constructor(private val monthlyIncomeDao: MonthlyIncomeDao) {


    var allMonthlyIncomeTypes = monthlyIncomeDao.getAllMonthlyIncomeTypes().asLiveData()

    var monthlyIncomesList = monthlyIncomeDao.getAllMonthlyIncomesByYearAndMonthLiveData(
        TimeUtils().getCurrentYear(),
        TimeUtils().getCurrentMonth()
    )!!.asLiveData()

    var valueOfIncomesThisMonth = monthlyIncomeDao.getValueOfIncomesByMonth(
        TimeUtils().getCurrentYear(),
        TimeUtils().getCurrentMonth()
    ).asLiveData()
    var sizeOfIncomesThisMonth = monthlyIncomeDao.getNrOfIncomesByMonth(
        TimeUtils().getCurrentYear(),
        TimeUtils().getCurrentMonth()
    ).asLiveData()

    var valueOfIncomesThisYear =
        monthlyIncomeDao.getValueOfIncomesByYear(TimeUtils().getCurrentYear()).asLiveData()
    var sizeOfIncomesThisYear =
        monthlyIncomeDao.getNrOfIncomesByYear(TimeUtils().getCurrentYear()).asLiveData()


    suspend fun saveMonthlyIncomeType(monthlyIncomeType: MonthlyIncomeType) {

        monthlyIncomeDao.saveMonthlyIncomeType(monthlyIncomeType)

    }

    suspend fun saveMonthlyIncome(monthlyIncome: MonthlyIncome) {

        monthlyIncomeDao.saveMonthlyIncome(monthlyIncome)

    }

    suspend fun deleteMonthlyIncome(monthlyIncome: MonthlyIncome) {

        monthlyIncomeDao.deleteMonthlyIncome(monthlyIncome)
    }


    fun getMonthlyIncomeById(id: Int): LiveData<MonthlyIncome?> {

        return monthlyIncomeDao.getMonthlyIncomeById(id).asLiveData()
    }

    fun getAllMonthlyIncomesByYearMonthDate(
        year: Int,
        month: Int,
        date: Int
    ): LiveData<List<MonthlyIncome?>?> {

        return monthlyIncomeDao.getAllMonthlyIncomesByYearMonthDate(year, month, date)!!
            .asLiveData()
    }

    fun getValueOfIncomesByYearMonth(year: Int, month: Int): LiveData<Int?> {
        return monthlyIncomeDao.getValueOfIncomesByYearMonth(year, month).asLiveData()
    }

    fun getValueOfIncomesByYearMonthDate(year: Int, month: Int, date: Int): LiveData<Int?> {

        return monthlyIncomeDao.getValueOfIncomesByYearMonthDate(year, month, date).asLiveData()
    }


    fun getAllMonthlyIncomeByName(name: String?): LiveData<List<MonthlyIncome?>?> {
        return monthlyIncomeDao.getAllMonthlyIncomeByName(name)!!.asLiveData()
    }

    fun getValueOfIncomesByName(name: String?): LiveData<Int?> {

        return monthlyIncomeDao.getValueOfIncomesByName(name).asLiveData()
    }


    fun getBiggestMonthlyIncomeByYearMonth(year: Int, month: Int): LiveData<MonthlyIncome?> {
        return monthlyIncomeDao.getBiggestMonthlyIncomeByYearMonth(year, month).asLiveData()
    }

    fun getBiggestMonthlyIncomeByYear(year: Int): LiveData<MonthlyIncome?> {
        return monthlyIncomeDao.getBiggestMonthlyIncomeByYear(year).asLiveData()

    }


}