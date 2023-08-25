package dev.proxyfox.command

import kotlin.math.abs

public class StringCursor(public val command: String) {
    public var index: Int = 0
        private set

    public val beginning: Boolean get() = index <= 0
    public val end: Boolean get() = index >= command.length

    private val positions: ArrayList<Int> = arrayListOf()

    public fun checkout() {
        positions.add(index)
    }
    public fun rollback() {
        index = positions.removeLast()
    }
    public fun commit() {
        positions.removeLast()
    }

    public fun get(): Char? {
        if (index >= command.length || index < 0) return null
        return command[index]
    }

    public operator fun inc(): StringCursor {
        if (!end) index++
        return this
    }

    public operator fun dec(): StringCursor {
        if (!beginning) index--
        return this
    }

    public operator fun plusAssign(amount: Int) {
        for (i in 0..<abs(amount)) {
            if (amount > 0) inc()
            else dec()
        }
    }

    public operator fun minusAssign(amount: Int): Unit = plusAssign(-amount)

    public fun extractString(allowQuotes: Boolean = false): String = when {
        get() == '"' && allowQuotes -> { inc(); getString('"', allowEscape = true) }
        get() == '\'' && allowQuotes -> { inc(); getString('\'', allowEscape = true) }
        else -> getString()
    }

    public fun getString(endChar: Char = ' ', allowEscape: Boolean = false): String {
        var out = ""

        while (get() != endChar && !end) {
            if (get() == '\\' && allowEscape) {
                inc()
                if (get() != endChar) out += '\\'
            }
            out += get()
            inc()
        }
        if (endChar != ' ') inc()

        return out
    }

    public fun seekToEnd(): String {
        var out = ""
        while (!end) {
            out += get()
            inc()
        }
        return out
    }

    override fun toString(): String {
        return "StringCursor(command='$command', index=$index, positions=$positions)"
    }
}
