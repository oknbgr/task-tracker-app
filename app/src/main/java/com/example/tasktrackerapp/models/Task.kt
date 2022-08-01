package com.example.tasktrackerapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Task(
    val user_id: String = "",
    val title: String = "",
    val description: String = "",
    val time: String = "",
    var task_id: String = "",
) : Parcelable
