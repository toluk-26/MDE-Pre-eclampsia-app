package com.example.pre_eclampsiascreener.ble

sealed class ConnectState {
    data object Idle : ConnectState()
    data object Connecting : ConnectState()
    data object Connected : ConnectState()     // services loaded, navigate to menu
    data object Failed : ConnectState()    // connection or service error
}