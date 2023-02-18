package dev.proxyfox.command.node.builtin

import dev.proxyfox.command.CommandContext
import dev.proxyfox.command.NodeActionParam
import dev.proxyfox.command.NodeHolder
import dev.proxyfox.command.StringCursor
import dev.proxyfox.command.node.CommandNode
import dev.proxyfox.command.node.Priority
import java.lang.NullPointerException

public class GreedyNode<T, C: CommandContext<T>>(override val name: String): CommandNode<T, C>() {
    override val priority: Priority = Priority.GREEDY

    override fun parse(str: StringCursor, ctx: C): Boolean {
        if (str.end) return false
        ctx[name] = str.seekToEnd()
        return true
    }
}

public suspend fun <T, C: CommandContext<T>> NodeHolder<T, C>.greedy(
    name: String,
    action: NodeActionParam<T, C, String>
): CommandNode<T, C> {
    val node = GreedyNode<T, C>(name)
    node.action {
        this[name] ?: throw NullPointerException("Parameter $name for $command is null!")
    }
    addNode(node)
    return node
}
