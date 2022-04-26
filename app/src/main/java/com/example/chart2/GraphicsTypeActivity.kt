package com.example.chart2

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.chart2.database.GraphicsDataType
import com.example.chart2.databinding.ActivityGraphicsTypeBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GraphicsTypeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGraphicsTypeBinding
    private val viewModel: ViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGraphicsTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.dataLiveData2.observe(this){
            showPieChart(it)
            Log.d("Debug", it.toString())
        }
        binding.btnSetDateRange.setOnClickListener {
            val dateRangePicker =
                MaterialDatePicker.Builder.dateRangePicker().setTitleText("Выберите диапазон")
                    .build()

            dateRangePicker.show(supportFragmentManager, "date_range_picker")
            dateRangePicker.addOnPositiveButtonClickListener { datePicked ->
                val firstDateUnix = datePicked.first/1000
                val secondDateUnix = datePicked.second/1000
                viewModel.getDataForGraphicsTypes(firstDateUnix, secondDateUnix)
            }
        }


    }
    private fun showPieChart(data: List<GraphicsDataType>){
        val entries = mutableListOf<PieEntry>()
        data.forEach{
            entries.add(PieEntry(it.sum.toFloat(), it.type))
        }
        val pieDataset = PieDataSet(entries, "Categories")
        val pieData = PieData(pieDataset)
        pieDataset.colors = ColorTemplate.JOYFUL_COLORS.toList()
        binding.apply {
            chart.data = pieData
            chart.invalidate()
        }
    }
}