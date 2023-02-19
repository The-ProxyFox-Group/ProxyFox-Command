package dev.proxyfox.command.node.builtin

import dev.proxyfox.command.CommandContext
import dev.proxyfox.command.NodeActionParam
import dev.proxyfox.command.NodeHolder
import dev.proxyfox.command.StringCursor
import dev.proxyfox.command.node.CommandNode
import dev.proxyfox.command.node.Priority
import java.lang.NullPointerException

public class UnixNode<T, C: CommandContext<T>>(override val name: String): CommandNode<T, C>() {
    override val priority: Priority = Priority.SEMI_STATIC

    override fun parse(cursor: StringCursor, ctx: C): Boolean {
        if (cursor.end) return false
        val arr = ArrayList<String>()

        while (!cursor.end) {
            cursor.checkout()
            val str = cursor.extractString(false)
            arr += if (str.startsWith("--")) {
                str.substring(2)
            } else if (str.startsWith("-")) {
                str.substring(1)
            } else {
                cursor.rollback()
                break
            }
            cursor.commit()
            cursor.inc()
        }

        if (arr.isEmpty()) return false
        ctx[name] = arr
        cursor.inc()
        return true
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
