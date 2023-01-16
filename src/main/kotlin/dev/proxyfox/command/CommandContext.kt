package dev.proxyfox.command

import dev.proxyfox.command.menu.CommandMenu

public abstract class CommandContext<T> {
    /**
     * The command trigger for the context
     * */
    public abstract val value: T
    /**
     * The raw command string
     * */
    public abstract val command: String

    public val params: HashMap<String, Any> = hashMapOf()

    public inline operator fun <reified T : Any> get(name: String): T? {
        return params[name] as? T
    }

    public inline operator fun <reified T : Any> set(name: String, value: T) {
        params[name] = value
    }

    /**
     * Responds in plain text
     * @return the newly created response object
     * */
    public abstract suspend fun respondPlain(text: String, private: Boolean = false): T

    /**
     * A response that indicates the command executed successfully
     * @return the newly created response object
     * */
    public abstract suspend fun respondSuccess(text: String, private: Boolean = false): T

    /**
     * A response that indicates the command didn't execute successfully
     * @return the newly created response object
     * */
    public abstract suspend fun respondFailure(text: String, private: Boolean = false): T

    /**
     * A response that indicates the command threw a warning
     * @return the newly created response object
     * */
    public abstract suspend fun respondWarning(text: String, private: Boolean = false): T

    public abstract suspend fun menu(action: MenuBuilder)
}
