package dev.proxyfox.command

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlinx.serialization.serializer
import kotlin.reflect.*
import kotlin.reflect.full.callSuspendBy
import kotlin.reflect.full.functions

public data class ParseError(
    public val function: KFunction<Any>,
    public val ordinal: Int,
    public val parameter: KParameter?,
    public val validationException: CommandValidationException?
) {
    override fun toString(): String =
        if (parameter != null)
            "${function.name}[$ordinal]:${parameter.name}${validationException?.let { " -> ${it.reason}" } ?: ""}"
        else "${function.name}[$ordinal]"

    public companion object {
        public const val NOT_COMMAND: Int = -2
        public const val ROOT_LITERAL_NOT_MATCH: Int = -1
    }
}

public suspend fun <T, C : Any> C.parse(context: CommandContext<T>): Either<Unit, List<ParseError>> {
    val functions = this::class.functions
    val errors = ArrayList<ParseError>()
    for (function in functions) {
        val result = function.parseFunc(context, this)
        if (result.isLeft())
            return Unit.left()
        errors.add(result.getOrNull()!!)
    }
    return errors.right()
}

public suspend fun <T, F: KFunction<R>, R> F.parseFunc(context: CommandContext<T>, base: Any? = null): Either<Unit, ParseError> {
    annotations.find { it is Command } ?: return ParseError(this as KFunction<Any>, ParseError.NOT_COMMAND, null, null).right()

    val cursor = StringCursor(context.command)

    val map = HashMap<KParameter, Any?>()

    if (base != null) {
        val arg = base::class.annotations.find { it is LiteralArgument } as? LiteralArgument
        if (arg != null && arg.getLiteral(cursor) == null) {
            return ParseError(this as KFunction<Any>, ParseError.ROOT_LITERAL_NOT_MATCH, null, null).right()
        }
    }

    for (i in parameters.indices) {
        val parameter = parameters[i]
        if (i == 0 && base != null) {
            map[parameter] = base
            continue
        }
        if (parameter.annotations.find { it is Context } != null) {
            map[parameter] = context
            continue
        }
        val literal = parameter.annotations.find { it is LiteralArgument } as? LiteralArgument?
        if (literal != null) {
            literal.getLiteral(cursor) ?: return ParseError(this as KFunction<Any>, i, parameter, null).right()
            map[parameter] = Unit
            continue
        }
        val value = try {
            decode(cursor, context as CommandContext<Any>, serializer(parameter.type))
        } catch (err: CommandDecodingException) {
            if (parameter.isOptional || parameter.type.isMarkedNullable)
                null
            else return ParseError(this as KFunction<Any>, i, parameter, null).right()
        } catch (err: CommandValidationException) {
            return ParseError(this as KFunction<Any>, i, parameter, err).right()
        }
        map[parameter] = value
        if (value != null)
            context[parameter.name!!] = value
    }

    if (isSuspend)
        callSuspendBy(map)
    else callBy(map)

    return Unit.left()
}
