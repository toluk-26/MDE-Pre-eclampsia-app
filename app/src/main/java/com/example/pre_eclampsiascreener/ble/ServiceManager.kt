package com.example.pre_eclampsiascreener.ble

import kotlinx.coroutines.CoroutineScope
import no.nordicsemi.kotlin.ble.client.RemoteService

interface ServiceManager {
    val profile: Profile

    suspend fun observeServiceInteractions(
//        peripheral: Peripheral,
        remoteService: RemoteService,
        scope: CoroutineScope
    )

    companion object{
        const val TAG_DATA = "BleData"
    }
}