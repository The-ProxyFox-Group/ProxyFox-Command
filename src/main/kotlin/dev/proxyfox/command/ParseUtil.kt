package dev.proxyfox.command

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlinx.serialization.serializer
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.callSuspendBy
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions
import kotlin.reflect.full.hasAnnotation

public data class ParseError(
    public val function: KFunction<Any>,
    public val ordinal: Int,
    public val parameter: KParameter?,
    public val rootLiteral: LiteralArgument?,
    public val exception: CommandDecodingException?
) {
    override fun toString(): String =
        "${rootLiteral?.values?.let { "$it " } ?: ""}${function.name}[$ordinal]${parameter?.name.let {": $it"} ?: ""}${exception?.let { " -> ${it.reason}" } ?: ""}"

    public companion object {
        public const val NOT_COMMAND: Int = -2
        public const val ROOT_LITERAL_NOT_MATCH: Int = -1
    }
}

public suspend fun <T, C : Any> C.parse(context: CommandContext<T>): Either<Unit, List<ParseError>> {
    val functions = this::class.functions
        .asSequence()
        .filter { it.hasAnnotation<Command>() }
        .sortedByDescending { it.parameters.size }
        .sortedByDescending { it.findAnnotation<Command>()!!.priority }
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
    annotations.find { it is Command } ?: return ParseError(this as KFunction<Any>, ParseError.NOT_COMMAND, null, null, null).right()

    val cursor = StringCursor(context.command)

    val map = HashMap<KParameter, Any?>()

    var rootLiteral: LiteralArgument? = null

    if (base != null) {
        rootLiteral = base::class.annotations.find { it is LiteralArgument } as? LiteralArgument
        if (rootLiteral != null && rootLiteral.getLiteral(cursor) == null) {
            return ParseError(this as KFunction<Any>, ParseError.ROOT_LITERAL_NOT_MATCH, null, rootLiteral, null).right()
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
            literal.getLiteral(cursor) ?: return ParseError(this as KFunction<Any>, i, parameter, rootLiteral, null).right()
            map[parameter] = Unit
            continue
        }
        val value = try {
            decode(cursor, context as CommandContext<Any>, serializer(parameter.type))
        } catch (err: CommandDecodingException) {
            if (parameter.isOptional || parameter.type.isMarkedNullable)
                null
            else return ParseError(this as KFunction<Any>, i, parameter, rootLiteral, err).right()
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
