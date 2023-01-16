package dev.proxyfox.command.menu

import dev.proxyfox.command.ScreenBuilder

public abstract class CommandMenu {
    private var default: CommandScreen? = null
    public var active: CommandScreen? = null

    public fun default(name: String = "default", action: ScreenBuilder): CommandScreen {
        val screen = createScreen(name)
        screen.action()
        default = screen
        return screen
    }

    public operator fun String.invoke(action: ScreenBuilder): CommandScreen {
        val screen = createScreen(this)
        screen.action()
        if (default == null)
            default = screen
        return screen
    }

    internal fun setScreen(screen: CommandScreen) {
        active = screen
    }

    public open fun init() {
        active = default
    }

    public abstract fun createScreen(name: String): CommandScreen

    public abstract fun close()
}