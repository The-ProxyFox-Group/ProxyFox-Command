package dev.proxyfox.command

import dev.proxyfox.command.node.CommandNode

public open class NodeHolder<T, C: CommandContext<T>> {
    public val nodes: ArrayList<CommandNode<T, C>> = arrayListOf()

    public fun <N: CommandNode<T, C>> addNode(node: N) {
        nodes.add(node)
        nodes.sortBy {
            it.priority.value
        }
    }
}