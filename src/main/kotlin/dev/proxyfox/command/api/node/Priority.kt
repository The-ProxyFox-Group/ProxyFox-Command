package dev.proxyfox.command.api.node

public enum class Priority(public val value: Int) {
    /**
     * Static string, never changes
     * */
    STATIC(3),
    /**
     * Limited charset
     * */
    SEMI_STATIC(2),
    /**
     * Non-limited charset
     * */
    VARIABLE(1),
    /**
     * Takes up rest of command
     * */
    GREEDY(0)
}