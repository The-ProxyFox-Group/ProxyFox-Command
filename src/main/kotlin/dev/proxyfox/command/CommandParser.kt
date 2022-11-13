package dev.proxyfox.command

import dev.proxyfox.command.node.CommandNode

public class CommandParser<T, C: CommandContext<T>>: NodeHolder<T, C>() {
    public suspend fun parse(ctx: C): Boolean? {
        for (node in nodes) {
            val output = tryParseNode(ctx.command, node, ctx)
            return output ?: continue
        }
        return null
    }

    private suspend fun tryParseNode(input: String, node: CommandNode<T, C>, ctx: C): Boolean? {
        val idx = node.parse(input, ctx)
        if (idx == -1) return null
        if (idx >= input.length) return node.execute(ctx)
        val substr = input.substring(idx).trim()
        for (subNode in node.nodes) {
            val output = tryParseNode(substr, subNode, ctx)
            return output ?: continue
        }
        return node.execute(ctx)
    }
}
