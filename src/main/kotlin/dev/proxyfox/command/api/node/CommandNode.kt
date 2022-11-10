package dev.proxyfox.command.api.node

import dev.proxyfox.command.api.CommandContext

public abstract class CommandNode<T, C: CommandContext<T>> {

    /**
     * The priority of the node, higher number = higher priority
     * */
    public abstract val priority: Priority

    private val subNodes = ArrayList<CommandNode<T, C>>()

    /**
     * Parses the node
     * @param str The string, trimmed to the current index for parsing
     * @param ctx The command context
     * @return The new index, return -1 if parsing doesn't succeed
     * */
    public abstract fun parse(str: String, ctx: C): Int

    public fun getSubNodes(): Array<CommandNode<T,C>> = subNodes.toTypedArray()

    public fun <N: CommandNode<T,C>> addSubNode(node: N) {
        subNodes.add(node)
        subNodes.sortBy {
            it.priority.value
        }
    }
}