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
import com.edimitre.bllokuim.data.model.Description
import com.edimitre.bllokuim.data.model.Expense
import com.edimitre.bllokuim.data.utils.TimeUtils
import com.edimitre.bllokuim.data.viewModel.DescriptionViewModel
import com.edimitre.bllokuim.data.viewModel.ExpenseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_expense.*

@AndroidEntryPoint
class AddExpenseForm : AppCompatDialogFragment() {


    private lateinit var _descriptionViewModel: DescriptionViewModel

    private lateinit var _expenseViewModel: ExpenseViewModel

    private lateinit var listener: AddExpenseListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(
            STYLE_NORMAL,
            android.R.style.Theme_Black_NoTitleBar_Fullscreen
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_expense, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()

        loadSpinner()

        setListeners()
    }


    private fun initViewModel(){
        _descriptionViewModel = ViewModelProvider(this)[DescriptionViewModel::class.java]

        _expenseViewModel = ViewModelProvider(this)[ExpenseViewModel::class.java]


    }

    private fun loadSpinner() {

        val descriptions = _descriptionViewModel.getAllDescriptionsList()


        val spinnerAdapter: ArrayAdapter<Description> =
            ArrayAdapter<Description>(
                requireActivity(),
                android.R.layout.simple_spinner_item,
                descriptions
            )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        expense_type_spinner.adapter = spinnerAdapter

    }

    private fun setListeners() {

        btn_save_expense.setOnClickListener{

            if (inputIsOk()){
                val description: Description = expense_type_spinner.selectedItem as Description

                val value = input_expense_value.text.toString().toDouble()

                val expense = Expense(0, TimeUtils().getCurrentYear(),TimeUtils().getCurrentMonth(),
                    TimeUtils().getCurrentDate(),TimeUtils().getCurrentHour(),TimeUtils().getCurrentMinute(),value,description)

                listener.addExpense(expense)

                dismiss()
            }

        }

        btn_close_expense_dialog.setOnClickListener{
            dismiss()
        }
    }

    private fun inputIsOk():Boolean{
        return input_expense_value.text.toString().isNotEmpty()

    }

    interface AddExpenseListener {
        fun addExpense(expense: Expense)
    }

    override fun onAttach(context: Context) {
        listener = try {
            context as AddExpenseListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "shto listener")
        }
        super.onAttach(context)
    }
}