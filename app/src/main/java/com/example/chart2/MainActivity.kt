package com.example.chart2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.applandeo.materialcalendarview.EventDay
import com.example.chart2.adapters.ActionListener
import com.example.chart2.adapters.ItemRVAdapter
import com.example.chart2.databinding.ActivityMainBinding
import com.example.chart2.fragments.AddItemFragment
import com.example.chart2.utils.HelperMethods.Companion.convertMillisToDate
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var selectedDateMillis: Long = Calendar.getInstance().timeInMillis
    private val viewModel: ViewModel by viewModels()
    private val adapter by lazy { ItemRVAdapter(object: ActionListener {
        override fun setState(id: Int, state: Boolean) {
            viewModel.updateState(id, state)
//            updateUI()
        }

    }) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val events = mutableListOf<EventDay>()
        val calendar = Calendar.getInstance()
        val todayDay = convertMillisToDate(calendar.timeInMillis).split('/')[0].toInt()
        val lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val calendar1 = Calendar.getInstance()
//        calendar1.add(Calendar.DAY_OF_MONTH, -todayDay+1)
        calendar1.add(Calendar.DAY_OF_MONTH, lastDay - todayDay)
//        events.add(EventDay(calendar1, R.drawable.ic_cancel))
//        events.add(EventDay(calendar, R.drawable.ic_ok))
        binding.calendarView.setEvents(events)
        binding.floatingActionButton.setOnClickListener{
            val bottomSheet = AddItemFragment(selectedDateMillis/1000, convertMillisToDate(selectedDateMillis))
            if (!bottomSheet.isAdded) {
                bottomSheet.show(supportFragmentManager, "")
            }
        }
        binding.calendarView.setOnDayClickListener{ eventDay ->
            selectedDateMillis = eventDay.calendar.timeInMillis
            Log.d("calendar", selectedDateMillis.toString())
            updateUI()
        }
        binding.rv.adapter = adapter
        updateUI()

//        binding.btnShowMoneyCountChart.setOnClickListener {
//            startActivity(Intent(this, GraphicsCountActivity::class.java))
//        }
//
//        binding.btnShowMoneyTypeChart.setOnClickListener {
//            startActivity(Intent(this, GraphicsTypeActivity::class.java))
//        }
    }

    fun updateUI(){
        Log.d("updateUI", selectedDateMillis.toString())
        viewModel.getDataForRV(selectedDateMillis/1000)
        Log.d("updateUI", "UI Updating")
        viewModel.dataLiveData3.observe(this){ list ->
//            Log.d("updateUI", list.toString())
            list.let {
                Log.d("Observer", "submitting list ${it.toString()}")
                adapter.submitList(it) }
        }
    }
}