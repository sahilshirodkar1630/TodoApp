package com.example.todoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.activity_task.view.*
import kotlinx.android.synthetic.main.item_todo.view.*
import java.text.SimpleDateFormat
import java.util.*

class TodoAdapter (val list: List<TodoModel>):RecyclerView.Adapter<TodoAdapter.TodoViewFolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewFolder {
        return TodoViewFolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo,parent,false)
        )

    }

    override fun getItemId(position: Int): Long {
        return list[position].id
    }
    override fun onBindViewHolder(holder: TodoViewFolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size;

    class TodoViewFolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(todoModel: TodoModel) {
            with(itemView){
                val colors = resources.getIntArray(R.array.random_color)
                val randomColor = colors[Random().nextInt(colors.size)]
                viewColorTag.setBackgroundColor(randomColor)
                txtShowTitle.text = todoModel.title;
                txtShowTask.text = todoModel.description;
                txtShowCategory.text = todoModel.catergory
                updateTime(todoModel.time)
                updateDate(todoModel.date)

            }

        }

        private fun updateTime(time: Long) {
            val myformat = "h:mm a"
            val sdf = SimpleDateFormat(myformat)
            itemView.txtShowTime.setText(sdf.format(Date(time)))
        }

        private fun updateDate(date:Long) {
            val myformat = "EEE, d MMM yyyy"
            val sdf = SimpleDateFormat(myformat)
            itemView.txtShowDate.setText(sdf.format(Date(date)))

        }

    }
}