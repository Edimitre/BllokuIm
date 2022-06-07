package com.edimitre.bllokuim.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.edimitre.bllokuim.R
import com.edimitre.bllokuim.adapter.ExpenseAdapter
import com.edimitre.bllokuim.adapter.MonthlyIncomeAdapter
import com.edimitre.bllokuim.data.model.Expense
import com.edimitre.bllokuim.data.model.MainUser
import com.edimitre.bllokuim.data.model.MonthlyIncome
import com.edimitre.bllokuim.data.viewModel.ExpenseViewModel
import com.edimitre.bllokuim.data.viewModel.MainUserViewModel
import com.edimitre.bllokuim.data.viewModel.MonthlyIncomeViewModel
import com.edimitre.bllokuim.fragment.AddExpenseForm
import com.edimitre.bllokuim.fragment.AddMonthlyIncomeForm
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_expense.*
import kotlinx.android.synthetic.main.activity_monthly_income.*

@AndroidEntryPoint
class MonthlyIncomeActivity : AppCompatActivity() ,AddMonthlyIncomeForm.AddMonthlyIncomeListener{

    private lateinit var adapter: MonthlyIncomeAdapter
    private lateinit var _monthlyIncomeViewModel:MonthlyIncomeViewModel
    private lateinit var _userViewModel:MainUserViewModel
    private var user:MainUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_income)

        initViewModel()

        initRecyclerView()

        initUser()

        showThisMonthIncomes()

        setListeners()
    }


    private fun initViewModel(){
        _monthlyIncomeViewModel = ViewModelProvider(this)[MonthlyIncomeViewModel::class.java]
        _userViewModel = ViewModelProvider(this)[MainUserViewModel::class.java]

    }

    private fun initUser(){
        _userViewModel.getUser().observe(this){
            user=it

        }

    }

    private fun initRecyclerView() {

        adapter = MonthlyIncomeAdapter()

        monthly_income_recycler_view.setHasFixedSize(true)
        monthly_income_recycler_view.layoutManager = LinearLayoutManager(this)
        monthly_income_recycler_view.adapter = adapter
    }

    private fun setToolbar(value:String) {

        setSupportActionBar(monthly_income_toolbar)
        supportActionBar?.title = "Te ardhurat : $value"
        monthly_income_toolbar.setTitleTextColor(Color.WHITE)

    }

    private fun setListeners(){

        btn_add_monthly_income.setOnClickListener{
            openMonthlyIncomeForm()
        }
    }

    private fun openMonthlyIncomeForm(){
        val monthlyIncomeForm = AddMonthlyIncomeForm()
        monthlyIncomeForm.show(supportFragmentManager, "add monthly income")

    }

    private fun showThisMonthIncomes(){
        _monthlyIncomeViewModel.monthlyIncomesList.observe(this){
            adapter.putMonthlyIncomes(it as List<MonthlyIncome>)
        }
        _monthlyIncomeViewModel.valueOfIncomesThisMonth.observe(this){
            setToolbar(it.toString())
        }
    }


    // comes from fragment form
    override fun addMonthlyIncome(monthlyIncome: MonthlyIncome) {


        if (user != null){
            user!!.addMoney(monthlyIncome.value)
            val job = _userViewModel.saveUser(user!!)
            job.start()

            val job2 = _monthlyIncomeViewModel.saveMonthlyIncome(monthlyIncome)
            job2.start()
        }else{
            val job2 = _monthlyIncomeViewModel.saveMonthlyIncome(monthlyIncome)
            job2.start()
        }
    }


}