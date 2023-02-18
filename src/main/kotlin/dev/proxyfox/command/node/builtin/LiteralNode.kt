package dev.proxyfox.command.node.builtin

import dev.proxyfox.command.CommandContext
import dev.proxyfox.command.NodeAction
import dev.proxyfox.command.node.CommandNode
import dev.proxyfox.command.node.Priority
import dev.proxyfox.command.NodeHolder
import dev.proxyfox.command.StringCursor


public class LiteralNode<T, C: CommandContext<T>>(public val literals: Array<out String>): CommandNode<T, C>() {
    init {
        for (literal in literals)
            if (literal.contains(" "))
                throw IllegalArgumentException("Literal `$literal` contains a space!")
    }

    override val priority: Priority = Priority.STATIC
    override val name: String = ""

    override fun parse(cursor: StringCursor, ctx: C): Boolean {
        val str = cursor.extractString(false)
        cursor.inc()
        for (literal in literals) {
            if (literal.lowercase() == str.lowercase()) return true
        }
        return false
    }
}

public suspend fun <T, C: CommandContext<T>> NodeHolder<T, C>.literal(
    vararg names: String,
    action: NodeAction<T, C>
): CommandNode<T, C> {
    val node = LiteralNode<T, C>(names)
    node.action()
    addNode(node)
    return node
}

public suspend fun <T, C: CommandContext<T>> NodeHolder<T, C>.unixLiteral(
    vararg names: String,
    action: NodeAction<T, C>
): CommandNode<T, C> {
    val newNames = ArrayList<String>()
    names.forEach {
        newNames.add("-$it")
        newNames.add("--$it")
    }
    val node = LiteralNode<T, C>(newNames.toTypedArray())
    node.action()
    addNode(node)
    return node
}
