package com.edimitre.bllokuim.activity

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.edimitre.bllokuim.R
import com.edimitre.bllokuim.adapter.ReminderAdapter
import com.edimitre.bllokuim.data.model.Reminder
import com.edimitre.bllokuim.data.viewModel.ReminderViewModel
import com.edimitre.bllokuim.fragment.AddReminderForm
import com.edimitre.bllokuim.systemservices.SystemService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_reminder.*
import javax.inject.Inject


@AndroidEntryPoint
class ReminderActivity : AppCompatActivity(), AddReminderForm.AddReminderListener {

    private lateinit var adapter: ReminderAdapter

    private lateinit var _reminderViewModel: ReminderViewModel

    @Inject
    lateinit var systemService: SystemService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        initViewModel()

        setToolbar()

        initRecyclerView()

        showAllReminders()

        setListeners()
    }

    private fun initViewModel() {
        _reminderViewModel = ViewModelProvider(this)[ReminderViewModel::class.java]
    }

    private fun setToolbar() {

        setSupportActionBar(reminder_toolbar)

        supportActionBar?.title = "Kujtesat"

        reminder_toolbar.setTitleTextColor(Color.WHITE)


    }

    private fun initRecyclerView() {
        adapter = ReminderAdapter()

        reminder_recycler_view.setHasFixedSize(true)
        reminder_recycler_view.layoutManager = LinearLayoutManager(this)
        reminder_recycler_view.adapter = adapter
    }

    private fun setListeners() {

        btn_open_reminder_dialog.setOnClickListener {
            val reminderFragment = AddReminderForm()
            reminderFragment.show(supportFragmentManager, "add reminder")
        }
    }

    private fun showAllReminders() {

        _reminderViewModel.reminderList.observe(this) {
            adapter.putReminders(it as List<Reminder>)
        }


    }

    private fun isReminderValid(reminder: Reminder): Boolean {
        return reminder.alarmTimeInMillis >= System.currentTimeMillis()
    }


    // comes from reminder form fragment
    override fun addReminder(reminder: Reminder?) {
        if (reminder != null && isReminderValid(reminder)) {

            _reminderViewModel.saveReminder(reminder)

            systemService.setAlarm(reminder.alarmTimeInMillis)

        }
    }

}