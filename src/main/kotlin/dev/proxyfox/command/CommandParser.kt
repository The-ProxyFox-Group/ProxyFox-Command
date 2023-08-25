package dev.proxyfox.command

import kotlin.reflect.KFunction

public class CommandParser<T, C: CommandContext<T>> {
    public suspend fun parse(ctx: C): CompletionStruct {
        val errors = ArrayList<ParseError>()

        for (function in functionMembers) {
            val result = function.parseFunc(ctx)
            if (result.isLeft())
                return CompletionStruct(true, errors.apply { sortByDescending { it.ordinal } })
            errors.add(result.getOrNull()!!)
        }

        var completed = false

        for (clazz in classMembers) {
            val result = clazz.parse(ctx)
            errors.addAll(result.second)
            if (result.first) {
                completed = true
                break
            }
        }
        errors.sortBy { -it.ordinal }
        return CompletionStruct(completed, errors)
    }

    private val classMembers = ArrayList<Any>()
    private val functionMembers = ArrayList<KFunction<Any>>()

    public operator fun plusAssign(value: Any) {
        if (value is KFunction<*>)
            functionMembers.add(value as KFunction<Any>)
        else classMembers.add(value)
    }
}
