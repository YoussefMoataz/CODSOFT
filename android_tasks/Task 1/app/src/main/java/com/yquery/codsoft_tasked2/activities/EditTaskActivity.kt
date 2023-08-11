package com.yquery.codsoft_tasked2.activities

import android.graphics.Paint
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.yquery.codsoft_tasked2.R
import com.yquery.codsoft_tasked2.databinding.ActivityEditTaskBinding
import com.yquery.codsoft_tasked2.models.Task
import com.yquery.codsoft_tasked2.viewmodels.TasksViewModel
import kotlinx.coroutines.runBlocking
import java.util.Calendar

class EditTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditTaskBinding

    private var id = 0
    private var title = ""
    private var details = ""
    private var category = ""
    private var date = ""
    private var time = ""
    private var completed = false
    private var workName = ""

    private var dateSelected = false
    private var timeSelected = false

    private val tasksViewModel: TasksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.activityEditToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.action == "Edit") {

            showSavedData()

            val editTitleInput = binding.editTaskTitleInput
            val editDateButton = binding.editDateButton
            val editTimeButton = binding.editTimeButton
            val editSaveButton = binding.editSaveButton

            editDateButton.setOnClickListener {

                if (date.isBlank()) {
                    date =
                        DateFormat.format("dd/MM/yyyy", MaterialDatePicker.todayInUtcMilliseconds())
                            .toString()
                }

                val dateList: List<String> = date.split("/")

                val datePicker =
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setSelection(
                            getTimeInMillis(
                                dateList[0].toInt(),
                                dateList[1].toInt() - 1,
                                dateList[2].toInt()
                            )
                        )
                        .build()

                datePicker.show(supportFragmentManager, "main")

                datePicker.addOnPositiveButtonClickListener {
                    date =
                        DateFormat.format("dd/MM/yyyy", datePicker.selection!!.toLong()).toString()
                    editDateButton.text = date
                    dateSelected = true
                }
            }

            editTimeButton.setOnClickListener {

                if (time.isBlank()) {
                    val calendar = Calendar.getInstance()
                    time = "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"
                }

                val timeList: List<String> = time.split(":")

                val picker =
                    MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(timeList[0].toInt())
                        .setMinute(timeList[1].toInt())
                        .build()

                picker.show(supportFragmentManager, "main")

                picker.addOnPositiveButtonClickListener {
                    time = "${picker.hour}:${picker.minute}"
                    editTimeButton.text = time

                    if (!dateSelected) {
                        date = DateFormat.format(
                            "dd/MM/yyyy",
                            MaterialDatePicker.todayInUtcMilliseconds()
                        ).toString()
                        editDateButton.text = date

                    }
                    timeSelected = true

                }

            }

            editDateButton.setOnLongClickListener(View.OnLongClickListener {

                if (date.isNotBlank()) {
                    date = ""
                    time = ""
                    binding.editDateButton.text = "Date"
                    binding.editTimeButton.text = "Time"
                    dateSelected = false
                    timeSelected = false
                }

                return@OnLongClickListener true
            })

            editTimeButton.setOnLongClickListener(View.OnLongClickListener {

                if (time.isNotBlank()) {
                    time = ""
                    binding.editTimeButton.text = "Time"
                    timeSelected = false
                }

                return@OnLongClickListener true
            })

            editSaveButton.setOnClickListener {
                if (editTitleInput.text!!.isNotBlank()) {
                    tasksViewModel.updateTask(
                        Task(
                            id,
                            editTitleInput.text.toString(),
                            binding.editTaskDetailsInput.text.toString(),
                            date,
                            time,
                            completed,
                        )
                    )

                    finish()

                } else {
                    findViewById<TextInputLayout>(R.id.edit_task_title_input_layout)
                        .error = "Title can't be empty"
                }
            }
        }
    }


    override fun onPause() {
        super.onPause()
        if (binding.editTaskTitleInput.text!!.isNotBlank()) {

            tasksViewModel.updateTask(
                Task(
                    id,
                    binding.editTaskTitleInput.text.toString(),
                    binding.editTaskDetailsInput.text.toString(),
                    date,
                    time,
                    completed,
                )
            )

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.edit_task_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.delete_task -> {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Delete Task")
                    .setMessage("Are you sure you want to Delete this task ?")
                    .setPositiveButton("Yes") { dialog, which ->

                        tasksViewModel.deleteTaskByID(id)

                        Log.d("TAG", "onCreate: okaay")

                        finish()
                    }
                    .setNegativeButton("Cancel") { dialog, which ->
                        dialog.dismiss()
                    }.show()

                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showSavedData() {

        id = intent.getIntExtra("id", 0)
        runBlocking {
            val task: Task = tasksViewModel.getTask(id)

            title = task.taskTitle.toString()
            details = task.taskDetails.toString()
            date = task.taskDate.toString()
            time = task.taskTime.toString()
            completed = task.taskCompleted!!
        }


        binding.editTaskTitleInput.setText(title)
        binding.editTaskDetailsInput.setText(details)

        if (date.isNotBlank()) {
            binding.editDateButton.text = date
            dateSelected = true
        }

        if (time.isNotBlank()) {
            binding.editTimeButton.text = time
            timeSelected = true
        }

        if (completed) {
            binding.editCompletedButton.text = "Set as Not Completed"
            binding.editTaskTitleInput.paintFlags =
                Paint.STRIKE_THRU_TEXT_FLAG or binding.editTaskTitleInput.paintFlags
        } else {
            binding.editCompletedButton.text = "Set as Completed"
        }

        binding.editCompletedButton.setOnClickListener {
            completed = !completed
            if (completed) {
                binding.editCompletedButton.text = "Set as Not Completed"
                binding.editTaskTitleInput.paintFlags =
                    Paint.STRIKE_THRU_TEXT_FLAG or binding.editTaskTitleInput.paintFlags
            } else {
                binding.editCompletedButton.text = "Set as Completed"
                binding.editTaskTitleInput.paintFlags = 0
            }
        }

    }

    private fun getTimeInMillis(day: Int, month: Int, year: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return calendar.timeInMillis
    }

}