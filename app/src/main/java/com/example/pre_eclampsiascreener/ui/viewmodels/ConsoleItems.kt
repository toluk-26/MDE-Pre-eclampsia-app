package com.example.pre_eclampsiascreener.ui.viewmodels

import com.example.pre_eclampsiascreener.data.ConsoleItem

object ConsoleItems {
    val items = mutableListOf<ConsoleItem>()

    fun add(item: ConsoleItem){
        items.add(item)
    }

    fun listOfConsoleItems(): List<ConsoleItem>{
        return items
    }

}