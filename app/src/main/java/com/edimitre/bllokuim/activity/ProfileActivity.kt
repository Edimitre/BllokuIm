package com.edimitre.bllokuim.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.edimitre.bllokuim.R
import com.edimitre.bllokuim.data.model.MainUser
import com.edimitre.bllokuim.data.viewModel.ExpenseViewModel
import com.edimitre.bllokuim.data.viewModel.MainUserViewModel
import com.edimitre.bllokuim.data.viewModel.MonthlyIncomeViewModel
import com.edimitre.bllokuim.fragment.AddUserForm
import com.edimitre.bllokuim.systemservices.SystemService
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity(),AddUserForm.AddUserListener {


    private lateinit var _userViewModel: MainUserViewModel

    private lateinit var _expenseViewModel: ExpenseViewModel

    private lateinit var _monthlyIncomeViewModel: MonthlyIncomeViewModel

    @Inject
    lateinit var systemService: SystemService

    private var user: MainUser? = null

    var TAG = "BllokuIm =>"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initViewModel()

        initUser()

        showUserProfile()

        setListeners()

    }

    private fun initViewModel() {


        _userViewModel = ViewModelProvider(this)[MainUserViewModel::class.java]
        _expenseViewModel = ViewModelProvider(this)[ExpenseViewModel::class.java]

        _monthlyIncomeViewModel = ViewModelProvider(this)[MonthlyIncomeViewModel::class.java]

    }

    private fun setToolBar(name:String) {

        setSupportActionBar(profile_toolbar)

        supportActionBar?.title = "Pershendetje ${name.uppercase(Locale.getDefault())}"

        profile_toolbar.setTitleTextColor(Color.WHITE)
    }

    private fun initUser() {
        _userViewModel.getUser().observe(this) {
            user = it
            setToolBar(user!!.name)
        }
    }

    private fun showUserProfile() {

        _expenseViewModel.sizeOfThisMonthExpenses.observe(this) {
            monthly_expenses_size_text.text = it.toString()
        }

        _expenseViewModel.valueOfThisMonthExpenses.observe(this) {
            monthly_expenses_value_text.text = it.toString()
        }

        _expenseViewModel.sizeOfExpensesThisYear.observe(this) {
            yearly_expenses_size_text.text = it.toString()
        }
        _expenseViewModel.valueOfExpensesThisYear.observe(this) {
            yearly_expenses_value_text.text = it.toString()
        }
        _monthlyIncomeViewModel.sizeOfIncomesThisMonth.observe(this) {
            monthly_incomes_size_text.text = it.toString()
        }
        _monthlyIncomeViewModel.valueOfIncomesThisMonth.observe(this) {
            monthly_incomes_value_text.text = it.toString()
        }


    }

    private fun setListeners() {

        btn_watch_balance.setOnClickListener {
            Toast.makeText(this, "balanca => " + user!!.totalAmountOfMoney, Toast.LENGTH_SHORT)
                .show()
        }

        btn_go_monthly_incomes.setOnClickListener {

            intent = Intent(this, MonthlyIncomeActivity::class.java)
            startActivity(intent)
        }

        btn_go_reports.setOnClickListener{
            intent = Intent(this, DailyReportActivity::class.java)
            startActivity(intent)
        }

        btn_edit_profile.setOnClickListener{
            openEditProfileDialog()
        }

    }

    private fun openEditProfileDialog() {
        val alertDialog = MaterialAlertDialogBuilder(
            this@ProfileActivity,
            R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background
        )

        alertDialog.setTitle("Deshironi te ndryshoni te dhenat e profilit ?\n!!KUJDES!!")

        alertDialog.setMessage("Ky Veprim nuk mund te rikthehet")

        alertDialog.setPositiveButton("Ndrysho") { _, _ ->

            // open register dialog
            val registerForm = AddUserForm()
            registerForm.show(supportFragmentManager, "edit user profile")

        }

        alertDialog.setNegativeButton("Mbyll") { _, _ ->



        }

        alertDialog.show()
    }

    override fun addUser(mainUser: MainUser) {
        mainUser.id = 1
        _userViewModel.saveUser(mainUser)
        Toast.makeText(this, " te dhenat u ruajten me sukses", Toast.LENGTH_SHORT).show()
    }

}