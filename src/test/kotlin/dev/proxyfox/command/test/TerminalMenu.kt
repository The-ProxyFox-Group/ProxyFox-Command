package dev.proxyfox.command.test

import dev.proxyfox.command.menu.CommandMenu
import dev.proxyfox.command.menu.CommandScreen

class TerminalMenu : CommandMenu() {
    var closed = false

    override fun createScreen(name: String): CommandScreen {
        return TerminalScreen(name, this)
    }

    override fun close() {
        closed = true
    }

    override fun init() {
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
) : CommandScreen(name, menu) {
    override fun init() {
        println("Options in $name:")
        for (button in buttons.keys) {
            println("  $button")
        }
    }
}