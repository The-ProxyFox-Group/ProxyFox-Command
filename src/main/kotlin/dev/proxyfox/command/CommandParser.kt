package dev.proxyfox.command

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlin.reflect.KFunction

public class CommandParser<T, C: CommandContext<T>> {
    public suspend fun parse(ctx: C): Either<Unit, List<ParseError>> {
        val errors = ArrayList<ParseError>()

        for (function in functionMembers) {
            val result = function.parseFunc(ctx)
            if (result.isLeft())
                return Unit.left()
            errors.add(result.getOrNull()!!)
        }

        for (clazz in classMembers) {
            val result = clazz.parse(ctx)
            if (result.isLeft())
                return Unit.left()
            errors.addAll(result.getOrNull()!!)
        }
        errors.sortBy { -it.ordinal }
        return errors.right()
    }

    private val classMembers = ArrayList<Any>()
    private val functionMembers = ArrayList<KFunction<Any>>()

    public operator fun plusAssign(value: Any) {
        if (value is KFunction<*>)
            functionMembers.add(value as KFunction<Any>)
        else classMembers.add(value)
    }
}
