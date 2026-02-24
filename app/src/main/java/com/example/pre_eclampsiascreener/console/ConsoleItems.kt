package com.example.pre_eclampsiascreener.console

object ConsoleItems {
    val items = mutableListOf<ConsoleItem>()

    fun add(item: ConsoleItem){
        items.add(item)
    }

    fun listOfConsoleItems(): List<ConsoleItem>{
        return items
    }

}