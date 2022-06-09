package com.edimitre.bllokuim.activity

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edimitre.bllokuim.R
import com.edimitre.bllokuim.adapter.ExpenseAdapter
import com.edimitre.bllokuim.data.model.Expense
import com.edimitre.bllokuim.data.model.MainUser
import com.edimitre.bllokuim.data.viewModel.ExpenseViewModel
import com.edimitre.bllokuim.data.viewModel.MainUserViewModel
import com.edimitre.bllokuim.fragment.AddExpenseForm
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_expense.*
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class ExpenseActivity : AppCompatActivity(), ExpenseAdapter.OnExpenseClickListener,
    AddExpenseForm.AddExpenseListener {

    private lateinit var adapter: ExpenseAdapter

    private lateinit var _expenseViewModel: ExpenseViewModel

    private lateinit var _userViewModel: MainUserViewModel


    private var user: MainUser? = null

    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_expense)

        initViewModel()

        initRecyclerView()

        setListeners()

        showTodayExpenses()

        initUser()

    }

    private fun setToolbar(value: Double) {

        setSupportActionBar(expense_toolbar)

        supportActionBar?.title = "Shpenzuar : $value"

        expense_toolbar.setTitleTextColor(Color.WHITE)
    }

    private fun initViewModel() {
        _expenseViewModel = ViewModelProvider(this)[ExpenseViewModel::class.java]
        _userViewModel = ViewModelProvider(this)[MainUserViewModel::class.java]

    }

    private fun initUser() {
        _userViewModel.getUser().observe(this) {
            user = it
        }

    }

    private fun initRecyclerView() {

        adapter = ExpenseAdapter(this)

        expenses_recycler_view.setHasFixedSize(true)
        expenses_recycler_view.layoutManager = LinearLayoutManager(this)
        expenses_recycler_view.adapter = adapter
    }

    private fun setListeners() {

        btn_add_expense.setOnClickListener {
            openAddExpenseDialog()

        }

        btn_open_date_picker.setOnClickListener {

            openDatePicker()

        }

        btn_close_selected_expenses.setOnClickListener {
            showTodayExpenses()

        }
    }

    private fun showTodayExpenses() {

        var spentValue: Int?

        _expenseViewModel.todayExpenses?.observe(this) { adapter.putExpenses(it as List<Expense>) }

        _expenseViewModel.valueOfTodayExpenses.observe(this) {
            spentValue = it
            if (spentValue != null) {
                setToolbar(spentValue!!.toDouble())
            } else {
                val emptyValue = 0
                setToolbar(emptyValue.toDouble())
            }
        }

        btn_close_selected_expenses.visibility = View.INVISIBLE

        enableTouchFunctions()
    }

    private fun openAddExpenseDialog() {

        val expenseDialog = AddExpenseForm()
        expenseDialog.show(supportFragmentManager, "add expense")

    }

    private fun openDatePicker() {

        var spentValue: Int? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val dateDialog = DatePickerDialog(this, R.style.DialogTheme)
            dateDialog.setOnDateSetListener { _, year, month, day ->

                _expenseViewModel.getAllExpensesByYearMonthDate(year, month, day)!!
                    .observe(this) {
                        if (it != null) {
                            if (it.isNotEmpty()) {
                                adapter.putExpenses(it as List<Expense>)
                                disableTouchFunctions()
                            } else {
                                Toast.makeText(
                                    this,
                                    "Dita e zgjedhur nuk ka shpenzime",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }
                    }


                _expenseViewModel.getValueOfExpenseListByYearMonthDate(year, month, day)
                    .observe(this) {
                        spentValue = it
                        if (spentValue != null) {
                            setToolbar(spentValue!!.toDouble())
                            btn_close_selected_expenses.visibility = View.VISIBLE
                        }
                    }
            }
            dateDialog.show()
        }

    }

    // comes from adapter
    override fun onExpenseClicked(expense: Expense) {

        // todo do something with clicked expense
    }

    // comes from dialog
    override fun addExpense(expense: Expense) {

        executeUserExpense(expense)
    }

    private fun executeUserExpense(expense: Expense) {

        runBlocking {
            if (user != null) {

                user!!.spentMoney(expense.spentValue)

                val saveJob = _userViewModel.saveUser(user!!)
                saveJob.start()


                val saveExpenseJob = _expenseViewModel.saveExpense(expense)
                saveExpenseJob.start()

            } else {

                val saveJob = _expenseViewModel.saveExpense(expense)
                saveJob.start()

            }
        }


    }

    private fun enableTouchFunctions() {
        itemTouchHelper =
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val expense: Expense? =
                        adapter.getExpenseByPos(viewHolder.adapterPosition)

                    if (expense != null) {

                        val alertDialog = MaterialAlertDialogBuilder(
                            this@ExpenseActivity,
                            R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background
                        )

                        alertDialog.setTitle("Shpenzimi :" + expense.description)

                        alertDialog.setMessage(
                            "Deshironi Ta Fshini ?\n" +
                                    "KUJDES! ..ky veperim nuk mund te rikthehet!"
                        )
                        alertDialog.setPositiveButton("Fshij") { _, _ ->

                            deleteExpense(expense)


                            Toast.makeText(
                                applicationContext,
                                "shpenzimi u fshi",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                        alertDialog.setNegativeButton("mbyll") { _, _ ->


                        }

                        alertDialog.setOnDismissListener {
                            showTodayExpenses()
                        }

                        alertDialog.show()

                    }


                }
            })

        itemTouchHelper.attachToRecyclerView(expenses_recycler_view)
    }

    private fun disableTouchFunctions() {
        itemTouchHelper.attachToRecyclerView(null)
    }

    private fun deleteExpense(expense: Expense) {

        user!!.addMoney(expense.spentValue)

        _userViewModel.saveUser(user!!)
        _expenseViewModel.deleteExpense(expense)

    }

}
