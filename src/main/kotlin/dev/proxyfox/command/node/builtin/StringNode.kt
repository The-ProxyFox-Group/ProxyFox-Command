package dev.proxyfox.command.node.builtin

import dev.proxyfox.command.*
import dev.proxyfox.command.node.CommandNode
import dev.proxyfox.command.node.Priority
import java.lang.NullPointerException

public class StringNode<T, C: CommandContext<T>>(override val name: String): CommandNode<T, C>() {
    override val priority: Priority = Priority.VARIABLE

    override fun parse(cursor: StringCursor, ctx: C): Boolean {
        if (cursor.end) return false
        ctx[name] = cursor.extractString(true)
        cursor.inc()
        return true
    }
}

public suspend fun <T, C: CommandContext<T>> NodeHolder<T, C>.string(
    name: String,
    action: NodeActionParam<T, C, String>
): CommandNode<T, C> {
    val node = StringNode<T, C>(name)
    node.action {
        this[name] ?: throw NullPointerException("Parameter $name for $command is null!")
    }
    addNode(node)
    return node
}
