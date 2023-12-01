package com.example.quetzalli.ui.views

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.quetzalli.R
import com.example.quetzalli.databinding.FragmentPnotificacionesBinding
import com.example.quetzalli.receivers.ReminderBroadcast
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import android.provider.Settings
import android.widget.TextView
import java.util.Locale

class PnotificacionesFragment : Fragment() {

    private lateinit var binding: FragmentPnotificacionesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPnotificacionesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        registerEvents()
    }

    private fun init() {
        // Date Picker
        binding.tilDateProgram.setEndIconOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build()
            datePicker.show(childFragmentManager, "date_picker")
            datePicker.addOnPositiveButtonClickListener {
                binding.etDateProgram.setText(datePicker.headerText)
            }
        }

        // Time Picker
        binding.tilTimeProgram.setEndIconOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Select Appointment time")
                .build()
            timePicker.show(childFragmentManager, "time_picker")
            timePicker.addOnPositiveButtonClickListener {
                // Format the time to show
                val hour = if (timePicker.hour < 10) "0${timePicker.hour}" else "${timePicker.hour}"
                val minute = if (timePicker.minute < 10) "0${timePicker.minute}" else "${timePicker.minute}"
                binding.etTimeProgram.setText(getString(R.string.selected_time, hour, minute))
            }
        }
    }

    private fun getMillisFromDateTime(date: String, time: String): Long {
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val dateTime = format.parse("$date $time")
        return dateTime?.time ?: 0
    }

    private fun showSnackbarWithIcon(message: String, iconResId: Int) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
        val snackbarLayout = snackbar.view
        val textView = snackbarLayout.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0)
        textView.compoundDrawablePadding = resources.getDimensionPixelOffset(R.dimen.snackbar_icon_padding)
        snackbar.show()
    }

    private fun registerEvents() {
        binding.btnProgram.setOnClickListener {

        }
    }

}
