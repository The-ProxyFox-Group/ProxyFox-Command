package dev.proxyfox.command.node.builtin

import dev.proxyfox.command.CommandContext
import dev.proxyfox.command.NodeAction
import dev.proxyfox.command.node.CommandNode
import dev.proxyfox.command.node.Priority
import dev.proxyfox.command.NodeHolder


public class LiteralNode<T, C: CommandContext<T>>(private val literals: Array<out String>): CommandNode<T, C>() {
    override val priority: Priority = Priority.STATIC
    override val name: String = ""

    override fun parse(str: String, ctx: C): Int {
        for (literal in literals) {
            if (str.length < literal.length) continue
            if (str.length == literal.length && str.lowercase() == literal.lowercase())
                return literal.length
            if (str.length > literal.length && str.lowercase().startsWith(literal.lowercase() + " "))
                return literal.length
        }
        return -1
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
