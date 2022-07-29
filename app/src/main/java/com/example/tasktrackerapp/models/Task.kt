package com.example.tasktrackerapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Task(
    val title: String,
    val description: String,
    val time: String,
    val employee: Employee
) : Parcelable
