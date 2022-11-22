package dev.proxyfox.command.node.builtin

import dev.proxyfox.command.CommandContext
import dev.proxyfox.command.NodeActionParam
import dev.proxyfox.command.NodeHolder
import dev.proxyfox.command.node.CommandNode
import dev.proxyfox.command.node.Priority
import java.lang.NullPointerException

public class UnixNode<T, C: CommandContext<T>>(override val name: String): CommandNode<T, C>() {
    override val priority: Priority = Priority.SEMI_STATIC

    override fun parse(str: String, ctx: C): Int {
        if (str.isEmpty()) return -1
        var idx = 0
        val arr = ArrayList<String>()
        while (idx < str.length) {
            while (idx < str.length) {
                if (str[idx] != ' ') break
                idx++
            }
            var substr = str.substring(idx)
            val start =
                if (substr.startsWith("--")) "--"
                else if (substr.startsWith("-")) "-"
                else break
            idx += start.length
            substr = str.substring(idx)
            var out = ""
            for (i in substr) {
                if (i == ' ')
                    break
                out += i
            }
            arr.add(out)
            idx += out.length
        }
        if (arr.isEmpty()) return -1
        ctx[name] = arr
        return idx
    }
}

public suspend fun <T, C: CommandContext<T>> NodeHolder<T, C>.unix(
    name: String,
    action: NodeActionParam<T, C, ArrayList<String>>
): CommandNode<T, C> {
    val node = UnixNode<T, C>(name)
    node.action {
        this[name] ?: throw NullPointerException("Parameter $name for $command is null!")
    }
    addNode(node)
    return node
}
