package dev.proxyfox.command

import kotlin.reflect.KFunction

public class CommandParser<T, C: CommandContext<T>> {
    public suspend fun parse(ctx: C): Boolean {
        for (function in functionMembers)
            if (function.parseFunc(ctx))
                return true

        for (clazz in classMembers)
            if (clazz.parse(ctx))
                return true

        return false
    }

    private val classMembers = ArrayList<Any>()
    private val functionMembers = ArrayList<KFunction<Any>>()

    public operator fun plusAssign(value: Any) {
        if (value is KFunction<*>)
            functionMembers.add(value as KFunction<Any>)
        else classMembers.add(value)
    }
}
