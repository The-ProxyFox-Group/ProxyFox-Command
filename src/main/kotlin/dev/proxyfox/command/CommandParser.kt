package dev.proxyfox.command

import dev.proxyfox.command.node.CommandNode
import dev.proxyfox.command.node.builtin.LiteralNode

public class CommandParser<T, C: CommandContext<T>>: NodeHolder<T, C>() {
    public suspend fun parse(ctx: C): Boolean? {
        val literals = ArrayList<LiteralNode<T, C>>()
        val cursor = StringCursor(ctx.command)
        for (node in nodes) {
            if (node is LiteralNode) literals.add(node)
            cursor.checkout()
            val parsed = tryParseNode(cursor, node, ctx)
            if (parsed == null) {
                cursor.rollback()
                continue
            }
            cursor.commit()
            return parsed
        }
        val test = ctx.command.split(" ")[0]
        val closest = getLevenshtein(test, literals) ?: return null
        if (closest.second == 0) return null
        ctx.respondFailure("Command `$test` not found. Did you mean `${closest.first}`? Closeness: ${closest.second}")
        return false
    }

    private suspend fun tryParseNode(cursor: StringCursor, node: CommandNode<T, C>, ctx: C): Boolean? {
        // Try parsing the node
        val parsed = node.parse(cursor, ctx)
        // Return if parsing failed or there's no string left to consume
        if (!parsed) return null
        // Iterate through sub nodes and try parsing them
        val literals = ArrayList<LiteralNode<T, C>>()
        for (subNode in node.nodes) {
            if (subNode is LiteralNode) literals.add(subNode)
            cursor.checkout()
            val parsed = tryParseNode(cursor, subNode, ctx)
            if (parsed == null) {
                cursor.rollback()
                continue
            }
            cursor.commit()
            return parsed
        }
        if (cursor.end) return node.execute(ctx)
        val test = cursor.extractString(allowQuotes = false)
        val closest = getLevenshtein(test, literals) ?: return node.execute(ctx)
        if (closest.second == 0) return node.execute(ctx)
        ctx.respondFailure("Command `$test` not found. Did you mean `${closest.first}`?")
        return false
    }

    private fun getLevenshtein(test: String, literals: ArrayList<LiteralNode<T, C>>): Pair<String, Int>? {
        var closest: Pair<String, Int>? = null
        for (literal in literals) {
            for (str in literal.literals) {
                val dist = test.levenshtein(str)
                if (closest == null) {
                    closest = Pair(str, dist)
                } else if (closest.second > dist) {
                    closest = Pair(str, dist)
                }
            }
        }
        return closest
    }
}
