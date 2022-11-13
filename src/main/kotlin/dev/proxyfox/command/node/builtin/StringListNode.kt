package dev.proxyfox.command.node.builtin

import dev.proxyfox.command.CommandContext
import dev.proxyfox.command.NodeActionParam
import dev.proxyfox.command.NodeHolder
import dev.proxyfox.command.node.CommandNode
import dev.proxyfox.command.node.Priority
import java.lang.NullPointerException

public class StringListNode<T, C: CommandContext<T>>(override val name: String): CommandNode<T, C>() {
    override val priority: Priority = Priority.GREEDY

    override fun parse(str: String, ctx: C): Int {
        if (str.isEmpty()) return -1
        var i = 0
        val arr = ArrayList<String>()
        while (i < str.length) {
            when (str[i]) {
                '"' -> {
                    var out = ""
                    for (j in str.substring(1).indices) {
                        if (str[j] == '"') {
                            arr.add(out)
                            out = ""
                            i += j + 1
                            continue
                        }
                        out += str[j].toString()
                    }
                    arr.add(out)
                }

                '\'' -> {
                    var out = ""
                    for (j in str.substring(1).indices) {
                        if (str[j] == '\'') {
                            arr.add(out)
                            out = ""
                            i += j + 1
                            continue
                        }
                        out += str[j].toString()
                    }
                    arr.add(out)
                }

                else -> {
                    var out = ""
                    for (j in str.indices) {
                        if (str[j] == ' ') {
                            arr.add(out)
                            out = ""
                            i += j
                            continue
                        }
                        out += str[j].toString()
                    }
                    arr.add(out)
                }
            }
            i = str.length
        }
        ctx[name] = arr
        return str.length
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
