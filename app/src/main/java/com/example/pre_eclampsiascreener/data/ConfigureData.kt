package com.example.pre_eclampsiascreener.data

data class ConfigureData(
    val patientID: String? = null,
    val minDiastolic: Int? = null, // TODO: replace with string resource
    val minSystolic: Int? = null,
    val maxDiastolic: Int? = null,
    val maxSystolic: Int? = null,
)
