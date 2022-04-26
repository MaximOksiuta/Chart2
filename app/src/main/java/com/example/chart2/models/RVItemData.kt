package com.example.chart2.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RVItemData(val id: Int, val category: String, val description: String, val price: Float, val state: Boolean):
    Parcelable
