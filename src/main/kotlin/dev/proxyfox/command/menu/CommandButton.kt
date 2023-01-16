package dev.proxyfox.command.menu

public class CommandButton(private val commandScreen: CommandScreen) {
    public var screen: CommandScreen
        get() = commandScreen
        set(value) = commandScreen.menu.setScreen(value)
}