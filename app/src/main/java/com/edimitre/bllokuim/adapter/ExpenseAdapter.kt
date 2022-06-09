package com.edimitre.bllokuim.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.edimitre.bllokuim.R
import com.edimitre.bllokuim.data.model.Expense
import com.edimitre.bllokuim.data.utils.TimeUtils
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ExpenseAdapter(private val onExpenseClickListener: OnExpenseClickListener) :
    RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    private var allExpenses: List<Expense>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.expense_item_view, parent, false)
        return ExpenseViewHolder(view)

    }


    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val currentExpense = allExpenses?.getOrNull(position)


        if (currentExpense != null) {
            holder.bind(currentExpense, onExpenseClickListener)

        }

    }


    class ExpenseViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val descriptionName: TextView = itemView.findViewById(R.id.description_name_text);
        private val valueText: TextView = itemView.findViewById(R.id.expense_value_text);
        private val dateText: TextView = itemView.findViewById(R.id.expense_date_text);
        private val hourText: TextView = itemView.findViewById(R.id.expense_hour_text);


        fun bind(expense: Expense, onExpenseClickListener: OnExpenseClickListener) {


            @SuppressLint("SimpleDateFormat")
            val dateFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")

            @SuppressLint("SimpleDateFormat")
            val timeFormat: DateFormat = SimpleDateFormat("HH:mm:ss")


            val date = Date(
                TimeUtils().getTimeInMilliSeconds(
                    expense.year,
                    expense.month,
                    expense.date,
                    expense.hour,
                    expense.minute
                )
            )

            val expDate = dateFormat.format(date)

            val expTime = timeFormat.format(date)
            descriptionName.text = expense.description.name
            valueText.text = expense.spentValue.toString()
            dateText.text = expDate
            hourText.text = expTime


            itemView.setOnClickListener {
                onExpenseClickListener.onExpenseClicked(expense)
            }

        }

    }

    override fun getItemCount(): Int {

        return allExpenses?.size ?: 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun putExpenses(expenseList: List<Expense>) {
        allExpenses = expenseList
        notifyDataSetChanged()
    }

    fun getExpenseByPos(pos: Int): Expense? {
        return this.allExpenses?.get(pos)
    }

    interface OnExpenseClickListener {
        fun onExpenseClicked(expense: Expense)
    }
}