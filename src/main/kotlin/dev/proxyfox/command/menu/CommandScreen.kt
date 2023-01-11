package dev.proxyfox.command.menu

import dev.proxyfox.command.ButtonBuilder
import dev.proxyfox.command.CommandContext

public abstract class CommandScreen<T, C : CommandContext<T>>(public val name: String, internal val menu: CommandMenu<T, C>) {
    private val buttons: HashMap<String, ButtonBuilder<T, C>> = hashMapOf()

    public fun button(name: String, action: ButtonBuilder<T, C>) {
        buttons[name] = action
    }

    public fun click(name: String, ctx: C) {
        buttons[name]!!(CommandButton(this, ctx), ctx)
    }

    public abstract fun init()
}