package dev.proxyfox.command.node.builtin

import dev.proxyfox.command.CommandContext
import dev.proxyfox.command.NodeActionParam
import dev.proxyfox.command.NodeHolder
import dev.proxyfox.command.StringCursor
import dev.proxyfox.command.node.CommandNode
import dev.proxyfox.command.node.Priority
import java.lang.NullPointerException

public class IntNode<T, C: CommandContext<T>>(override val name: String): CommandNode<T, C>() {
    override val priority: Priority = Priority.SEMI_STATIC

    override fun parse(cursor: StringCursor, ctx: C): Boolean {
        if (cursor.end) return false
        ctx[name] = cursor.extractString(true).toULongOrNull() ?: return false
        cursor.inc()
        return true
    }
}

public suspend fun <T, C: CommandContext<T>> NodeHolder<T, C>.int(
    name: String,
    action: NodeActionParam<T, C, ULong>
): CommandNode<T, C> {
    val node = IntNode<T, C>(name)
    node.action {
        this[name] ?: throw NullPointerException("Parameter $name for $command is null!")
    }
    addNode(node)
    return node
}
