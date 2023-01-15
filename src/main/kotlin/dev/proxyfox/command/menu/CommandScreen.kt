package dev.proxyfox.command.menu

import dev.proxyfox.command.ButtonBuilder
import dev.proxyfox.command.CommandContext

public abstract class CommandScreen(public val name: String, internal val menu: CommandMenu) {
    internal val buttons: HashMap<String, ButtonBuilder> = hashMapOf()

    public fun button(name: String, action: ButtonBuilder) {
        buttons[name] = action
    }

    public fun click(name: String) {
        buttons[name]!!(CommandButton(this))
    }

    public abstract fun init()
}