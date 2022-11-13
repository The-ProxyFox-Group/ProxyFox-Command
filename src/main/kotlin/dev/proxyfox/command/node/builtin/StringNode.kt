package dev.proxyfox.command.node.builtin

import dev.proxyfox.command.*
import dev.proxyfox.command.node.CommandNode
import dev.proxyfox.command.node.Priority
import java.lang.NullPointerException

public class StringNode<T, C: CommandContext<T>>(override val name: String): CommandNode<T, C>() {
    override val priority: Priority = Priority.VARIABLE

    override fun parse(str: String, ctx: C): Int {
        if (str.isEmpty()) return -1
        when (str[0]) {
            '"' -> {
                var out = ""
                for (i in str.substring(1).indices) {
                    if (str[i+1] == '"') {
                        ctx[name] = out
                        return i + 2
                    }
                    out += str[i+1].toString()
                }
                ctx[name] = out
            }

            '\'' -> {
                var out = ""
                for (i in str.substring(1).indices) {
                    if (str[i+1] == '\'') {
                        ctx[name] = out
                        return i + 2
                    }
                    out += str[i+1].toString()
                }
                ctx[name] = out
            }

            else -> {
                var out = ""
                for (i in str.indices) {
                    if (str[i] == ' ') {
                        ctx[name] = out
                        return i
                    }
                    out += str[i].toString()
                }
                ctx[name] = out
            }
        }
        return str.length
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
