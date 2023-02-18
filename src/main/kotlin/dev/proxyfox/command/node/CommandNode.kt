package dev.proxyfox.command.node

import dev.proxyfox.command.CommandContext
import dev.proxyfox.command.Executor
import dev.proxyfox.command.NodeHolder
import dev.proxyfox.command.StringCursor

public abstract class CommandNode<T, C: CommandContext<T>>: NodeHolder<T, C>() {
    /**
     * The priority of the node
     * */
    public abstract val priority: Priority

    public abstract val name: String

    private var executor: Executor<T>? = null

    /**
     * Parses the node
     * @param cursor The cursor, at the current index
     * @param ctx The command context
     * @return Whether the parsing succeeded
     * */
    public abstract fun parse(cursor: StringCursor, ctx: C): Boolean

    public fun executes(executor: Executor<T>) {
        this.executor = executor
    }

    public suspend fun execute(ctx: C): Boolean? {
        val ex = executor ?: return null
        return ctx.ex()
    }
}
