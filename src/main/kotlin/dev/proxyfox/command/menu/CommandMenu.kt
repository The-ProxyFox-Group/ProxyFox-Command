package dev.proxyfox.command.menu

import dev.proxyfox.command.ScreenBuilder

public abstract class CommandMenu {
    private var default: CommandScreen? = null
    public var active: CommandScreen? = null

    public open suspend fun default(name: String = "default", action: ScreenBuilder): CommandScreen {
        val screen = createScreen(name)
        screen.action()
        default = screen
        return screen
    }

    public open suspend operator fun String.invoke(action: ScreenBuilder): CommandScreen {
        val screen = createScreen(this)
        screen.action()
        if (default == null)
            default = screen
        return screen
    }

    public open suspend fun setScreen(screen: CommandScreen) {
        active = screen
        screen.init()
    }

    public open suspend fun init() {
        setScreen(default ?: return)
    }

    public abstract suspend fun createScreen(name: String): CommandScreen

    public abstract suspend fun close()
}