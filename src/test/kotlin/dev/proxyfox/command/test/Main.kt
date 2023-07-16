package dev.proxyfox.command.test

import dev.proxyfox.command.*
import dev.proxyfox.command.types.CommandSerializer
import dev.proxyfox.command.types.GreedyString
import dev.proxyfox.command.types.UnixList
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@JvmInline
@Serializable(with = ValidatedStringSerializer::class)
value class ValidatedString(val value: String) {
    fun validate(): Boolean {
        if (value.length > 5) return false
        return value.isNotEmpty()
    }
}

class ValidatedStringSerializer : CommandSerializer<ValidatedString> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ValidatedString", PrimitiveKind.STRING)

    override fun decodeCommand(decoder: CommandDecoder): ValidatedString {
        decoder.cursor.checkout()
        val value = ValidatedString(decoder.decodeString())
        if (!value.validate()) {
            decoder.cursor.rollback()
            decoder.fails()
        }
        decoder.cursor.commit()
        return value
    }

    override fun decodeRegular(decoder: Decoder): ValidatedString = ValidatedString(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: ValidatedString) {
        encoder.encodeString(value.value)
    }
}

@LiteralArgument("member", "mem", "m")
object MemberCommands {
    @Command
    suspend fun test(
        @Context ctx: CommandContext<Any>,
        member: ValidatedString,
        @LiteralArgument("name", "n") literal_name: Unit,
        name: String
    ) {
        println(member)
        println(name)
    }
}

suspend fun main() {
    val parser = CommandParser<String, StringContext>()
    parser += MemberCommands
    println(parser.parse(StringContext("m owo n uwu")))
    println(parser.parse(StringContext("m owowowo n uwu")))
}
