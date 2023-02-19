package dev.proxyfox.command.test

import dev.proxyfox.command.CommandContext
import dev.proxyfox.command.NodeAction
import dev.proxyfox.command.NodeHolder
import dev.proxyfox.command.StringCursor
import dev.proxyfox.command.node.CommandNode
import dev.proxyfox.command.node.Priority

class ZeroWidthNode<T, C: CommandContext<T>>(override val name: String) : CommandNode<T, C>() {
    override val priority: Priority = Priority.SEMI_STATIC
    override fun parse(cursor: StringCursor, ctx: C): Boolean {
        return true
    }
}

public suspend fun <T, C: CommandContext<T>> NodeHolder<T, C>.zw(
    name: String,
    action: NodeAction<T, C>
): CommandNode<T, C> {
    val node = ZeroWidthNode<T, C>(name)
    node.action()
    addNode(node)
    return node
}
