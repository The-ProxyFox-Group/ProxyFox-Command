package dev.proxyfox.command.menu

import dev.proxyfox.command.CommandContext
import dev.proxyfox.command.ScreenBuilder

public abstract class CommandMenu {
    public var default: Pair<String, ScreenBuilder>? = null
    public var active: CommandScreen? = null
    private var screens: HashMap<String, ScreenBuilder> = hashMapOf()

    public fun default(name: String = "default", action: ScreenBuilder) {
        screens[name] = action
        default = Pair(name, action)
    }

    public operator fun String.invoke(action: ScreenBuilder) {
        screens[this] = action
        if (default == null)
            default = Pair(this, action)
    }

    internal fun setScreen(name: String) {
        val screen = createScreen(name)
        screens[name]!!(screen)
        screen.init()
        active = screen
    }

    public open fun init() {
        val screen = createScreen(default!!.first)
        default!!.second(screen)
        screen.init()
        active = screen
    }

    public abstract fun createScreen(name: String): CommandScreen

    public abstract fun close()
}