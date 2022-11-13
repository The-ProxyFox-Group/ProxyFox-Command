package dev.proxyfox.command.test

import dev.proxyfox.command.CommandParser
import dev.proxyfox.command.node.builtin.*

suspend fun main() {
    val parser = CommandParser<String, StringContext>()

    parser.literal("test") {
        literal("int") {
            int("someInt") {
                executes {
                    respondSuccess("${it()}")
                    true
                }
            }
        }

        literal("string") {
            string("someString") {
                executes {
                    respondSuccess(it())
                    true
                }
            }
        }

        literal("stringlist") {
            stringList("someStringList") {
                executes {
                    for (i in it()) {
                        respondSuccess(i)
                    }
                    true
                }
            }
        }

        literal("greedy") {
            greedy("someGreedy") {
                executes {
                    respondSuccess(it())
                    true
                }
            }
        }

        literal("unix") {
            unix("someUnix") {
                executes {
                    for (i in it()) {
                        respondSuccess(i)
                    }
                    true
                }
            }
        }

        literal("unixliteral") {
            unixLiteral("test") {
                executes {
                    respondSuccess("It worked!")
                    true
                }
            }
        }
    }
    println("testing unixLiteral")
    parser.parse(StringContext("test unixliteral -test"))
    println("testing int")
    parser.parse(StringContext("test int 15"))
    println("testing string")
    parser.parse(StringContext("test string owo"))
    println("testing unix")
    parser.parse(StringContext("test unix --owo -uwu --nya"))
    println("testing stringList")
    parser.parse(StringContext("test stringlist owo uwu"))
    println("testing greedy")
    parser.parse(StringContext("test greedy owo owo uwu"))
}
