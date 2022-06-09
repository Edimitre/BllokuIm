package com.edimitre.bllokuim.activity

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.edimitre.bllokuim.R
import com.edimitre.bllokuim.data.dao.SettingsDao
import com.edimitre.bllokuim.data.model.Description
import com.edimitre.bllokuim.data.model.MainUser
import com.edimitre.bllokuim.data.model.MonthlyIncomeType
import com.edimitre.bllokuim.data.model.MyApplicationSettings
import com.edimitre.bllokuim.data.viewModel.DescriptionViewModel
import com.edimitre.bllokuim.data.viewModel.MainUserViewModel
import com.edimitre.bllokuim.data.viewModel.MonthlyIncomeViewModel
import com.edimitre.bllokuim.fragment.AddUserForm
import com.edimitre.bllokuim.fragment.SettingsForm
import com.edimitre.bllokuim.systemservices.SystemService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import kotlin.concurrent.thread

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), AddUserForm.AddUserListener {


    private lateinit var _userViewModel: MainUserViewModel

    private lateinit var _descriptionViewModel: DescriptionViewModel

    private lateinit var _monthlyIncomeViewModel: MonthlyIncomeViewModel


    private var user: MainUser? = null

    private var descriptions: List<Description?>? = null

    private var monthlyIncomeTypes: List<MonthlyIncomeType?>? = null

    @Inject
    lateinit var settingsDao: SettingsDao

    @Inject
    lateinit var systemService: SystemService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        initViewModel()

        setToolBar()

        setListeners()

        initUser()

        initDescriptions()

        initMonthlyIncomeTypes()

        askForWritePermissions()

        thread {
            loadSettings()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.btn_settings -> {
                openSettingsDialog()
            }

        }

        return super.onOptionsItemSelected(item)
    }

    private fun setToolBar() {

        setSupportActionBar(main_toolbar)

        supportActionBar?.title = "BllokuIm"

        main_toolbar.setTitleTextColor(Color.WHITE)
    }

    private fun initViewModel() {

        _userViewModel = ViewModelProvider(this)[MainUserViewModel::class.java]

        _descriptionViewModel = ViewModelProvider(this)[DescriptionViewModel::class.java]

        _monthlyIncomeViewModel = ViewModelProvider(this)[MonthlyIncomeViewModel::class.java]


    }

    private fun setListeners() {

        description_card.setOnClickListener {
            intent = Intent(this, DescriptionActivity::class.java)
            startActivity(intent)
        }

        expenses_card.setOnClickListener {

            if (descriptions!!.isNotEmpty()) {
                intent = Intent(this, ExpenseActivity::class.java)
                startActivity(intent)
            } else {
                val dialog = AlertDialog.Builder(this)

                dialog.setTitle("Ju lutem shtoni pershkrim!")

                dialog.setMessage(
                    "Nuk mund te shtoni dot asnje shpenzim \n" +
                            "pa pasur dicka qe e pershkruan !"
                )

                dialog.setNegativeButton("Mbyll", DialogInterface.OnClickListener { _, i -> })

                dialog.show()
            }

        }

        reminder_card.setOnClickListener {
            intent = Intent(this, ReminderActivity::class.java)
            startActivity(intent)

        }

        profile_card.setOnClickListener {
            if (hasUser()) {
                intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            } else {
                openRegisterDialog()
            }
        }


    }

    private fun initUser() {
        _userViewModel.getUser().observe(this) {
            user = it

        }

    }

    private fun initDescriptions() {
        _descriptionViewModel.descriptionList.observe(this) {
            descriptions = it
        }

    }

    private fun initMonthlyIncomeTypes() {

        _monthlyIncomeViewModel.allMonthlyIncomeTypes.observe(this) {
            monthlyIncomeTypes = it

            if (monthlyIncomeTypes.isNullOrEmpty()) {

                _monthlyIncomeViewModel.insertMonthlyIncomeTypes()
                systemService.restartApp()

            }
        }

    }

    private fun hasUser(): Boolean {


        return user != null
    }

    private fun openRegisterDialog() {

        val registerForm = AddUserForm()
        registerForm.show(supportFragmentManager, "add user profile")

    }

    private fun openSettingsDialog() {
        val settingsForm = SettingsForm()
        settingsForm.show(supportFragmentManager, "settings")
    }

    private fun askForWritePermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            0
        )
    }

    // it gets fired from Add user form
    override fun addUser(mainUser: MainUser) {

        _userViewModel.saveUser(mainUser)

    }

    private fun loadSettings() {

        systemService.createNotificationChannel()

        val myApplicationSettings = settingsDao.getSettings()

        when (myApplicationSettings) {
            null -> {
                saveDefaultSettings()
            }
        }


        when {
            myApplicationSettings!!.dailyReportGeneratorEnabled -> {
                systemService.scheduleDailyReportAlarm()
            }
            myApplicationSettings.workerEnabled -> {
                systemService.startNotificationWorker()
            }
            myApplicationSettings.backDbEnabled -> {
                systemService.startDbBackupWorker()
            }
        }


    }

    private fun saveDefaultSettings() {

        val mySettings = MyApplicationSettings(
            0,
            workerEnabled = false,
            dailyReportGeneratorEnabled = false,
            backDbEnabled = false
        )


        settingsDao.saveSettings(mySettings)

    }
}