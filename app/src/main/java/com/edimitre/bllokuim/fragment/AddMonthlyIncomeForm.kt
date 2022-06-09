package com.edimitre.bllokuim.fragment

import android.content.Context
import android.os.Bundle


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.ViewModelProvider
import com.edimitre.bllokuim.R

import com.edimitre.bllokuim.data.model.MonthlyIncome
import com.edimitre.bllokuim.data.model.MonthlyIncomeType
import com.edimitre.bllokuim.data.utils.TimeUtils

import com.edimitre.bllokuim.data.viewModel.MonthlyIncomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

import kotlinx.android.synthetic.main.fragment_add_expense.*
import kotlinx.android.synthetic.main.fragment_add_monthly_income_form.*

@AndroidEntryPoint
class AddMonthlyIncomeForm : BottomSheetDialogFragment() {


    private lateinit var _monthlyIncomeViewModel: MonthlyIncomeViewModel


    private lateinit var listener: AddMonthlyIncomeListener



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_monthly_income_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()

        loadSpinner()

        setListeners()
    }


    private fun initViewModel(){
        _monthlyIncomeViewModel = ViewModelProvider(this)[MonthlyIncomeViewModel::class.java]

    }

    private fun loadSpinner() {

        _monthlyIncomeViewModel.allMonthlyIncomeTypes.observe(this){

            val spinnerAdapter: ArrayAdapter<MonthlyIncomeType> =
                ArrayAdapter<MonthlyIncomeType>(
                    requireActivity(),
                    android.R.layout.simple_spinner_item,
                    it as List<MonthlyIncomeType>
                )
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            monthly_income_type_spinner.adapter = spinnerAdapter
        }


    }

    private fun setListeners(){

        btn_save_monthly_income.setOnClickListener{
            if (isInputOk()){
                val monthlyIncomeType:MonthlyIncomeType = monthly_income_type_spinner.selectedItem as MonthlyIncomeType

                val value = input_monthly_income_value.text.toString().toDouble()
                val description = input_monthly_income_description.text.toString()
                val monthlyIncome = MonthlyIncome(0,description,value,TimeUtils().getCurrentYear(),
                    TimeUtils().getCurrentMonth(),TimeUtils().getCurrentDate(),TimeUtils().getCurrentHour(),TimeUtils().getCurrentMinute(),monthlyIncomeType)

                listener.addMonthlyIncome(monthlyIncome)

                dismiss()
            }

        }

        btn_close_add_monthly_income_dialog.setOnClickListener{
            dismiss()
        }
    }

    private fun isInputOk():Boolean{
        return input_monthly_income_value.text.toString().isNotEmpty() && input_monthly_income_description.text.toString().isNotEmpty()
    }

    interface AddMonthlyIncomeListener {
        fun addMonthlyIncome(monthlyIncome:MonthlyIncome)
    }

    override fun onAttach(context: Context) {
        listener = try {
            context as AddMonthlyIncomeListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "shto listener")
        }
        super.onAttach(context)
    }


}