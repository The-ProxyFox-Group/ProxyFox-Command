package dev.proxyfox.command.test

import dev.proxyfox.command.*
import kotlinx.serialization.Serializable

@Serializable
data class Test(
    val test: String,
    val uwu: Int?,
    val owo: TestInner
)

@Serializable
data class TestInner(
    val nya: String,
)

@LiteralArgument("nya")
object TestCommandGroup {
    @Command
    suspend fun test(@Context ctx: CommandContext<Any>, @LiteralArgument("owo", "uwu") owo: Unit, uwu: Test) {
        println(uwu)
        println(ctx.command)
    }
}

suspend fun main() {
    val parser = CommandParser<String, StringContext>()
    parser += TestCommandGroup
    parser.parse(StringContext("nya owo uwu 15 nya"))
}
