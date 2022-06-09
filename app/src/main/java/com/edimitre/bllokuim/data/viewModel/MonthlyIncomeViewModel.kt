package com.edimitre.bllokuim.data.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edimitre.bllokuim.data.model.MonthlyIncome
import com.edimitre.bllokuim.data.model.MonthlyIncomeType
import com.edimitre.bllokuim.data.service.MonthlyIncomeService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MonthlyIncomeViewModel @Inject constructor(private val monthlyIncomeService: MonthlyIncomeService) :
    ViewModel() {


    var allMonthlyIncomeTypes = monthlyIncomeService.allMonthlyIncomeTypes

    var monthlyIncomesList = monthlyIncomeService.monthlyIncomesList

    var valueOfIncomesThisMonth = monthlyIncomeService.valueOfIncomesThisMonth

    var sizeOfIncomesThisMonth = monthlyIncomeService.sizeOfIncomesThisMonth

    var valueOfIncomesThisYear = monthlyIncomeService.valueOfIncomesThisYear

    var sizeOfIncomesThisYear = monthlyIncomeService.sizeOfIncomesThisYear


    fun insertMonthlyIncomeTypes() {

        // HARDCODED
        val monthlyIncomeTypeList: MutableList<MonthlyIncomeType> = ArrayList()
        val m1 = MonthlyIncomeType(0, "RROGA_MUJORE")
        val m2 = MonthlyIncomeType(0, "TE_ARDHURA_NGA_BIZNESI")
        val m3 = MonthlyIncomeType(0, "TE_TJERA")

        val job1 = saveMonthlyIncomeType(m1)
        val job2 = saveMonthlyIncomeType(m2)
        val job3 = saveMonthlyIncomeType(m3)

        runBlocking {

            job1.start()

            delay(1000)

            job2.start()

            delay(1000)

            job3.start()


        }

    }

    private fun saveMonthlyIncomeType(monthlyIncomeType: MonthlyIncomeType): Job = viewModelScope.launch {

        monthlyIncomeService.saveMonthlyIncomeType(monthlyIncomeType)

    }

    fun saveMonthlyIncome(monthlyIncome: MonthlyIncome): Job = viewModelScope.launch {

        monthlyIncomeService.saveMonthlyIncome(monthlyIncome)

    }

    fun deleteMonthlyIncome(monthlyIncome: MonthlyIncome): Job = viewModelScope.launch {

        monthlyIncomeService.deleteMonthlyIncome(monthlyIncome)
    }


}