package com.example.todoapp


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

const val DB_NAME = "todo.db"
class TaskActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var myCalender:Calendar

    lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener

    var finalDate = 0L
    var finalTime = 0L


    private val labels = arrayListOf("Personal","Shopping","Business","Banking","Insurance")

    val db by lazy {
        AppDatabase.getDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        dateEdt.setOnClickListener(this)
        timeEdt.setOnClickListener(this)
        saveBtn.setOnClickListener(this)



        setUpSpinner()
    }

    private fun setUpSpinner() {
        val adapter =
                ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,labels)

        labels.sort()
        spinnerCategory.adapter = adapter

    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.dateEdt -> {
                setListener()
            }
            R.id.timeEdt -> {
                setTimeListener()
            }
            R.id.saveBtn -> {
                saveTodo()
            }
        }
    }

    private fun saveTodo() {
        val category = spinnerCategory.selectedItem.toString();
        val title = titleInpLay.editText?.text.toString();
        val description = taskInpLay.editText?.text.toString()

        GlobalScope.launch (Dispatchers.Main){

            val id = withContext(Dispatchers.IO){
                return@withContext db.todoDao().insertTask(
                        TodoModel(
                                title,
                                description,
                                category,
                                finalDate,
                                finalTime,
                        )
                )
            }
            finish()
            myCalender.timeInMillis

        }
    }

    private fun setTimeListener() {
        myCalender = Calendar.getInstance()

        timeSetListener = TimePickerDialog.OnTimeSetListener{ _: TimePicker, hourOfDay: Int, min: Int ->
            myCalender.set(Calendar.HOUR_OF_DAY,hourOfDay)
            myCalender.set(Calendar.MINUTE,min)
            updateTime()
        }

        val timePickerDialog = TimePickerDialog (
            this,
            timeSetListener,
            myCalender.get(Calendar.HOUR_OF_DAY),
            myCalender.get(Calendar.MINUTE),
            false
        )

        timePickerDialog.show()
    }

    private fun setListener() {
        myCalender = Calendar.getInstance()

        dateSetListener = DatePickerDialog.OnDateSetListener{ _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            myCalender.set(Calendar.YEAR,year)
            myCalender.set(Calendar.MONTH,month)
            myCalender.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            updateDate()
        }

        val datePickerDialog = DatePickerDialog(
            this,
            dateSetListener,
            myCalender.get(Calendar.YEAR),
            myCalender.get(Calendar.MONTH),
            myCalender.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun updateTime() {
        val myformat = "h:mm a"
        val sdf = SimpleDateFormat(myformat)
        finalTime = myCalender.time.time
        timeEdt.setText(sdf.format(myCalender.time))

    }

    private fun updateDate() {
        val myformat = "EEE, d MMM yyyy"
        val sdf = SimpleDateFormat(myformat)

        finalDate  = myCalender.time.time
        dateEdt.setText(sdf.format(myCalender.time))

        timeInptLay.visibility = View.VISIBLE

    }
}