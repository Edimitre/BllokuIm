package com.edimitre.bllokuim.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edimitre.bllokuim.data.model.Expense
import com.edimitre.bllokuim.data.service.ExpenseService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(private val expenseService: ExpenseService) :
    ViewModel() {

    var sizeOfExpensesThisYear = expenseService.sizeOfExpensesThisYear

    var valueOfExpensesThisYear = expenseService.valueOfExpensesThisYear

    var thisMonthExpensesList = expenseService.thisMonthExpensesList

    var valueOfThisMonthExpenses = expenseService.valueOfThisMonthExpenses

    var sizeOfThisMonthExpenses = expenseService.sizeOfThisMonthExpenses


    var todayExpenses = expenseService.todayExpensesList

    var valueOfTodayExpenses = expenseService.valueOfTodayExpenses

    var sizeOfTodayExpenses = expenseService.sizeOfTodayExpenses


    var biggestExpenseThisMonth = expenseService.biggestExpenseThisMonth

    var biggestExpenseThisYear = expenseService.biggestExpenseThisYear


    // CRUD
    fun saveExpense(expense: Expense): Job = viewModelScope.launch {
        expenseService.saveExpense(expense)

    }

    fun deleteExpense(expense: Expense): Job = viewModelScope.launch {
        expenseService.deleteExpense(expense)
    }


    //  YEAR MONTH DATE
    fun getAllExpensesByYearMonthDate(
        year: Int,
        month: Int,
        date: Int
    ): LiveData<List<Expense?>?>? {

        return expenseService.getAllExpensesByYearMonthDate(year, month, date)

    }

    fun getValueOfExpenseListByYearMonthDate(year: Int, month: Int, date: Int): LiveData<Int?> {

        return expenseService.getValueOfExpenseListByYearMonthDate(year, month, date)
    }

    fun getValueOfExpenseListByYearMonth(year: Int, month: Int): LiveData<Int?> {

        return expenseService.getValueOfExpenseListByYearMonth(year, month)
    }

    fun getSizeOfExpenseListByYearMonth(year: Int, month: Int): LiveData<Int?> {

        return expenseService.getSizeOfExpenseListByYearMonth(year, month)
    }


    // NAME
    fun getAllExpensesByName(name: String): LiveData<List<Expense?>?> {

        return expenseService.getAllExpensesByName(name)
    }

    fun getValueOfExpenseListByName(name: String): LiveData<Int?> {


        return expenseService.getValueOfExpenseListByName(name)
    }

    fun getSizeOfExpenseListByName(name: String): LiveData<Int?> {

        return expenseService.getSizeOfExpenseListByName(name)
    }


    // BIGGEST
    fun getBiggestExpenseByYear(year: Int): LiveData<Expense?> {

        return expenseService.getBiggestExpenseByYear(year)
    }

    fun getBiggestExpenseByYearMonth(year: Int, month: Int): LiveData<Expense?> {

        return expenseService.getBiggestExpenseByYearMonth(year, month)
    }

}