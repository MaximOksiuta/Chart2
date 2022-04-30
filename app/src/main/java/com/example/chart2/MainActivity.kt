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
import com.example.chart2.models.StateOfDate
import com.example.chart2.utils.HelperMethods.Companion.convertMillisToDate
import com.example.chart2.utils.HelperMethods.Companion.convertMillisToDateMills
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.properties.Delegates

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var selectedDateMillis: Long = Calendar.getInstance().timeInMillis
    private val viewModel: ViewModel by viewModels()
    private val adapter by lazy { ItemRVAdapter(object: ActionListener {
        override fun setState(id: Int, state: Boolean) {
            viewModel.updateState(id, state)
            updateUI()
        }

    }) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //        val calendar1 = Calendar.getInstance()
//        calendar1.add(Calendar.DAY_OF_MONTH, -todayDay+1)
//        calendar1.add(Calendar.DAY_OF_MONTH, lastDay - todayDay)
        //calendar1.set(2022, 5, 1)
////        calendar1.add(Calendar.MONTH)
//        calendar1.add(Calendar.DAY_OF_MONTH, 10)
//        Log.d("Calendar", lastDay.toString())
//        events.add(EventDay(calendar1, R.drawable.ic_baseline_close_24))
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
        binding.calendarView.setOnForwardPageChangeListener {
//            val calendar2 = Calendar.getInstance()
////            calendar2.add(Calendar.DAY_OF_MONTH, -todayDay+1)
//            calendar2.add(Calendar.DAY_OF_MONTH, lastDayOfThisMonth - todayDay)
//            Log.d("Calendar", calendar2.toString())
        }
        binding.rv.adapter = adapter
        viewModel.getStatesWithDate()
        updateUI()
        viewModel.dataLiveData4.observe(this){list->
            var datesWithState: MutableList<StateOfDate> = mutableListOf()
            Log.d("calculateState","list $list")
            for (item in list){
                var id = -1


                for (it in 0 until datesWithState.size) {
                    if (datesWithState[it].date == item.date){
                        id = it
                    }
                }
                Log.d("calculateState", "datesWithState $datesWithState item $item id $id")
                if (id>=0){
                    if ((datesWithState[id].state == 0 && item.state) || (datesWithState[id].state == 2 && !item.state)){
                        datesWithState[id].state = 1
                        Log.d("calculateState", "set state to 1")
                    }
                } else{
                    if (item.state){
                        Log.d("calculateState", "set state to 2")
                        datesWithState.add(StateOfDate(item.date, 2))
                    } else{
                        Log.d("calculateState", "set state to 0")
                        datesWithState.add(StateOfDate(item.date, 0))
                    }
                }
            }
            val events = mutableListOf<EventDay>()
            datesWithState.forEach {
                val days = getDifference(convertMillisToDateMills(System.currentTimeMillis()), it.date*1000)
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DAY_OF_MONTH, days)
                Log.d("iconDebug", it.toString())
                val icon = when (it.state){
                    0 -> {
                        Log.d("iconDebug","close")
                        R.drawable.ic_baseline_close_24
                    }
                    1 -> {
                        Log.d("iconDebug","flaky")
                        R.drawable.ic_baseline_flaky_24
                    }
                    2 -> {
                        Log.d("iconDebug","check")
                        R.drawable.ic_baseline_check_24
                    }
                    else -> R.drawable.ic_baseline_close_24
                }
                events.add(EventDay(calendar, icon))
            }
            binding.calendarView.setEvents(events)
        }

//        binding.btnShowMoneyCountChart.setOnClickListener {
//            startActivity(Intent(this, GraphicsCountActivity::class.java))
//        }
//
//        binding.btnShowMoneyTypeChart.setOnClickListener {
//            startActivity(Intent(this, GraphicsTypeActivity::class.java))
//        }
    }
    fun getDaysInMonth(month: Int, visoc: Boolean): Int {
        if (visoc && month == 2){
            return 29
        } else{
            return when (month){
                1-> 31
                2->28
                3->31
                4->30
                5->31
                6->30
                7->31
                8->31
                9->30
                10->31
                11->30
                12->31
                else -> 30
            }
        }
    }
    fun getDifference(now: Long, target: Long): Int{
        var answer = 0
        val nowDate = convertMillisToDate(now)
        val targetDate = convertMillisToDate(target)
        val nowYear = nowDate.split("/")[2].toInt()
        val nowMonth = nowDate.split("/")[1].toInt()
        val nowDay = nowDate.split("/")[0].toInt()
        val targetYear = targetDate.split("/")[2].toInt()
        val targetMonth = targetDate.split("/")[1].toInt()
        val targetDay = targetDate.split("/")[0].toInt()
        if (nowYear == targetYear){
            return if(nowMonth == targetMonth){
                Log.d("HelpMethod", "1 now $nowDate target $targetDate result ${targetDay-nowDay}")
                targetDay-nowDay
            } else {
                if (targetMonth > nowMonth) {
                    Log.d("HelpMethod", "target >")
                    for (i in nowMonth until targetMonth) {
                        Log.d("HelpMethod", i.toString())
                        Log.d("HelpMethod", getDaysInMonth(i, nowYear % 4 == 0).toString())
                        answer += getDaysInMonth(i, nowYear % 4 == 0)
                    }
                } else{
                    Log.d("HelpMethod", "target <")
                    for (i in targetMonth until nowMonth) {
                        answer -= getDaysInMonth(i, nowYear % 4 == 0)
                    }
                }
                Log.d("HelpMethod", "2 answer $answer now $nowDate target $targetDate result ${targetDay-nowDay+answer}")
                targetDay-nowDay+answer
            }
        } else{
            if (targetYear>nowYear){
                for (i in nowYear until targetYear){
                    answer += if (i%4==0) 366 else 365
                }
            } else{
                for (i in targetYear until nowYear){
                    answer -= if (i%4==0) 366 else 365
                }
            }
            if (targetMonth > nowMonth) {
                for (i in nowMonth until targetMonth) {
                    answer += getDaysInMonth(i, nowYear % 4 == 0)
                }
            } else{
                for (i in targetMonth until nowMonth) {
                    answer -= getDaysInMonth(i, nowYear % 4 == 0)
                }
            }
            Log.d("HelpMethod", "3 now $nowDate target $targetDate result ${targetDay-nowDay+answer}")
            return targetDay-nowDay+answer
        }
//        Log.d("HelpMethod", "now {}")
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