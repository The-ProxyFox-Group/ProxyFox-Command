package dev.proxyfox.command.node.builtin

import dev.proxyfox.command.CommandContext
import dev.proxyfox.command.NodeActionParam
import dev.proxyfox.command.NodeHolder
import dev.proxyfox.command.node.CommandNode
import dev.proxyfox.command.node.Priority

public class StringListNode<T, C: CommandContext<T>>(override val name: String): CommandNode<T, C>() {
    override val priority: Priority = Priority.GREEDY

    override fun parse(str: String, ctx: C): Int {
        if (str.isEmpty()) return -1
        var i = 0
        val arr = ArrayList<String>()
        while (i < str.length) {
            if (str[i] == ' ') {
                i++
                continue
            }
            when (str[i]) {
                '"' -> {
                    var out = ""
                    val substr = str.substring(i + 1)
                    for (j in substr.indices) {
                        if (substr[j] == '"')
                            break
                        out += substr[j].toString()
                    }
                    if (out.isNotEmpty()) {
                        arr.add(out)
                        i += out.length + 2
                        continue
                    }
                    if (out.isNotEmpty()) arr.add(out)
                }

                '\'' -> {
                    var out = ""
                    val substr = str.substring(i + 1)
                    for (j in substr.indices) {
                        if (substr[j] == '\'')
                            break
                        out += substr[j].toString()
                    }
                    if (out.isNotEmpty()) {
                        arr.add(out)
                        i += out.length + 2
                        continue
                    }
                }

                else -> {
                    var out = ""
                    val substr = str.substring(i)
                    for (j in substr.indices) {
                        if (substr[j] == ' ')
                            break
                        out += substr[j].toString()
                    }
                    if (out.isNotEmpty()) {
                        arr.add(out)
                        i += out.length
                        continue
                    }
                }
            }
            i++
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
