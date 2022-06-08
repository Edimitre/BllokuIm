package com.edimitre.bllokuim.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.edimitre.bllokuim.R
import com.edimitre.bllokuim.data.dao.MonthlyIncomeDao
import com.edimitre.bllokuim.data.dao.SettingsDao
import com.edimitre.bllokuim.data.model.MyApplicationSettings
import com.edimitre.bllokuim.data.utils.TimeUtils
import com.edimitre.bllokuim.systemservices.SystemService
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_settings_form.*
import javax.inject.Inject


@AndroidEntryPoint
class SettingsForm : BottomSheetDialogFragment() {

    @Inject
    lateinit var settingsDao: SettingsDao

    @Inject
    lateinit var monthlyIncomeDao: MonthlyIncomeDao

    @Inject
    lateinit var systemService: SystemService

    private var mySettings: MyApplicationSettings? = null

    var hasIncomes = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setExistingValues()

        setListeners()
    }

    private fun setListeners() {
        btn_save_settings.setOnClickListener{
            saveSettings()
            dismiss()
        }

        btn_close_settings.setOnClickListener{
            dismiss()
        }
    }

    private fun setExistingValues() {

        val thread = Thread {
            mySettings = settingsDao.getSettings()

            if (mySettings != null && mySettings!!.workerEnabled) {
                notify_setting_switch.isChecked = true
            }

            if (mySettings != null && mySettings!!.dailyReportGeneratorEnabled) {
                daily_report_switch.isChecked = true
            }

            if (mySettings != null && mySettings!!.backDbEnabled) {
                db_backup_switch.isChecked = true
            }

        }

        thread.start()

    }

    private fun saveSettings() {

        if (notify_setting_switch.isChecked) {
            mySettings!!.workerEnabled = true

        }

        if (daily_report_switch.isChecked) {

            if (!hasMonthlyIncomes()) {
                Toast.makeText(
                    activity, "raportet nuk mund te gjenerohen \n" +
                            "sepse ky muaj ska te ardhura", Toast.LENGTH_SHORT
                ).show()
            } else {
                mySettings!!.dailyReportGeneratorEnabled = true
            }

        }


        if (db_backup_switch.isChecked){
            // todo check for permission and request if not given
            if (!systemService.permissionGranted()){
                Toast.makeText(activity, "ju lutem lejoni aplikacionin te aksesoje storage", Toast.LENGTH_SHORT).show()
            }else{
                mySettings!!.backDbEnabled = true
            }
        }

        val thread = Thread{
            settingsDao.saveSettings(mySettings!!)
        }

        thread.start()
        thread.join()

        Toast.makeText(activity, "preferencat u ruajten", Toast.LENGTH_SHORT).show()

    }

    private fun hasMonthlyIncomes(): Boolean {
        val thread = Thread {

            val value = monthlyIncomeDao.getValueOfIncomesByYearMonthOnThread(
                TimeUtils().getCurrentYear(),
                TimeUtils().getCurrentMonth()
            )

            if (value != null && value > 100) {

                hasIncomes = true
            }
        }
        thread.start()
        thread.join()

        return hasIncomes
    }

}