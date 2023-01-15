package dev.proxyfox.command.menu

public class CommandButton(private val commandScreen: CommandScreen) {
    public var screen: String
        get() = commandScreen.name
        set(value) = commandScreen.menu.setScreen(value)
}