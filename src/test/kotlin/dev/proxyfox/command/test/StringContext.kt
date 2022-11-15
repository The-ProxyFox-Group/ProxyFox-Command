package dev.proxyfox.command.test

import dev.proxyfox.command.CommandContext
import dev.proxyfox.command.Executor

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

    override suspend fun timedYesNoPrompt(
        text: String,
        yesAction: Pair<String, Executor<String>>,
        noAction: Pair<String, Executor<String>>,
        timeoutAction: Executor<String>,
        private: Boolean
    ) {
        print("$text [y/n]: ")
        val line = readln()
        when (line.trim().lowercase()) {
            "y", "yes" -> yesAction.second(this)
            "n", "no" -> noAction.second(this)
            else -> timeoutAction()
        }
    }
}
