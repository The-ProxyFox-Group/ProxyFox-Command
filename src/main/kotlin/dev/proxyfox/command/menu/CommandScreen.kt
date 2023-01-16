package dev.proxyfox.command.menu

import dev.proxyfox.command.ButtonAction

public abstract class CommandScreen(public val name: String) {
    internal val buttons: HashMap<String, ButtonAction> = hashMapOf()

    public open suspend fun button(name: String, action: ButtonAction) {
        buttons[name] = action
    }

    public open suspend fun click(name: String) {
        buttons[name]!!()
    }

    public abstract suspend fun init()
}