package dev.proxyfox.command

import kotlinx.serialization.serializer
import kotlin.reflect.*
import kotlin.reflect.full.callSuspendBy
import kotlin.reflect.full.functions

public suspend fun <T, C : Any> C.parse(context: CommandContext<T>): Boolean {
    return this::class.functions.firstNotNullOfOrNull {
        it.parseFunc(context, this)
    } ?: false
}

public suspend fun <T, F: KFunction<R>, R> F.parseFunc(context: CommandContext<T>, base: Any? = null): Boolean {
    annotations.find { it is Command } ?: return false

    val cursor = StringCursor(context.command)

    val map = HashMap<KParameter, Any?>()

    if (base != null) {
        val arg = base::class.annotations.find { it is LiteralArgument } as? LiteralArgument
        if (arg != null && arg.getLiteral(cursor) == null) {
            return false
        }
    }

    for (parameter in parameters) {
        if (parameter == parameters[0] && base != null) {
            map[parameter] = base
            continue
        }
        if (parameter.annotations.find { it is Context } != null) {
            map[parameter] = context
            continue
        }
        val literal = parameter.annotations.find { it is LiteralArgument } as? LiteralArgument?
        if (literal != null) {
            literal.getLiteral(cursor) ?: return false
            map[parameter] = Unit
            continue
        }
        val value = try {
            decode(cursor, context as CommandContext<Any>, serializer(parameter.type))
        } catch (err: CommandDecodingException) {
            if (parameter.isOptional || parameter.type.isMarkedNullable)
                null
            else return false
        }
        map[parameter] = value
        if (value != null)
            context[parameter.name!!] = value
    }

    if (isSuspend)
        callSuspendBy(map)
    else callBy(map)

    return true
}
