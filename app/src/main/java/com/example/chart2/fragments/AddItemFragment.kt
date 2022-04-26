package com.example.chart2.fragments

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.chart2.database.Item
import com.example.chart2.databinding.FragmentAddItemBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddItemFragment(val dateUnix: Long, val date: String): BottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddItemBinding
    private val viewModel: AddItemViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddItemBinding.inflate(inflater, container, false)

        var selectedCat = ""
        binding.apply {
            val items = listOf(
                "Кафе",
                "Продукты",
                "Транспорт",
                "Онлайн-покупки",
                "Канцелярия",
                "Кинотеатры"
            )
            tvDateToBuy.text = date
            val adapter = ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, items)
            etCategories.setAdapter(adapter)
            etCategories.setOnItemClickListener { _, _, position, _ ->
                selectedCat = items[position]
            }

            btnSave.setOnClickListener {
                val plan = etPlans.text.toString()
                val sum = etSumToBuy.text.toString().toFloat()
                val item = Item(name = plan, price = sum, date = dateUnix, category = selectedCat, state = false)
                viewModel.insertItem(item)
            }
        }
        viewModel.insertItemLiveData.observe(viewLifecycleOwner){
            if (it != 0L){
                Toast.makeText(requireContext() , "Данные сохранены", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(), "Ошибка", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
}


