package com.example.pre_eclampsiascreener.ui.state

sealed class NavEvent {
    object GoToMenu : NavEvent()
    object GoToConnection : NavEvent()
}
