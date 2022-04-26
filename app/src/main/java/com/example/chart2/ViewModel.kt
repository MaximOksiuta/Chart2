package com.example.chart2

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chart2.database.GraphicsDataCount
import com.example.chart2.database.GraphicsDataType
import com.example.chart2.database.Item
import com.example.chart2.database.ItemDao
import com.example.chart2.models.RVItemData
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ViewModel @ViewModelInject constructor(private val dao: ItemDao): ViewModel() {

    private val _dataLiveData1 = MutableLiveData<List<GraphicsDataCount>>()
    val dataLiveData1: LiveData<List<GraphicsDataCount>> = _dataLiveData1
    private val _dataLiveData2 = MutableLiveData<List<GraphicsDataType>>()
    val dataLiveData2: LiveData<List<GraphicsDataType>> = _dataLiveData2
    private val _dataLiveData3 = MutableLiveData<List<RVItemData>>()
    val dataLiveData3: LiveData<List<RVItemData>> = _dataLiveData3

    fun getDataForGraphicsCount(fromDate: Long, toDate: Long){
        viewModelScope.launch {
            dao.getAllInRangeWithSum(fromDate, toDate).collect {
                _dataLiveData1.postValue(it)
            }
        }
    }

    fun getDataForGraphicsTypes(fromDate: Long, toDate: Long){
        viewModelScope.launch {
            dao.getAllTypesInRange(fromDate, toDate).collect {
                _dataLiveData2.postValue(it)
            }
        }
    }

    fun getDataForRV(date: Long){
        Log.d("DataRV", "methodCalled")
        viewModelScope.launch {
            dao.getAllWithTime(date).collectLatest{
//                if (it != _dataLiveData3.value && it.isNotEmpty()){
                    Log.d("ViewModel", it.toString())
                    _dataLiveData3.postValue(it)
                }
//                _dataLiveData3.postValue(dao.getAllWithTime(date))
//

        }
    }

    fun updateState(id: Int, state: Boolean){
        viewModelScope.launch {
//            dao.getItemById(id).collect { item ->
//                val newItem = Item(id = item.id, name = item.name, category = item.category, price= item.price, date = item.date, state = state)
//                dao.updateState(newItem)
//            }
            val item = dao.getItemById(id)
            val newItem = Item(id = item.id, name = item.name, category = item.category, price= item.price, date = item.date, state = state)
            dao.updateState(newItem)
        }
    }
}