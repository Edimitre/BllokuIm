package com.edimitre.bllokuim.data.service


import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.edimitre.bllokuim.data.dao.ExpenseDao
import com.edimitre.bllokuim.data.model.Expense
import com.edimitre.bllokuim.data.utils.TimeUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExpenseService @Inject constructor(private val expenseDao: ExpenseDao) {

    val TAG = "BllokuIm => "

    var sizeOfExpensesThisYear = expenseDao.getSizeOfExpenseListByYear(TimeUtils().getCurrentYear()).asLiveData()

    var valueOfExpensesThisYear = expenseDao.getValueOfExpenseListByYear(TimeUtils().getCurrentYear()).asLiveData()

    var thisMonthExpensesList = expenseDao
        .getAllExpensesByYearMonth(
            TimeUtils().getCurrentYear(),
            TimeUtils().getCurrentMonth()
        )?.asLiveData()

    var valueOfThisMonthExpenses = expenseDao.getValueOfExpenseListByYearMonth(TimeUtils().getCurrentYear(),
        TimeUtils().getCurrentMonth()).asLiveData()

    var sizeOfThisMonthExpenses = expenseDao.getSizeOfExpenseListByYearMonth(TimeUtils().getCurrentYear(),
        TimeUtils().getCurrentMonth()).asLiveData()






    var todayExpensesList = expenseDao.getAllExpensesByYearMonthAndDateFlow(
        TimeUtils().getCurrentYear(),
        TimeUtils().getCurrentMonth(), TimeUtils().getCurrentDate()
    )?.asLiveData()

    var valueOfTodayExpenses = expenseDao.getValueOfExpenseListByYearMonthDate(TimeUtils().getCurrentYear(),
        TimeUtils().getCurrentMonth(), TimeUtils().getCurrentDate()).asLiveData()

    var sizeOfTodayExpenses = expenseDao.getSizeOfExpenseListByYearMonthDate(TimeUtils().getCurrentYear(),
        TimeUtils().getCurrentMonth(), TimeUtils().getCurrentDate()).asLiveData()


    var biggestExpenseThisMonth = expenseDao.getBiggestExpenseByYearMonth(TimeUtils().getCurrentYear(),
        TimeUtils().getCurrentMonth()).asLiveData()

    var biggestExpenseThisYear = expenseDao.getBiggestExpenseByYear(TimeUtils().getCurrentYear()).asLiveData()

    suspend fun saveExpense(expense: Expense) {

        expenseDao.save(expense)

    }

    suspend fun deleteExpense(expense: Expense) {
        expenseDao.delete(expense)

    }

    //
    fun getAllExpensesByYearMonthDate(year: Int, month: Int, date: Int): LiveData<List<Expense?>?>? {

        return expenseDao.getAllExpensesByYearMonthAndDateFlow(year, month, date)?.asLiveData()
    }

    fun getValueOfExpenseListByYearMonthDate(year: Int, month: Int, date: Int): LiveData<Int?> {

        return expenseDao.getValueOfExpenseListByYearMonthDate(year, month, date).asLiveData()
    }

    fun getValueOfExpenseListByYearMonth(year: Int,month: Int):LiveData<Int?>{

        return expenseDao.getValueOfExpenseListByYearMonth(year, month).asLiveData()
    }

    fun getSizeOfExpenseListByYearMonthDate(year: Int, month: Int,date: Int): LiveData<Int?>{

        return expenseDao.getSizeOfExpenseListByYearMonthDate(year, month, date).asLiveData()
    }

    fun getSizeOfExpenseListByYearMonth(year:Int,month:Int):LiveData<Int?>{

        return expenseDao.getSizeOfExpenseListByYearMonth(year, month).asLiveData()
    }



    fun getAllExpensesByName(name:String): LiveData<List<Expense?>?> {

        return expenseDao.getAllExpensesByName(name)!!.asLiveData()
    }

    fun getValueOfExpenseListByName(name: String): LiveData<Int?> {


        return expenseDao.getValueOfExpenseListByName(name).asLiveData()
    }

    fun getSizeOfExpenseListByName(name:String): LiveData<Int?>{

        return expenseDao.getSizeOfExpenseListByName(name).asLiveData()
    }


    fun getBiggestExpenseByYear(year: Int): LiveData<Expense?>{

        return expenseDao.getBiggestExpenseByYear(year).asLiveData()
    }

    fun getBiggestExpenseByYearMonth(year: Int, month: Int): LiveData<Expense?>{

        return expenseDao.getBiggestExpenseByYearMonth(year, month).asLiveData()
    }


}