package com.example.chart2.utils

import android.util.Log
import java.text.SimpleDateFormat

class HelperMethods {
    companion object{
        fun convertMillisToDate(millis: Long): String{
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            return sdf.format(millis)
        }

        fun convertDateToMillis(date: String): Long{
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            return sdf.parse(date).time
        }

        fun convertMillisToDateMills(millis: Long): Long{
            val tmp = convertMillisToDate(millis)
            return convertDateToMillis(tmp)
        }
    }
}