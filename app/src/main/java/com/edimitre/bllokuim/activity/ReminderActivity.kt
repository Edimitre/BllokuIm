package com.edimitre.bllokuim.activity

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edimitre.bllokuim.R
import com.edimitre.bllokuim.adapter.ReminderAdapter
import com.edimitre.bllokuim.data.model.Reminder
import com.edimitre.bllokuim.data.viewModel.ReminderViewModel
import com.edimitre.bllokuim.fragment.AddReminderForm
import com.edimitre.bllokuim.systemservices.SystemService
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_reminder.*
import javax.inject.Inject


@AndroidEntryPoint
class ReminderActivity : AppCompatActivity(), AddReminderForm.AddReminderListener {

    private lateinit var adapter: ReminderAdapter

    private lateinit var _reminderViewModel: ReminderViewModel

    private lateinit var itemTouchHelper: ItemTouchHelper

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

        enableTouchFunctions()


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
                    val reminder: Reminder? =
                        adapter.getReminderByPos(viewHolder.adapterPosition)

                    if (reminder != null) {

                        val alertDialog = MaterialAlertDialogBuilder(
                            this@ReminderActivity,
                            R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background
                        )

                        alertDialog.setTitle("Kujtesa: ${reminder.description} ")

                        alertDialog.setMessage(
                            "Deshironi Ta Fshini ?\n" +
                                    "KUJDES! ..ky veperim nuk mund te rikthehet!"
                        )
                        alertDialog.setPositiveButton("Fshij") { _, _ ->

                            deleteReminder(reminder)


                            Toast.makeText(
                                applicationContext,
                                "kujtesa ${reminder.description} u fshi",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                        alertDialog.setNegativeButton("mbyll") { _, _ ->


                        }

                        alertDialog.setOnDismissListener {
                            showAllReminders()
                        }

                        alertDialog.show()

                    }


                }
            })

        itemTouchHelper.attachToRecyclerView(reminder_recycler_view)
    }

    private fun deleteReminder(reminder: Reminder) {

        systemService.cancelAllAlarms()
        _reminderViewModel.deleteReminder(reminder)
        _reminderViewModel.firstReminder.observe(this) {

            val nextReminder = it

            if (nextReminder != null) {

                systemService.setAlarm(nextReminder.alarmTimeInMillis)

            } else {
                Toast.makeText(this, "nuk ka me kujtesa te mbetura", Toast.LENGTH_SHORT).show()
            }
        }

    }

}