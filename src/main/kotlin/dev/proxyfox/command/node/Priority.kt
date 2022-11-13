package dev.proxyfox.command.node

public enum class Priority(public val value: Int) {
    /**
     * Static string, never changes
     * */
    STATIC(0),

    /**
     * Limited charset
     * */
    SEMI_STATIC(1),

    /**
     * Non-limited charset
     * */
    VARIABLE(2),

    /**
     * Takes up rest of command
     * */
    GREEDY(3)
}
