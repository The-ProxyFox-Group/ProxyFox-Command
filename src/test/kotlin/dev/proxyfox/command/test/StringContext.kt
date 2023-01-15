package dev.proxyfox.command.test

import dev.proxyfox.command.CommandContext
import dev.proxyfox.command.MenuBuilder

class StringContext(override val value: String): CommandContext<String>() {
    override val command: String = value

    override suspend fun respondPlain(text: String, private: Boolean): String {
        println(text)
        return text
    }

    override suspend fun respondSuccess(text: String, private: Boolean): String {
        println(text)
        return text
    }

    override suspend fun respondFailure(text: String, private: Boolean): String {
        System.err.println(text)
        return text
    }

    override suspend fun respondWarning(text: String, private: Boolean): String {
        System.err.println(text)
        return text
    }

    override suspend fun menu(action: MenuBuilder) {
        val menu = TerminalMenu()
        menu.action()
        menu.init()
    }
}
