package dev.proxyfox.command.menu

import dev.proxyfox.command.CommandContext

public class CommandButton<T, C: CommandContext<T>>(private val commandScreen: CommandScreen<T, C>, private val context: C) {
    public var screen: String
        get() = commandScreen.name
        set(value) = commandScreen.menu.setScreen(value, context)
}