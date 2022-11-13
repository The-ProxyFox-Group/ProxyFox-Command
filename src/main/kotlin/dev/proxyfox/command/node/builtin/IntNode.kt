package dev.proxyfox.command.node.builtin

import dev.proxyfox.command.CommandContext
import dev.proxyfox.command.NodeActionParam
import dev.proxyfox.command.NodeHolder
import dev.proxyfox.command.node.CommandNode
import dev.proxyfox.command.node.Priority
import java.lang.NullPointerException

public class IntNode<T, C: CommandContext<T>>(override val name: String): CommandNode<T, C>() {
    override val priority: Priority = Priority.SEMI_STATIC

    override fun parse(str: String, ctx: C): Int {
        if (str.isEmpty()) return -1
        var out = ""
        for (i in str.indices) {
            if (str[i] == ' ') {
                ctx[name] = out.toULongOrNull() ?: return -1
                return i
            }
            out += str[i].toString()
        }
        ctx[name] = out.toULongOrNull() ?: return -1
        return str.length
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
