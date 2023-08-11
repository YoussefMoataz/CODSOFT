package com.yquery.codsoft_tasked2.activities

import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.yquery.codsoft_tasked2.R
import com.yquery.codsoft_tasked2.adapters.TasksAdapter
import com.yquery.codsoft_tasked2.databinding.ActivityMainBinding
import com.yquery.codsoft_tasked2.models.Task
import com.yquery.codsoft_tasked2.viewmodels.TasksViewModel
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TasksAdapter

    private lateinit var tasksList: MutableList<Task>

    private lateinit var vmodel: TasksViewModel

    private var timeSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_Tasked2)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.activityMainToolbar)

        recyclerView = binding.recyclerviewTasks
        adapter = TasksAdapter(this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        vmodel = ViewModelProvider(this).get(TasksViewModel::class.java)

        showAllTasks()

        val itemTouchHelperCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val deletedTask = tasksList[viewHolder.adapterPosition]

                    vmodel.deleteTaskByID(deletedTask.taskID)

                    Snackbar.make(binding.root, "Task Deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo") {

                            vmodel.insertTask(deletedTask)

                        }.setAnchorView(binding.fabAddTask).show()
                }

            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        adapter.setOnTaskClickListener(object : TasksAdapter.TaskClickListener {
            override fun onTaskClicked(position: Int) {

                val currentItem = tasksList[position]

                val editTaskIntent = Intent(this@MainActivity, EditTaskActivity::class.java).apply {

                    putExtra("id", currentItem.taskID)

                    action = "Edit"
                }
                startActivity(editTaskIntent)

            }
        })

        adapter.setCompletedClickListener(object : TasksAdapter.CompletedClickListener {
            override fun onCompletedClicked(position: Int, completed: Boolean) {
                val currentItem = tasksList[position]

                currentItem.taskCompleted = completed
                vmodel.updateTask(currentItem)
                adapter.notifyItemChanged(position)

            }
        })

        binding.fabAddTask.setOnClickListener {
            val bottomSheet = BottomSheetDialog(this)

            bottomSheet.setContentView(R.layout.add_task_bottomsheet)

            val calendar = Calendar.getInstance()

            var date = ""
            var time = ""
            var dateSelected = false

            val titleInput = bottomSheet.findViewById<TextInputEditText>(R.id.task_title_input)
            val detailsInput = bottomSheet.findViewById<TextInputEditText>(R.id.task_details_input)
            val dateButton = bottomSheet.findViewById<MaterialButton>(R.id.date_button)
            val timeButton = bottomSheet.findViewById<MaterialButton>(R.id.time_button)
            val saveButton = bottomSheet.findViewById<MaterialButton>(R.id.save_button)

            dateButton?.setOnClickListener {
                val datePicker =
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build()

                datePicker.show(supportFragmentManager, "main")

                datePicker.addOnPositiveButtonClickListener {

                    date =
                        DateFormat.format("dd/MM/yyyy", datePicker.selection!!.toLong()).toString()
                    dateSelected = true
                    dateButton.text = date
                }
            }

            timeButton?.setOnClickListener {
                val picker =
                    MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                        .setMinute(calendar.get(Calendar.MINUTE))
                        .build()

                picker.show(supportFragmentManager, "main")

                picker.addOnPositiveButtonClickListener {
                    time = "${picker.hour}:${picker.minute}"
                    timeButton.text = time

                    if (!dateSelected) {
                        date = DateFormat.format(
                            "dd/MM/yyyy",
                            MaterialDatePicker.todayInUtcMilliseconds()
                        ).toString()
                        dateButton?.text = date
                    }
                    timeSelected = true
                }

            }

            saveButton?.setOnClickListener {
                if (titleInput?.text!!.isNotBlank()) {

                    val workName = Calendar.getInstance().timeInMillis.toString()

                    vmodel.insertTask(
                        Task(
                            0,
                            titleInput.text.toString(),
                            detailsInput?.text.toString(),
                            date,
                            time,
                            false,
                        )
                    )

                    bottomSheet.dismiss()

                } else {
                    bottomSheet.findViewById<TextInputLayout>(R.id.task_title_input_layout)
                        ?.error = "Title can't be empty"
                }
            }

            bottomSheet.setCanceledOnTouchOutside(false)
            bottomSheet.show()

        }

    }

    private fun showAllTasks() {
        vmodel.getAllTasks.observe(this) {
            adapter.setData(it)
            tasksList = it
        }
    }

}