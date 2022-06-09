package com.edimitre.bllokuim.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.ViewModelProvider
import com.edimitre.bllokuim.R
import com.edimitre.bllokuim.data.model.Reminder
import com.edimitre.bllokuim.data.utils.TimeUtils
import com.edimitre.bllokuim.data.viewModel.ReminderViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_reminder_form.*
import java.text.DateFormat
import java.text.SimpleDateFormat

@AndroidEntryPoint
class AddReminderForm : BottomSheetDialogFragment() {


    private lateinit var _reminderViewModel: ReminderViewModel

    private var listener: AddReminderListener? = null

    private var year: Int? = null

    private var month: Int? = null

    private var date: Int? = null

    private var hour: Int? = null

    private var minutes: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        return inflater.inflate(R.layout.fragment_add_reminder_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        setListeners()
    }


    private fun loadViewModel() {


        _reminderViewModel = ViewModelProvider(this)[ReminderViewModel::class.java]


    }

    private fun setListeners() {
        btn_open_alarm_date_picker.setOnClickListener {

            openDatePicker()

        }

        btn_add_reminder.setOnClickListener {

            if (inputIsOk()) {

                val timeInMillis = TimeUtils().getTimeInMilliSeconds(year!!, month!!, date!!, hour!!, minutes!!)
                val description = reminder_description_input.text.toString()
                val reminder = Reminder(0, timeInMillis, description, true)
                listener!!.addReminder(reminder)

                dismiss()
            }

        }

        btn_close_reminder_dialog.setOnClickListener {
            dismiss()
        }
    }

    private fun openDatePicker() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val dateDialog = DatePickerDialog(requireContext(), R.style.DialogTheme)
            dateDialog.setOnDateSetListener { _, y, m, d ->
                year = y
                month = m
                date = d
                showSelectedDate(
                    year!!,
                    month!!,
                    date!!,
                    TimeUtils().getCurrentHour(),
                    TimeUtils().getCurrentMinute()
                )
                openTimePicker()
            }
            dateDialog.show()
        }

    }

    private fun openTimePicker() {

        val timePickerDialog = TimePickerDialog(
            context, R.style.DialogTheme, { _, h, m ->
                hour = h
                minutes = m
                showSelectedDate(year!!, month!!, date!!, hour!!, minutes!!)
            },
            TimeUtils().getCurrentHour(), TimeUtils().getCurrentMinute(), true
        )

        timePickerDialog.show()

    }

    private fun inputIsOk(): Boolean {


        val description = reminder_description_input.text.toString()

        return when {

            year == null && month == null && date == null && hour == null -> {
                Toast.makeText(
                    requireContext(),
                    "dita edhe ora nuk mund te jene bosh",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            description.isEmpty() -> {
                Toast.makeText(requireContext(), "vendosni nje pershkrim", Toast.LENGTH_SHORT)
                    .show()
                false
            }
            else -> {
                true
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showSelectedDate(year: Int, month: Int, day: Int, hour: Int, minute: Int) {

        @SuppressLint("SimpleDateFormat")
        val dateFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")

        @SuppressLint("SimpleDateFormat")
        val timeFormat: DateFormat = SimpleDateFormat("HH:mm:ss")


        val rDate =
            dateFormat.format(TimeUtils().getTimeInMilliSeconds(year, month, day, hour, minute))

        val rTime =
            timeFormat.format(TimeUtils().getTimeInMilliSeconds(year, month, day, hour, minute))

        selected_date_text.text = rDate

        selected_time_text.text = rTime
    }

    interface AddReminderListener {
        fun addReminder(reminder: Reminder?)
    }

    override fun onAttach(context: Context) {
        listener = try {
            context as AddReminderListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "shto listener")
        }
        super.onAttach(context)
    }


}
