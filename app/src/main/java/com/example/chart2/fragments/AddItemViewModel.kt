package com.example.chart2.fragments

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chart2.database.Item
import com.example.chart2.database.ItemDao
import kotlinx.coroutines.launch

class AddItemViewModel @ViewModelInject constructor(private val dao: ItemDao): ViewModel() {
    private val _inserItemLiveData = MutableLiveData<Long>()
    var insertItemLiveData: LiveData<Long> = _inserItemLiveData

    fun insertItem(item: Item){
        viewModelScope.launch {
            val response = dao.addItem(item)
            _inserItemLiveData.postValue(response)
        }
    }
}