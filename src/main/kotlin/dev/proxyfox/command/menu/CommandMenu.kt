package dev.proxyfox.command.menu

import dev.proxyfox.command.CommandContext
import dev.proxyfox.command.ScreenBuilder

public abstract class CommandMenu<T, C : CommandContext<T>> {
    private var activeScreen: ScreenBuilder<T, C> = {}
    private var screens: HashMap<String, ScreenBuilder<T, C>> = hashMapOf()

    public operator fun String.invoke(action: ScreenBuilder<T, C>) {
        screens[this] = action
    }

    internal fun setScreen(name: String, ctx: C) {
        val screen = createScreen(name)
        screens[name]!!(screen, ctx)
        screen.init()
    }

    public abstract fun createScreen(name: String): CommandScreen<T, C>
}