package dev.proxyfox.command

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

    /**
     * A timed yes/no prompt
     * */
    public abstract suspend fun timedYesNoPrompt(
        text: String,
        yesAction: Pair<String, Executor<T>>,
        noAction: Pair<String, Executor<T>>,
        timeoutAction: Executor<T>,
        private: Boolean = false
    )
}
