package dev.proxyfox.command.test

import dev.proxyfox.command.menu.CommandMenu
import dev.proxyfox.command.menu.CommandScreen

class TerminalMenu : CommandMenu() {
    private var closed = false

    override suspend fun createScreen(name: String): CommandScreen {
        return TerminalScreen(name, this)
    }

    override suspend fun close() {
        closed = true
    }

    override suspend fun init() {
        super.init()
        while (!closed) {
            val line = readln()
            active!!.click(line)
        }
    }
}

class TerminalScreen(
    name: String,
    menu: CommandMenu
) : CommandScreen(name) {
    override suspend fun init() {
        println("Options in $name:")
        for (button in buttons.keys) {
            println("  $button")
        }
    }
}