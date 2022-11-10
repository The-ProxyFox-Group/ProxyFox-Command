package dev.proxyfox.command.api

public interface CommandContext<T> {
    /**
     * The value for the context
     * */
    public val value: T

    /**
     * Responds in plain text
     * @return the newly created response object
     * */
    public fun respondPlain(text: String): T

    /**
     * A response that indicates the command executed successfully
     * @return the newly created response object
     * */
    public fun respondSuccess(text: String): T

    /**
     * A response that indicates the command didn't execute successfully
     * @return the newly created response object
     * */
    public fun respondFailure(text: String): T

    /**
     * A response that indicates the command threw a warning
     * @return the newly created response object
     * */
    public fun respondWarning(text: String): T

    /**
     * A timed yes/no prompt
     * @return 1: The yes/no prompt message, 2: The response message, 3: The selected option
     * */
    public fun timedYesNoPrompt(text: String, yes: String, no: String): Triple<T, T, Boolean>
}
