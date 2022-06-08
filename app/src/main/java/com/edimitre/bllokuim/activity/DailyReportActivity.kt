package com.edimitre.bllokuim.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.edimitre.bllokuim.R
import com.edimitre.bllokuim.adapter.DailyReportsAdapter
import com.edimitre.bllokuim.adapter.DescriptionAdapter
import com.edimitre.bllokuim.data.model.DailyReport
import com.edimitre.bllokuim.data.viewModel.DailyReportViewModel
import com.edimitre.bllokuim.data.viewModel.DescriptionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_daily_report.*
import kotlinx.android.synthetic.main.activity_description.*

@AndroidEntryPoint
class DailyReportActivity : AppCompatActivity() {

    private lateinit var adapter: DailyReportsAdapter
    private lateinit var _dailyReportViewModel: DailyReportViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_report)

        initViewModel()

        setToolbar()

        initRecyclerView()

        showDailyReports()
    }


    private fun setToolbar() {

        setSupportActionBar(daily_report_toolbar)

        supportActionBar?.title = "Raportet Ditore"

        daily_report_toolbar.setTitleTextColor(Color.WHITE)


    }
    private fun initViewModel(){
        _dailyReportViewModel = ViewModelProvider(this)[DailyReportViewModel::class.java]


    }

    private fun initRecyclerView(){
        adapter = DailyReportsAdapter()

        daily_report_recycler_view.setHasFixedSize(true)
        daily_report_recycler_view.layoutManager = LinearLayoutManager(this)
        daily_report_recycler_view.adapter = adapter
    }

    private fun showDailyReports() {


        _dailyReportViewModel.allDailyReports.observe(this){

            adapter.putDailyReports(it as List<DailyReport>)
        }


    }

}