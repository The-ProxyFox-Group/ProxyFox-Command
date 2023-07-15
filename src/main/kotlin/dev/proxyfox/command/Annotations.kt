package dev.proxyfox.command

@Target(AnnotationTarget.FUNCTION)
public annotation class Command

@Target(AnnotationTarget.CLASS, AnnotationTarget.VALUE_PARAMETER)
public annotation class LiteralArgument(public vararg val values: String)

@Target(AnnotationTarget.VALUE_PARAMETER)
public annotation class Context

public fun LiteralArgument.getLiteral(cursor: StringCursor): String? {
    cursor.checkout()
    val string = cursor.extractString(false)
    cursor.inc()
    if (values.contains(string.lowercase())) {
        cursor.commit()
        return string.lowercase()
    }
    cursor.rollback()
    return null
}