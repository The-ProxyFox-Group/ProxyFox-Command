package dev.proxyfox.command.test

import dev.proxyfox.command.*
import dev.proxyfox.command.types.GreedyString
import dev.proxyfox.command.types.UnixList
import kotlinx.serialization.Serializable

@Serializable
data class Test(
    val test: String,
    val uwu: Int?,
    val owo: TestInner
)

@Serializable
data class TestInner(
    val nya: UnixList,
)

@LiteralArgument("nya")
object TestCommandGroup {
    @Command
    suspend fun test(
        @Context ctx: CommandContext<Any>,
        @LiteralArgument("owo", "uwu") _literal: Unit,
        owo: String,
        uwu: UnixList,
        nya: GreedyString
    ) {
        println("Input: ${ctx.command}")
        println(owo)
        println(uwu)
        println(nya)
    }
}

suspend fun main() {
    val parser = CommandParser<String, StringContext>()
    parser += TestCommandGroup
    parser.parse(StringContext("nya owo uwu -awoo --arf 15 nya"))
}
