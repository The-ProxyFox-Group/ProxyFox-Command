package dev.proxyfox.command.node

import dev.proxyfox.command.CommandContext
import dev.proxyfox.command.Executor
import dev.proxyfox.command.NodeHolder

public abstract class CommandNode<T, C: CommandContext<T>>: NodeHolder<T, C>() {
    /**
     * The priority of the node
     * */
    public abstract val priority: Priority

    public abstract val name: String

    private var executor: Executor<T>? = null

    /**
     * Parses the node
     * @param str The string, trimmed to the current index for parsing
     * @param ctx The command context
     * @return The new index, return -1 if parsing doesn't succeed
     * */
    public abstract fun parse(str: String, ctx: C): Int

    public fun executes(executor: Executor<T>) {
        this.executor = executor
    }

    public suspend fun execute(ctx: C): Boolean? {
        val ex = executor ?: return null
        return ctx.ex()
    }
}
