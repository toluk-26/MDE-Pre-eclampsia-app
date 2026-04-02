package com.example.pre_eclampsiascreener.data

data class TransferHeaderData(
    val type: PayloadType,
    val length: UInt,
    val timestamp: Long
)
