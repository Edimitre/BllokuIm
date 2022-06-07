package com.edimitre.bllokuim.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.edimitre.bllokuim.R
import com.edimitre.bllokuim.data.model.MonthlyIncome
import com.edimitre.bllokuim.data.utils.TimeUtils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class MonthlyIncomeAdapter : RecyclerView.Adapter<MonthlyIncomeAdapter.MonthlyIncomeHolder>() {

    private var allMonthlyIncomes: List<MonthlyIncome>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthlyIncomeHolder {


        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.monthly_income_item_view, parent, false)

        return MonthlyIncomeHolder(view)
    }

    override fun onBindViewHolder(holder: MonthlyIncomeHolder, position: Int) {


        val monthlyIncome: MonthlyIncome? = allMonthlyIncomes?.getOrNull(position)

        if (monthlyIncome != null) {

            holder.bind(monthlyIncome)

        }

    }


    override fun getItemCount(): Int {
        return allMonthlyIncomes?.size ?: 0
    }

    class MonthlyIncomeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        private val monthlyIncomeTypeNameText: TextView =
            itemView.findViewById(R.id.monthly_income_type_name_text)
        private val monthlyIncomeDateText: TextView =
            itemView.findViewById(R.id.monthly_income_date_text)
        private val monthlyIncomeTimeText: TextView =
            itemView.findViewById(R.id.monthly_income_time_text)
        private val monthlyIncomeValueText: TextView =
            itemView.findViewById(R.id.monthly_income_value_text)
        private val monthlyIncomeDescriptionText: TextView =
            itemView.findViewById(R.id.monthly_income_description_text)


        fun bind(monthlyIncome: MonthlyIncome) {

            @SuppressLint("SimpleDateFormat")
            val dateFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")

            @SuppressLint("SimpleDateFormat")
            val timeFormat: DateFormat = SimpleDateFormat("HH:mm:ss")


            val date = Date(
                TimeUtils().getTimeInMilliSeconds(
                    monthlyIncome.year,
                    monthlyIncome.month,
                    monthlyIncome.day,
                    monthlyIncome.hour,
                    monthlyIncome.minute
                )
            )

            val miDate = dateFormat.format(date)

            val miTime = timeFormat.format(date)



            monthlyIncomeTypeNameText.text = monthlyIncome.monthlyIncomeType.name
            monthlyIncomeDateText.text = miDate
            monthlyIncomeTimeText.text = miTime
            monthlyIncomeValueText.text = monthlyIncome.value.toString()
            monthlyIncomeDescriptionText.text = monthlyIncome.description

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun putMonthlyIncomes(monthlyIncomesList: List<MonthlyIncome>) {
        allMonthlyIncomes = monthlyIncomesList
        notifyDataSetChanged()
    }

    fun getMonthlyIncomeByPos(pos: Int): MonthlyIncome? {
        return this.allMonthlyIncomes?.get(pos)
    }
}