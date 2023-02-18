package dev.proxyfox.command.node.builtin

import dev.proxyfox.command.CommandContext
import dev.proxyfox.command.NodeActionParam
import dev.proxyfox.command.NodeHolder
import dev.proxyfox.command.StringCursor
import dev.proxyfox.command.node.CommandNode
import dev.proxyfox.command.node.Priority

public class StringListNode<T, C: CommandContext<T>>(override val name: String): CommandNode<T, C>() {
    override val priority: Priority = Priority.GREEDY

    override fun parse(cursor: StringCursor, ctx: C): Boolean {
        if (cursor.end) return false
        val arr = ArrayList<String>()
        while (!cursor.end) {
            arr += cursor.extractString(true)
            cursor.inc()
        }
        ctx[name] = arr
        return true
    }
}

public suspend fun <T, C: CommandContext<T>> NodeHolder<T, C>.stringList(
    name: String,
    action: NodeActionParam<T, C, ArrayList<String>>
): CommandNode<T, C> {
    val node = StringListNode<T, C>(name)
    node.action {
        this[name] ?: throw NullPointerException("Parameter $name for $command is null!")
    }
    addNode(node)
    return node
}
