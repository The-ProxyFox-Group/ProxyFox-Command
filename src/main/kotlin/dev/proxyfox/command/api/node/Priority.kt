package dev.proxyfox.command.api.node

public enum class Priority(public val value: Int) {
    STATIC(3),
    SEMI_STATIC(2),
    VARIABLE(1),
    GREEDY(0)
}