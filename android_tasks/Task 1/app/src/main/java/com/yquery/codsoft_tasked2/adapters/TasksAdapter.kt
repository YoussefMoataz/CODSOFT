package com.yquery.codsoft_tasked2.adapters

import android.content.Context
import android.graphics.Paint
import android.text.format.DateFormat.format
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textview.MaterialTextView
import com.yquery.codsoft_tasked2.R
import com.yquery.codsoft_tasked2.models.Task
import net.igenius.customcheckbox.CustomCheckBox
import java.util.Calendar

class TasksAdapter(context: Context) : RecyclerView.Adapter<TasksAdapter.MyViewHolder>() {

    private val ctx = context
    private var tasksList = emptyList<Task>()
    private lateinit var tListener: TaskClickListener
    private lateinit var completedListener: CompletedClickListener

    inner class MyViewHolder(
        itemView: View,
        tListener: TaskClickListener,
        completeListener: CompletedClickListener
    ) : RecyclerView.ViewHolder(itemView) {

        var taskTextview: MaterialTextView = itemView.findViewById(R.id.taskText)
        var checkboxCompleted: CustomCheckBox = itemView.findViewById(R.id.checkbox_completed)
        var taskDateTimeButton: MaterialButton = itemView.findViewById(R.id.task_date_time_button)

        init {
            itemView.setOnClickListener {
                tListener.onTaskClicked(adapterPosition)
            }
            checkboxCompleted.setOnClickListener {
                var isChecked = tasksList[adapterPosition].taskCompleted
                isChecked = !isChecked!!
                completeListener.onCompletedClicked(adapterPosition, isChecked)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.listitem_task, parent, false),
            tListener,
            completedListener
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = tasksList[position]
        holder.taskTextview.text = currentItem.taskTitle
        holder.checkboxCompleted.isChecked = currentItem.taskCompleted!!

        if (currentItem.taskDate!!.isNotBlank()) {

            var dateTimeText = currentItem.taskDate!!

            if (currentItem.taskTime!!.isNotBlank()) {
                dateTimeText += ", ${currentItem.taskTime!!}"

                val calendar = Calendar.getInstance()
                calendar.set(
                    currentItem.taskDate!!.split("/")[2].toInt(),
                    currentItem.taskDate!!.split("/")[1].toInt() - 1, // subtract 1 from months
                    currentItem.taskDate!!.split("/")[0].toInt(),
                    currentItem.taskTime!!.split(":")[0].toInt(),
                    currentItem.taskTime!!.split(":")[1].toInt(),
                    0
                )
                val taskTime = calendar.timeInMillis
                val currentTime = Calendar.getInstance().timeInMillis
//            holder.taskDateTimeButton.text = taskTime.toString()

                if ((currentItem.taskDate!! < format(
                        "dd/MM/yyyy",
                        MaterialDatePicker.todayInUtcMilliseconds()
                    ).toString()) or ((currentItem.taskDate!! == format(
                        "dd/MM/yyyy",
                        MaterialDatePicker.todayInUtcMilliseconds()
                    ).toString()) and (taskTime <= currentTime))
                ) {
                    holder.taskDateTimeButton.setTextColor(ctx.resources.getColor(R.color.redColor))
                } else if (((currentItem.taskDate!! == format(
                        "dd/MM/yyyy",
                        MaterialDatePicker.todayInUtcMilliseconds()
                    ).toString()) and (taskTime > currentTime))
                ) {
                    holder.taskDateTimeButton.setTextColor(ctx.resources.getColor(R.color.blueColor))
                } else {
                    holder.taskDateTimeButton.setTextColor(ctx.resources.getColor(R.color.textColor))
                }

            } else {
                if (currentItem.taskDate!! < format(
                        "dd/MM/yyyy",
                        MaterialDatePicker.todayInUtcMilliseconds()
                    ).toString()
                ) {
                    holder.taskDateTimeButton.setTextColor(ctx.resources.getColor(R.color.redColor))

                } else if (currentItem.taskDate!! == format(
                        "dd/MM/yyyy",
                        MaterialDatePicker.todayInUtcMilliseconds()
                    ).toString()
                ) {
                    holder.taskDateTimeButton.setTextColor(ctx.resources.getColor(R.color.blueColor))
                } else {
                    holder.taskDateTimeButton.setTextColor(ctx.resources.getColor(R.color.textColor))
                }
            }

            holder.taskDateTimeButton.visibility = View.VISIBLE
            holder.taskDateTimeButton.text = dateTimeText

        } else {
            holder.taskDateTimeButton.visibility = View.GONE
        }

        if (currentItem.taskCompleted!!) {
            holder.taskTextview.paintFlags =
                Paint.STRIKE_THRU_TEXT_FLAG or holder.taskTextview.paintFlags
        } else {
            holder.taskTextview.paintFlags = 0
        }

    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    fun setData(tasks: MutableList<Task>) {
        this.tasksList = tasks
        notifyDataSetChanged()
    }

    interface TaskClickListener {
        fun onTaskClicked(position: Int)
    }

    fun setOnTaskClickListener(listener: TaskClickListener) {
        tListener = listener
    }

    interface CompletedClickListener {
        fun onCompletedClicked(position: Int, completed: Boolean)
    }

    fun setCompletedClickListener(listener: CompletedClickListener) {
        completedListener = listener
    }
}