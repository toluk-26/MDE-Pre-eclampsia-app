package com.example.pre_eclampsiascreener.ble.data

import com.example.pre_eclampsiascreener.ble.Profile
import com.example.pre_eclampsiascreener.data.TransferHeaderData
import com.example.pre_eclampsiascreener.data.Payload

data class TransferServiceData(
    override val profile: Profile = Profile.TRANSFER,
    val header: TransferHeaderData,
    val payload: Payload
): ProfileServiceData()
