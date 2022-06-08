package com.edimitre.bllokuim.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.edimitre.bllokuim.R
import com.edimitre.bllokuim.data.model.DailyReport
import com.edimitre.bllokuim.data.utils.TimeUtils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DailyReportsAdapter : RecyclerView.Adapter<DailyReportsAdapter.DailyReportHolder>() {

    private var allDailyReports: List<DailyReport>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyReportHolder {


        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.daily_report_item_view, parent, false)

        return DailyReportHolder(view)
    }

    override fun onBindViewHolder(holder: DailyReportHolder, position: Int) {


        val dailyReport: DailyReport? = allDailyReports?.getOrNull(position)

        if (dailyReport != null) {

            holder.bind(dailyReport)
        }

    }

    override fun getItemCount(): Int {
        return allDailyReports?.size ?: 0
    }

    class DailyReportHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val dailyReportDateText: TextView =
            itemView.findViewById(R.id.daily_report_date_text)
        private val dailyReportTimeText: TextView =
            itemView.findViewById(R.id.daily_report_time_text)
        private val spentValueReportText: TextView =
            itemView.findViewById(R.id.spent_value_report_text)
        private val limitValueReportText: TextView =
            itemView.findViewById(R.id.limit_value_report_text)
        private val isPassedReportText: TextView =
            itemView.findViewById(R.id.is_passed_report_text)

        @SuppressLint("SetTextI18n")
        fun bind(dailyReport: DailyReport) {

            @SuppressLint("SimpleDateFormat")
            val dateFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")

            @SuppressLint("SimpleDateFormat")
            val timeFormat: DateFormat = SimpleDateFormat("HH:mm:ss")


            val date = Date(
                TimeUtils().getTimeInMilliSeconds(
                    dailyReport.year!!,
                    dailyReport.month!!,
                    dailyReport.date!!,
                    dailyReport.hour!!,
                    dailyReport.minute!!
                )
            )

            val drDate = dateFormat.format(date)

            val drTime = timeFormat.format(date)

            dailyReportDateText.text = drDate
            dailyReportTimeText.text = drTime
            val spentValue : Int = dailyReport.valueSpent?: 0
            spentValueReportText.text = "Vlera shpenzuar : $spentValue"
            limitValueReportText.text = "Vlera limit :" + dailyReport.valueLimit.toString()

            var ok = "ok"

            if (!dailyReport.isOk) {
                ok = "Not Ok"
            }

            isPassedReportText.text = ok
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun putDailyReports(dailyReportsList: List<DailyReport>) {
        allDailyReports = dailyReportsList
        notifyDataSetChanged()
    }

    fun getDailyReportByPos(pos: Int): DailyReport? {
        return this.allDailyReports?.get(pos)
    }

}