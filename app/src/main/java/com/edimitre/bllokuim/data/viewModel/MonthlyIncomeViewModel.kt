package com.edimitre.bllokuim.data.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edimitre.bllokuim.data.model.MonthlyIncome
import com.edimitre.bllokuim.data.model.MonthlyIncomeType
import com.edimitre.bllokuim.data.service.MonthlyIncomeService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
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


    fun insertMonthlyIncomeTypes(): Job = viewModelScope.launch {

        // HARDCODED
        val monthlyIncomeTypeList: MutableList<MonthlyIncomeType> = ArrayList()
        val m1 = MonthlyIncomeType(0, "RROGA_MUJORE")
        val m2 = MonthlyIncomeType(0, "TE_ARDHURA_NGA_BIZNESI")
        val m3 = MonthlyIncomeType(0, "TE_TJERA")

        monthlyIncomeTypeList.add(m1)
        monthlyIncomeTypeList.add(m2)
        monthlyIncomeTypeList.add(m3)

        monthlyIncomeTypeList.forEach {
            val job = saveMonthlyIncomeType(it)
            job.start()
        }
    }

    fun saveMonthlyIncomeType(monthlyIncomeType: MonthlyIncomeType): Job = viewModelScope.launch {

        monthlyIncomeService.saveMonthlyIncomeType(monthlyIncomeType)

    }

    fun saveMonthlyIncome(monthlyIncome: MonthlyIncome): Job = viewModelScope.launch {

        monthlyIncomeService.saveMonthlyIncome(monthlyIncome)

    }

    fun deleteMonthlyIncome(monthlyIncome: MonthlyIncome): Job = viewModelScope.launch {

        monthlyIncomeService.deleteMonthlyIncome(monthlyIncome)
    }


}