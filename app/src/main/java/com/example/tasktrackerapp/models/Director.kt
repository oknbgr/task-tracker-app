package com.example.tasktrackerapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Director(
    val id: String,
    val name: String,
    val surname: String,
    val email: String,
    val department: String
) : Parcelable