package com.edimitre.bllokuim.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edimitre.bllokuim.R
import com.edimitre.bllokuim.adapter.DailyReportsAdapter
import com.edimitre.bllokuim.adapter.DescriptionAdapter
import com.edimitre.bllokuim.data.model.DailyReport
import com.edimitre.bllokuim.data.model.Description
import com.edimitre.bllokuim.data.viewModel.DailyReportViewModel
import com.edimitre.bllokuim.data.viewModel.DescriptionViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_daily_report.*
import kotlinx.android.synthetic.main.activity_description.*

@AndroidEntryPoint
class DailyReportActivity : AppCompatActivity() {

    private lateinit var adapter: DailyReportsAdapter

    private lateinit var _dailyReportViewModel: DailyReportViewModel

    private lateinit var itemTouchHelper: ItemTouchHelper


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

        enableTouchFunctions()

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
                    val dailyReport:DailyReport? =
                        adapter.getDailyReportByPos(viewHolder.adapterPosition)

                    if (dailyReport != null) {

                        val alertDialog = MaterialAlertDialogBuilder(
                            this@DailyReportActivity,
                            R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background
                        )

                        alertDialog.setTitle("Raporti dates : ${dailyReport.year} : ${dailyReport.month} : ${dailyReport.date} " )

                        alertDialog.setMessage(
                            "Deshironi Ta Fshini ?\n" +
                                    "KUJDES! ..ky veperim nuk mund te rikthehet!"
                        )
                        alertDialog.setPositiveButton("Fshij") { _, _ ->

                            _dailyReportViewModel.deleteDailyReport(dailyReport)


                            Toast.makeText(
                                applicationContext,
                                "Raporti dates : ${dailyReport.year} : ${dailyReport.month} : ${dailyReport.date}  u fshi",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                        alertDialog.setNegativeButton("mbyll") { _, _ ->


                        }

                        alertDialog.setOnDismissListener {
                            showDailyReports()
                        }

                        alertDialog.show()

                    }


                }
            })

        itemTouchHelper.attachToRecyclerView(daily_report_recycler_view)
    }

}